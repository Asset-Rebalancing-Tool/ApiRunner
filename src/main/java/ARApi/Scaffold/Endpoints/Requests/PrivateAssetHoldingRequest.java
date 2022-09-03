package ARApi.Scaffold.Endpoints.Requests;

import ARApi.Scaffold.Database.Entities.PrivateAsset.PrivateAssetHolding;
import ARApi.Scaffold.Database.Repos.UserRepository;
import ARApi.Scaffold.Shared.Enums.AssetType;

import java.util.UUID;

/**
 * Is same for POST and PATCH
 */
public class PrivateAssetHoldingRequest {

    public AssetType assetType;

    public Double currentPrice;

    public String title;

    public Double targetPrecentage;

    public PrivateAssetHolding toPrivateAssetHolding(UUID userUuid, UserRepository userRepository){
        PrivateAssetHolding privateAssetHolding = new PrivateAssetHolding();
        setEditableFields(privateAssetHolding);
        privateAssetHolding.SetUser(userUuid, userRepository);
        return privateAssetHolding;
    }

    public PrivateAssetHolding patchPrivateAssetHolding(PrivateAssetHolding privateAssetHolding){
        setEditableFields(privateAssetHolding);
        return privateAssetHolding;
    }

    private void setEditableFields(PrivateAssetHolding privateAssetHolding){
        if(assetType != null) privateAssetHolding.asset_type = assetType;
        if(title != null) privateAssetHolding.title = title;
        if(currentPrice != null) privateAssetHolding.current_price = currentPrice;
        if(targetPrecentage != null) privateAssetHolding.target_percentage = targetPrecentage;
    }
}
