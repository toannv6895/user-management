package org.toannguyen;

import jakarta.validation.constraints.*;

import java.util.Set;

public class EditUser {
    @NotBlank
    private String givenName;

    @NotBlank
    private String familyName;
    @Email
    private String email;
    @Pattern(regexp = "^\\+[0-9]+$|$", message = "Phone number is not valid, should only contain number")
    private String phone;
    private String picture;
    private Set<String> authorities;
    private Set<String> sites;

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
}
