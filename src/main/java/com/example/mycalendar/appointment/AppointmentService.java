package com.example.mycalendar.appointment;

import com.example.mycalendar.mail.MyJavaMailSender;
import com.example.mycalendar.user.User;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final MyJavaMailSender mailSender;
    private final Util util;
    
    @Autowired
    public AppointmentService(AppointmentRepository repository, MyJavaMailSender mailSender, Util util ) {
        this.appointmentRepository = repository;
        this.mailSender = mailSender;
        this.util = util;
    }

    public Appointment getSingle(String id) {
        return appointmentRepository.findById(id).orElseThrow(
                () ->  new ResponseStatusException(HttpStatus.BAD_REQUEST, "Appointment with email "+id+" not found")
        );
    }

    public List<Appointment> getAll() {
       User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
       return appointmentRepository.findAppointmentByUser(user);
    }

    public void deleteSingle(String id) {
        appointmentRepository.deleteById(id);
    }
    @Transactional
    public Appointment updatespecific(String id, Appointment appointment) {
        Appointment appointment1 = appointmentRepository.findById(id).orElseThrow(
                () ->  new ResponseStatusException(HttpStatus.BAD_REQUEST, "Appointment with email "+id+" not found")
        );

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
        return appointment1;
    }

    public Appointment createAppointment(Appointment appointment)  {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        appointment.setUser(user);
        appointmentRepository.save(appointment);
        return appointment;
    }
}
