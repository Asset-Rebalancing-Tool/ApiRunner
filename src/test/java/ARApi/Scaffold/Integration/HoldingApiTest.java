package ARApi.Scaffold.Integration;

import ARApi.Scaffold.AuthTestUserService;
import ARApi.Scaffold.BaseIntegrationTest;
import ARApi.Scaffold.Database.Entities.PublicAsset.PublicAsset;
import ARApi.Scaffold.Database.Entities.PublicAsset.PublicAssetHolding;
import ARApi.Scaffold.Database.Repos.PublicAssetRepository;
import ARApi.Scaffold.Endpoints.Model.ModelAssetHoldingGroup;
import ARApi.Scaffold.Endpoints.Model.ModelPrivateAssetHolding;
import ARApi.Scaffold.Endpoints.Model.ModelPrivateCategory;
import ARApi.Scaffold.Endpoints.Model.ModelPublicAssetHolding;
import ARApi.Scaffold.Endpoints.Requests.*;
import ARApi.Scaffold.Shared.Enums.AssetType;
import ARApi.Scaffold.Shared.Enums.Currency;
import liquibase.repackaged.org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.ArrayDeque;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.fail;



public class HoldingApiTest extends BaseIntegrationTest {

    @Autowired
    private PublicAssetRepository publicAssetRepository;
    
    @Test
    public void patchHoldings(){
        PublicAssetHolding();
        PrivateAssetHolding();

        var publicHoldings = authService.AddAuth(webTestClient.get().uri("/holding_api/asset_holding/public")).exchange().expectBody(ModelPublicAssetHolding[].class).returnResult().getResponseBody();
        if(publicHoldings.length < 2){
            fail("too little assets in test db to perform");
        }

        var privateHoldings = authService.AddAuth(webTestClient.get().uri("/holding_api/asset_holding/private")).exchange().expectBody(ModelPrivateAssetHolding[].class).returnResult().getResponseBody();
        if(privateHoldings.length < 2){
            fail("too little assets in test db to perform");
        }
    }

    @Test
    public void BothHoldingsAndHoldingGroups(){
        PublicAssetHolding();

        // get inserted holdings
        var publicHoldings = authService.AddAuth(webTestClient.get().uri("/holding_api/asset_holding/public")).exchange().expectBody(ModelPublicAssetHolding[].class).returnResult().getResponseBody();

        var request = new PostAssetHoldingGroupRequest();

        if(publicHoldings.length < 2){
            fail("too little assets in test db to perform");
        }
        request.publicAssetHoldingUuids = Arrays.stream(publicHoldings).map(pa -> pa.holdingUuid).toArray(String[]::new);
        request.privateAssetHoldingUuids = new String[]{};
        request.groupName = "testgroup";
        request.targetPercentage = 50;

        // post
        var holdingGroup = authService.AddAuth(webTestClient.post().uri("/holding_api/asset_holding/group")).body(BodyInserters.fromValue(request))
                .exchange().expectBody(ModelAssetHoldingGroup.class).returnResult().getResponseBody();
        Assert.notNull(holdingGroup, "holding group can't be null");

        // get
        var holdingGroups = authService.AddAuth(webTestClient.get().uri("/holding_api/asset_holding/group")).exchange()
                .expectBody(ModelAssetHoldingGroup[].class).returnResult().getResponseBody();
        Assert.notEmpty(holdingGroups, "holding groups should at least return the one created");

        // delete
        authService.AddAuth(webTestClient.delete().uri("/holding_api/asset_holding/group/" + holdingGroups[0].uuid)).exchange()
                .expectStatus().isOk();
    }

    @Test
    public void PrivateAssetHolding(){
        var postRequest = new PrivateAssetHoldingRequest();
        postRequest.assetType = AssetType.Etf;
        postRequest.currentPrice = 20d;
        postRequest.title = "test private asset";

        var endpoint = "/holding_api/asset_holding/private";

        // post
        var postedHolding = authService.AddAuth(webTestClient.post().uri(endpoint))
                .body(BodyInserters.fromValue(postRequest))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ModelPrivateAssetHolding.class)
                .returnResult().getResponseBody();

        Assert.notNull(postedHolding, "posted holding is null");

