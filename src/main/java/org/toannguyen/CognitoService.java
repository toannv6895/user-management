package org.toannguyen;

import io.quarkus.runtime.util.StringUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

import java.util.*;
import java.util.stream.Collectors;

import static org.toannguyen.Constants.*;

@ApplicationScoped
public class CognitoService {
    @ConfigProperty(name = "cognito.poolId")
    String poolId;

    @Inject
    CognitoIdentityProviderClient cognitoIdentityProviderClient;

    protected GetUser adminCreateUser(CreateUser createUserRequest) {
        List<AttributeType> attributes = convertRequestToAttributes(createUserRequest);
        AdminCreateUserRequest signUpRequest = AdminCreateUserRequest.builder()
                .userPoolId(poolId)
                .username(createUserRequest.getUsername())
                .temporaryPassword(createUserRequest.getPassword())
                .desiredDeliveryMediums(DeliveryMediumType.EMAIL)
                .userAttributes(attributes)
                .build();

        AdminCreateUserResponse signupResponse = cognitoIdentityProviderClient.adminCreateUser(signUpRequest);

        GetUser getUser = convertAttributesToResponse(signupResponse);
        getUser.setAuthorities(createUserRequest.getAuthorities());

        return getUser;
    }

    protected void adminEditUser(EditUser editUserRequest, String username) {
        AdminUpdateUserAttributesRequest adminEditUserAttributesRequest = AdminUpdateUserAttributesRequest.builder()
                .userPoolId(poolId)
                .username(username)
                .userAttributes(convertRequestToAttributes(editUserRequest))
                .build();

        cognitoIdentityProviderClient.adminUpdateUserAttributes(adminEditUserAttributesRequest);
    }

    protected void importRoles(Set<String> roles, String username) {
        if (roles != null) {
            roles.forEach(r -> addUserToGroup(username, r));
        }
    }

    protected void updateRoles(Set<String> roles, String username) {
        if (roles != null) {
            var currentRoles = getGroups(username);
            currentRoles.stream().filter(x -> !roles.contains(x))
                    .forEach(r -> deleteUserFromGroup(username, r));

            roles.stream().filter(x -> !currentRoles.contains(x))
                    .forEach(r -> addUserToGroup(username, r));
        }
    }

    protected void delete(String username) {
        AdminDeleteUserRequest userRequest = AdminDeleteUserRequest.builder()
                .userPoolId(poolId)
                .username(username)
                .build();
        cognitoIdentityProviderClient.adminDeleteUser(userRequest);
    }

    public GetUser findByUsername(String username) {
        AdminGetUserRequest adminGetUserRequest = AdminGetUserRequest.builder()
                .userPoolId(poolId)
                .username(username)
                .build();
        AdminGetUserResponse adminGetUserResponse = cognitoIdentityProviderClient.adminGetUser(adminGetUserRequest);
        GetUser userResponse = convertAttributesToResponse(adminGetUserResponse);
        userResponse.setAuthorities(getGroups(username));
        return userResponse;
    }

    protected ListUsersResponse getListUsers(int pageSize, String paginationToken, String filter) {
        ListUsersRequest usersRequest = ListUsersRequest
                .builder()
                .limit(pageSize)
                .paginationToken(paginationToken)
                .filter(filter)
                .userPoolId(poolId)
                .build();

        return cognitoIdentityProviderClient.listUsers(usersRequest);
    }

