package click.bxtsang.shortener;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

@Singleton
public class ShortenerService {

    @Inject
    UrlRecordRepository urlRecordRepository;

    private String DB_TABLE = "url_records";

    private int STRING_LENGTH = 6;
    private String domain = "bxtsang.click";

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
