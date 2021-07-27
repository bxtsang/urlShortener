package click.bxtsang;

import click.bxtsang.shortener.ShortenerService;
import io.micronaut.context.annotation.Property;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.exceptions.HttpStatusException;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import javax.inject.Inject;

@MicronautTest
public class ShortenerServiceTest {

    @Inject
    ShortenerService shortenerService;

    @MockBean(UrlRecordRepository.class)
    UrlRecordRepository mockUrlRecordRepository() {
        return mock(UrlRecordRepository.class);
    }

    @Inject
    UrlRecordRepository mockUrlRecordRepository;

    @Property(name = "domain.location")
    private String domain;

    private final int HASH_LENGTH = 6;


    @Test
    public void testGenerateRandomString() {
        String generatedString = shortenerService.generateRandomString();

        Assertions.assertNotNull(generatedString);
        Assertions.assertEquals(generatedString.length(), HASH_LENGTH);
        Assertions.assertTrue(generatedString.matches("^[a-zA-Z0-9]+$"));
    }

    @Test
    public void testShortenUrlDbFail() {
        doThrow(new RuntimeException()).when(mockUrlRecordRepository).write(isA(String.class), isA(String.class));

        Exception exception = Assertions.assertThrows(HttpStatusException.class, () -> shortenerService.shortenUrl("https://www.google.com"));
        Assertions.assertEquals(exception.getMessage(), "Unable to connect to database");
    }

    @Test
    public void testFormatUrl() {
        String[] validUrls = { "https://www.google.com", "http://www.google.com", "//www.google.com" };
        String urlToFormat = "www.google.com";

        for (String validUrl: validUrls) {
            Assertions.assertEquals(shortenerService.formatUrl(validUrl), validUrl);
        }

        String formattedUrl = shortenerService.formatUrl(urlToFormat);

        Assertions.assertNotEquals(formattedUrl, urlToFormat);
        Assertions.assertTrue(formattedUrl.startsWith("//"));
    }

    @Test
    public void testShortenUrl() {
        doNothing().when(mockUrlRecordRepository).write(isA(String.class), isA(String.class));

        String generatedUrl = shortenerService.shortenUrl("https://www.google.com");

        Assertions.assertNotNull(generatedUrl);
        Assertions.assertTrue(generatedUrl.startsWith(domain));

        String[] generatedUrlArr = generatedUrl.split("/");
        Assertions.assertEquals(generatedUrlArr[generatedUrlArr.length - 1].length(), HASH_LENGTH);
    }
}
