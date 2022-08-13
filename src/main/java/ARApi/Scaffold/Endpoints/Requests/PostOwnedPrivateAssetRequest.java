package ARApi.Scaffold.Endpoints.Requests;

import ARApi.Scaffold.Shared.Enums.AssetType;
import ARApi.Scaffold.Shared.Enums.Currency;
import ARApi.Scaffold.Shared.Enums.UnitType;

import java.util.List;

public class PostOwnedPrivateAssetRequest {

    public List<String> customCategories;

    public double ownedQuantity;

    public double targetPercentage;

    public AssetType assetType;

    public double currentPrice;

    public UnitType unitType;

    public Currency currency;
}
