package ARApi.Scaffold.AssetFetchers;

import ARApi.Scaffold.Database.Entities.PublicAsset;

import java.util.List;

public interface IAssetFetcher {

    List<PublicAsset> FetchViaSearchString(String searchString);

    PublicAsset FetchViaIsin(String isin);

}
