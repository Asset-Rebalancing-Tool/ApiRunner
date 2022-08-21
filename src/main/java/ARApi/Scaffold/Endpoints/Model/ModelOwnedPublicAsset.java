package ARApi.Scaffold.Endpoints.Model;

import ARApi.Scaffold.Database.Entities.PublicAsset.OwnedPublicAsset;
import ARApi.Scaffold.Shared.Enums.AssetType;
import ARApi.Scaffold.Shared.Enums.UnitType;

public class ModelOwnedPublicAsset {

    public ModelPublicAsset publicAsset;

    public String customName;

    public boolean displayCustomName;

    public UnitType[] convertibleUnitTypes;

}
