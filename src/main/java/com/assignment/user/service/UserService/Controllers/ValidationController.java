package com.assignment.user.service.UserService.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.assignment.user.service.UserService.DTO.TokenInformationDTO;
import com.assignment.user.service.UserService.Services.ValidationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Token Validation APIs", description = "Validates JWT tokens and returns user info")
public class ValidationController {

    @Autowired
    private ValidationService validationService;

    @Operation(
        summary = "Validate JWT token",
        description = "Validates the Authorization bearer token and returns user info",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/validateToken")
    public ResponseEntity<TokenInformationDTO> validateToken(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        TokenInformationDTO tokenInformationDTO = validationService.retriveUserDetailsFromToken(token);
        return ResponseEntity.ok().body(tokenInformationDTO);
    }
}
