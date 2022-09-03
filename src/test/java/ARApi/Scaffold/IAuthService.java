package ARApi.Scaffold;


import org.springframework.test.web.reactive.server.WebTestClient;

public interface IAuthService {

    void Initialize(WebTestClient webTestClient);

    WebTestClient.RequestBodySpec AddAuth(WebTestClient.RequestBodySpec spec);

    <T extends WebTestClient.RequestHeadersSpec<T>> WebTestClient.RequestHeadersSpec<T> AddAuth(WebTestClient.RequestHeadersSpec<T> spec);
}
