package com.portfolio.salesmanager.controller;

import com.portfolio.salesmanager.dto.request.ChangePasswordRequest;
import com.portfolio.salesmanager.dto.request.UpdateUserProfileRequest;
import com.portfolio.salesmanager.dto.request.UpdateUserRequest;
import com.portfolio.salesmanager.dto.request.UserRequest;
import com.portfolio.salesmanager.dto.response.UserResponse;
import com.portfolio.salesmanager.service.UserService;
import com.portfolio.salesmanager.util.Role;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasAuthority('READ_ALL_USERS')")
    public ResponseEntity<List<UserResponse>> findAll(){
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/role")
    @PreAuthorize("hasAuthority('READ_ALL_USERS')")
    public ResponseEntity<List<UserResponse>> findUsersByRole(@RequestParam Role role){
        return ResponseEntity.ok(userService.findUsersByRole(role));
    }

    @GetMapping("/active")
    @PreAuthorize("hasAuthority('READ_ALL_USERS')")
    public ResponseEntity<List<UserResponse>> findActiveUsers(){
        return ResponseEntity.ok(userService.findActiveUsers());
    }

    @GetMapping("/branch/{idBranch}")
    @PreAuthorize("hasAuthority('READ_ALL_USERS')")
    public ResponseEntity<List<UserResponse>> findUsersByBranch(@PathVariable Long idBranch){
        return ResponseEntity.ok(userService.findUsersByBranch(idBranch));
    }

    @GetMapping("/{idUser}")
    @PreAuthorize("hasAuthority('READ_ALL_USERS')")
    public ResponseEntity<UserResponse> findUserId(@PathVariable Long idUser){
        return ResponseEntity.ok(userService.findUser(idUser));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SAVE_ONE_USER')")
    public ResponseEntity<UserResponse> saveUser(@Valid @RequestBody UserRequest userRequest){
        UserResponse creado= userService.saveUser(userRequest);
        return ResponseEntity.created(URI.create("/users/" + creado.getIdUser())).body(creado);
    }

    @PatchMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> updateMyProfile(@Valid @RequestBody UpdateUserProfileRequest updateUserProfileRequest){
        return ResponseEntity.ok(userService.updateMyProfile(updateUserProfileRequest));
    }

    @PatchMapping("/me/password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest){
        userService.changePassword(changePasswordRequest);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{idUser}")
    @PreAuthorize("hasAuthority('UPDATE_ONE_USER')")
    public ResponseEntity<UserResponse> updateUserAdmin(@PathVariable Long idUser,
                                                        @Valid @RequestBody UpdateUserRequest updateUserRequest){
        return ResponseEntity.ok(userService.updateUserAdmin(idUser,updateUserRequest));
    }

    @DeleteMapping("/{idUser}")
    @PreAuthorize("hasAuthority('DELETE_ONE_USER')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long idUser){
        userService.deleteUser(idUser);
        return ResponseEntity.noContent().build();
    }
}
