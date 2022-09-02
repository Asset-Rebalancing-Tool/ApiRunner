package ARApi.Scaffold.Integration;

import ARApi.Scaffold.Database.Entities.PublicAsset.PublicAsset;
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
        PublicAssetHolding();

        // get inserted holdings
        var publicHoldings = AddAuth(webTestClient.get().uri("/holding_api/asset_holding/public")).exchange().expectBody(ModelPublicAssetHolding[].class).returnResult().getResponseBody();

        var request = new PostAssetHoldingGroupRequest();

        if(publicHoldings.length < 2){
            fail("too little assets in test db to perform");
        }
        request.publicAssetHoldingUuids = Arrays.stream(publicHoldings).map(pa -> pa.holdingUuid).toArray(String[]::new);
        request.privateAssetHoldingUuids = new String[]{};
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

    @Test
    public void PrivateAssetHolding(){
        var postRequest = new PrivateAssetHoldingRequest();
        postRequest.assetType = AssetType.Etf;
        postRequest.currentPrice = 20;
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

        // post name conflict
        AddAuth(webTestClient.post().uri(endpoint)).body(BodyInserters.fromValue(postRequest))
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT);

        // post working second
        postRequest.title = "heuhuee";
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
        AddAuth(webTestClient.post().uri("/holding_api/category"))
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus().isCreated();

        // get
        var result = AddAuth(webTestClient.get().uri("/holding_api/category"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(ModelPrivateCategory[].class).returnResult().getResponseBody();

        Assert.notEmpty(result, "Categories empty even tho one just got posted");

        // delete
        AddAuth(webTestClient.delete().uri("/holding_api/category/" + result[0].categoryUuid))
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
        var postAssetHoldingRequest = new PostPublicAssetHoldingRequest();
        postAssetHoldingRequest.publicAssetUuid = ForceAssetFlags(firstAsset, mainCurrency).uuid.toString();
        postAssetHoldingRequest.currency = mainCurrency;
        postAssetHoldingRequest.customName = "testname";
        postAssetHoldingRequest.shouldDisplayCustomName = true;
        postAssetHoldingRequest.targetPercentage = 10;
        postAssetHoldingRequest.ownedQuantity = 10;
        postAssetHoldingRequest.selectedUnitType = firstAsset.unit_type;

        // post
        AddAuth(webTestClient.post().uri("/holding_api/asset_holding/public"))
                .body(BodyInserters.fromValue(postAssetHoldingRequest))
                .exchange()
                .expectStatus().isCreated();

        // post duplicate
        AddAuth(webTestClient.post().uri("/holding_api/asset_holding/public"))
                .body(BodyInserters.fromValue(postAssetHoldingRequest))
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT);

        // post different currency
        postAssetHoldingRequest.currency = subCurrency;
        postAssetHoldingRequest.publicAssetUuid = ForceAssetFlags(assets.removeFirst(), subCurrency).uuid.toString();
        AddAuth(webTestClient.post().uri("/holding_api/asset_holding/public"))
                .body(BodyInserters.fromValue(postAssetHoldingRequest))
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT);

        // post same success
        var sameSuccess = ForceAssetFlags(assets.removeFirst(), mainCurrency);
        postAssetHoldingRequest.currency = mainCurrency;
        postAssetHoldingRequest.selectedUnitType = sameSuccess.unit_type;
        postAssetHoldingRequest.publicAssetUuid = sameSuccess.uuid.toString();
        AddAuth(webTestClient.post().uri("/holding_api/asset_holding/public"))
                .body(BodyInserters.fromValue(postAssetHoldingRequest))
                .exchange()
                .expectStatus().isCreated();

        // post another success
        var anotherSuccess = ForceAssetFlags(assets.removeFirst(), mainCurrency);
        postAssetHoldingRequest.currency = mainCurrency;
        postAssetHoldingRequest.selectedUnitType = anotherSuccess.unit_type;
        postAssetHoldingRequest.publicAssetUuid = anotherSuccess.uuid.toString();
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
