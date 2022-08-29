package ARApi.Scaffold.Integration;

import ARApi.Scaffold.Database.Entities.PublicAsset.PublicAsset;
import ARApi.Scaffold.Database.Entities.PublicAsset.PublicAssetHolding;
import ARApi.Scaffold.Database.Repos.AssetHoldingGroupRepository;
import ARApi.Scaffold.Database.Repos.PrivateAssetHoldingRepository;
import ARApi.Scaffold.Database.Repos.PublicAssetHoldingRepository;
import ARApi.Scaffold.Database.Repos.PublicAssetRepository;
import ARApi.Scaffold.Endpoints.Model.ModelAssetHoldingGroup;
import ARApi.Scaffold.Endpoints.Model.ModelPrivateAssetHolding;
import ARApi.Scaffold.Endpoints.Model.ModelPrivateCategory;
import ARApi.Scaffold.Endpoints.Model.ModelPublicAssetHolding;
import ARApi.Scaffold.Endpoints.Requests.*;
import ARApi.Scaffold.Shared.Enums.AssetType;
import ARApi.Scaffold.Shared.Enums.Currency;
import ARApi.Scaffold.Shared.Enums.UnitType;
import liquibase.repackaged.org.apache.commons.lang3.RandomStringUtils;
import org.checkerframework.checker.units.qual.A;
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
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.fail;


