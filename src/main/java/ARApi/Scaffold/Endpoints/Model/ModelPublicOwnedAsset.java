package ARApi.Scaffold.Endpoints.Model;

import ARApi.Scaffold.Database.Entities.PublicAsset.PublicOwnedAsset;
import ARApi.Scaffold.Shared.Enums.UnitType;

public class ModelPublicOwnedAsset {

    public ModelPublicAsset publicAsset;

    public String customName;

    public boolean displayCustomName;

    public double targetPercentage;

    public boolean brokerConnected;

    public double ownedQuantity;

    public ModelPublicOwnedAsset(PublicOwnedAsset publicOwnedAsset){
        publicAsset = new ModelPublicAsset(publicOwnedAsset.public_asset);
        customName = publicOwnedAsset.custom_name;
        displayCustomName = publicOwnedAsset.display_custom_name;
        targetPercentage = publicOwnedAsset.target_percentage;
        brokerConnected = publicOwnedAsset.broker_connected;
        ownedQuantity = publicOwnedAsset.owned_quantity;
    }
}
