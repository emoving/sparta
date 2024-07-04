package com.hhplus.reservation_concert.presentation.token;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/tokens")
public class TokenController {

    @GetMapping("/{userId}")
    public ResponseEntity<String> getToken(@PathVariable Long userId) {
        Long waitNumber = 13L;

        return ResponseEntity.ok(String.format("userId=%d&waitNumber=%d", userId, waitNumber));
    }
}
