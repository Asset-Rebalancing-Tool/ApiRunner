package ARApi.Scaffold.Endpoints.Model;

import ARApi.Scaffold.Database.Entities.PrivateAsset.OwnedPrivateAsset;
import ARApi.Scaffold.Shared.Enums.AssetType;
import ARApi.Scaffold.Shared.Enums.Currency;

public class ModelOwnedPrivateAsset {

    public String title;

    public AssetType assetType;

    public ModelOwnedPrivateAsset(OwnedPrivateAsset privateAsset, Currency targetCurrency){
        title = privateAsset.title;
        assetType = privateAsset.asset_type;
    }

}
