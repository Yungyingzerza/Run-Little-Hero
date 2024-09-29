package com.yungying.servergame.SERVER.Mapper;

public class UserResponse {
    private String id;
    private String username;

    public UserResponse() {}

    public UserResponse(String id, String username) {
        this.id = id;
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
