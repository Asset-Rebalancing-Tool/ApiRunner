package ARApi.Scaffold.Endpoints.Requests;

import ARApi.Scaffold.Database.Entities.PublicAsset.PublicOwnedAsset;
import ARApi.Scaffold.Database.Repos.PrivateOwnedAssetRepository;
import ARApi.Scaffold.Database.Repos.PublicAssetRepository;
import ARApi.Scaffold.Database.Repos.PublicOwnedAssetRepository;
import ARApi.Scaffold.Endpoints.Validators.AssetValidator;
import ARApi.Scaffold.Shared.Enums.Currency;
import ARApi.Scaffold.Shared.Enums.UnitType;
import liquibase.pro.packaged.P;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PostOwnedPublicAssetRequest {

    public String publicAssetUuid;

    public String customName;

    public List<String> customCategories;

    public double ownedQuantity;

    public double targetPercentage;

    public UnitType desiredUnitType;

    public boolean shouldDisplayCustomName;

    public Currency currency;

    public PublicOwnedAsset toPublicOwnedAsset(UUID userUuid, PublicOwnedAssetRepository publicOwnedAssetRepository, PublicAssetRepository publicAssetRepository, PrivateOwnedAssetRepository privateOwnedAssetRepository){

        var publicAsset = publicAssetRepository.findById(UUID.fromString(publicAssetUuid)).orElseThrow();

        if(!publicAsset.getAvailableCurrencies().contains(currency)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Provided currency not available for asset. Available are: " + publicAsset.getAvailableCurrencies().stream().map(Currency::toString).collect(Collectors.joining(", ")));
        }

        AssetValidator.ThrowExceptionOnCurrencyMismatch(currency, userUuid, publicOwnedAssetRepository, privateOwnedAssetRepository);

        var publicOwnedAsset = new PublicOwnedAsset();
        publicOwnedAsset.user_uuid = userUuid;

        publicOwnedAsset.public_asset = publicAsset;
        publicOwnedAsset.target_percentage = targetPercentage;
        publicOwnedAsset.owned_quantity = ownedQuantity;
        publicOwnedAsset.display_custom_name = shouldDisplayCustomName;
        publicOwnedAsset.custom_name = customName;
        publicOwnedAsset.currency = currency;

        return publicOwnedAsset;
    }
}
