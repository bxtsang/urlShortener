package click.bxtsang.shortener;

import click.bxtsang.UrlRecordRepository;
import io.micronaut.context.annotation.Property;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Random;

@Singleton
public class ShortenerService {

    @Inject
    UrlRecordRepository urlRecordRepository;

    @Property(name = "domain.location")
    private String domain;

    private String DB_TABLE = "url_records";

    private int STRING_LENGTH = 6;

    public String shortenUrl(String url) {
        String hash = generateRandomString();
        if (urlRecordRepository.write(hash, url)) {
            return domain + "/" + hash;
        }

        return "something went wrong";
    }

    public String generateRandomString() {
        Random random = new Random();

        String generatedString = random.ints(48, 123)
                .filter(i -> (i <= 57 || (i >= 65 && i <= 90) || i >= 97 ))
                .limit(STRING_LENGTH)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        // add validation

        return generatedString;
    }
}