    protected List<AttributeType> convertRequestToAttributes(CreateUser createUserRequest) {
        List<AttributeType> attributes = new ArrayList<>();
        attributes.add(AttributeType
                .builder()
                .name(EMAIL_FIELD_NAME)
                .value(createUserRequest.getEmail())
                .build());
        //because cognito don't support to search custom field, so we use name field to save tenant information
        attributes.add(AttributeType
                .builder()
                .name(CUSTOMER_ID_FIELD_NAME)
                .value(createUserRequest
                        .getCustomerUniqueId()
                        .toString())
                .build());
        attributes.add(AttributeType
                .builder()
                .name(CUSTOM_CUSTOMER_ID_FIELD_NAME)
                .value(createUserRequest
                        .getCustomerUniqueId()
                        .toString())
                .build());
        attributes.add(AttributeType
                .builder()
                .name(GIVEN_NAME_FIELD_NAME)
                .value(createUserRequest.getGivenName())
                .build());
        attributes.add(AttributeType
                .builder()
                .name(FAMILY_NAME_FIELD_NAME)
                .value(createUserRequest.getFamilyName())
                .build());
        if (!StringUtil.isNullOrEmpty(createUserRequest.getPicture())) {
            attributes.add(AttributeType
                    .builder()
                    .name(PICTURE_FIELD_NAME)
                    .value(createUserRequest.getPicture())
                    .build());
        }

        if (!StringUtil.isNullOrEmpty(createUserRequest.getPhone())) {
            attributes.add(AttributeType
                    .builder()
                    .name(PHONE_NUMBER_FIELD_NAME)
                    .value(createUserRequest.getPhone())
                    .build());
            attributes.add(AttributeType
                    .builder()
                    .name(PHONE_NUMBER_VERIFIED_FIELD_NAME)
                    .value(DEFAULT_PHONE_NUMBER_VERIFIED)
                    .build());
        }

        return attributes;
    }

    protected List<AttributeType> convertRequestToAttributes(EditUser editUserRequest) {
        List<AttributeType> attributes = new ArrayList<>();
        attributes.add(AttributeType
                .builder()
                .name(EMAIL_FIELD_NAME)
                .value(editUserRequest.getEmail())
                .build());
        attributes.add(AttributeType
                .builder()
                .name(GIVEN_NAME_FIELD_NAME)
                .value(editUserRequest.getGivenName())
                .build());
        attributes.add(AttributeType
                .builder()
                .name(FAMILY_NAME_FIELD_NAME)
                .value(editUserRequest.getFamilyName())
                .build());
        if (!StringUtil.isNullOrEmpty(editUserRequest.getPhone())) {
            attributes.add(AttributeType
                    .builder()
                    .name(PHONE_NUMBER_FIELD_NAME)
                    .value(editUserRequest.getPhone())
                    .build());
        }
        if (!StringUtil.isNullOrEmpty(editUserRequest.getPicture())) {
            attributes.add(AttributeType
                    .builder()
                    .name(PICTURE_FIELD_NAME)
                    .value(editUserRequest.getPicture())
                    .build());
        }

        return attributes;
    }


    protected GetUser convertAttributesToResponse(AdminCreateUserResponse inputGetUser) {
        List<AttributeType> attributeTypes = inputGetUser
                .user()
                .attributes();
        GetUser GetUser = convertUserTypeAttributesToResponse(attributeTypes);
        GetUser.setUsername(inputGetUser
                .user()
                .username());
        GetUser.setStartDate(Date.from(inputGetUser
                .user()
                .userCreateDate()));
        return GetUser;
    }

    protected GetUser convertAttributesToResponse(AdminGetUserResponse inputGetUser) {
        List<AttributeType> attributeTypes = inputGetUser.userAttributes();
        GetUser GetUser = convertUserTypeAttributesToResponse(attributeTypes);

        GetUser.setUsername(inputGetUser.username());

        GetUser.setStartDate(Date.from(inputGetUser.userCreateDate()));
        return GetUser;
    }

    protected GetUser convertUserTypeAttributesToResponse(UserType userType) {
        List<AttributeType> attributeTypes = userType.attributes();
        GetUser GetUser = convertUserTypeAttributesToResponse(attributeTypes);

        GetUser.setUsername(userType.username());
        GetUser.setStartDate(Date.from(userType.userCreateDate()));
        GetUser.setAuthorities(getGroups(userType.username()));
        return GetUser;
    }

