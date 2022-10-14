package ARApi.Scaffold.Endpoints.Model;

import ARApi.Scaffold.Database.Entities.PrivateAsset.PrivateHolding;
import ARApi.Scaffold.Shared.Enums.AssetType;
import ARApi.Scaffold.Shared.Enums.Currency;
import ARApi.Scaffold.Shared.Enums.UnitType;

public class ModelPrivateHolding {

    public String title;

    public AssetType assetType;

    public UnitType unitType;

    public double ownedQuantity;

    public double pricePerUnit;

    public String uuid;

    public double targetPercentage;

    public Currency currency;

    public ModelPrivateHolding(PrivateHolding privateHolding){
        title = privateHolding.title;
        currency = privateHolding.currency;
        assetType = privateHolding.asset_type;
        uuid = privateHolding.uuid.toString();
        targetPercentage = privateHolding.target_percentage;
        unitType = privateHolding.unit_type;
        pricePerUnit =privateHolding.price_per_unit;
        ownedQuantity = privateHolding.owned_quantity;
    }

    public ModelPrivateHolding(){

    }
}
