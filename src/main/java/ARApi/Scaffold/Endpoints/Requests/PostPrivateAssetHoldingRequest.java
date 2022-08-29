package ARApi.Scaffold.Endpoints.Requests;

import ARApi.Scaffold.Database.Entities.PrivateAsset.PrivateAssetHolding;
import ARApi.Scaffold.Database.Repos.PrivateAssetHoldingRepository;
import ARApi.Scaffold.Database.Repos.PublicAssetHoldingRepository;
import ARApi.Scaffold.Database.Repos.UserRepository;
import ARApi.Scaffold.Endpoints.Validators.HoldingValidator;
import ARApi.Scaffold.Shared.Enums.AssetType;
import ARApi.Scaffold.Shared.Enums.Currency;
import ARApi.Scaffold.Shared.Enums.UnitType;

import java.util.List;
import java.util.UUID;

public class PostPrivateAssetHoldingRequest {

    public List<String> customCategories;

    public double ownedQuantity;

    public double targetPercentage;

    public AssetType assetType;

    public double currentPrice;

    public UnitType unitType;

    public Currency currency;

    public String title;

    public PrivateAssetHolding toPrivateAssetHolding(UUID userUuid, UserRepository userRepository, PrivateAssetHoldingRepository privateOwnedAssetRepository, PublicAssetHoldingRepository publicOwnedAssetRepository){
        PrivateAssetHolding privateAssetHolding = new PrivateAssetHolding();
        privateAssetHolding.asset_type = assetType;
        privateAssetHolding.title = title;
        privateAssetHolding.unit_type = unitType;
        privateAssetHolding.target_percentage = targetPercentage;
        privateAssetHolding.owned_quantity = ownedQuantity;
        privateAssetHolding.price = currentPrice;
        privateAssetHolding.currency = currency;
        privateAssetHolding.SetUser(userUuid, userRepository);

        HoldingValidator.ThrowExceptionOnCurrencyMismatch(currency, userUuid, publicOwnedAssetRepository, privateOwnedAssetRepository);

        return privateAssetHolding;
    }
}
