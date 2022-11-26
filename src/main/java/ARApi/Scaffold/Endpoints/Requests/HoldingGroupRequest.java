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
    private void setEditableFields(HoldingGroup holdingGroup,  PublicAssetHoldingRepository publicAssetHoldings, PrivateAssetHoldingRepository privateAssetHoldings){
        if(publicHoldingUuids != null) {
            holdingGroup.publicHoldings = Arrays.stream(publicHoldingUuids)
                    .map(holdingUuid -> publicAssetHoldings.findById(UUID.fromString(holdingUuid)).orElseThrow()).collect(Collectors.toList());
            holdingGroup.publicHoldings.forEach(ph -> ph.HoldingGroup = holdingGroup);
        }

        if(privateHoldingUuids != null) {
            holdingGroup.privateHoldings= Arrays.stream(privateHoldingUuids)
                    .map(privateHoldingUuid -> privateAssetHoldings.findById(UUID.fromString(privateHoldingUuid)).orElseThrow()).collect(Collectors.toList());
            holdingGroup.privateHoldings.forEach(ph -> ph.HoldingGroup = holdingGroup);
        }

        if(groupName != null) holdingGroup.group_name = groupName;
        if(targetPercentage != null) holdingGroup.target_percentage = targetPercentage;

    }
}
