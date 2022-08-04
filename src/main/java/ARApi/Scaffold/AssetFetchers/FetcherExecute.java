package ARApi.Scaffold.AssetFetchers;

public interface FetcherExecute<T>  {

    T Execute(IAssetFetcher fetcher);
}
