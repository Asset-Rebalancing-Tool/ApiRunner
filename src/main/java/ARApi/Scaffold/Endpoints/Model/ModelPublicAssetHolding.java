package ARApi.Scaffold.Endpoints.Model;

import ARApi.Scaffold.Database.Entities.PublicAsset.HoldingOrigin;
import ARApi.Scaffold.Database.Entities.PublicAsset.PublicAssetHolding;
import ARApi.Scaffold.Shared.Enums.Currency;
import ARApi.Scaffold.Shared.Enums.UnitType;

public class ModelPublicAssetHolding {

    public ModelPublicAsset publicAsset;

    public String holdingUuid;

    public String customName;

    public boolean displayCustomName;

    public double targetPercentage;

    public HoldingOrigin holdingOrigin;

    public double ownedQuantity;

    public UnitType selectedUnitType;

    public Currency selectedCurrency;

    public ModelPublicAssetHolding(PublicAssetHolding publicAssetHolding){
        publicAsset = new ModelPublicAsset(publicAssetHolding.public_asset);
        customName = publicAssetHolding.custom_name;
        displayCustomName = publicAssetHolding.display_custom_name;
        targetPercentage = publicAssetHolding.target_percentage;
        holdingOrigin = publicAssetHolding.holding_origin;
        ownedQuantity = publicAssetHolding.owned_quantity;
        holdingUuid = publicAssetHolding.uuid.toString();
        selectedUnitType = publicAssetHolding.selected_unit_type;
        selectedCurrency = publicAssetHolding.selected_currency;
    }

    public ModelPublicAssetHolding(){

    }
}
