package com.yungying.game.hooks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.utils.Json;
import com.google.gson.Gson;

import java.util.concurrent.CountDownLatch;

public class UseUser {

    private Gson gson;
    public static String username;
    public static String userId;


    public UseUser() {
        gson = new Gson();
    }



    public void login(String username, String password, CountDownLatch latch) {
        // Create a new HttpRequestBuilder
        HttpRequestBuilder requestBuilder = new HttpRequestBuilder();

        // Convert the User object to JSON
        String jsonContent = gson.toJson(new User(username, password));

        System.out.println(jsonContent);

        // Build the request
        Net.HttpRequest request = requestBuilder.newRequest()
            .method("POST")
            .url("http://localhost:8080/user/login") // Update this URL to match your Spring Boot API
            .header("Content-Type", "application/json")
            .content(jsonContent) // Set the JSON content for the request
            .build();

        // Send the HTTP request
        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse response) {
                if (response.getStatus().getStatusCode() == HttpStatus.SC_OK) { // Check for OK status
                    // Parse the JSON response into User object
                    User user = gson.fromJson(response.getResultAsString(), User.class); // Parse the JSON response

                    // Output the username from the response
                    UseUser.username = user.getUsername();
                    UseUser.userId = user.getId();

                    latch.countDown(); // Count down the latch to signal completion

                } else {
                    System.out.println("Failed to fetch user: " + response.getResultAsString());
                    latch.countDown(); // Count down the latch to signal completion
                }
            }

            @Override
            public void failed(Throwable t) {
                System.out.println("Request failed: " + t.getMessage());
                latch.countDown(); // Count down the latch to signal completion
            }

            @Override
            public void cancelled() {
                System.out.println("Request cancelled");
                latch.countDown(); // Count down the latch to signal completion
            }
        });
    }


    public void getProfile(){
        // Create a new HttpRequestBuilder
        HttpRequestBuilder requestBuilder = new HttpRequestBuilder();

        // Build the request
        Net.HttpRequest request = requestBuilder.newRequest()
            .method("GET")
            .url("http://localhost:8080/user/profile") // Update this URL to match your Spring Boot API
            .header("Content-Type", "application/json")
            .build();

        // Send the HTTP request
        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse response) {
                if (response.getStatus().getStatusCode() == HttpStatus.SC_OK) { // Check for OK status
                    // Parse the JSON response into User object
                    UserResponse userResponse = gson.fromJson(response.getResultAsString(), UserResponse.class); // Parse the JSON response

                    UseUser.username = userResponse.getUsername();
                    UseUser.userId = userResponse.getId();
                } else {
                    System.out.println("Failed to fetch user: " + response.getResultAsString());
                }
            }

            @Override
            public void failed(Throwable t) {
                System.out.println("Request failed: " + t.getMessage());
            }

            @Override
            public void cancelled() {
                System.out.println("Request cancelled");
            }
        });
    }

    // User class definition
    public static class User {
        private String id;
        private String username;
        private String password;

        public User() {}

        public User(String username, String password) {
            this.username = username;
            this.password = password;
        }

        // Getters and setters
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

        public String getPassword() {
            return password;
        }

    }

    public static class ErrorResponse {
        private String message;

        public ErrorResponse() {}

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public static class UserResponse {
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
}
