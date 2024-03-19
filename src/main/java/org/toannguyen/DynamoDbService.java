package org.toannguyen;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;

import java.util.List;
import java.util.stream.Collectors;

import static org.toannguyen.Constants.DYNAMODB_USER_TABLE_NAME;

@ApplicationScoped
public class DynamoDbService {
    @Inject
    private DynamoDbEnhancedClient enhancedClient;

    private DynamoDbTable getDynamoDbTable(){
        TableSchema<User> userTableSchema = TableSchema.fromBean(User.class);
        var userDynamoDbTable = enhancedClient.table(DYNAMODB_USER_TABLE_NAME, userTableSchema);
        return userDynamoDbTable;
    }

    public User get(String username) {
        Key key = Key.builder().partitionValue(username).build();
        var user = getDynamoDbTable().getItem(key);
        return ((User)user);
    }

    public List<User> getUsers() {
        PageIterable<User> allItems = getDynamoDbTable().scan();
        return allItems.items().stream()
                .collect(Collectors.toList());
    }

    public void add(User user) {
        getDynamoDbTable().putItem(user);
    }

    public void update(User user) {
        getDynamoDbTable().updateItem(user);
    }

    public void delete(String username) {
        Key key = Key.builder().partitionValue(username).build();
        getDynamoDbTable().deleteItem(key);
    }
}
