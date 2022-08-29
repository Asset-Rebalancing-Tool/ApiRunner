package ARApi.Scaffold.Endpoints.Validators;

import ARApi.Scaffold.Database.Repos.PrivateAssetHoldingRepository;
import ARApi.Scaffold.Database.Repos.PublicAssetHoldingRepository;
import ARApi.Scaffold.Shared.Enums.Currency;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;


import java.util.UUID;

public class HoldingValidator {

    public static void ThrowExceptionOnCurrencyMismatch(Currency currency, UUID userUuid, PublicAssetHoldingRepository publicAssetHoldingsRepo, PrivateAssetHoldingRepository privateAssetHoldingRepository){
        // validate that there is no currency conflict with other owned assets
        var publicAssetHoldings = publicAssetHoldingsRepo.GetAssetsOfUser(userUuid);
        if (!publicAssetHoldings.isEmpty() && publicAssetHoldings.get(0).currency != currency){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CURRENCY_MISSMATCH");
        }
        var privateAssetHoldings = privateAssetHoldingRepository.GetAssetsOfUser(userUuid);
        if (!privateAssetHoldings.isEmpty() && privateAssetHoldings.get(0).currency != currency){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "CURRENCY_MISSMATCH");
        }
    }
}
