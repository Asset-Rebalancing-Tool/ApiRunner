package ARApi.Scaffold.Endpoints.Model;

import ARApi.Scaffold.Database.Entities.PrivateAsset.PrivateHolding;
import ARApi.Scaffold.Shared.Enums.AssetType;

public class ModelPrivateHolding {

    public String title;

    public AssetType assetType;

    public double currentPrice;

    public String uuid;

    public double targetPercentage;

    public ModelPrivateHolding(PrivateHolding privateHolding){
        title = privateHolding.title;
        assetType = privateHolding.asset_type;
        currentPrice = privateHolding.current_price;
        uuid = privateHolding.uuid.toString();
        targetPercentage = privateHolding.target_percentage;
    }
    public ModelPrivateHolding(){

    }
}
