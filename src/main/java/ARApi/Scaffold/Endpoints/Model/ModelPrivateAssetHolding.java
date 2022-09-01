package ARApi.Scaffold.Endpoints.Model;

import ARApi.Scaffold.Database.Entities.PrivateAsset.PrivateAssetHolding;
import ARApi.Scaffold.Shared.Enums.AssetType;
import ARApi.Scaffold.Shared.Enums.UnitType;

public class ModelPrivateAssetHolding {

    public String title;

    public AssetType assetType;

    public double currentPrice;

    public String holdingUuid;

    public ModelPrivateAssetHolding(PrivateAssetHolding privateHolding){
        title = privateHolding.title;
        assetType = privateHolding.asset_type;
        currentPrice = privateHolding.current_price;
        holdingUuid = privateHolding.uuid.toString();
    }
    public ModelPrivateAssetHolding(){

    }
}
