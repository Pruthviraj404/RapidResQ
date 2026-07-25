package com.rapidresq.rapidresq_backend.security;


import com.rapidresq.rapidresq_backend.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter  extends OncePerRequestFilter{

    private static final String HEADER="Authorization";
    private static final String PREFIX ="Bearer ";

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,@NonNull FilterChain filterChain) throws ServletException,IOException{
        String header = request.getHeader(HEADER);

        if(header !=null && header.startsWith(PREFIX) ){
            String token= header.substring(PREFIX.length());

            try{
                Claims claims= jwtService.parseAndValidate(token);
                UUID userId = UUID.fromString(claims.getSubject());

                userRepository.findById(userId).ifPresent(user->{
                    if(user.isEnabled() && user.isAccountNonExpired()){
                        var authToken = new UsernamePasswordAuthenticationToken(
                            user, null,user.getAuthorities());

                            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                });
            }catch(Exception ex){
                SecurityContextHolder.clearContext();
            }

        }

        filterChain.doFilter(request, response);

    }

    
}
