package ARApi.Scaffold.Endpoints.Model;

import ARApi.Scaffold.Database.Entities.PublicAsset.HoldingOrigin;
import ARApi.Scaffold.Database.Entities.PublicAsset.PublicHolding;
import ARApi.Scaffold.Shared.Enums.Currency;
import ARApi.Scaffold.Shared.Enums.UnitType;

public class ModelPublicHolding {

    public ModelPublicAsset publicAsset;

    public String uuid;

    public String customName;

    public boolean displayCustomName;

    public double targetPercentage;

    public HoldingOrigin holdingOrigin;

    public double ownedQuantity;

    public UnitType selectedUnitType;

    public Currency selectedCurrency;

    public ModelPublicHolding(PublicHolding publicHolding){
        publicAsset = new ModelPublicAsset(publicHolding.public_asset);
        customName = publicHolding.custom_name;
        displayCustomName = publicHolding.display_custom_name;
        targetPercentage = publicHolding.target_percentage;
        holdingOrigin = publicHolding.holding_origin;
        ownedQuantity = publicHolding.owned_quantity;
        uuid = publicHolding.uuid.toString();
        selectedUnitType = publicHolding.selected_unit_type;
        selectedCurrency = publicHolding.selected_currency;
    }

    public ModelPublicHolding(){

    }
}
