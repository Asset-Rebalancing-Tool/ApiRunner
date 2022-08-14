package ARApi.Scaffold.Endpoints;


import ARApi.Scaffold.AssetFetchers.AssetFetcherManager;
import ARApi.Scaffold.Database.Entities.PublicAsset.PublicAsset;


import ARApi.Scaffold.Database.Entities.PublicAssetRepository;
import ARApi.Scaffold.Database.Entities.RepoInsertOnDuplicateReturn;
import ARApi.Scaffold.Endpoints.Model.ModelPublicAsset;
import ARApi.Scaffold.Endpoints.Model.ModelResponse;
import ARApi.Scaffold.Endpoints.Requests.PostOwnedAssetGroupingRequest;
import ARApi.Scaffold.Endpoints.Requests.PostOwnedPublicAssetRequest;
import ARApi.Scaffold.Endpoints.Requests.SearchAssetRequest;
import ARApi.Scaffold.Services.StringProcessingService;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@Component
@RequestMapping("asset_api")
public class AssetApi {

    private final int MIN_SEARCH_STRING_LENGHT = 3;

    private final int MIN_FUZZY_SCORE = 3;

    private final int MIN_FUZZY_MATCHES = 3;

    private AssetFetcherManager assetFetcherManager;

    private StringProcessingService fuzzyScore;

    private PublicAssetRepository publicAssetRepository;

    @Autowired
    public AssetApi( AssetFetcherManager assetFetcherManager, StringProcessingService fuzzyScore, PublicAssetRepository publicAssetRepository) {
        this.assetFetcherManager = assetFetcherManager;
        this.fuzzyScore = fuzzyScore;
        this.publicAssetRepository = publicAssetRepository;
    }

    private int GetHighestFuzzyScore(PublicAsset asset, String SearchString){
        // find the highest fuzzy score based on other fields
        var symbolScore = asset.symbol == null ? 0 : fuzzyScore.fuzzyScore(asset.symbol, SearchString);
        var assetNameScore = asset.asset_name == null ? 0 : fuzzyScore.fuzzyScore(asset.asset_name, SearchString);
        var isinScore = asset.isin == null ? 0 : fuzzyScore.fuzzyScore(asset.isin, SearchString);

        return Collections.max(Arrays.asList(symbolScore, assetNameScore, isinScore));
    }

    @PostMapping("/asset/search")
    public ModelResponse<List<ModelPublicAsset>> SearchAssets(@RequestBody SearchAssetRequest searchAssetRequest) {

        if(searchAssetRequest.SearchString.length() < MIN_SEARCH_STRING_LENGHT){
            return new ModelResponse<>("SearchString has to be longer than " + (MIN_SEARCH_STRING_LENGHT -1), HttpStatus.BAD_REQUEST);
        }

        PublicAsset fullMatch = null;
        List<HighScoreAsset> highScoreAssets = new ArrayList<>();

        // check database for perfect / good enough matches
        var dbAssets = publicAssetRepository.GetFullAssets();

        for (var asset : dbAssets) {
            // full match check
            var exactIsinMatch = asset.isin == null ? false : fuzzyScore.Same(asset.isin, searchAssetRequest.SearchString);
            var exactSymbolMatch = asset.symbol == null ? false : fuzzyScore.Same(asset.symbol, searchAssetRequest.SearchString);

            if (exactIsinMatch || exactSymbolMatch) {
                fullMatch = asset;
                // found our fullmatch can return
                break;
            }

            int highestScore = GetHighestFuzzyScore(asset, searchAssetRequest.SearchString);

            highScoreAssets.add(new HighScoreAsset(asset, highestScore));
        }

        var scoreComp = Comparator.<HighScoreAsset>comparingInt(o -> o.highestFuzzyScore);
        // filter out too low scoring assets
        highScoreAssets = highScoreAssets.stream().filter(ha -> ha.highestFuzzyScore > MIN_FUZZY_SCORE)
                .sorted(scoreComp.reversed()).toList();

        highScoreAssets.forEach(ha -> publicAssetRepository.IncreaseSearchHitCount(ha.publicAsset.uuid));

        // check if db results are satisfactory enough ro return
        if (fullMatch != null || highScoreAssets.size() >= MIN_FUZZY_MATCHES) {
            var modelAssetList = new ArrayList<ModelPublicAsset>();
            if (fullMatch != null) {
                modelAssetList.add(0, new ModelPublicAsset(fullMatch));
            }
            highScoreAssets.forEach(hA -> modelAssetList.add(new ModelPublicAsset(hA.publicAsset)));

            return new ModelResponse<>(modelAssetList, HttpStatus.OK);
        }

        // run fetchers to get information of asset
        var fetchedAssets = assetFetcherManager.ExecuteWithFetcher(fetcher -> fetcher.FetchViaSearchString(fuzzyScore.Process(searchAssetRequest.SearchString)));

        // insert fetched assets safely
        var newAssets = RepoInsertOnDuplicateReturn.InsertAll(publicAssetRepository, fetchedAssets,
                failedInsert -> publicAssetRepository.findByIsin(failedInsert.isin));

        // map to model and return
        return new ModelResponse<>(newAssets.stream().map(ModelPublicAsset::new).toList(), HttpStatus.OK);
    }
}
