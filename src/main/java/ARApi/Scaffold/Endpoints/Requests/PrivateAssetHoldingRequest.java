package ARApi.Scaffold.Endpoints.Requests;

import ARApi.Scaffold.Database.Entities.PrivateAsset.PrivateHolding;
import ARApi.Scaffold.Database.Repos.UserRepository;
import ARApi.Scaffold.Shared.Enums.AssetType;
import ARApi.Scaffold.Shared.Enums.Currency;
import ARApi.Scaffold.Shared.Enums.UnitType;
import io.swagger.annotations.ApiModelProperty;

import java.util.UUID;

/**
 * Is same for POST and PATCH
 */
public class PrivateAssetHoldingRequest {

    public AssetType assetType;

    public String title;

    public Double targetPercentage;

    public Double ownedQuantity;

    public Double pricePerUnit;

    public Currency currency;

    public UnitType unitType;

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
        if(pricePerUnit != null) privateHolding.price_per_unit = pricePerUnit;
        if(targetPercentage != null) privateHolding.target_percentage = targetPercentage;
        if(ownedQuantity != null) privateHolding.owned_quantity = ownedQuantity;
        if(unitType != null) privateHolding.unit_type = unitType;
        if(currency != null) privateHolding.currency = currency;
    }
}
