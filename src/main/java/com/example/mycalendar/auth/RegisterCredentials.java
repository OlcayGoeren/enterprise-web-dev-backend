package com.example.mycalendar.auth;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class RegisterCredentials {
    private String username;
    private String email;
    private String password;
}
