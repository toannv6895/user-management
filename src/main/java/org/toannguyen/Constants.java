package org.toannguyen;

public class Constants {
    public static final String PASSWORD_PATTERN = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$|$";
    public static final String EMAIL_PATTERN = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

    public static final String EMAIL_FIELD_NAME = "email";
    public static final String CUSTOMER_ID_FIELD_NAME = "name";
    public static final String GIVEN_NAME_FIELD_NAME = "given_name";
    public static final String FAMILY_NAME_FIELD_NAME = "family_name";
    public static final String PHONE_NUMBER_FIELD_NAME = "phone_number";
    public static final String PHONE_NUMBER_VERIFIED_FIELD_NAME = "phone_number_verified";
    public static final String DEFAULT_PHONE_NUMBER_VERIFIED = "false";
    public static final String EMAIL_VERIFIED_FIELD_NAME = "email_verified";
    public static final String PICTURE_FIELD_NAME = "picture";
    public static final String CUSTOM_CUSTOMER_ID_FIELD_NAME = "custom:tenant";
    public static final String USERNAME_FIELD_NAME = "cognito:username";
    public static final String DEFAULT_EMAIL_VERIFIED = "true";
    public static final Integer MAX_USERS_PER_REQUEST = 60;
}
