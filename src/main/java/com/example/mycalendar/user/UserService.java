package com.example.mycalendar.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;


    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    public void createUser(User user) {
        if (user != null && userRepository.findUserByEmail(user.getEmail()).isEmpty()){
            String encodedPass = encoder.encode(user.getPassword());
            user.setPassword(encodedPass);
            userRepository.save(user);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"User with Email: "+user.getEmail()+ " is already existing.");
        }
    }

    public Optional<User> getSingleUser(String id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user;
        }else {
            throw new IllegalStateException("User with ID:"+id+" not found");
        }
    }

    public void deleteSelf() {
        String id = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userRepository.deleteById(id);
    }

    @Transactional
    public void updateSelf(String email, String username) {
        String id = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        boolean isExisting = userRepository.existsById(id);

        User user = userRepository.findById(id).get();
        if (email!=null && email.length() > 0){
            user.setEmail(email);
        }

        if (username!=null && username.length() > 0){
            user.setUsername(username);
        }
    }

    public User getSelf() {
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Transactional
    public void updatespecific(String id, String email, String username) {
        User user = userRepository.findById(id).orElseThrow(
                () ->  new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with email "+email+" not found")
        );
        user.setEmail(email);
        user.setUsername(username);
    }

    public void deleteUserById(String id) {
        User user = userRepository.findById(id).orElseThrow(
                () ->  new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with id "+id+" not found")
        );
        userRepository.deleteById(id);
    }
}