    protected GetUser convertUserTypeAttributesToResponse(List<AttributeType> attributeTypes) {
        GetUser GetUser = new GetUser();
        for (AttributeType attribute : attributeTypes) {
            switch (attribute.name()) {
                case EMAIL_FIELD_NAME -> GetUser.setEmail(attribute.value());
                case CUSTOMER_ID_FIELD_NAME -> GetUser.setCustomerUniqueId(Integer.parseInt(attribute.value()));
                case GIVEN_NAME_FIELD_NAME -> GetUser.setGivenName(attribute.value());
                case FAMILY_NAME_FIELD_NAME -> GetUser.setFamilyName(attribute.value());
                case PHONE_NUMBER_FIELD_NAME -> GetUser.setPhone(attribute.value());
                case PICTURE_FIELD_NAME -> GetUser.setPicture(attribute.value());
            }
        }

        return GetUser;
    }

    protected void adminSetUserPassword(String username, String password) {
        AdminSetUserPasswordRequest adminSetUserPasswordRequest = AdminSetUserPasswordRequest
                .builder()
                .userPoolId(poolId)
                .username(username)
                .password(password)
                .permanent(true)
                .build();
        AdminSetUserPasswordResponse response = cognitoIdentityProviderClient.adminSetUserPassword(
                adminSetUserPasswordRequest);
    }

    protected AdminAddUserToGroupResponse addUserToGroup(String username, String groupName) {
        AdminAddUserToGroupRequest adminAddUserToGroupRequest = AdminAddUserToGroupRequest
                .builder()
                .groupName(groupName)
                .username(username)
                .userPoolId(poolId)
                .build();

        AdminAddUserToGroupResponse adminAddUserToGroupResponse = cognitoIdentityProviderClient.adminAddUserToGroup(
                adminAddUserToGroupRequest);
        return adminAddUserToGroupResponse;
    }

    protected AdminRemoveUserFromGroupResponse deleteUserFromGroup(String username, String groupName) {
        AdminRemoveUserFromGroupRequest adminAddUserToGroupRequest = AdminRemoveUserFromGroupRequest
                .builder()
                .groupName(groupName)
                .username(username)
                .userPoolId(poolId)
                .build();

        AdminRemoveUserFromGroupResponse adminRemoveUserFromGroupResponse =
                cognitoIdentityProviderClient.adminRemoveUserFromGroup(
                        adminAddUserToGroupRequest);
        return adminRemoveUserFromGroupResponse;
    }

    protected AdminListGroupsForUserResponse getGroupsForUser(String username) {
        var adminListGroupsForUserRequest = AdminListGroupsForUserRequest
                .builder()
                .userPoolId(poolId)
                .username(username)
                .build();

        AdminListGroupsForUserResponse response = cognitoIdentityProviderClient.adminListGroupsForUser(
                adminListGroupsForUserRequest);
        return response;
    }

    protected Set<String> getGroups(String username) {
        var groups = getGroupsForUser(username);
        return groups
                .groups()
                .stream()
                .map(GroupType::groupName)
                .collect(Collectors.toSet());
    }

    protected void disable(String username) {
        AdminDisableUserRequest disableUserRequest = AdminDisableUserRequest
                .builder()
                .userPoolId(poolId)
                .username(username)
                .build();
        cognitoIdentityProviderClient.adminDisableUser(disableUserRequest);
    }

    protected void updateVerifiedEmail(String username, String value) {
        AdminUpdateUserAttributesRequest adminUpdateUserAttributesRequest = AdminUpdateUserAttributesRequest
                .builder()
                .userPoolId(poolId)
                .username(username)
                .userAttributes(AttributeType
                        .builder()
                        .name(EMAIL_VERIFIED_FIELD_NAME)
                        .value(value)
                        .build())
                .build();

        cognitoIdentityProviderClient.adminUpdateUserAttributes(adminUpdateUserAttributesRequest);
    }
}
