package ARApi.Scaffold.Endpoints.Model;

import ARApi.Scaffold.Database.Entities.OwnedAssetGrouping;
import ARApi.Scaffold.Shared.Enums.Currency;

import java.util.List;

public class ModelOwnedAssetGrouping {

    public ModelOwnedAssetGrouping(OwnedAssetGrouping assetGrouping, Currency targetCurrency){
        groupName = assetGrouping.group_name;
    }

    public ModelOwnedAssetGrouping() {
    }

    public List<ModelOwnedPublicAsset> ownedPrivateAsset;

    public List<ModelOwnedPublicAsset> ownedPublicAsset;

    public String groupName;

    public double targetPercentage;
}
