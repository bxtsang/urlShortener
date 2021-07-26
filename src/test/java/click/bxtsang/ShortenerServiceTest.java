package click.bxtsang;

import click.bxtsang.shortener.ShortenerService;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

@MicronautTest
public class ShortenerServiceTest {

    @Inject
    ShortenerService shortenerService;

    @Test
    public void testGenerateRandomString() {
        String generatedString = shortenerService.generateRandomString();

        Assertions.assertNotNull(generatedString);
        Assertions.assertEquals(generatedString.length(), 6);
        Assertions.assertTrue(generatedString.matches("^[a-zA-Z0-9]+$"));
    }
}
