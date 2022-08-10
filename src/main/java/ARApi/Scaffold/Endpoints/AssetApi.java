package ARApi.Scaffold.Endpoints;


import ARApi.Scaffold.AssetFetchers.AssetFetcherManager;
import ARApi.Scaffold.Database.Entities.PublicAsset;


import ARApi.Scaffold.Services.StringProcessingService;
import ARApi.Scaffold.Services.QueryInserterService;
import ARApi.Scaffold.Services.PublicAssetInserter;
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

    private AssetFetcherManager assetFetcherManager;

    private QueryInserterService queryInserterService;

    private StringProcessingService fuzzyScore;

    @Autowired
    public AssetApi(AssetFetcherManager assetFetcherManager, QueryInserterService queryInserterService, StringProcessingService fuzzyScore) {
        this.assetFetcherManager = assetFetcherManager;
        this.fuzzyScore = fuzzyScore;
        this.queryInserterService = queryInserterService;
    }

    @PostMapping("/grouping")
    public ResponseEntity<HttpStatus> PostAssetGrouping(@RequestBody PostAssetGroupingRequest assetGroupingRequest) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/asset/owned/public")
    public ResponseEntity<HttpStatus> PostOwnedPublicShares(@RequestBody PostOwnedShareRequest postOwnedShareRequest) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/asset/owned/private")
    public ResponseEntity<HttpStatus> PostOwnedPrivateShares(@RequestBody PostOwnedShareRequest postOwnedShareRequest) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/asset/search")
    public ModelResponse<List<ModelAsset>> SearchAssets(@RequestBody SearchAssetRequest searchAssetRequest) {

        if(searchAssetRequest.SearchString.length() < 3){
            return new ModelResponse<>("Searchstring has to be longer than 3", HttpStatus.BAD_REQUEST);
        }

        var inserter = queryInserterService.GetInserter(
                (sessionFactory, lock, query) -> new PublicAssetInserter(query, sessionFactory, lock),
                "Select pa from PublicAsset pa " +
                        "left join fetch pa.AssetPriceRecords apr " +
                        "left join fetch pa.AssetInformation ai"
                 );

        PublicAsset fullMatch = null;
        List<PublicAsset> partMatches = new ArrayList<>();

        var dbAssets = inserter.GetLoadedEntities();

        for (var asset : dbAssets) {
            // full match check
            var exactIsinMatch = asset.isin == null ? false : fuzzyScore.Same(asset.isin, searchAssetRequest.SearchString);
            var exactSymbolMatch = asset.symbol == null ? false : fuzzyScore.Same(asset.symbol, searchAssetRequest.SearchString);

            if (exactIsinMatch || exactSymbolMatch) {
                fullMatch = asset;
                break;
            }

            var symbolMatches = asset.symbol == null ? false : fuzzyScore.SimilarEnough(asset.symbol, searchAssetRequest.SearchString);

            var assetNameMatches = asset.assetName == null ? false : fuzzyScore.SimilarEnough(asset.assetName, searchAssetRequest.SearchString);

            if (symbolMatches || assetNameMatches) {
                partMatches.add(asset);
            }
        }

        if (fullMatch != null || partMatches.size() >= 4) {
            var modelAssetList = new ArrayList<>(partMatches.stream().map(ModelAsset::new).toList());
            if (fullMatch != null) {
                modelAssetList.add(0, new ModelAsset(fullMatch));
            }

            return new ModelResponse<>(modelAssetList, HttpStatus.OK);
        }

        // run fetchers only if results not exact
        var fetchedAssets = assetFetcherManager.ExecuteWithFetcher(fetcher -> fetcher.FetchViaSearchString(fuzzyScore.Process(searchAssetRequest.SearchString)));

        var newAssets = inserter.InsertLocked(fetchedAssets);

        return new ModelResponse<>(newAssets.stream().map(ModelAsset::new).toList(), HttpStatus.OK);
    }
}
