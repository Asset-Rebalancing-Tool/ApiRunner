package ARApi.Scaffold.Endpoints.Model;

import ARApi.Scaffold.Database.Entities.PrivateAsset.PrivateAsset;
import ARApi.Scaffold.Shared.Enums.AssetType;
import ARApi.Scaffold.Shared.Enums.Currency;

public class ModelOwnedPrivateAsset {

    public String title;

    public AssetType assetType;

    public ModelOwnedPrivateAsset(PrivateAsset privateAsset, Currency targetCurrency){
        title = privateAsset.title;
        assetType = privateAsset.asset_type;
    }

    public ModelOwnedPrivateAsset(){

    }

}
