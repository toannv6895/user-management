package org.toannguyen;

import io.quarkus.runtime.util.StringUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import static org.toannguyen.Constants.DEFAULT_EMAIL_VERIFIED;

@ApplicationScoped
public class UserService {
    @Inject
    CognitoService cognitoService;

    protected void createUser(CreateUser createUserRequest){
        String code = null;
        if (StringUtil.isNullOrEmpty(createUserRequest.getPassword())) {
            code = Utils.generatePassword();
            createUserRequest.setPassword(code);
        }

        cognitoService.adminCreateUser(createUserRequest);
        cognitoService.updateVerifiedEmail(createUserRequest.getUsername(), DEFAULT_EMAIL_VERIFIED);
        cognitoService.importRoles(createUserRequest.getAuthorities(), createUserRequest.getUsername());
    }

    protected void editUser(EditUser editUser, String username){
        cognitoService.adminEditUser(editUser, username);
        cognitoService.updateRoles(editUser.getAuthorities(), username);
    }

    protected GetUser getUser(String username){
        return cognitoService.findByUsername(username);
    }

    protected void getUsers(){

    }

    protected void deleteUser(String username){
        cognitoService.delete(username);
    }
}
