package com.example.mycalendar.auth;

import com.example.mycalendar.mail.MyJavaMailSender;
import com.example.mycalendar.user.Roles;
import com.example.mycalendar.user.User;
import com.example.mycalendar.user.UserRepository;
import com.example.mycalendar.verify.Verify;
import com.example.mycalendar.verify.VerifyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.security.core.AuthenticationException;
import org.springframework.web.server.ResponseStatusException;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {


    private AuthenticationManager authenticationManager;
    private JWTUtil jwtUtil;
    private UserRepository userRepository;
    private final PasswordEncoder encoder;
    private MyJavaMailSender mailSender;
    private VerifyRepository verifyRepository;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager, JWTUtil jwtUtil, UserRepository userRepository, PasswordEncoder encoder, MyJavaMailSender mailSender, VerifyRepository verifyRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.mailSender = mailSender;
        this.verifyRepository = verifyRepository;
    }

    public void registerHandler(RegisterCredentials registerCredentials) {
        if (registerCredentials != null && userRepository.findUserByEmail(registerCredentials.getEmail()).isEmpty()){
            String encodedPass = encoder.encode(registerCredentials.getPassword());
            registerCredentials.setPassword(encodedPass);
            User user = new User(registerCredentials.getEmail(), registerCredentials.getPassword(), Roles.NON_VERIFIED);
            userRepository.save(user);
            Verify verify = new Verify(user);
            verifyRepository.save(verify);
            try {
                mailSender.send(user, "Verifiziere dich jetzt für MyCalendar",
                        "<a href=\"http://localhost:8080/api/v1/verify/"+verify.getId()+"\">Verifiziere dich hier!</a>");
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not a valid Email adress: "+user.getEmail());
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"User with Email: "+registerCredentials.getEmail()+ " is already existing.");
        }
    }

    public Map<String, String> registerGuest(String email) {
        User user = new User();
        user.setEmail(email);
        user.setRoles(Roles.NON_VERIFIED);
        user.setVerified(true);
        user.setPassword("guest");
        User savedUser = userRepository.save(user);
        String token = jwtUtil.generateToken(savedUser.getId());
        return Collections.singletonMap("jwt-token", token);
    }

    public Map<String, String> loginGuest(String email) {
        User savedUser = userRepository.findUserByEmail(email).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.UNAUTHORIZED)
        );
        String token = jwtUtil.generateToken(savedUser.getId());
        return Collections.singletonMap("jwt-token", token);
    }

    public Map<String, String> loginHandler(String auth) {
        try {
            String encodedString = auth.split(" ")[1];
            byte[] bla = Base64.getDecoder().decode(encodedString);
            String credentials = new String(bla, StandardCharsets.UTF_8);
            final String[] values = credentials.split(":", 2);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(values[0],
                    values[1]);
            authenticationManager.authenticate(authenticationToken);
            Optional<User> user = userRepository.findUserByEmail(values[0]);
            if (!user.get().isVerified()) {
                throw new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Please verify your account first"
                );
            } else {
                String token = jwtUtil.generateToken(user.get().getId());
                return Collections.singletonMap("jwt-token", token);
            }
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED ,ex.getMessage());
        }
    }

}
