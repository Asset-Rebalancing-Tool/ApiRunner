package ARApi.Scaffold.Endpoints.Model;

import ARApi.Scaffold.Database.Entities.PrivateAsset.PrivateAssetHolding;
import ARApi.Scaffold.Shared.Enums.AssetType;
import ARApi.Scaffold.Shared.Enums.UnitType;

public class ModelPrivateAssetHolding {

    public String title;

    public AssetType assetType;

    public UnitType unitType;

    public double price;

    public double ownedQuantity;

    public double targetPercentage;

    public String holdingUuid;

    public ModelPrivateAssetHolding(PrivateAssetHolding privateHolding){
        title = privateHolding.title;
        assetType = privateHolding.asset_type;
        unitType = privateHolding.unit_type;
        price = privateHolding.price;
        ownedQuantity = privateHolding.owned_quantity;
        targetPercentage = privateHolding.target_percentage;
        holdingUuid = privateHolding.uuid.toString();
    }
    public ModelPrivateAssetHolding(){

    }
}
