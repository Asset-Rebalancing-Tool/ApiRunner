package ARApi.Scaffold.Integration;

import org.asynchttpclient.uri.Uri;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BrokerApiTest {

    @Test
    public void Test() throws URISyntaxException, IOException, InterruptedException {

        var client = HttpClient.newHttpClient();
        // First request - get redirect url
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://de.scalable.capital/cockpit/extended-login"))
                .GET()
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String redirectURI = response.headers().firstValue("location").orElseThrow();
        // Second request - redirect
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(new URI(redirectURI))
                .GET()
                .build();
        var response1 = client.send(request2, HttpResponse.BodyHandlers.ofString());

        var params = UriComponentsBuilder.fromUriString("https://de.scalable.capital" + response1.headers().firstValue("location").toString())
                    .build().getQueryParams();
        var state = params.getFirst("state");
        var formattedString = String.format("https://secure.scalable.capital/u/login?state=%s&ui_locales=%s", state, params.getFirst("ui_locales"));
        // First post request
        HttpRequest requestPost = HttpRequest.newBuilder()
                .headers("Content-Type", "application/x-www-form-urlencoded")
                .uri(URI.create(String.format("https://secure.scalable.capital/u/login?state=%s&ui_locales=%s", state, params.getFirst("ui_locales"))))
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();
    }
}
