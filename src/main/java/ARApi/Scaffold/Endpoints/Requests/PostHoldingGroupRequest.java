package ARApi.Scaffold.Endpoints.Requests;

import ARApi.Scaffold.Database.Entities.AssetHoldingGroup;
import ARApi.Scaffold.Database.Repos.PrivateAssetHoldingRepository;
import ARApi.Scaffold.Database.Repos.PublicAssetHoldingRepository;
import ARApi.Scaffold.Database.Repos.UserRepository;

import java.util.HashSet;
import java.util.UUID;
import java.util.stream.Stream;

public class PostHoldingGroupRequest {

    public String[] publicHoldingUuids;

    public String[] privateHoldingUuids;

    public double targetPercentage;

    public String groupName;

    public AssetHoldingGroup toAssetHoldingGrouping(UUID userUuid, UserRepository userRepository, PublicAssetHoldingRepository publicAssetHoldings, PrivateAssetHoldingRepository privateAssetHoldings){
        var assetHoldingGrouping = new AssetHoldingGroup();
        assetHoldingGrouping.SetUser(userUuid, userRepository);
        assetHoldingGrouping.publicHoldings = new HashSet<>(publicAssetHoldings.FindByUuids(Stream.of(publicHoldingUuids).map(UUID::fromString).toList()));
        assetHoldingGrouping.privateHoldings = new HashSet<>(privateAssetHoldings.FindByUuids(Stream.of(privateHoldingUuids).map(UUID::fromString).toList()));
        assetHoldingGrouping.target_percentage = targetPercentage;
        assetHoldingGrouping.group_name = groupName;

        return assetHoldingGrouping;
    }
}
