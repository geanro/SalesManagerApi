package com.portfolio.salesmanager.service;

import com.portfolio.salesmanager.dto.request.AuthenticationRequest;
import com.portfolio.salesmanager.dto.request.RegisterRequest;
import com.portfolio.salesmanager.dto.response.AuthenticationResponse;
import com.portfolio.salesmanager.dto.response.UserResponse;
import com.portfolio.salesmanager.entity.User;
import com.portfolio.salesmanager.exception.UserAlreadyExistsException;
import com.portfolio.salesmanager.exception.UserDisabledException;
import com.portfolio.salesmanager.mapper.UserMapper;
import com.portfolio.salesmanager.repository.UserRepository;
import com.portfolio.salesmanager.util.Role;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepo;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Transactional
    public UserResponse register(RegisterRequest request){

        if (userRepo.existsByUsername(request.getUsername())){
            throw new UserAlreadyExistsException("El username ya existe");
        }

        User user = User.builder()
                .name(request.getName())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.CUSTOMER)
                .active(true)
                .branch(null)
                .build();

        return userMapper.toResponse(userRepo.save(user));
    }

    public AuthenticationResponse login(AuthenticationRequest authenticationRequest){

        UsernamePasswordAuthenticationToken authenticationToken= new UsernamePasswordAuthenticationToken(
             authenticationRequest.getUsername(),
                authenticationRequest.getPassword());

        authenticationManager.authenticate(authenticationToken);

        User user= userRepo.findByUsername(authenticationRequest.getUsername())
                .orElseThrow(()->new UsernameNotFoundException("username no encontrado"));

        if (!user.getActive()) {
            throw new UserDisabledException("El usuario está desactivado");
        }

        String jwt= jwtService.generateToken(user,generateExtraClaims(user));

        return new AuthenticationResponse(jwt);
    }

    private Map<String,Object> generateExtraClaims(User user) {

        Map<String,Object> extraClaims= new HashMap<>();
        extraClaims.put("name", user.getName());
        extraClaims.put("role", user.getRole().name());
        extraClaims.put(
                "permissions",
                user.getAuthorities()
                        .stream()
                        .map(authority -> authority.getAuthority())
                        .toList()
        );
        return extraClaims;
    }
}
