package com.example.mycalendar.auth;

import com.example.mycalendar.user.Roles;
import com.example.mycalendar.user.User;
import com.example.mycalendar.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class MyUserDetailService implements UserDetailsService {


    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findUserByEmail(email);
        Optional<User> user2 = userRepository.findById(email);
        if (user.isEmpty() && user2.isEmpty()){
            throw new UsernameNotFoundException("Could not findUser with email = " + email);
        } else {

            Set<GrantedAuthority> authoritiesAdmin = new HashSet<>();
            authoritiesAdmin.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

            Set<GrantedAuthority> authoritiesUser = new HashSet<>();
            authoritiesUser.add(new SimpleGrantedAuthority("ROLE_USER"));

            Set<GrantedAuthority> authoritiesNonVerifiedUser = new HashSet<>();
            authoritiesNonVerifiedUser.add(new SimpleGrantedAuthority("ROLE_NONV_USER"));

            if (user.isPresent()) {
                User getUser = user.get();
                return new org.springframework.security.core.userdetails.User(
                        getUser.getEmail(),
                        getUser.getPassword(),
                        getUser.getRoles() == Roles.ADMIN ? authoritiesAdmin:
                                getUser.getRoles() == Roles.USER? authoritiesUser: authoritiesNonVerifiedUser
//                        Objects.equals(getUser.getUsername(), "admin")? authoritiesAdmin: authoritiesUser
                        );
            }else {
                User getUser2 = user2.get();
                return new org.springframework.security.core.userdetails.User(
                        getUser2.getEmail(),
                        getUser2.getPassword(),
                        getUser2.getRoles() == Roles.ADMIN ? authoritiesAdmin:
                                getUser2.getRoles() == Roles.USER? authoritiesUser: authoritiesNonVerifiedUser
//                        Objects.equals(getUser2.getUsername(), "admin")? authoritiesAdmin: authoritiesUser
                        );
            }

        }
    }

    public User getUser(String id) {
        return userRepository.findById(id).get();
    }
}
