package ARApi.Scaffold.Endpoints.Requests;

import ARApi.Scaffold.Database.Entities.PrivateAsset.PrivateAssetHolding;
import ARApi.Scaffold.Database.Repos.UserRepository;
import ARApi.Scaffold.Shared.Enums.AssetType;

import java.util.UUID;

public class PrivateAssetHoldingRequest {

    public AssetType assetType;

    public double currentPrice;

    public String title;

    public double targetPrecentage;

    public PrivateAssetHolding toPrivateAssetHolding(UUID userUuid, UserRepository userRepository){
        PrivateAssetHolding privateAssetHolding = new PrivateAssetHolding();
        privateAssetHolding.asset_type = assetType;
        privateAssetHolding.title = title;
        privateAssetHolding.current_price = currentPrice;
        privateAssetHolding.SetUser(userUuid, userRepository);
        privateAssetHolding.target_percentage = targetPrecentage;

        return privateAssetHolding;
    }
}
