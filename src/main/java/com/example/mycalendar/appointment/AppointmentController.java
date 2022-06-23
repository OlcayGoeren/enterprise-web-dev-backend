package com.example.mycalendar.appointment;

import com.example.mycalendar.mail.MyJavaMailSender;
import com.example.mycalendar.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@PreAuthorize("isAuthenticated()")
@RestController
@CrossOrigin
@RequestMapping(path = "api/v1/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;
    private final Util util;
    private final MyJavaMailSender mailSender;

    @Autowired
    public AppointmentController(AppointmentService service, Util util, MyJavaMailSender mailSender) {
        this.appointmentService = service;
        this.util = util;
        this.mailSender = mailSender;
    }

    @PreAuthorize("hasRole('USER') or hasRole('NONV_USER')")
    @GetMapping(path = "{aId}")
    public Appointment getSingleAppointment(@PathVariable("aId") String id){
        return appointmentService.getSingle(id);
    }

    @PreAuthorize("hasRole('USER') or hasRole('NONV_USER')")
    @GetMapping()
    public List<Appointment> getAllAppointments(){
        return appointmentService.getAll();
    }

    @PreAuthorize("hasRole('USER') or hasRole('NONV_USER')")
    @PostMapping
    public Appointment postAppointment( @RequestBody Appointment appointment) throws IOException, MessagingException {
        if (appointment.getTitle() != null && appointment.getTitle().length() > 2
            && appointment.getDetails() != null && appointment.getDetails().length() > 2
                && appointment.getDate().length() > 0
        ) {
            Appointment appointment1= appointmentService.createAppointment(appointment);
            if (appointment1.getEmailList().length > 0) {
                String string = util.getCSV(appointment);
                mailSender.sendFile("Termin", "Hiermit wird dir eine Termineinladung verschickt", string);
            }
            return  appointment1;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title, details and date have to be set!");
        }

    }

    @PreAuthorize("hasRole('USER') or hasRole('NONV_USER')")
    @DeleteMapping(path = "{aId}")
    public void deleteAppointment(@PathVariable("aId") String id){
        appointmentService.deleteSingle(id);
    }

    @PreAuthorize("hasRole('USER') or hasRole('NONV_USER') ")
    @PutMapping(path ="{aId}")
    public Appointment updateSpecific(@PathVariable("aId") String id, @RequestBody Appointment appointment) throws IOException, MessagingException {
        Appointment appointment1 = appointmentService.updatespecific(id, appointment);
        if (appointment1.getEmailList().length > 0 ) {
            String string = util.getCSV(appointment);
            mailSender.sendFile("Termin", "Hiermit wird dir eine Termineinladung verschickt", string);

        }
        return  appointment1;
         }
}
