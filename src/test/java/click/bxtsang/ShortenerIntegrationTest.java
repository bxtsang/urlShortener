package click.bxtsang;

import click.bxtsang.shortener.ShortenerService;
import io.micronaut.context.annotation.Property;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@MicronautTest
public class ShortenerIntegrationTest {

    @Inject
    EmbeddedServer server;

    @Inject
    ShortenerService shortenerService;

    @Inject
    UrlRecordRepository urlRecordRepository;

    @Inject
    @Client("http://localhost")
    HttpClient client;

    @Property(name = "domain.location")
    private String domain;

    @Test
    public void testShortenerService() {
        String[] validUrls = { "https://www.google.com", "http://www.google.com", "//www.google.com" };
        String urlToFormat = "www.google.com";

        for (String validUrl: validUrls) {
            String generatedUrl = shortenerService.shortenUrl(validUrl);
            String[] generatedUrlArr = generatedUrl.split("/");

            String hash = generatedUrlArr[generatedUrlArr.length - 1];
            String storedUrl = urlRecordRepository.getUrlFromHash(hash);

            Assertions.assertNotNull(storedUrl);
            Assertions.assertEquals(validUrl, storedUrl);
        }

        String generatedUrl = shortenerService.shortenUrl(urlToFormat);
        String[] generatedUrlArr = generatedUrl.split("/");

        String hash = generatedUrlArr[generatedUrlArr.length - 1];
        String storedUrl = urlRecordRepository.getUrlFromHash(hash);

        Assertions.assertNotNull(storedUrl);
        Assertions.assertEquals("//" + urlToFormat, storedUrl);
    }

    @Test
    public void testShortenerController() {
        Map<String, String> body = new HashMap<>();
        String url = "https://www.google.com";
        body.put("url", url);

        HttpRequest<Map<String, String>> request = HttpRequest.POST(server.getURI().toString() + "/shortener/create", body);
        String response = client.toBlocking().retrieve(request);

        Assertions.assertTrue(response.startsWith(domain));

        String[] generatedUrlArr = response.split("/");
        String hash = generatedUrlArr[generatedUrlArr.length - 1];
        String storedUrl = urlRecordRepository.getUrlFromHash(hash);

        Assertions.assertEquals(url, storedUrl);
    }
}