        // post name conflict
        authService.AddAuth(webTestClient.post().uri(endpoint)).body(BodyInserters.fromValue(postRequest))
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT);

        // post working second
        postRequest.title = "heuhuee";
        authService.AddAuth(webTestClient.post().uri(endpoint))
                .body(BodyInserters.fromValue(postRequest))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ModelPrivateAssetHolding.class)
                .returnResult().getResponseBody();

        // get
        var holdings = authService.AddAuth(webTestClient.get().uri(endpoint))
                .exchange()
                .expectStatus().isOk()
                .expectBody(ModelPrivateAssetHolding[].class).returnResult().getResponseBody();
        Assert.notEmpty(holdings, "holdings are empty");

        // delete
        authService.AddAuth(webTestClient.delete().uri(endpoint + "/" + postedHolding.holdingUuid))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void PrivateCategory() {
        var request = new PrivateCategoryRequest();
        request.categoryName = "testcat";

        // post
        authService.AddAuth(webTestClient.post().uri("/holding_api/category"))
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus().isCreated();

        // get
        var result = authService.AddAuth(webTestClient.get().uri("/holding_api/category"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(ModelPrivateCategory[].class).returnResult().getResponseBody();

        Assert.notEmpty(result, "Categories empty even tho one just got posted");

        // delete
        authService.AddAuth(webTestClient.delete().uri("/holding_api/category/" + result[0].categoryUuid))
                .exchange()
                .expectStatus().isOk();
    }

    private PublicAsset ForceAssetFlags(PublicAsset asset, Currency currency){
        if(!asset.getAvailableCurrencies().contains(currency)){
            // rewrite random record to eur
            asset.AssetPriceRecords.stream().findAny().get().currency = currency;
            asset = publicAssetRepository.saveAndFlush(asset);
            Assert.isTrue(asset.getAvailableCurrencies().contains(currency), "Currency of record was not updated: " + currency.toString());
        }
        return asset;
    }

    public void PublicAssetHolding(){
        var assets = new ArrayDeque<>(publicAssetRepository.GetFullAssets());
        if(assets.size() < 4){
            fail("too little assets in test db to perform");
        }
        var mainCurrency = Currency.EUR;
        var subCurrency = Currency.USD;

        var secondAssetToPublish = assets.removeFirst();
        ForceAssetFlags(secondAssetToPublish, mainCurrency);
        // find asset with other currency or create one

        var firstAsset = assets.removeFirst();
        var postAssetHoldingRequest = new PublicAssetHoldingRequest();
        postAssetHoldingRequest.publicAssetUuid = ForceAssetFlags(firstAsset, mainCurrency).uuid.toString();
        postAssetHoldingRequest.currency = mainCurrency;
        postAssetHoldingRequest.customName = "testname";
        postAssetHoldingRequest.shouldDisplayCustomName = true;
        postAssetHoldingRequest.targetPercentage = 10d;
        postAssetHoldingRequest.ownedQuantity = 10d;
        postAssetHoldingRequest.selectedUnitType = firstAsset.unit_type;

        // post
        authService.AddAuth(webTestClient.post().uri("/holding_api/asset_holding/public"))
                .body(BodyInserters.fromValue(postAssetHoldingRequest))
                .exchange()
                .expectStatus().isCreated();

        // post duplicate
        authService.AddAuth(webTestClient.post().uri("/holding_api/asset_holding/public"))
                .body(BodyInserters.fromValue(postAssetHoldingRequest))
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT);

        // post different currency
        postAssetHoldingRequest.currency = subCurrency;
        postAssetHoldingRequest.publicAssetUuid = ForceAssetFlags(assets.removeFirst(), subCurrency).uuid.toString();
        authService.AddAuth(webTestClient.post().uri("/holding_api/asset_holding/public"))
                .body(BodyInserters.fromValue(postAssetHoldingRequest))
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT);

        // post same success
        var sameSuccess = ForceAssetFlags(assets.removeFirst(), mainCurrency);
        postAssetHoldingRequest.currency = mainCurrency;
        postAssetHoldingRequest.selectedUnitType = sameSuccess.unit_type;
        postAssetHoldingRequest.publicAssetUuid = sameSuccess.uuid.toString();
        authService.AddAuth(webTestClient.post().uri("/holding_api/asset_holding/public"))
                .body(BodyInserters.fromValue(postAssetHoldingRequest))
                .exchange()
                .expectStatus().isCreated();

        // post another success
        var anotherSuccess = ForceAssetFlags(assets.removeFirst(), mainCurrency);
        postAssetHoldingRequest.currency = mainCurrency;
        postAssetHoldingRequest.selectedUnitType = anotherSuccess.unit_type;
        postAssetHoldingRequest.publicAssetUuid = anotherSuccess.uuid.toString();
        authService.AddAuth(webTestClient.post().uri("/holding_api/asset_holding/public"))
                .body(BodyInserters.fromValue(postAssetHoldingRequest))
                .exchange()
                .expectStatus().isCreated();

        // get
        var holdings = authService.AddAuth(webTestClient.get().uri("/holding_api/asset_holding/public"))
                .exchange()
                .expectStatus().isOk()
                .expectBody( ModelPublicAssetHolding[].class)
                .returnResult().getResponseBody();
        Assert.notNull(holdings, "holdings shouldn`t be null");
        Assert.notEmpty(holdings, "posted holding should be returned");

        // delete
        authService.AddAuth(webTestClient.delete().uri("/holding_api/asset_holding/public/" + Arrays.stream(holdings).findAny().get().holdingUuid))
                .exchange()
                .expectStatus().isOk();
    }
}
