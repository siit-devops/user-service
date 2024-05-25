package com.devops.user_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/")
    public ResponseEntity<String> helloPublic() {
        return ResponseEntity.ok("Hello Public User");
    }

    @GetMapping("/guests/hello")
    public ResponseEntity<String> helloMember() {
        return ResponseEntity.ok("Hello dear member");
    }

    @GetMapping("/hosts/hello")
    public ResponseEntity<String> helloAdmin() {
        return ResponseEntity.ok("Hello dear admin");
    }

}
