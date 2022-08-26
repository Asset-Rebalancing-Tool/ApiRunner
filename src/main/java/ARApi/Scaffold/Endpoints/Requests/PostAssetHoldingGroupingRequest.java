package ARApi.Scaffold.Endpoints.Requests;

import ARApi.Scaffold.Database.Entities.AssetHoldingGrouping;
import ARApi.Scaffold.Database.Repos.PrivateAssetHoldingRepository;
import ARApi.Scaffold.Database.Repos.PublicAssetHoldingRepository;

import java.util.HashSet;
import java.util.UUID;
import java.util.stream.Stream;

public class PostAssetHoldingGroupingRequest {

    public String[] publicAssetUuids;

    public String[] privateAssetUuids;

    public double targetPercentage;

    public String groupName;

    public AssetHoldingGrouping toAssetHoldingGrouping(UUID userUuid, PublicAssetHoldingRepository publicAssetHoldings, PrivateAssetHoldingRepository privateAssetHoldings){
        var assetHoldingGrouping = new AssetHoldingGrouping();
        assetHoldingGrouping.user_uuid = userUuid;
        assetHoldingGrouping.PublicAssetHoldings = new HashSet<>(publicAssetHoldings.FindByUuids(Stream.of(publicAssetUuids).map(UUID::fromString).toList()));
        assetHoldingGrouping.PrivateAssetHoldings = new HashSet<>(privateAssetHoldings.FindByUuids(Stream.of(privateAssetUuids).map(UUID::fromString).toList()));
        assetHoldingGrouping.target_percentage = targetPercentage;
        assetHoldingGrouping.group_name = groupName;

        return assetHoldingGrouping;
    }
}
