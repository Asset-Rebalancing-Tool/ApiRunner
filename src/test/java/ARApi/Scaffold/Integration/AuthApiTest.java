package ARApi.Scaffold.Integration;

import ARApi.Scaffold.Constants;
import ARApi.Scaffold.Endpoints.Requests.AuthRequest;
import ARApi.Scaffold.Endpoints.Requests.SearchAssetRequest;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import liquibase.repackaged.org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.BodyInserters;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthApiTest {

    @Autowired
    private WebTestClient webTestClient;
        
    @Test
    public void TestAuthorizationInEffect(){
        SearchAssetRequest searchAssetRequest = new SearchAssetRequest();
        searchAssetRequest.SearchString = "spdr";

        webTestClient.post().uri("/asset_api/asset/search").body(BodyInserters.fromValue(searchAssetRequest)).exchange().expectStatus().isUnauthorized();
    }

    @Test
    public void DuplicateRegister() {
        var authRequest = new AuthRequest();
        authRequest.email = RandomStringUtils.randomAlphanumeric(10) + "@test.com";
        authRequest.password = "testpassword";

        webTestClient.post().uri("/auth_api/register")
                .contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(authRequest))
                .exchange().expectStatus().isCreated();

        webTestClient.post().uri("/auth_api/register")
                .contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(authRequest))
                .exchange().expectStatus().is4xxClientError();
    }

    @Test
    public void TokenAccess() throws InterruptedException {
        var authRequest = new AuthRequest();
        authRequest.email = RandomStringUtils.randomAlphanumeric(10) + "@test.com";
        authRequest.password = "testpassword";

        var tokenOnRegistration = webTestClient.post().uri("/auth_api/register")
                .contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(authRequest))
                .exchange().expectStatus().isCreated()
                .expectBody(String.class).returnResult().getResponseBody();
        
        var tokenOnLogin = webTestClient.post().uri("/auth_api/login")
                .body(BodyInserters.fromValue(authRequest)).exchange().expectStatus().isOk().expectBody(String.class)
                .returnResult().getResponseBody();

        SearchAssetRequest searchAssetRequest = new SearchAssetRequest();
        searchAssetRequest.SearchString = "spdr";

        // should be able to query with auth
        webTestClient.post().uri("/asset_api/asset/search").header("Authorization", "Bearer " + tokenOnLogin)
                .contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(searchAssetRequest)).exchange().expectStatus().isOk();
        webTestClient.post().uri("/asset_api/asset/search").header("Authorization", "Bearer " + tokenOnRegistration)
                .contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(searchAssetRequest)).exchange().expectStatus().isOk();


        // get refresh token with later expiry date
        Thread.sleep(1000);
        var renewedToken = webTestClient.get().uri("/auth_api/renew").header("Authorization","Bearer " + tokenOnLogin).exchange()
                .expectStatus().isAccepted().expectBody(String.class).returnResult().getResponseBody();

        var secret = System.getenv(Constants.ENV_VAR_JWT_SECRET);
        var verifier = JWT.require(Algorithm.HMAC256(secret))
                .withIssuer("auth0")
                .build();

        var registrationJwt = verifier.verify(tokenOnRegistration);

        var renewedJwt = verifier.verify(renewedToken);

        Assert.isTrue(renewedJwt.getExpiresAtAsInstant().isAfter(registrationJwt.getExpiresAtAsInstant()), "renewed token does not expire after registration token");
    }
}
