package com.example.mycalendar.visitor;
import com.example.mycalendar.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface VisitorRepository extends JpaRepository<Visitor, String> {

    Optional<Visitor> findByEmail(String email);
}