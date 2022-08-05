package ARApi.Scaffold.Endpoints;


import ARApi.Scaffold.AssetFetchers.AssetFetcherManager;
import ARApi.Scaffold.Database.Entities.PublicAsset;


import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.sql.Array;
import java.util.*;

@RestController
@Component
@RequestMapping("asset_api")
public class AssetApi {

    private AssetFetcherManager assetFetcherManager;

    private SessionFactory DbSessionProvider;

    @Autowired
    public AssetApi(AssetFetcherManager assetFetcherManager, SessionFactory dbSessionProvider) {
        this.assetFetcherManager = assetFetcherManager;
        DbSessionProvider = dbSessionProvider;
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
        // fetch fitting assets from db
        var dbSession = DbSessionProvider.openSession();

        var trimmedLoweredSearchString = searchAssetRequest.SearchString.trim().toLowerCase(Locale.ROOT);

        PublicAsset fullMatch = null;
        List<PublicAsset> partMatches = new ArrayList<>();

        var dbAssets = dbSession.createQuery("Select pa " +
                "from PublicAsset pa left join pa.AssetPriceRecords apr left join pa.AssetInformation ai", PublicAsset.class).stream().distinct().toList();

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
        var newAssets = new ArrayList<PublicAsset>();
        var transaction = dbSession.beginTransaction();
        for (var fetchedAsset : fetchedAssets) {
            // check if asset already exists
            if (fetchedAsset.isin != null) {
                var assetWithIsin = dbAssets.stream().filter(dbAsset -> dbAsset.isin != null && dbAsset.isin == fetchedAsset.isin).findFirst();
                if (assetWithIsin.isPresent()) {
                    newAssets.add(assetWithIsin.get());
                    // TODO: add newer price records
                    continue;
                }
            }

            dbSession.save(fetchedAsset);
            if (fetchedAsset.AssetPriceRecords != null)
                for (var priceRecord : fetchedAsset.AssetPriceRecords) {
                    dbSession.save(priceRecord);
                }
            if (fetchedAsset.AssetInformation != null)
                for (var priceInfo : fetchedAsset.AssetInformation) {
                    dbSession.save(priceInfo);
                }
            newAssets.add(fetchedAsset);

        }

        transaction.commit();
        dbSession.close();

        return newAssets.stream().map(ModelAsset::new).toList();
    }

    private List<PublicAsset> GetNewAssets(List<PublicAsset> fetchedAssets, List<PublicAsset> dbAssets) {
        Set<String> existingIsins = new HashSet<>();
        for (var dbAsset : dbAssets) {
            if (dbAsset.isin != null) {
                existingIsins.add(dbAsset.isin);
            }
        }
        List<PublicAsset> newAssets = new ArrayList<>();
        for (var fetchedAsset : fetchedAssets) {
            var isin = fetchedAsset.isin;
            if (isin == null) {
                newAssets.add(fetchedAsset);
            } else {
                if (!existingIsins.contains(isin)) {
                    existingIsins.add(isin);
                    newAssets.add(fetchedAsset);
                }
            }
        }
        return newAssets;
    }


}
