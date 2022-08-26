package ARApi.Scaffold.Endpoints.Requests;

import ARApi.Scaffold.Database.Entities.OwnedAssetGrouping;
import ARApi.Scaffold.Database.Repos.PrivateOwnedAssetRepository;
import ARApi.Scaffold.Database.Repos.PublicOwnedAssetRepository;

import java.util.HashSet;
import java.util.UUID;
import java.util.stream.Stream;

public class PostOwnedAssetGroupingRequest {

    public String[] publicAssetUuids;

    public String[] privateAssetUuids;

    public double targetPercentage;

    public String groupName;

    public OwnedAssetGrouping toOwnedAssetGrouping(UUID userUuid, PublicOwnedAssetRepository publicOwnedAssetRepository, PrivateOwnedAssetRepository privateOwnedAssetRepository){
        var ownedAssetGrouping = new OwnedAssetGrouping();
        ownedAssetGrouping.user_uuid = userUuid;
        ownedAssetGrouping.PublicOwnedAssets = new HashSet<>(publicOwnedAssetRepository.FindByUuids(Stream.of(publicAssetUuids).map(UUID::fromString).toList()));
        ownedAssetGrouping.PrivateOwnedAssets = new HashSet<>(privateOwnedAssetRepository.FindByUuids(Stream.of(privateAssetUuids).map(UUID::fromString).toList()));
        ownedAssetGrouping.target_percentage = targetPercentage;
        ownedAssetGrouping.group_name = groupName;

        return ownedAssetGrouping;
    }
}
