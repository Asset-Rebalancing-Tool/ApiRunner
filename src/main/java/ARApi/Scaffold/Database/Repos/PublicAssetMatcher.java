package ARApi.Scaffold.Database.Repos;

import ARApi.Scaffold.Database.Entities.PublicAsset.PublicAsset;
import ARApi.Scaffold.Services.SearchCompareHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PublicAssetMatcher {

    @Autowired
    public PublicAssetRepository publicAssetRepository;

    @Autowired
    private SearchCompareHelper searchCompareHelper;

    public MatchingResult GetMatchingResult(String searchString){
        PublicAsset fullMatch = null;
        List<PublicAsset> highScoreAssets = new ArrayList<>();

        // check database for perfect / good enough matches
        var dbAssets = publicAssetRepository.GetFullAssets();

        for (var asset : dbAssets) {
            // full match check
            var exactIsinMatch = asset.isin != null && searchCompareHelper.Same(asset.isin, searchString);
            var exactSymbolMatch = asset.symbol != null && searchCompareHelper.Same(asset.symbol, searchString);

            if (exactIsinMatch || exactSymbolMatch) {
                fullMatch = asset;
                // found our fullmatch can return
                break;
            }

            // contains in other fields
            var assetNameContains = searchCompareHelper.Contains(asset.asset_name, searchString);
            var isinContains = asset.isin != null && searchCompareHelper.Contains(asset.isin, searchString);

            if(assetNameContains || isinContains){
                highScoreAssets.add(asset);
            }
        }

        var result = new MatchingResult();
        result.exactMatch = fullMatch;
        result.looseMatches = highScoreAssets;

        return result;
    }
}
