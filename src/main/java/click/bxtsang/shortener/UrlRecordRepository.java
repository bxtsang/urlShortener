package click.bxtsang.shortener;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class UrlRecordRepository {

    @Inject
    DynamoDbClient dbClient;

    private String DB_TABLE = "url_records";

    private String HASH_COL = "hash";
    private String URL_COL = "url";

    public String get(String key, String keyVal) {

        Map<String, AttributeValue> keyMap = new HashMap<>();
        keyMap.put(key, AttributeValue.builder().s(keyVal).build());

        GetItemRequest request = GetItemRequest.builder()
                .key(keyMap)
                .tableName(DB_TABLE)
                .build();

        Map<String, AttributeValue> item = dbClient.getItem(request).item();
        return item.get("url").s();
    }

    public boolean write(String hash, String url) {

        Map<String, AttributeValue> itemValues = new HashMap<>();

        itemValues.put(HASH_COL, AttributeValue.builder().s(hash).build());
        itemValues.put(URL_COL, AttributeValue.builder().s(url).build());

        PutItemRequest request = PutItemRequest.builder()
                .tableName(DB_TABLE)
                .item(itemValues)
                .build();

        dbClient.putItem(request);

        return true;
    }
}
