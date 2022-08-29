package ARApi.Scaffold.Endpoints.Model;

import ARApi.Scaffold.Database.Entities.PublicAsset.PublicAssetHolding;

public class ModelPublicAssetHolding {

    public ModelPublicAsset publicAsset;

    public String holdingUuid;

    public String customName;

    public boolean displayCustomName;

    public double targetPercentage;

    public boolean brokerConnected;

    public double ownedQuantity;

    public ModelPublicAssetHolding(PublicAssetHolding publicAssetHolding){
        publicAsset = new ModelPublicAsset(publicAssetHolding.public_asset);
        customName = publicAssetHolding.custom_name;
        displayCustomName = publicAssetHolding.display_custom_name;
        targetPercentage = publicAssetHolding.target_percentage;
        brokerConnected = publicAssetHolding.broker_connected;
        ownedQuantity = publicAssetHolding.owned_quantity;
        holdingUuid = publicAssetHolding.uuid.toString();
    }

    public ModelPublicAssetHolding(){

    }
}
