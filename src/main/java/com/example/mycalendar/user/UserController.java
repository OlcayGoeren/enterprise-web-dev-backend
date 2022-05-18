package com.example.mycalendar.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping(path = "api/v1/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "{userId}")
    public Optional<User> getSingleUser(@PathVariable("userId") String id){
        return userService.getSingleUser(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<User> getAllUser() {
        return userService.getAllUser();
    }

    @GetMapping(path = "self")
    @PreAuthorize("hasRole('USER')")
    public User getSelf() {
       return userService.getSelf();
    }

    @PutMapping
    @PreAuthorize("hasRole('USER')")
    public void updateSelf(
//            @PathVariable("userId") String id,
            @RequestParam(required = false) String email
    ){
        userService.updateSelf(email);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path ="{userId}")
    public void updateSpecific(
            @PathVariable("userId") String id,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String username
    ){
        userService.updatespecific(id, email, username);
    }


    @DeleteMapping(path = "self")
    @PreAuthorize("hasRole('USER')")
    public void deleteSelf(){
        userService.deleteSelf();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "{userId}")
    public void deleteUser(@PathVariable("userId") String id){
        userService.deleteUserById(id);
    }
}
