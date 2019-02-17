package com.smartcontract.authentication;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

public class Users {

    @Id
    private final String id;
    private final String username;
    private final String password;

    public Users(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getPassword() { 
        return password; 
    }

    public String getUsername() { 
        return username; 
    }

    @Override
    public String toString() {
        return String.format(
            "Users[id=%s, username='%s', password='%s']",
            id, username, password);
    }
}