package ARApi.Scaffold.Endpoints.Validators;

import ARApi.Scaffold.Database.Repos.PrivateOwnedAssetRepository;
import ARApi.Scaffold.Database.Repos.PublicOwnedAssetRepository;
import ARApi.Scaffold.Shared.Enums.Currency;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


import java.util.UUID;

public class AssetValidator {

    public static void ThrowExceptionOnCurrencyMismatch(Currency currency, UUID userUuid, PublicOwnedAssetRepository publicOwnedAssetRepository, PrivateOwnedAssetRepository privateOwnedAssetRepository){
        // validate that there is no currency conflict with other owned assets
        var publicOwnedAssets = publicOwnedAssetRepository.GetAssetsOfUser(userUuid);
        if (!publicOwnedAssets.isEmpty() && publicOwnedAssets.get(0).currency != currency){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "public owned assets with different currency exist");
        }
        var privateOwnedAssets = privateOwnedAssetRepository.GetAssetsOfUser(userUuid);
        if (!privateOwnedAssets.isEmpty() && privateOwnedAssets.get(0).currency != currency){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "private owned assets with different currency exist");
        }
    }
}
