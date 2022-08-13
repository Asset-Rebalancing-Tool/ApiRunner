package ARApi.Scaffold.Endpoints.Model;

import ARApi.Scaffold.Shared.Enums.AssetType;
import ARApi.Scaffold.Shared.Enums.UnitType;

public class ModelOwnedPublicAsset {

    public ModelPublicAsset publicAsset;

    public String assetName;

    public AssetType assetType;

    public String isin;

    public String symbol;

    public UnitType[] convertibleUnitTypes;

}
