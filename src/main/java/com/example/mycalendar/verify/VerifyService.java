package com.example.mycalendar.verify;

import com.example.mycalendar.user.Roles;
import com.example.mycalendar.user.User;
import com.example.mycalendar.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;

@Service
public class VerifyService {

    private VerifyRepository verifyRepository;

    @Autowired
    public VerifyService(VerifyRepository verifyRepository) {
        this.verifyRepository = verifyRepository;
    }

    @Transactional
    public void verify(String id) {
        Verify verify= verifyRepository.findById(id).orElseThrow(
                () -> {throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There is no user linked to verifyId "+ id);
                }
        );
        verify.getUser().setVerified(true);
        verify.getUser().setRoles(Roles.USER);
        verifyRepository.delete(verify);
    }
}
