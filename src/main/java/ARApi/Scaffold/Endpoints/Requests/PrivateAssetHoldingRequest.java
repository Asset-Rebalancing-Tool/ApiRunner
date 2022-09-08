package ARApi.Scaffold.Endpoints.Requests;

import ARApi.Scaffold.Database.Entities.PrivateAsset.PrivateHolding;
import ARApi.Scaffold.Database.Repos.UserRepository;
import ARApi.Scaffold.Shared.Enums.AssetType;
import io.swagger.annotations.ApiModelProperty;

import java.util.UUID;

/**
 * Is same for POST and PATCH
 */
public class PrivateAssetHoldingRequest {

    public AssetType assetType;

    public Double currentPrice;

    public String title;

    public Double targetPrecentage;

    public PrivateHolding toPrivateAssetHolding(UUID userUuid, UserRepository userRepository){
        PrivateHolding privateHolding = new PrivateHolding();
        setEditableFields(privateHolding);
        privateHolding.SetUser(userUuid, userRepository);
        return privateHolding;
    }

    public PrivateHolding patchPrivateAssetHolding(PrivateHolding privateHolding){
        setEditableFields(privateHolding);
        return privateHolding;
    }

    @ApiModelProperty(hidden = true)
    private void setEditableFields(PrivateHolding privateHolding){
        if(assetType != null) privateHolding.asset_type = assetType;
        if(title != null) privateHolding.title = title;
        if(currentPrice != null) privateHolding.current_price = currentPrice;
        if(targetPrecentage != null) privateHolding.target_percentage = targetPrecentage;
    }
}
