package ARApi.Scaffold.Endpoints.Requests;

import ARApi.Scaffold.Database.Entities.HoldingGroup;
import ARApi.Scaffold.Database.Entities.PrivateAsset.PrivateHolding;
import ARApi.Scaffold.Database.Repos.PrivateAssetHoldingRepository;
import ARApi.Scaffold.Database.Repos.PublicAssetHoldingRepository;
import ARApi.Scaffold.Database.Repos.UserRepository;
import io.swagger.annotations.ApiModelProperty;

import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class HoldingGroupRequest {

    public String[] publicHoldingUuids;

    public String[] privateHoldingUuids;

    public Double targetPercentage;

    public String groupName;

    public HoldingGroup toAssetHoldingGrouping(UUID userUuid, UserRepository userRepository, PublicAssetHoldingRepository publicAssetHoldings, PrivateAssetHoldingRepository privateAssetHoldings){
        var assetHoldingGrouping = new HoldingGroup();
        assetHoldingGrouping.SetUser(userUuid, userRepository);
        setEditableFields(assetHoldingGrouping, publicAssetHoldings, privateAssetHoldings);

        return assetHoldingGrouping;
    }

    public HoldingGroup patchHoldingGroup(HoldingGroup holdingGroup, PublicAssetHoldingRepository publicAssetHoldings, PrivateAssetHoldingRepository privateAssetHoldings){
        setEditableFields( holdingGroup, publicAssetHoldings, privateAssetHoldings);
        return holdingGroup;
    }

    @ApiModelProperty(hidden = true)
    private void setEditableFields(HoldingGroup privateHolding,  PublicAssetHoldingRepository publicAssetHoldings, PrivateAssetHoldingRepository privateAssetHoldings){
        if(publicHoldingUuids != null) privateHolding.publicHoldings = Arrays.stream(publicHoldingUuids)
                .map(holding -> publicAssetHoldings.getReferenceById(UUID.fromString(holding))).collect(Collectors.toSet());
        if(privateHoldingUuids != null) privateHolding.privateHoldings= Arrays.stream(privateHoldingUuids)
                .map(holding -> privateAssetHoldings.getReferenceById(UUID.fromString(holding))).collect(Collectors.toSet());
        if(groupName != null) privateHolding.group_name = groupName;
        if(targetPercentage != null) privateHolding.target_percentage = targetPercentage;
    }
}
