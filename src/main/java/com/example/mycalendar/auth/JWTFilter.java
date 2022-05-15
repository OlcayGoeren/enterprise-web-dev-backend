package com.example.mycalendar.auth;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.mycalendar.user.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JWTFilter  extends OncePerRequestFilter {


    private MyUserDetailService userDetailsService;
    private JWTUtil jwtUtil;


    public JWTFilter(MyUserDetailService userDetailsService, JWTUtil jwtUtil) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        // Checking if the header contains a Bearer token
        if(authHeader != null && !authHeader.isBlank() && authHeader.startsWith("Bearer ")){
            // Extract JWT
            String jwt = authHeader.substring(7);
            if(jwt.isBlank()){
                // Invalid JWT
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT Token in Bearer Header");
            }else {
                try{
                    // Verify token and extract id
                    String id = jwtUtil.validateTokenAndRetrieveSubject(jwt);

                    UserDetails userDetails = userDetailsService.loadUserByUsername(id);
                    User user = userDetailsService.getUser(id);

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(user, userDetails.getPassword(), userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }catch(JWTVerificationException exc){
                    // Failed to verify JWT
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid JWT Token");
                }
            }
        }

        // Continuing the execution of the filter chain
        filterChain.doFilter(request, response);
    }

}
