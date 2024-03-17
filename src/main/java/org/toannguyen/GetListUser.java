package org.toannguyen;

import java.util.List;

public class GetListUser {
    List<GetUser> users;
    String paginationToken;

    public List<GetUser> getUsers() {
        return users;
    }

    public void setUsers(List<GetUser> users) {
        this.users = users;
    }

    public String getPaginationToken() {
        return paginationToken;
    }

    public void setPaginationToken(String paginationToken) {
        this.paginationToken = paginationToken;
    }
}
