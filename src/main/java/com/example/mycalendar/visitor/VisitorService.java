package com.example.mycalendar.visitor;

import com.example.mycalendar.auth.AuthService;
import com.example.mycalendar.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Map;

@Service
public class VisitorService {
    private final VisitorRepository repository;
    private final AuthService service;

    @Autowired
    public VisitorService(VisitorRepository repository, AuthService service) {
        this.repository = repository;
        this.service = service;
    }

    public String createVisitor() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Visitor visitor = new Visitor();
        visitor.setUser(user);
        Visitor savedVisitor = this.repository.save(visitor);
        return savedVisitor.getId();
    }
    @Transactional
    public Map<String, String> postEmail(VisitorCredentials credentials) {
        Visitor visitor = repository.getById(credentials.getShareId());
        if (visitor.getEmail() != null && visitor.getEmail().length() >0) {
            Map<String, String> jwt = service.loginGuest(credentials.getVisitorEmail());
            return jwt;
        } else {
            visitor.setEmail(credentials.getVisitorEmail());
            Map<String, String> jwt = service.registerGuest(credentials.getVisitorEmail());
            return jwt;
        }


    }


}
