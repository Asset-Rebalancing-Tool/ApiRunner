package ARApi.Scaffold.Endpoints;


import ARApi.Scaffold.AssetFetchers.IPublicAssetFetcher;
import ARApi.Scaffold.Database.Entities.PublicAsset.PublicAsset;


import ARApi.Scaffold.Database.Entities.User;
import ARApi.Scaffold.Database.Repos.HighScorePublicAssetRepository;
import ARApi.Scaffold.Database.Entities.DuplicateAwareInserter;
import ARApi.Scaffold.Endpoints.Model.ModelPublicAsset;
import ARApi.Scaffold.Endpoints.Requests.SearchAssetRequest;
import ARApi.Scaffold.Services.StringProcessingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.*;

@RestController
@Component
@RequestMapping("asset_api")
public class AssetApi {

    private final int MIN_FUZZY_MATCHES = 3;

    private final int MIN_SEARCH_STRING_LENGHT = 3;

    private StringProcessingService fuzzyScore;

    private HighScorePublicAssetRepository highScorePublicAssetRepository;

    private IPublicAssetFetcher publicAssetFetcher;

    @Autowired
    public AssetApi(StringProcessingService fuzzyScore, HighScorePublicAssetRepository highScorePublicAssetRepository, IPublicAssetFetcher publicAssetFetcher) {
        this.fuzzyScore = fuzzyScore;
        this.highScorePublicAssetRepository = highScorePublicAssetRepository;
        this.publicAssetFetcher = publicAssetFetcher;
    }



    @PostMapping("/asset/search")
    public List<ModelPublicAsset> SearchAssets(Authentication principal, @RequestBody SearchAssetRequest searchAssetRequest) {

        if(searchAssetRequest.SearchString.length() < MIN_SEARCH_STRING_LENGHT){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "SearchString has to be longer than " + (MIN_SEARCH_STRING_LENGHT -1));
        }

        var matchingResult = highScorePublicAssetRepository.GetMatchingResult(searchAssetRequest.SearchString);
        PublicAsset fullMatch = matchingResult.a;
        List<HighScoreAsset> highScoreAssets = matchingResult.b;

        highScoreAssets.forEach(ha -> highScorePublicAssetRepository.publicAssetRepository.IncreaseSearchHitCount(ha.publicAsset.uuid));

        // check if db results are satisfactory enough ro return
        if (fullMatch != null || highScoreAssets.size() >= MIN_FUZZY_MATCHES) {
            var modelAssetList = new ArrayList<ModelPublicAsset>();
            if (fullMatch != null) {
                modelAssetList.add(0, new ModelPublicAsset(fullMatch));
            }
            highScoreAssets.forEach(hA -> modelAssetList.add(new ModelPublicAsset(hA.publicAsset)));

            return modelAssetList;
        }

        // run fetchers to get information of asset
        var fetchedAssets = publicAssetFetcher.FetchViaSearchString(fuzzyScore.Process(searchAssetRequest.SearchString));

        // insert fetched assets safely
        var newAssets = DuplicateAwareInserter.InsertAll(highScorePublicAssetRepository.publicAssetRepository, fetchedAssets,
                failedInsert -> highScorePublicAssetRepository.publicAssetRepository.findByIsin(failedInsert.isin));

        // map to model and return
        return newAssets.stream().map(ModelPublicAsset::new).toList();
    }
}
