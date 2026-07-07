package com.portfolio.salesmanager.controller;

import com.portfolio.salesmanager.dto.request.AuthenticationRequest;
import com.portfolio.salesmanager.dto.request.RegisterRequest;
import com.portfolio.salesmanager.dto.response.AuthenticationResponse;
import com.portfolio.salesmanager.dto.response.UserResponse;
import com.portfolio.salesmanager.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login (
            @RequestBody @Valid AuthenticationRequest authenticationRequest){
        AuthenticationResponse jwtDTO= authenticationService.login(authenticationRequest);
        return ResponseEntity.ok(jwtDTO);
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest request){

        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.register(request));
    }
    
}
