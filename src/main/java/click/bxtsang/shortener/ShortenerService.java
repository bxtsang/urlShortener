package click.bxtsang.shortener;

import click.bxtsang.UrlRecordRepository;
import io.micronaut.context.annotation.Property;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.exceptions.HttpStatusException;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Random;

@Singleton
public class ShortenerService {

    @Inject
    UrlRecordRepository urlRecordRepository;

    @Property(name = "domain.location")
    private String domain;

    private final String DB_TABLE = "url_records";

    private final int STRING_LENGTH = 6;

    public String shortenUrl(String url) {
        String formattedUrl = formatUrl(url);
        String hash = generateRandomString();

        try {
            urlRecordRepository.write(hash, formattedUrl);
            return domain + "/" + hash;
        } catch (Exception e) {
            throw new HttpStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to connect to database");
        }
    }

    public String generateRandomString() {
        Random random = new Random();

        String generatedString = random.ints(48, 123)
                .filter(i -> (i <= 57 || (i >= 65 && i <= 90) || i >= 97 ))
                .limit(STRING_LENGTH)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        // can add validation

        return generatedString;
    }

    public String formatUrl(String url) {
        if (!url.substring(0, 4).equals("http") && !url.substring(0, 2).equals("//")) {
            return "//" + url;
        }

        return url;
    }
}
