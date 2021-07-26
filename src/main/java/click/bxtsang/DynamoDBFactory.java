package click.bxtsang;

import io.micronaut.context.annotation.Factory;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import javax.inject.Singleton;

@Factory
public class DynamoDBFactory {

    @Singleton
    DynamoDbClient client = DynamoDbClient.create();
}
