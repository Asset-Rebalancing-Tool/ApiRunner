package ARApi.Scaffold.Integration;

import ARApi.Scaffold.Endpoints.Requests.AuthRequest;
import ARApi.Scaffold.Endpoints.Requests.PrivateCategoryRequest;
import ARApi.Scaffold.Endpoints.HoldingApi;
import liquibase.repackaged.org.apache.commons.lang3.RandomStringUtils;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HoldingApiTest {


    @Autowired
    private WebTestClient webTestClient;


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

    @Test
    public void PostPrivateCategory(){
        var request = new PrivateCategoryRequest();
        request.categoryName = "testcat";

        webTestClient.post().uri("/holding_api/category").header("Authorization", token).
                body(BodyInserters.fromValue(request)).exchange().expectStatus().isCreated();
    }


}
