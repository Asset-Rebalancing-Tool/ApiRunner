package ARApi.Scaffold.Endpoints.Requests;

import ARApi.Scaffold.Shared.Enums.UnitType;

import java.util.List;

public class PostOwnedPublicAssetRequest {

    public String publicAssetUuid;

    public String customName;

    public List<String> customCategories;

    public double ownedQuantity;

    public double targetPercentage;

    public UnitType desiredUnitType;
}
