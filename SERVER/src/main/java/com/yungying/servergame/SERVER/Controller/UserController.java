package com.yungying.servergame.SERVER.Controller;


import com.yungying.servergame.SERVER.Mapper.ErrorResponse;
import com.yungying.servergame.SERVER.Mapper.UserResponse;
import com.yungying.servergame.SERVER.Model.User;
import com.yungying.servergame.SERVER.Repository.UserRepository;
import com.yungying.servergame.SERVER.Utils.JwtUtils;
import com.yungying.servergame.SERVER.config.SecurityConfig;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Value("${IS_PRODUCTION}")
    private boolean isProduction;

    @Value("${JWT_EXPIRATION}")
    private long EXPIRATION;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(HttpServletRequest request) {
        // Extract the JWT from cookies
        String jwt = null;
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    jwt = cookie.getValue();
                    break;
                }
            }
        }

        // Validate the JWT
        if (jwt == null || !jwtUtils.validateJwtToken(jwt)) {
            return ResponseEntity.status(401).body(new ErrorResponse("Unauthorized: Invalid or missing token"));
        }

        try {
            // Get the username or ID from the token
            String userId = jwtUtils.getUserIdFromJwtToken(jwt); // Adjust if using ID

            Optional<User> userOptional = userRepository.findById(userId); // Adjust to match your repository method

            // Check if the user is present
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                UserResponse userResponse = new UserResponse(user.getId(), user.getUsername());
                return ResponseEntity.ok(userResponse);
            } else {
                return ResponseEntity.status(404).body(new ErrorResponse("User not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ErrorResponse("An error occurred"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user, HttpServletResponse response) {

        User existingUser = userRepository.findByUsername(user.getUsername());

        if (existingUser == null) {
            return ResponseEntity.status(400).body(new ErrorResponse("User not found"));
        }

        if (!passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
            return ResponseEntity.status(400).body(new ErrorResponse("Invalid password"));
        }

        String token = jwtUtils.generateJwtToken(existingUser.getId());

        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge((int) EXPIRATION);
        cookie.setSecure(isProduction);

        response.addCookie(cookie);

        UserResponse userResponse = new UserResponse(existingUser.getId(), existingUser.getUsername());

        return ResponseEntity.ok(userResponse);


    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user, HttpServletResponse response) {

        //check if user already exists
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return ResponseEntity.status(400).body(new ErrorResponse("User already exists"));
        }

        try{

            user.setPassword(passwordEncoder.encode(user.getPassword()));

            userRepository.save(user);

            String token = jwtUtils.generateJwtToken(user.getId());

            Cookie cookie = new Cookie("token", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge((int) EXPIRATION);
            cookie.setSecure(isProduction);

            response.addCookie(cookie);

            UserResponse userResponse = new UserResponse(user.getId(), user.getUsername());

            return ResponseEntity.ok(userResponse);

        }catch (Exception e){
            return ResponseEntity.status(500).body(new ErrorResponse("An error occurred"));
        }
    }



}
