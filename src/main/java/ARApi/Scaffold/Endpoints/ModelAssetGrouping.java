package ARApi.Scaffold.Endpoints;

import ARApi.Scaffold.Database.Entities.AssetGrouping;
import ARApi.Scaffold.Shared.Enums.Currency;

import java.util.List;

public class ModelAssetGrouping {

    public ModelAssetGrouping(AssetGrouping assetGrouping, Currency targetCurrency){
        name = assetGrouping.group_name;
    }

    public List<ModelOwnedPublicAsset> ownedPrivateAsset;

    public List<ModelOwnedPublicAsset> ownedPublicAsset;

    public String name;
}
