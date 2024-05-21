package com.epam.gym_app_main_mq.controller;

import com.epam.gym_app_main_mq.api.AuthenticationResponse;
import com.epam.gym_app_main_mq.api.PasswordUpdateRequest;
import com.epam.gym_app_main_mq.api.UsernamePassword;
import com.epam.gym_app_main_mq.aspect.LogRestDetails;
import com.epam.gym_app_main_mq.global.EndpointSuccessCounter;
import com.epam.gym_app_main_mq.model.Token;
import com.epam.gym_app_main_mq.service.*;
import io.micrometer.core.instrument.MeterRegistry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@LogRestDetails
public class UserManagementController {

    private final UserService userService;
    private final UsernamePasswordValidationService usernamePasswordValidationService;
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;
    private final EndpointSuccessCounter endpointSuccessCounter;
    private final TokenService tokenService;

    @Autowired
    public UserManagementController(
            UserService userService,
            JwtService jwtService,
            CustomUserDetailsService customUserDetailsService,
            UsernamePasswordValidationService usernamePasswordValidationService,
            EndpointSuccessCounter endpointSuccessCounter,
            MeterRegistry meterRegistry,
            TokenService tokenService
    ) {
        this.userService = userService;
        this.usernamePasswordValidationService = usernamePasswordValidationService;
        this.endpointSuccessCounter = endpointSuccessCounter;
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
        this.tokenService = tokenService;
    }

    @Operation(summary = "User Login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful login")
    })
    @GetMapping("/gym-app/public/user/login")
    public ResponseEntity<AuthenticationResponse> login(
            @Valid @RequestBody UsernamePassword usernamePassword
    ) {
        String username = usernamePassword.getUsername();
        String password = usernamePassword.getPassword();

        /*
           this is how I built it first, I need it to set invalid validation attempts.
           Otherwise, I could have used authenticate method of authenticationManager and passed in
           new UsernamePasswordAuthenticationToken with username and password as parameters
         */
        usernamePasswordValidationService.validateUsernamePasswordAndReturnRole(username, password);
        log.info("\n\npublic/user/login> usernamePasswordValidationService > validateUsernamePassword\n");
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
        String token = jwtService.generateToken(userDetails);
        AuthenticationResponse authenticationResponse = new AuthenticationResponse(token);
        endpointSuccessCounter.incrementCounter("GET/public/user/login");
        return ResponseEntity.ok().body(authenticationResponse);
    }

    @Operation(summary = "update password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content")
    })
    @PutMapping("/gym-app/user/login")
    public ResponseEntity<Void> updatePassword(
            @Valid @RequestBody PasswordUpdateRequest usernamePassword
    ) {
        String username = usernamePassword.getUsername();
        String currentPassword = usernamePassword.getPassword();
        String newPassword = usernamePassword.getNewPassword();
        userService.updatePassword(username, currentPassword, newPassword);
        endpointSuccessCounter.incrementCounter("PUT/user/login");
        return ResponseEntity.status(HttpStatusCode.valueOf(204)).build();
    }

    @Operation(summary = "logout")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content")
    })
    @GetMapping("/gym-app/user/logout")
    public ResponseEntity<Void> logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String tokenToken = token();
        Token token = tokenService.findByToken(tokenToken);
        token.setRevoked(true);
        tokenService.save(token);
        endpointSuccessCounter.incrementCounter("GET/user/logout");
        return ResponseEntity.status(HttpStatusCode.valueOf(204)).build();
    }

    public String token() {
        log.info("\n\nUserManagementController > token() called\n");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("\n\nUserManagementController > token() > authentication.getDetails()"
                         + authentication.getDetails().toString() + "\n");
        return authentication.getDetails().toString();
    }
}
