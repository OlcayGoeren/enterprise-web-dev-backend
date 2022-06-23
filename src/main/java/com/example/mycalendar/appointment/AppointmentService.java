package com.example.mycalendar.appointment;

import com.example.mycalendar.mail.MyJavaMailSender;
import com.example.mycalendar.user.Roles;
import com.example.mycalendar.user.User;
import com.example.mycalendar.visitor.Visitor;
import com.example.mycalendar.visitor.VisitorRepository;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final VisitorRepository visitorRepository;
    private final MyJavaMailSender mailSender;
    private final Util util;
    
    @Autowired
    public AppointmentService(AppointmentRepository repository, MyJavaMailSender mailSender, Util util, VisitorRepository visitorRepository ) {
        this.appointmentRepository = repository;
        this.mailSender = mailSender;
        this.util = util;
        this.visitorRepository = visitorRepository;
    }

    public Appointment getSingle(String id) {
        return appointmentRepository.findById(id).orElseThrow(
                () ->  new ResponseStatusException(HttpStatus.BAD_REQUEST, "Appointment with email "+id+" not found")
        );
    }

    public List<Appointment> getAll() {
       User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
       if (user.getRoles() == Roles.USER || user.getRoles() == Roles.ADMIN)  {
           return appointmentRepository.findAppointmentByUser(user);
       } else {
           String email = user.getEmail();
           Visitor visitor = visitorRepository.findByEmail(email).orElseThrow(
                   () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong"));
           User userInVisitor = visitor.getUser();
           return appointmentRepository.findAppointmentByUser(userInVisitor);
       }
    }

    public void deleteSingle(String id) {
        Appointment appointment = appointmentRepository.findById(id).orElseThrow();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getRoles() == Roles.USER || user.getRoles() == Roles.ADMIN) {

            User user2 = appointment.getUser();
            if (user.equals(user2)) {
                appointmentRepository.deleteById(id);
            }
        } else {
            User userFromAppointment = appointment.getUser();
            String email = user.getEmail();
            Visitor visitor = visitorRepository.findByEmail(email).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong"));
            User userFromVisitor = visitor.getUser();
            if (userFromVisitor.equals(userFromAppointment) && !appointment.isFromBetreiber()) {
                appointmentRepository.deleteById(id);
            }

        }

    }


    @Transactional
    public Appointment updatespecific(String id, Appointment appointment) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Appointment appointment1 = appointmentRepository.findById(id).orElseThrow(
                () ->  new ResponseStatusException(HttpStatus.BAD_REQUEST, "Appointment with email "+id+" not found")
        );

        if (user.equals(appointment1.getUser())) {
            if (appointment.getTitle() != null) {
                appointment1.setTitle(appointment.getTitle());
            }

            if (appointment.getDetails() != null) {
                appointment1.setDetails(appointment.getDetails());
            }

            if (appointment.getDate() != null) {
                appointment1.setDate(appointment.getDate());
            }

            if (appointment.getOrt() != null) {
                appointment1.setOrt(appointment.getOrt());
            }

            if (appointment.getIntervall() != null) {
                appointment1.setIntervall(appointment.getIntervall());
            }

            if (appointment.getEmailList() != null) {
                appointment1.setEmailList(appointment.getEmailList());
            }

            if (appointment.getVon() != null) {
                appointment1.setVon(appointment.getVon());
            }

            if (appointment.getBis() != null) {
                appointment1.setBis(appointment.getBis());
            }
        } else {
            if (user.getRoles() == Roles.NON_VERIFIED) {
                User userFromAppointment = appointment.getUser();
                String email = user.getEmail();
                Visitor visitor = visitorRepository.findByEmail(email).orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong"));
                User userFromVisitor = visitor.getUser();
                if (userFromVisitor.equals(userFromAppointment) && !appointment.isFromBetreiber()) {
                    if (appointment.getTitle() != null) {
                        appointment1.setTitle(appointment.getTitle());
                    }

                    if (appointment.getDetails() != null) {
                        appointment1.setDetails(appointment.getDetails());
                    }

                    if (appointment.getDate() != null) {
                        appointment1.setDate(appointment.getDate());
                    }

                    if (appointment.getOrt() != null) {
                        appointment1.setOrt(appointment.getOrt());
                    }

                    if (appointment.getIntervall() != null) {
                        appointment1.setIntervall(appointment.getIntervall());
                    }

                    if (appointment.getEmailList() != null) {
                        appointment1.setEmailList(appointment.getEmailList());
                    }

                    if (appointment.getVon() != null) {
                        appointment1.setVon(appointment.getVon());
                    }

                    if (appointment.getBis() != null) {
                        appointment1.setBis(appointment.getBis());
                    }
                } else {
                    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
                }
            }
        }
        return appointment1;
    }

    public Appointment createAppointment(Appointment appointment)  {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user.getRoles() == Roles.USER || user.getRoles() == Roles.ADMIN)  {
            appointment.setUser(user);
            appointmentRepository.save(appointment);
            return appointment;
        } else {
            String email = user.getEmail();
            Visitor visitor = visitorRepository.findByEmail(email).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Something went wrong"));
            appointment.setUser(visitor.getUser());
            appointment.setFromBetreiber(false);
            return appointmentRepository.save(appointment);
        }
    }
}
