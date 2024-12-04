package com.yungying.servergame.SERVER.DTO;

public class UserDTO {
    private String username;
    private int highestScore;

    public UserDTO() {}

    public UserDTO(String username, int highestScore) {
        this.username = username;
        this.highestScore = highestScore;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getHighestScore() {
        return highestScore;
    }

    public void setHighestScore(int highestScore) {
        this.highestScore = highestScore;
    }

}
