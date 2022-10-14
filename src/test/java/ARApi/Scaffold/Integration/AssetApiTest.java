package ARApi.Scaffold.Integration;


import ARApi.Scaffold.BaseIntegrationTest;
import ARApi.Scaffold.Endpoints.Model.ModelPublicAsset;
import ARApi.Scaffold.Endpoints.Requests.SearchAssetRequest;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.BodyInserters;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

public class AssetApiTest extends BaseIntegrationTest {

    @Test
    public void AddAssetsViaSearch(){
        var searchParams = new String[]{"spdr", "microsoft", "starbucks", "apple", "amazon"};
        var assets = new HashSet<ModelPublicAsset>();

        for(var searchParam : searchParams){
            var request = new SearchAssetRequest();
            request.SearchString = searchParam;

            var fetchedAssets = authService.AddAuth(webTestClient.post().uri("/asset_api/asset/search")).body(BodyInserters.fromValue(request))
                    .exchange().expectStatus().is2xxSuccessful().expectBody(ModelPublicAsset[].class).returnResult().getResponseBody();
            Assert.notNull(fetchedAssets, "fetched assets can not be null, only empty allowed");
            assets.addAll(Arrays.stream(fetchedAssets).toList());
        }

        Assert.notEmpty(assets, "should find at least one asset");
    }

    @Test
    public void MaxMinSearchLength(){
        var request = new SearchAssetRequest();
        request.SearchString = "ts";

        authService.AddAuth(webTestClient.post().uri("/asset_api/asset/search")).body(BodyInserters.fromValue(request))
                .exchange().expectStatus().isBadRequest();

        byte[] byteArray = new byte[300]; new Random().nextBytes(byteArray);

        request.SearchString = new String(byteArray, StandardCharsets.UTF_8);

        authService.AddAuth(webTestClient.post().uri("/asset_api/asset/search")).body(BodyInserters.fromValue(request))
                .exchange().expectStatus().isBadRequest();
    }
}
