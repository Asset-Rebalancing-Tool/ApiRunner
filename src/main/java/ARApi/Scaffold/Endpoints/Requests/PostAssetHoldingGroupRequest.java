package ARApi.Scaffold.Endpoints.Requests;

import ARApi.Scaffold.Database.Entities.AssetHoldingGroup;
import ARApi.Scaffold.Database.Repos.PrivateAssetHoldingRepository;
import ARApi.Scaffold.Database.Repos.PublicAssetHoldingRepository;
import ARApi.Scaffold.Database.Repos.UserRepository;

import java.util.HashSet;
import java.util.UUID;
import java.util.stream.Stream;

public class PostAssetHoldingGroupRequest {

    public String[] publicAssetHoldingUuids;

    public String[] privateAssetHoldingUuids;

    public double targetPercentage;

    public String groupName;

    public AssetHoldingGroup toAssetHoldingGrouping(UUID userUuid, UserRepository userRepository, PublicAssetHoldingRepository publicAssetHoldings, PrivateAssetHoldingRepository privateAssetHoldings){
        var assetHoldingGrouping = new AssetHoldingGroup();
        assetHoldingGrouping.SetUser(userUuid, userRepository);
        assetHoldingGrouping.PublicAssetHoldings = new HashSet<>(publicAssetHoldings.FindByUuids(Stream.of(publicAssetHoldingUuids).map(UUID::fromString).toList()));
        assetHoldingGrouping.PrivateAssetHoldings = new HashSet<>(privateAssetHoldings.FindByUuids(Stream.of(privateAssetHoldingUuids).map(UUID::fromString).toList()));
        assetHoldingGrouping.target_percentage = targetPercentage;
        assetHoldingGrouping.group_name = groupName;

        return assetHoldingGrouping;
    }
}
