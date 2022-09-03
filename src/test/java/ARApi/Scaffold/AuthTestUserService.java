package ARApi.Scaffold;


import ARApi.Scaffold.Endpoints.Requests.AuthRequest;
import liquibase.pro.packaged.O;
import liquibase.repackaged.org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

@Service
public class AuthTestUserService implements IAuthService {

    private static String token = null;

    @Override
    public void Initialize(WebTestClient webTestClient){
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

    @Override
    public WebTestClient.RequestBodySpec AddAuth(WebTestClient.RequestBodySpec spec){
        return spec.header("Authorization", token);
    }

    @Override
    public <T extends WebTestClient.RequestHeadersSpec<T>> WebTestClient.RequestHeadersSpec<T> AddAuth(WebTestClient.RequestHeadersSpec<T> spec){
        return spec.header("Authorization", token);
    }
}
