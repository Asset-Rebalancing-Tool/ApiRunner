package ARApi.Scaffold.Endpoints;

import ARApi.Scaffold.Database.Entities.AssetPriceRecord;
import ARApi.Scaffold.Database.Entities.OwnedPrivateAsset;
import ARApi.Scaffold.Shared.Enums.AssetType;
import ARApi.Scaffold.Shared.Enums.Currency;

public class ModelOwnedPrivateAsset {

    public String title;

    public AssetType assetType;

    public ModelOwnedPrivateAsset(OwnedPrivateAsset privateAsset, Currency targetCurrency){
        title = privateAsset.title;
        assetType = privateAsset.asset_type;
        var totalValue = privateAsset.PriceRecords.stream().sorted(AssetPriceRecord.GetComp()).findFirst().get().price *
                privateAsset.owned_quantity;
    }

}
