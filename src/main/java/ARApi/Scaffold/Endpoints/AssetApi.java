package ARApi.Scaffold.Endpoints;


import ARApi.Scaffold.AssetFetchers.IPublicAssetFetcher;
import ARApi.Scaffold.Database.Entities.DuplicateAwareInserter;
import ARApi.Scaffold.Database.Repos.PublicAssetMatcher;
import ARApi.Scaffold.Endpoints.Model.ModelPublicAsset;
import ARApi.Scaffold.Endpoints.Requests.SearchAssetRequest;
import ARApi.Scaffold.Services.SearchCompareHelper;
import ARApi.Scaffold.Services.SearchSupervisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@Component
@RequestMapping("asset_api")
public class AssetApi {

    private final int MIN_FUZZY_MATCHES = 3;

    private final int MIN_SEARCH_STRING_LENGHT = 3;

    private final int MAX_SEARCH_STRING_LENGHT = 255;

    private final SearchCompareHelper fuzzyScore;

    private final PublicAssetMatcher publicAssetMatcher;

    private final IPublicAssetFetcher publicAssetFetcher;

    private final SearchSupervisor searchSupervisor;

    @Autowired
    public AssetApi(SearchCompareHelper fuzzyScore, PublicAssetMatcher publicAssetMatcher, IPublicAssetFetcher publicAssetFetcher, SearchSupervisor searchSupervisor) {
        this.fuzzyScore = fuzzyScore;
        this.publicAssetMatcher = publicAssetMatcher;
        this.publicAssetFetcher = publicAssetFetcher;
        this.searchSupervisor = searchSupervisor;
    }

    @PostMapping("/asset/search")
    public List<ModelPublicAsset> SearchAssets(@RequestBody SearchAssetRequest searchAssetRequest) {

        if(searchAssetRequest.SearchString.length() < MIN_SEARCH_STRING_LENGHT){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "searchString has to be  " + MIN_SEARCH_STRING_LENGHT + " or higher");
        }

        if(searchAssetRequest.SearchString.length() > MAX_SEARCH_STRING_LENGHT){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "searchString cannot be longer than " + MAX_SEARCH_STRING_LENGHT);
        }

        var matchingResult = publicAssetMatcher.GetMatchingResult(searchAssetRequest.SearchString);

        matchingResult.looseMatches.forEach(ha -> publicAssetMatcher.publicAssetRepository.IncreaseSearchHitCount(ha.uuid));

        // check if db results are satisfactory enough ro return
        // or if fetch is not permitted
        if (matchingResult.exactMatch != null || matchingResult.looseMatches.size() >= MIN_FUZZY_MATCHES
                || !searchSupervisor.fetchPermitted(fuzzyScore.Process(searchAssetRequest.SearchString))) {
            var modelAssetList = new ArrayList<ModelPublicAsset>();
            if (matchingResult.exactMatch != null) {
                modelAssetList.add(0, new ModelPublicAsset(matchingResult.exactMatch));
            }
            matchingResult.looseMatches.forEach(pa -> modelAssetList.add(new ModelPublicAsset(pa)));

            return modelAssetList;
        }

        // run fetchers to get information of asset
        var fetchedAssets = publicAssetFetcher.FetchViaSearchString(fuzzyScore.Process(searchAssetRequest.SearchString));

        // insert fetched assets safely
        var newAssets = DuplicateAwareInserter.InsertAll(publicAssetMatcher.publicAssetRepository, fetchedAssets,
                failedInsert -> publicAssetMatcher.publicAssetRepository.findByIsin(failedInsert.isin));

        // map to model and return
        return newAssets.stream().map(ModelPublicAsset::new).toList();
    }
}
