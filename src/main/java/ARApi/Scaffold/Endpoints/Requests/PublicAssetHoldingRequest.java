package ARApi.Scaffold.Endpoints.Requests;

import ARApi.Scaffold.Database.Entities.PublicAsset.HoldingOrigin;
import ARApi.Scaffold.Database.Entities.PublicAsset.PublicAssetHolding;
import ARApi.Scaffold.Database.Repos.PrivateAssetHoldingRepository;
import ARApi.Scaffold.Database.Repos.PublicAssetHoldingRepository;
import ARApi.Scaffold.Database.Repos.PublicAssetRepository;
import ARApi.Scaffold.Database.Repos.UserRepository;
import ARApi.Scaffold.Shared.Enums.Currency;
import ARApi.Scaffold.Shared.Enums.UnitType;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PublicAssetHoldingRequest {

    public String publicAssetUuid;

    public String customName;

    public Double ownedQuantity;

    public Double targetPercentage;

    public Boolean shouldDisplayCustomName;

    public Currency currency;

    public UnitType selectedUnitType;

    public PublicAssetHolding patchAssetHolding(PublicAssetHolding publicAssetHolding){
        setEditableFields(publicAssetHolding);
        return publicAssetHolding;
    }

    public PublicAssetHolding toPublicAssetHolding(UUID userUuid, UserRepository userRepository, PublicAssetHoldingRepository publicAssetHoldingsRepo, PublicAssetRepository publicAssetRepository, PrivateAssetHoldingRepository privateOwnedAssetRepository){

        var publicAsset = publicAssetRepository.findById(UUID.fromString(publicAssetUuid)).orElseThrow();

        // validate selected currency
        if(currency != null && !publicAsset.getAvailableCurrencies().contains(currency)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "SELECTED_CURRENCY_NOT_AVAILABLE available are: " + publicAsset.getAvailableCurrencies().stream().map(Currency::toString).collect(Collectors.joining(", ")));
        }

        // validate selected unit type
        if(selectedUnitType != null && !List.of(publicAsset.unit_type.GetConvertibleUnitTypes()).contains(selectedUnitType)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "SELECTED_UNIT_TYPE_NOT_AVAILABLE - available are: " + Stream.of(publicAsset.unit_type.GetConvertibleUnitTypes()).map(UnitType::toString).collect(Collectors.joining(", ")));
        }

        // validate currency collision with other asset holdings
        var publicAssetHoldings = publicAssetHoldingsRepo.GetAssetsOfUser(userUuid);
        if (currency != null && !publicAssetHoldings.isEmpty() && publicAssetHoldings.get(0).selected_currency != currency){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CURRENCY_MISMATCH");
        }

        // create the db holding
        var publicOwnedAsset = new PublicAssetHolding();
        publicOwnedAsset.SetUser(userUuid, userRepository);

        publicOwnedAsset.public_asset = publicAsset;
        publicOwnedAsset.holding_origin = HoldingOrigin.ManualEntry;

        setEditableFields(publicOwnedAsset);

        return publicOwnedAsset;
    }

    private void setEditableFields(PublicAssetHolding assetHolding){

        var notEditableFieldsSpecified = ownedQuantity != null || selectedUnitType != null || currency != null;
        if(assetHolding.holding_origin != HoldingOrigin.ManualEntry && notEditableFieldsSpecified){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot patch some specified fields on non manual holding");
        }
        if(ownedQuantity != null) assetHolding.owned_quantity = ownedQuantity;
        if(currency != null) assetHolding.selected_currency = currency;
        if(selectedUnitType != null) assetHolding.selected_unit_type = selectedUnitType;

        if(targetPercentage != null) assetHolding.target_percentage = targetPercentage;
        if(shouldDisplayCustomName != null) assetHolding.display_custom_name = shouldDisplayCustomName;
        if(customName != null)assetHolding.custom_name = customName;
    }
}
