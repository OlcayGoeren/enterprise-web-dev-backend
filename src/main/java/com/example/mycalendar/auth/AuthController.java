package com.example.mycalendar.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Map;


@RestController
@RequestMapping(path = "api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping(path = "register")
    public void register(@RequestBody RegisterCredentials body){
         authService.registerHandler(body);
    }

    @PostMapping(path = "login")
    public Map<String, String> login(@RequestHeader("Authorization") String auth)  {
        return authService.loginHandler(auth);
    }

}
