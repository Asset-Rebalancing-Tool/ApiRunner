package ARApi.Scaffold.Endpoints.Requests;

import ARApi.Scaffold.Database.Entities.PublicAsset.PublicAssetHolding;
import ARApi.Scaffold.Database.Repos.PrivateAssetHoldingRepository;
import ARApi.Scaffold.Database.Repos.PublicAssetRepository;
import ARApi.Scaffold.Database.Repos.PublicAssetHoldingRepository;
import ARApi.Scaffold.Database.Repos.UserRepository;
import ARApi.Scaffold.Endpoints.Validators.AssetValidator;
import ARApi.Scaffold.Shared.Enums.Currency;
import ARApi.Scaffold.Shared.Enums.UnitType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PostPublicAssetHoldingRequest {

    public String publicAssetUuid;

    public String customName;

    public List<String> customCategories;

    public double ownedQuantity;

    public double targetPercentage;

    public UnitType desiredUnitType;

    public boolean shouldDisplayCustomName;

    public Currency currency;

    public PublicAssetHolding toPublicAssetHolding(UUID userUuid, UserRepository userRepository, PublicAssetHoldingRepository publicOwnedAssetRepository, PublicAssetRepository publicAssetRepository, PrivateAssetHoldingRepository privateOwnedAssetRepository){

        var publicAsset = publicAssetRepository.findById(UUID.fromString(publicAssetUuid)).orElseThrow();

        if(!publicAsset.getAvailableCurrencies().contains(currency)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provided currency not available for asset. Available are: " + publicAsset.getAvailableCurrencies().stream().map(Currency::toString).collect(Collectors.joining(", ")));
        }

        AssetValidator.ThrowExceptionOnCurrencyMismatch(currency, userUuid, publicOwnedAssetRepository, privateOwnedAssetRepository);

        var publicOwnedAsset = new PublicAssetHolding();
        publicOwnedAsset.SetUser(userUuid, userRepository);

        publicOwnedAsset.public_asset = publicAsset;
        publicOwnedAsset.target_percentage = targetPercentage;
        publicOwnedAsset.owned_quantity = ownedQuantity;
        publicOwnedAsset.display_custom_name = shouldDisplayCustomName;
        publicOwnedAsset.custom_name = customName;
        publicOwnedAsset.currency = currency;

        return publicOwnedAsset;
    }
}
