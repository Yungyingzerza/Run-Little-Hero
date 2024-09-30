package com.yungying.game.hooks;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.utils.Json;
import com.google.gson.Gson;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

public class UseUser {

    private Gson gson;
    public static String username;
    public static String userId;


    public UseUser() {
        gson = new Gson();
    }



    public Enum<Authentication> login(String username, String password, CountDownLatch latch) {
        // Create a new HttpRequestBuilder
        HttpRequestBuilder requestBuilder = new HttpRequestBuilder();

        // Convert the User object to JSON
        String jsonContent = gson.toJson(new User(username, password));

        AtomicReference<Authentication> authResult = new AtomicReference<>(Authentication.ERROR);

        // Build the request
        Net.HttpRequest request = requestBuilder.newRequest()
            .method("POST")
            .url("https://api.yungying.com/gameoop/user/login") // Update this URL to match your Spring Boot API
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

                    authResult.set(Authentication.SUCCESS);

                    latch.countDown(); // Count down the latch to signal completion

                } else {
                    ErrorResponse errorResponse = gson.fromJson(response.getResultAsString(), ErrorResponse.class); // Parse the JSON response

                    if (errorResponse.getMessage().equals("User not found")) {
                        authResult.set(Authentication.USER_NOT_FOUND);
                    } else if (errorResponse.getMessage().equals("Invalid password")) {
                        authResult.set(Authentication.WRONG_PASSWORD);
                    } else {
                        authResult.set(Authentication.ERROR);
                    }

                    latch.countDown(); // Count down the latch to signal completion
                }
            }

            @Override
            public void failed(Throwable t) {
                System.out.println("Request failed: " + t.getMessage());

                authResult.set(Authentication.ERROR);

                latch.countDown(); // Count down the latch to signal completion
            }

            @Override
            public void cancelled() {
                System.out.println("Request cancelled");

                authResult.set(Authentication.ERROR);

                latch.countDown(); // Count down the latch to signal completion
            }
        });

        // Wait for the HTTP request to finish
        try {
            latch.await(); // This blocks until latch.countDown() is called
        } catch (InterruptedException e) {
            e.printStackTrace();
            authResult.set(Authentication.ERROR); // Set the result to ERROR if interrupted
        }

        // Return the authentication result
        return authResult.get();

    }

    public Enum<Authentication> register(String username, String password, CountDownLatch latch) {
        // Create a new HttpRequestBuilder
        HttpRequestBuilder requestBuilder = new HttpRequestBuilder();

        // Convert the User object to JSON
        String jsonContent = gson.toJson(new User(username, password));

        AtomicReference<Authentication> authResult = new AtomicReference<>(Authentication.ERROR);

        // Build the request
        Net.HttpRequest request = requestBuilder.newRequest()
            .method("POST")
            .url("https://api.yungying.com/gameoop/user/register") // Update this URL to match your Spring Boot API
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

                    authResult.set(Authentication.SUCCESS);

                    latch.countDown(); // Count down the latch to signal completion



                } else {

                    ErrorResponse errorResponse = gson.fromJson(response.getResultAsString(), ErrorResponse.class); // Parse the JSON response

                    if (errorResponse.getMessage().equals("User already exists")) {
                        authResult.set(Authentication.USER_ALREADY_EXISTS);
                    } else {
                        authResult.set(Authentication.ERROR);
                    }

                    latch.countDown(); // Count down the latch to signal completion
                }
            }

            @Override
            public void failed(Throwable t) {
                System.out.println("Request failed: " + t.getMessage());

                authResult.set(Authentication.ERROR);

                latch.countDown(); // Count down the latch to signal completion
            }

            @Override
            public void cancelled() {
                System.out.println("Request cancelled");

                authResult.set(Authentication.ERROR);

                latch.countDown(); // Count down the latch to signal completion
            }
        });

        // Wait for the HTTP request to finish
        try {
            latch.await(); // This blocks until latch.countDown() is called
        } catch (InterruptedException e) {
            e.printStackTrace();
            authResult.set(Authentication.ERROR); // Set the result to ERROR if interrupted
        }

        // Return the authentication result
        return authResult.get();
    }


    public void getProfile(){
        // Create a new HttpRequestBuilder
        HttpRequestBuilder requestBuilder = new HttpRequestBuilder();

        // Build the request
        Net.HttpRequest request = requestBuilder.newRequest()
            .method("GET")
            .url("https://api.yungying.com/gameoop/user/profile") // Update this URL to match your Spring Boot API
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
