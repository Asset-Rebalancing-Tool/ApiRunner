package ARApi.Scaffold.Integration;

import ARApi.Scaffold.Endpoints.Model.ModelPrivateCategory;
import ARApi.Scaffold.Endpoints.Requests.AuthRequest;
import ARApi.Scaffold.Endpoints.Requests.PostPublicAssetHoldingRequest;
import ARApi.Scaffold.Endpoints.Requests.PrivateCategoryRequest;
import ARApi.Scaffold.Endpoints.HoldingApi;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.BodyInserters;

import java.util.List;


@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HoldingApiTest {

    @Autowired
    private WebTestClient webTestClient;

    private ObjectMapper ob = new ObjectMapper();

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

    @Test
    public void PublicAssetHolding(){
    }
}
