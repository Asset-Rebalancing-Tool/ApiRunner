package ARApi.Scaffold.AssetFetchers;

import ARApi.Scaffold.Database.Entities.PublicAsset.PublicAsset;

import java.util.List;

/**
 * Extend this interface to make a source of public assets available
 * to the system (apis and jobs)
 */
public interface IPublicAssetFetcher {

    List<PublicAsset> FetchViaSearchString(String searchString);

    PublicAsset FetchViaIsin(String isin);

    // todo: see if really needed
    // long NeededRequestsPerFetch();
}
