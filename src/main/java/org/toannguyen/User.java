package org.toannguyen;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.quarkus.runtime.annotations.RegisterForReflection;
import software.amazon.awssdk.enhanced.dynamodb.internal.converter.attribute.LocalDateAttributeConverter;
import software.amazon.awssdk.enhanced.dynamodb.internal.converter.attribute.LocalDateTimeAttributeConverter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbConvertedBy;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Set;

@RegisterForReflection
@DynamoDbBean()
public class User {
    private String username;
    private String givenName;
    private String familyName;
    private String email;
    private Integer customerUniqueId;
    private String phone;
    private String picture;
    private Set<String> authorities;
    private Set<String> sites;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime startDate;

    public User() {

    }

    public User(GetUser getUser) {
        this.setUsername(getUser.getUsername());
        this.setEmail(getUser.getEmail());
        this.setFamilyName(getUser.getFamilyName());
        this.setGivenName(getUser.getGivenName());
        this.setPhone(getUser.getPhone());
        this.setPicture(getUser.getPicture());
        this.setCustomerUniqueId(getUser.getCustomerUniqueId());
        this.setAuthorities(getUser.getAuthorities());
        this.setSites(getUser.getSites());
        this.setStartDate(getUser.getStartDate().toInstant().atZone(ZoneId.systemDefault())
                .toLocalDateTime());
    }

    public User(CreateUser createUser) {
        this.setUsername(createUser.getUsername());
        this.setEmail(createUser.getEmail());
        this.setFamilyName(createUser.getFamilyName());
        this.setGivenName(createUser.getGivenName());
        this.setPhone(createUser.getPhone());
        this.setPicture(createUser.getPicture());
        this.setCustomerUniqueId(createUser.getCustomerUniqueId());
        this.setAuthorities(createUser.getAuthorities());
        this.setSites(createUser.getSites());
    }

    public User(EditUser editUser) {
//        this.setUsername(getUser.getUsername());
//        this.setCustomerUniqueId(getUser.getCustomerUniqueId());
        this.setEmail(editUser.getEmail());
        this.setFamilyName(editUser.getFamilyName());
        this.setGivenName(editUser.getGivenName());
        this.setPhone(editUser.getPhone());
        this.setPicture(editUser.getPicture());
        this.setAuthorities(editUser.getAuthorities());
        this.setSites(editUser.getSites());
    }

    @DynamoDbPartitionKey
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getCustomerUniqueId() {
        return customerUniqueId;
    }

    public void setCustomerUniqueId(Integer customerUniqueId) {
        this.customerUniqueId = customerUniqueId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Set<String> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<String> authorities) {
        this.authorities = authorities;
    }

    public Set<String> getSites() {
        return sites;
    }

    public void setSites(Set<String> sites) {
        this.sites = sites;
    }

    @DynamoDbConvertedBy(LocalDateTimeAttributeConverter.class)
    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }
}
