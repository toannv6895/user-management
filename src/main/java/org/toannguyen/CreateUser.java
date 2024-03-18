package org.toannguyen;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.Set;

import static org.toannguyen.Constants.EMAIL_PATTERN;
import static org.toannguyen.Constants.PASSWORD_PATTERN;

@RegisterForReflection
public class CreateUser {
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9@._-]{1,128}$", message = "username is not valid, should only contain number, letter, dot, underscore, hyphen and @")
    private String username;

    @NotBlank
    @Email(message = "Email is not valid", regexp = EMAIL_PATTERN)
    private String email;

    private String givenName;

    private String familyName;

    @Pattern(regexp = PASSWORD_PATTERN)
    private String password;

    @Pattern(regexp = "^\\+[0-9]+$|$", message = "Phone number is not valid, should only contain number")
    private String phone;

    @NotNull
    private Integer customerUniqueId;
    private String picture;

    private Set<String> authorities;
    private Set<String> sites;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getCustomerUniqueId() {
        return customerUniqueId;
    }

    public void setCustomerUniqueId(Integer customerUniqueId) {
        this.customerUniqueId = customerUniqueId;
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
}
