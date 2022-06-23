package com.example.mycalendar.visitor;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

//@PreAuthorize("permitAll()")
//@PreAuthorize("isAuthenticated()")
@RestController
@CrossOrigin
@RequestMapping(path = "api/v1/visitor")
public class VisitorController {
    @Autowired
    VisitorService service;


    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public String postVisitor(){
        return service.createVisitor();
    }

    @PutMapping
    public Map<String, String> postEmail(@RequestBody VisitorCredentials credentials) {
        Map<String, String> jwt = service.postEmail(credentials);
        return jwt;
    }

    public void getVisitor(){

    }

    public void deleteVisitor(){

    }

}
