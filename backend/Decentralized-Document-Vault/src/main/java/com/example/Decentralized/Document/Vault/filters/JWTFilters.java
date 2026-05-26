package com.example.Decentralized.Document.Vault.filters;

import com.example.Decentralized.Document.Vault.model.auth.User;
import com.example.Decentralized.Document.Vault.repos.auth.UserRepo;
import com.example.Decentralized.Document.Vault.services.auth.AuthUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JWTFilters extends OncePerRequestFilter {
    private final AuthUtil authUtil;
    private  final UserRepo userRepo;


    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        try{
            final String requestHeader = request.getHeader("Authorization");
            if(requestHeader == null || !requestHeader.startsWith("Bearer ")){
                log.info("auth request");
                filterChain.doFilter(request, response);
                return;
            }

            if (request.getServletPath().contains("/api/v1/auth")) {
                filterChain.doFilter(request, response);
                return;
            }
            String token  = requestHeader.substring(7);

            String email = authUtil.getUsernameFromToken(token);

            if(email!=null && SecurityContextHolder.getContext().getAuthentication()==null){
                Optional<User> userOptional = userRepo.findByEmail(email);
                if(userOptional.isEmpty()){
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    response.getWriter().write("{\"error\": \"JWT authentication failed. Invalid or expired token.\"}");
                    return;
                }
                User user = userOptional.get();
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                log.info("User {} successfully authenticated via JWT.", email);

            }
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Token has expired. Please refresh.\"}");
        } catch (SignatureException | MalformedJwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Invalid token signature.\"}");
        }
    }
}