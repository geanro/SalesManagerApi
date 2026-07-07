package com.portfolio.salesmanager.config.security.filter;


import com.portfolio.salesmanager.entity.User;
import com.portfolio.salesmanager.repository.UserRepository;
import com.portfolio.salesmanager.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserRepository userRepo;
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        //1. obetener el header que contiene el jwt
        String authHeader= request.getHeader("Authorization");// bearer jwt
        if (authHeader==null|| !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }
        //2. obtener ese jwt del header
        String jwt= authHeader.split(" ")[1];
        //3. obtener subjetct/username  desde el jwt
        String username= jwtService.extractUsername(jwt);
        //4. setear un objeto authentication dentro del SecurityContext
        User user = userRepo.findByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("username no encontrado"));
        UsernamePasswordAuthenticationToken authenticationToken= new UsernamePasswordAuthenticationToken(
          username,null,user.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //5. ejecutar el resto de filtros
        filterChain.doFilter(request,response);
    }
}
