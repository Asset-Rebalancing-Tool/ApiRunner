package ARApi.Scaffold.Endpoints.Requests;

import ARApi.Scaffold.Database.Entities.PrivateAsset.PrivateOwnedAsset;
import ARApi.Scaffold.Database.Repos.PrivateOwnedAssetRepository;
import ARApi.Scaffold.Database.Repos.PublicOwnedAssetRepository;
import ARApi.Scaffold.Endpoints.Validators.AssetValidator;
import ARApi.Scaffold.Shared.Enums.AssetType;
import ARApi.Scaffold.Shared.Enums.Currency;
import ARApi.Scaffold.Shared.Enums.UnitType;
import liquibase.pro.packaged.P;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

public class PostOwnedPrivateAssetRequest {

    public List<String> customCategories;

    public double ownedQuantity;

    public double targetPercentage;

    public AssetType assetType;

    public double currentPrice;

    public UnitType unitType;

    public Currency currency;

    public String title;

    public PrivateOwnedAsset toPrivateOwnedAsset(UUID userUuid, PrivateOwnedAssetRepository privateOwnedAssetRepository, PublicOwnedAssetRepository publicOwnedAssetRepository){
        PrivateOwnedAsset privateOwnedAsset = new PrivateOwnedAsset();
        privateOwnedAsset.asset_type = assetType;
        privateOwnedAsset.title = title;
        privateOwnedAsset.unit_type = unitType;
        privateOwnedAsset.target_percentage = targetPercentage;
        privateOwnedAsset.owned_quantity = ownedQuantity;
        privateOwnedAsset.price = currentPrice;
        privateOwnedAsset.currency = currency;
        privateOwnedAsset.user_uuid = userUuid;

        AssetValidator.ThrowExceptionOnCurrencyMismatch(currency, userUuid, publicOwnedAssetRepository, privateOwnedAssetRepository);

        return privateOwnedAsset;
    }
}
