package com.example.mycalendar.verify;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@PreAuthorize("permitAll()")
@RestController
@RequestMapping(path = "api/v1/verify")
public class VerifyController {


    @Autowired
    VerifyService verifyService;


    @GetMapping( "/{id}")
    public void getProfilePicture(@PathVariable(required = false) String id, HttpServletResponse response) throws IOException {
        verifyService.verify(id);
    }
}
