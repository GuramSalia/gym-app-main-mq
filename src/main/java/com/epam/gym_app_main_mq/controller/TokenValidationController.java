package com.epam.gym_app_main_mq.controller;

import com.epam.gym_app_main_mq.api.TokenValidationRequest;
import com.epam.gym_app_main_mq.api.TokenValidationResponse;
import com.epam.gym_app_main_mq.aspect.LogRestDetails;
import com.epam.gym_app_main_mq.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@LogRestDetails
@RestController
public class TokenValidationController {
    private final JwtService jwtService;

    @Autowired
    public TokenValidationController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @GetMapping("/gym-app/public/token-validation")
    public ResponseEntity<TokenValidationResponse> getTokenValidationResponse(
            @RequestBody TokenValidationRequest tokenValidationRequest
    ) {
        log.info("\n\n--- validating token in main-app ----\n\n");
        boolean isTokenValid = jwtService.isTokenValid(tokenValidationRequest.getToken());
        return ResponseEntity.ok().body(new TokenValidationResponse(isTokenValid));
    }
}
