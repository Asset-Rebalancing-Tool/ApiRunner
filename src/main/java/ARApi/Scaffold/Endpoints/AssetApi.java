package ARApi.Scaffold.Endpoints;


import ARApi.Scaffold.AssetFetchers.AssetFetcherManager;
import ARApi.Scaffold.Database.Entities.PublicAsset;


import ARApi.Scaffold.Services.StringProcessingService;
import ARApi.Scaffold.Services.QueryInserterService;
import ARApi.Scaffold.Services.PublicAssetInserter;
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

    private QueryInserterService queryInserterService;

    private StringProcessingService fuzzyScore;

    private SessionFactory sessionFactory;

    @Autowired
    public AssetApi(SessionFactory sessionFactory, AssetFetcherManager assetFetcherManager, QueryInserterService queryInserterService, StringProcessingService fuzzyScore) {
        this.assetFetcherManager = assetFetcherManager;
        this.sessionFactory = sessionFactory;
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

        if(searchAssetRequest.SearchString.length() < MIN_SEARCH_STRING_LENGHT){
            return new ModelResponse<>("SearchString has to be longer than " + (MIN_SEARCH_STRING_LENGHT -1), HttpStatus.BAD_REQUEST);
        }

        var inserter = queryInserterService.GetInserter(
                (sessionFactory, lock, query) -> new PublicAssetInserter(query, sessionFactory, lock),
                "Select pa from PublicAsset pa " +
                        "left join fetch pa.AssetPriceRecords apr " +
                        "left join fetch pa.AssetInformation ai"
                 );

        PublicAsset fullMatch = null;
        List<HighScoreAsset> highScoreAssets = new ArrayList<>();

        var dbAssets = inserter.GetLoadedEntities();

        for (var asset : dbAssets) {
            // full match check
            var exactIsinMatch = asset.isin == null ? false : fuzzyScore.Same(asset.isin, searchAssetRequest.SearchString);
            var exactSymbolMatch = asset.symbol == null ? false : fuzzyScore.Same(asset.symbol, searchAssetRequest.SearchString);

            if (exactIsinMatch || exactSymbolMatch) {
                fullMatch = asset;
                break;
            }

            var symbolScore = asset.symbol == null ? 0 : fuzzyScore.fuzzyScore(asset.symbol, searchAssetRequest.SearchString);
            var assetNameScore = asset.assetName == null ? 0 : fuzzyScore.fuzzyScore(asset.assetName, searchAssetRequest.SearchString);
            var isinScore = asset.isin == null ? 0 : fuzzyScore.fuzzyScore(asset.isin, searchAssetRequest.SearchString);

            int highestScore = Collections.max(Arrays.asList(symbolScore, assetNameScore, isinScore));

            highScoreAssets.add(new HighScoreAsset(asset, highestScore));
        }

        highScoreAssets = highScoreAssets.stream().filter(ha -> ha.highestFuzzyScore > MIN_FUZZY_SCORE)
                .sorted(Comparator.comparingInt(o -> o.highestFuzzyScore)).toList();

        var session = sessionFactory.openSession();
        session.beginTransaction();
        for(var highScoreAsset : highScoreAssets){
            var updated = session.createQuery("Update PublicAsset p " +
                            "set p.searchHitsTotal = p.searchHitsTotal + 1 " +
                            "where p.uuid = :uuid")
                    .setParameter("uuid", highScoreAsset.publicAsset.uuid).executeUpdate();
        }
        session.getTransaction().commit();
        session.close();

        if (fullMatch != null || highScoreAssets.size() >= MIN_FUZZY_MATCHES) {
            var modelAssetList = new ArrayList<ModelAsset>();
            if (fullMatch != null) {
                modelAssetList.add(0, new ModelAsset(fullMatch));
            }
            highScoreAssets.forEach(hA -> modelAssetList.add(new ModelAsset(hA.publicAsset)));

            return new ModelResponse<>(modelAssetList, HttpStatus.OK);
        }

        // run fetchers only if results not exact
        var fetchedAssets = assetFetcherManager.ExecuteWithFetcher(fetcher -> fetcher.FetchViaSearchString(fuzzyScore.Process(searchAssetRequest.SearchString)));

        var newAssets = inserter.InsertLocked(fetchedAssets);

        return new ModelResponse<>(newAssets.stream().map(ModelAsset::new).toList(), HttpStatus.OK);
    }
}
