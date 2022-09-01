package ARApi.Scaffold.Endpoints.Requests;

import ARApi.Scaffold.Database.Entities.PrivateAsset.PrivateAssetHolding;
import ARApi.Scaffold.Database.Repos.PrivateAssetHoldingRepository;
import ARApi.Scaffold.Database.Repos.PublicAssetHoldingRepository;
import ARApi.Scaffold.Database.Repos.UserRepository;
import ARApi.Scaffold.Shared.Enums.AssetType;

import java.util.List;
import java.util.UUID;

public class PostPrivateAssetHoldingRequest {

    public AssetType assetType;

    public double currentPrice;

    public String title;

    public PrivateAssetHolding toPrivateAssetHolding(UUID userUuid, UserRepository userRepository, PrivateAssetHoldingRepository privateOwnedAssetRepository, PublicAssetHoldingRepository publicOwnedAssetRepository){
        PrivateAssetHolding privateAssetHolding = new PrivateAssetHolding();
        privateAssetHolding.asset_type = assetType;
        privateAssetHolding.title = title;
        privateAssetHolding.current_price = currentPrice;
        privateAssetHolding.SetUser(userUuid, userRepository);

        return privateAssetHolding;
    }
}
