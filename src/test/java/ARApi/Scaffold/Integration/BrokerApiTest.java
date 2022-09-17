package ARApi.Scaffold.Integration;

import org.asynchttpclient.uri.Uri;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BrokerApiTest {

    @Test
    public void Test() throws URISyntaxException, IOException, InterruptedException {

        var client = HttpClient.newHttpClient();
        // First request - get redirect url
        HttpRequest requestExtendedLogin = HttpRequest.newBuilder()
                .uri(new URI("https://de.scalable.capital/cockpit/extended-login"))
                .GET()
                .build();
        var responseExtendedLogin = client.send(requestExtendedLogin, HttpResponse.BodyHandlers.ofString());
        String redirectURI = responseExtendedLogin.headers().firstValue("location").orElseThrow();
        // Second request - redirect
        HttpRequest requestAuthorize = HttpRequest.newBuilder()
                .uri(new URI(redirectURI))
                .GET()
                .build();
        var responseAuthorize = client.send(requestAuthorize, HttpResponse.BodyHandlers.ofString());
        var params = UriComponentsBuilder.fromUriString("https://de.scalable.capital" + responseAuthorize.headers().firstValue("location").orElseThrow())
                    .build()
                    .getQueryParams();
        var state = params.getFirst("state");
        var formattedString = String.format("https://secure.scalable.capital/u/login?state=%s&ui_locales=%s", state, params.getFirst("ui_locales"));
        // First post request
        HttpRequest requestPost = HttpRequest.newBuilder()
                .headers("Content-Type", "application/x-www-form-urlencoded")
                .uri(URI.create(formattedString))
                .POST(getParamsUrlEncoded(params))
                .build();
    }
    private HttpRequest.BodyPublisher getParamsUrlEncoded(MultiValueMap<String, String> parameters) {
        String urlEncoded = parameters.entrySet()
                .stream()
                .map(e -> e.getKey() + "=" + URLEncoder.encode(e.getValue().get(0), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));
        return HttpRequest.BodyPublishers.ofString(urlEncoded);
    }
}
