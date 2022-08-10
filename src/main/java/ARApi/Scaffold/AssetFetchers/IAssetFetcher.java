package ARApi.Scaffold.AssetFetchers;

import ARApi.Scaffold.Database.Entities.PublicAsset;

import java.util.List;

/**
 * Different sources for acquiring assets
 */
public interface IAssetFetcher {

    List<PublicAsset> FetchViaSearchString(String searchString);

    PublicAsset FetchViaIsin(String isin);

    /**
     * for example api.yahoo.finance
     * @return the host we get our assets from
     */
    String GetTargetHost();

}
