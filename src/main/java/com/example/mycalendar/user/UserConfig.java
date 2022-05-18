package com.example.mycalendar.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
public class UserConfig {

    @Autowired
    PasswordEncoder encoder;
    @Bean
    CommandLineRunner commandLineRunner(UserRepository repo) {

        return args -> {
            User user = new User("0","admin@example.com",encoder.encode("12345"), Roles.ADMIN, true);
            User olcay = new User("1","olcay@example.com",encoder.encode("12345"), Roles.USER , false);
            repo.saveAll(List.of(user, olcay));
        };
    }
}
