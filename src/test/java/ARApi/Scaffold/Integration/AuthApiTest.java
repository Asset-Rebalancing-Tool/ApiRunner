package ARApi.Scaffold.Integration;

import ARApi.Scaffold.Endpoints.Requests.AuthRequest;
import ARApi.Scaffold.Endpoints.Requests.SearchAssetRequest;
import liquibase.repackaged.org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
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
    public void TokenAccess() {
        var authRequest = new AuthRequest();
        authRequest.email = RandomStringUtils.randomAlphanumeric(10) + "@test.com";
        authRequest.password = "testpassword";

        webTestClient.post().uri("/auth_api/register")
                .contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(authRequest))
                .exchange().expectStatus().isCreated();
        
        var result = webTestClient.post().uri("/auth_api/login")
                .body(BodyInserters.fromValue(authRequest)).exchange().expectStatus().isOk().expectBody(String.class);
        
        var token = "Bearer " + result.returnResult().getResponseBody();
        SearchAssetRequest searchAssetRequest = new SearchAssetRequest();
        searchAssetRequest.SearchString = "spdr";

        webTestClient.post().uri("/asset_api/asset/search").header("Authorization", token)
                .contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromValue(searchAssetRequest)).exchange().expectStatus().isOk();
    }
}