@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HoldingApiTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private PublicAssetRepository publicAssetRepository;

    private static String token = null;

    @BeforeEach
    public void RegisterAndGetToken(){
        if(token != null) return;

        var authRequest = new AuthRequest();
        authRequest.email = RandomStringUtils.randomAlphanumeric(10) + "@test.com";
        authRequest.password = "testpassword";

        webTestClient.post().uri("/auth_api/register")
                .contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(authRequest))
                .exchange().expectStatus().isCreated();

        var result = webTestClient.post().uri("/auth_api/login")
                .body(BodyInserters.fromValue(authRequest)).exchange().expectStatus().isOk().expectBody(String.class);

        token = "Bearer " + result.returnResult().getResponseBody();
    }

    private  WebTestClient.RequestBodySpec AddAuth(WebTestClient.RequestBodySpec spec){
        return spec.header("Authorization", token);
    }
    private <T extends WebTestClient.RequestHeadersSpec<T>> WebTestClient.RequestHeadersSpec<T> AddAuth(WebTestClient.RequestHeadersSpec<T> spec){
        return spec.header("Authorization", token);
    }
    @Test
    public void BothHoldingsAndHoldingGroups(){
        PrivateAssetHolding();
        PublicAssetHolding();

        var publicHoldings = AddAuth(webTestClient.get().uri("/holding_api/asset_holding/public")).exchange().expectBody(ModelPublicAssetHolding[].class).returnResult().getResponseBody();
        var privateHoldings = AddAuth(webTestClient.get().uri("/holding_api/asset_holding/private")).exchange().expectBody(ModelPrivateAssetHolding[].class).returnResult().getResponseBody();

        var request = new PostAssetHoldingGroupRequest();

        if(publicHoldings.length < 2){
            fail("too little assets in test db to perform");
        }
        if(privateHoldings.length < 2){
            fail("too little assets in test db to perform");
        }
        request.publicAssetHoldingUuids = Arrays.stream(publicHoldings).map(pa -> pa.holdingUuid).toArray(String[]::new);
        request.privateAssetHoldingUuids = Arrays.stream(privateHoldings).map(pa -> pa.holdingUuid).toArray(String[]::new);
        request.groupName = "testgroup";
        request.targetPercentage = 50;

        // post
        var holdingGroup = AddAuth(webTestClient.post().uri("/holding_api/asset_holding/group")).body(BodyInserters.fromValue(request))
                .exchange().expectBody(ModelAssetHoldingGroup.class).returnResult().getResponseBody();
        Assert.notNull(holdingGroup, "holding group can't be null");

        // get
        var holdingGroups = AddAuth(webTestClient.get().uri("/holding_api/asset_holding/group")).exchange()
                .expectBody(ModelAssetHoldingGroup[].class).returnResult().getResponseBody();
        Assert.notEmpty(holdingGroups, "holding groups should at least return the one created");

        // delete
        AddAuth(webTestClient.delete().uri("/holding_api/asset_holding/group/" + holdingGroups[0].uuid)).exchange()
                .expectStatus().isOk();
    }

    public void PrivateAssetHolding(){
        var postRequest = new PostPrivateAssetHoldingRequest();
        postRequest.assetType = AssetType.Etf;
        postRequest.ownedQuantity = 10;
        postRequest.targetPercentage = 10;
        postRequest.currentPrice = 20;
        postRequest.unitType = UnitType.GetAvailableUnitTypes(postRequest.assetType).get(0);
        postRequest.currency = Currency.EUR;
        postRequest.title = "test private asset";

        var endpoint = "/holding_api/asset_holding/private";

        // post
        var postedHolding = AddAuth(webTestClient.post().uri(endpoint))
                .body(BodyInserters.fromValue(postRequest))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ModelPrivateAssetHolding.class)
                .returnResult().getResponseBody();

        Assert.notNull(postedHolding, "posted holding is null");

        // post currency conflict
        postRequest.currency = Currency.USD;
        AddAuth(webTestClient.post().uri(endpoint)).body(BodyInserters.fromValue(postRequest))
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT);

        // post working second
        postRequest.currency = Currency.EUR;
        postRequest.title = "heuhuee";
        AddAuth(webTestClient.post().uri(endpoint))
                .body(BodyInserters.fromValue(postRequest))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ModelPrivateAssetHolding.class)
                .returnResult().getResponseBody();
        AddAuth(webTestClient.post().uri(endpoint))
                .body(BodyInserters.fromValue(postRequest))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ModelPrivateAssetHolding.class)
                .returnResult().getResponseBody();

        // get
        var holdings = AddAuth(webTestClient.get().uri(endpoint))
                .exchange()
                .expectStatus().isOk()
                .expectBody(ModelPrivateAssetHolding[].class).returnResult().getResponseBody();
        Assert.notEmpty(holdings, "holdings are empty");

        // delete
        AddAuth(webTestClient.delete().uri(endpoint + "/" + postedHolding.holdingUuid))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void PrivateCategory() {
        var request = new PrivateCategoryRequest();
        request.categoryName = "testcat";

        // post
        webTestClient.post().uri("/holding_api/category")
                .header("Authorization", token)
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus().isCreated();

        // get
        var result = webTestClient.get().uri("/holding_api/category")
                .header("Authorization", token)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ModelPrivateCategory[].class).returnResult().getResponseBody();

        Assert.notEmpty(result, "Categories empty even tho one just got posted");

        // delete
        webTestClient.delete().uri("/holding_api/category/" + result[0].categoryUuid)
                .header("Authorization", token)
                .exchange()
                .expectStatus().isOk();
    }

    private PublicAsset ForceCurrencyIntoAsset(PublicAsset asset, Currency currency){
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
        ForceCurrencyIntoAsset(secondAssetToPublish, mainCurrency);
        // find asset with other currency or create one

        var postAssetHoldingRequest = new PostPublicAssetHoldingRequest();
        postAssetHoldingRequest.publicAssetUuid = ForceCurrencyIntoAsset(assets.removeFirst(), mainCurrency).uuid.toString();
        postAssetHoldingRequest.currency = mainCurrency;
        postAssetHoldingRequest.customName = "testname";
        postAssetHoldingRequest.shouldDisplayCustomName = true;
        postAssetHoldingRequest.targetPercentage = 10;
        postAssetHoldingRequest.ownedQuantity = 10;

        // post
        AddAuth(webTestClient.post().uri("/holding_api/asset_holding/public"))
                .body(BodyInserters.fromValue(postAssetHoldingRequest))
                .exchange()
                .expectStatus().isCreated();
        // post duplicate
        AddAuth(webTestClient.post().uri("/holding_api/asset_holding/public"))
                .body(BodyInserters.fromValue(postAssetHoldingRequest))
                .exchange()
                .expectStatus().isBadRequest();

        // post different currency
        postAssetHoldingRequest.currency = subCurrency;
        postAssetHoldingRequest.publicAssetUuid = ForceCurrencyIntoAsset(assets.removeFirst(), subCurrency).uuid.toString();
        AddAuth(webTestClient.post().uri("/holding_api/asset_holding/public"))
                .body(BodyInserters.fromValue(postAssetHoldingRequest))
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT);

        // post same x2
        postAssetHoldingRequest.currency = mainCurrency;
        postAssetHoldingRequest.publicAssetUuid = ForceCurrencyIntoAsset(assets.removeFirst(), mainCurrency).uuid.toString();
        AddAuth(webTestClient.post().uri("/holding_api/asset_holding/public"))
                .body(BodyInserters.fromValue(postAssetHoldingRequest))
                .exchange()
                .expectStatus().isCreated();

        postAssetHoldingRequest.publicAssetUuid = ForceCurrencyIntoAsset(assets.removeFirst(), mainCurrency).uuid.toString();
        AddAuth(webTestClient.post().uri("/holding_api/asset_holding/public"))
                .body(BodyInserters.fromValue(postAssetHoldingRequest))
                .exchange()
                .expectStatus().isCreated();

        // get
        var holdings = AddAuth(webTestClient.get().uri("/holding_api/asset_holding/public"))
                .exchange()
                .expectStatus().isOk()
                .expectBody( ModelPublicAssetHolding[].class)
                .returnResult().getResponseBody();
        Assert.notNull(holdings, "holdings shouldn`t be null");
        Assert.notEmpty(holdings, "posted holding should be returned");

        // delete
        AddAuth(webTestClient.delete().uri("/holding_api/asset_holding/public/" + Arrays.stream(holdings).findAny().get().holdingUuid))
                .exchange()
                .expectStatus().isOk();
    }
}
