package ARApi.Scaffold.Endpoints.Model;

import ARApi.Scaffold.Database.Entities.PrivateAsset.PrivateOwnedAsset;
import ARApi.Scaffold.Shared.Enums.AssetType;
import ARApi.Scaffold.Shared.Enums.UnitType;

public class ModelOwnedPrivateAsset {

    public String title;

    public AssetType assetType;

    public UnitType unitType;

    public double price;

    public double ownedQuantity;

    public double targetPercentage;

    public ModelOwnedPrivateAsset(PrivateOwnedAsset privateAsset){
        title = privateAsset.title;
        assetType = privateAsset.asset_type;
        unitType = privateAsset.unit_type;
        price = privateAsset.price;
        ownedQuantity = privateAsset.owned_quantity;
        targetPercentage = privateAsset.target_percentage;
    }
}
