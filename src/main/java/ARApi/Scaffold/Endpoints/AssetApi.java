package ARApi.Scaffold.Endpoints;


import ARApi.Scaffold.AssetFetchers.AssetFetcherManager;
import ARApi.Scaffold.Database.Entities.PublicAsset;


import ARApi.Scaffold.Services.InserterProvider;
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

    private AssetFetcherManager assetFetcherManager;

    private InserterProvider bundledInserterService;

    @Autowired
    public AssetApi(AssetFetcherManager assetFetcherManager, InserterProvider bundledInserterService) {
        this.assetFetcherManager = assetFetcherManager;
        this.bundledInserterService = bundledInserterService;
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
    public List<ModelAsset> SearchAssets(@RequestBody SearchAssetRequest searchAssetRequest) {

        var inserter = bundledInserterService.GetInserter(PublicAsset.class, "Select pa " +
                "from PublicAsset pa left join fetch pa.AssetPriceRecords apr left join fetch pa.AssetInformation ai");
        // TODO: query mit als key usen, ansonsten schwierig bei verschiedenen varianten der gleichen klasse

        var trimmedLoweredSearchString = searchAssetRequest.SearchString.trim().toLowerCase(Locale.ROOT);

        PublicAsset fullMatch = null;
        List<PublicAsset> partMatches = new ArrayList<>();

        var dbAssets = inserter.GetLoadedEntities();

        for (var asset : dbAssets) {
            if (asset.isin.toLowerCase(Locale.ROOT).equals(trimmedLoweredSearchString) || asset.symbol.toLowerCase(Locale.ROOT).equals(trimmedLoweredSearchString)) {
                fullMatch = asset;
                break;
            }

            var symbolMatches = asset.symbol == null ? false : asset.symbol.toLowerCase(Locale.ROOT).contains(trimmedLoweredSearchString);

            var assetNameMatches = asset.assetName == null ? false : asset.assetName.toLowerCase(Locale.ROOT).contains(trimmedLoweredSearchString);

            if (symbolMatches || assetNameMatches) {
                partMatches.add(asset);
            }
        }

        if (fullMatch != null || partMatches.size() >= 5) {
            var modelAssetList = new ArrayList<>(partMatches.stream().map(ModelAsset::new).toList());
            if (fullMatch != null) {
                modelAssetList.add(0, new ModelAsset(fullMatch));
            }

            return modelAssetList;
        }

        // run fetchers only if results not exact
        var fetchedAssets = assetFetcherManager.ExecuteWithFetcher(fetcher -> fetcher.FetchViaSearchString(trimmedLoweredSearchString));

        var newAssets = inserter.Insert(fetchedAssets);

        return newAssets.stream().map(ModelAsset::new).toList();
    }
}
