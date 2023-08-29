package com.dpc.erp.controller;


import com.dpc.erp.Request_Response.RegistrationRequest;
import com.dpc.erp.Request_Response.UpdatePasswordRequest;
import com.dpc.erp.entity.User;
import com.dpc.erp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/addUser")
    public ResponseEntity<?> addUser(@RequestBody RegistrationRequest request) {
        try {
            User user = userService.addNewUser(request);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

        }
    }

    @GetMapping("/users")
    public ResponseEntity<?> getUsers() {
        try {
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

        }
    }

    @GetMapping("/confirmToken/{token}")
    public ResponseEntity<?> confirmToken(@PathVariable String token) {
        try {
            String action = userService.confirmToken(token);
            return ResponseEntity.ok(action);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

        }
    }

    @PostMapping("/updatePassword")
    public ResponseEntity<?> updatePassword(@RequestBody UpdatePasswordRequest request) {
        try {
            boolean result = userService.updatePassword(request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());

        }
    }

}
