package ARApi.Scaffold.Database.Repos;

import ARApi.Scaffold.Database.Entities.PublicAsset.PublicAsset;
import ARApi.Scaffold.Endpoints.HighScoreAsset;
import ARApi.Scaffold.Services.StringProcessingService;
import ARApi.Scaffold.Shared.Pair;
import liquibase.pro.packaged.P;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class HighScorePublicAssetRepository {

    private final int MIN_FUZZY_SCORE = 3;

    @Autowired
    public PublicAssetRepository publicAssetRepository;

    @Autowired
    private StringProcessingService fuzzyScore;

    public Pair<PublicAsset, List<HighScoreAsset>> GetMatchingResult(String searchString){
        PublicAsset fullMatch = null;
        List<HighScoreAsset> highScoreAssets = new ArrayList<>();

        // check database for perfect / good enough matches
        var dbAssets =publicAssetRepository.GetFullAssets();

        for (var asset : dbAssets) {
            // full match check
            var exactIsinMatch = asset.isin != null && fuzzyScore.Same(asset.isin, searchString);
            var exactSymbolMatch = asset.symbol != null && fuzzyScore.Same(asset.symbol, searchString);

            if (exactIsinMatch || exactSymbolMatch) {
                fullMatch = asset;
                // found our fullmatch can return
                break;
            }

            int highestScore = GetHighestFuzzyScore(asset, searchString);

            highScoreAssets.add(new HighScoreAsset(asset, highestScore));
        }

        var scoreComp = Comparator.<HighScoreAsset>comparingInt(o -> o.highestFuzzyScore);
        // filter out too low scoring assets
        highScoreAssets = highScoreAssets.stream().filter(ha -> ha.highestFuzzyScore > MIN_FUZZY_SCORE)
                .sorted(scoreComp.reversed()).toList();

        var result = new Pair<PublicAsset, List<HighScoreAsset>>();
        result.a = fullMatch;
        result.b = highScoreAssets;

        return result;
    }

    private int GetHighestFuzzyScore(PublicAsset asset, String SearchString){
        // find the highest fuzzy score based on other fields
        var symbolScore = asset.symbol == null ? 0 : fuzzyScore.fuzzyScore(asset.symbol, SearchString);
        var assetNameScore = asset.asset_name == null ? 0 : fuzzyScore.fuzzyScore(asset.asset_name, SearchString);
        var isinScore = asset.isin == null ? 0 : fuzzyScore.fuzzyScore(asset.isin, SearchString);

        return Collections.max(Arrays.asList(symbolScore, assetNameScore, isinScore));
    }
}
