package ARApi.Scaffold.AssetFetchers;

import ARApi.Scaffold.Database.Entities.PublicAsset.PublicAsset;
import ARApi.Scaffold.Database.Repos.PublicAssetMatcher;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Fetches assets only from a specific database
 */
public class DbAssetFetcher implements IPublicAssetFetcher {

    @Autowired
    PublicAssetMatcher publicAssetMatcher;

    @Override
    public List<PublicAsset> FetchViaSearchString(String searchString) {
        var result = publicAssetMatcher.GetMatchingResult(searchString);
        var assets = result.looseMatches;
        if(result.exactMatch != null){
            assets.add(0, result.exactMatch);
        }
        return assets;
    }

    @Override
    public PublicAsset FetchViaIsin(String isin) {
        return publicAssetMatcher.publicAssetRepository.findByIsin(isin);
    }

}
