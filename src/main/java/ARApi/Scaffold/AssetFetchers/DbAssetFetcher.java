package ARApi.Scaffold.AssetFetchers;

import ARApi.Scaffold.Database.Entities.PublicAsset;

import java.util.List;

public class DbAssetFetcher implements IAssetFetcher{

    @Override
    public List<PublicAsset> FetchViaSearchString(String searchString) {
        return null;
    }

    @Override
    public PublicAsset FetchViaIsin(String isin) {
        return null;
    }

    @Override
    public String GetTargetHost() {
        return "127.0.0.1";
    }
}
