package ARApi.Scaffold.AssetFetchers;

import ARApi.Scaffold.Database.Entities.PublicAsset.PublicAsset;
import ARApi.Scaffold.Database.Repos.HighScorePublicAssetRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Fetches assets only from a specific database
 */
public class DbAssetFetcher implements IPublicAssetFetcher {

    @Autowired
    HighScorePublicAssetRepository highScorePublicAssetRepository;

    @Override
    public List<PublicAsset> FetchViaSearchString(String searchString) {
        var result = highScorePublicAssetRepository.GetMatchingResult(searchString);
        var assets = new ArrayList<>(result.b.stream().map(ha -> ha.publicAsset).toList());
        if(result.a != null){
            assets.add(0, result.a);
        }

        return assets;
    }

    @Override
    public PublicAsset FetchViaIsin(String isin) {
        return highScorePublicAssetRepository.publicAssetRepository.findByIsin(isin);
    }

}
