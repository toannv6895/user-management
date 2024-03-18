package org.toannguyen;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.toannguyen.Constants.*;

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
        return ((User)getDynamoDbTable().getItem(username));
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
        getDynamoDbTable().deleteItem(username);
    }

    protected User convert(Map<String, AttributeValue> item) {
        User user = new User();
        if (item != null && !item.isEmpty()) {
            user.setEmail(item.get(EMAIL_FIELD_NAME).s());
            user.setUsername(item.get(DYNAMODB_USER_USERNAME_COL).s());
            user.setGivenName(item.get(GIVEN_NAME_FIELD_NAME).s());
            user.setFamilyName(item.get(FAMILY_NAME_FIELD_NAME).s());
            user.setPhone(item.get(PHONE_NUMBER_FIELD_NAME).s());
            user.setPicture(item.get(PICTURE_FIELD_NAME).s());
            user.setCustomerUniqueId(Integer.parseInt(item.get(DYNAMODB_CUSTOMER_ID_FIELD_NAME).n()));
        }
        return user;
    }

    protected PutItemRequest putRequest(User user) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put(USERNAME_FIELD_NAME, AttributeValue.builder().s(user.getUsername()).build());
        item.put(EMAIL_FIELD_NAME, AttributeValue.builder().s(user.getEmail()).build());
        item.put(FAMILY_NAME_FIELD_NAME, AttributeValue.builder().s(user.getFamilyName()).build());
        item.put(GIVEN_NAME_FIELD_NAME, AttributeValue.builder().s(user.getGivenName()).build());
        item.put(PHONE_NUMBER_FIELD_NAME, AttributeValue.builder().s(user.getPhone()).build());
        item.put(PICTURE_FIELD_NAME, AttributeValue.builder().s(user.getPicture()).build());
        item.put(DYNAMODB_CUSTOMER_ID_FIELD_NAME, AttributeValue.builder().s(user.getCustomerUniqueId().toString()).build());

        return PutItemRequest.builder()
                .tableName(DYNAMODB_USER_TABLE_NAME)
                .item(item)
                .build();
    }

    protected DeleteItemRequest deleteRequest(String username) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put(DYNAMODB_USER_USERNAME_COL, AttributeValue.builder().s(username).build());

        return DeleteItemRequest.builder().tableName(DYNAMODB_USER_TABLE_NAME).key(key).build();
    }

    protected GetItemRequest getRequest(String username) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put(DYNAMODB_USER_USERNAME_COL, AttributeValue.builder().s(username).build());

        return GetItemRequest.builder()
                .tableName(DYNAMODB_USER_TABLE_NAME)
                .key(key)
                .attributesToGet(DYNAMODB_USER_USERNAME_COL, DYNAMODB_CUSTOMER_ID_FIELD_NAME, EMAIL_FIELD_NAME, GIVEN_NAME_FIELD_NAME, FAMILY_NAME_FIELD_NAME, PHONE_NUMBER_FIELD_NAME, PICTURE_FIELD_NAME)
                .build();
    }

    protected ScanRequest scanRequest() {
        return ScanRequest.builder().tableName(DYNAMODB_USER_TABLE_NAME)
                .attributesToGet(DYNAMODB_USER_USERNAME_COL, DYNAMODB_CUSTOMER_ID_FIELD_NAME, EMAIL_FIELD_NAME, GIVEN_NAME_FIELD_NAME, FAMILY_NAME_FIELD_NAME, PHONE_NUMBER_FIELD_NAME, PICTURE_FIELD_NAME).build();
    }
}
