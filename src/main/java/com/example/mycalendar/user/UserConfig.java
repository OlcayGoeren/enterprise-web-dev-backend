package com.example.mycalendar.user;

import com.example.mycalendar.appointment.Appointment;
import com.example.mycalendar.appointment.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Configuration
public class UserConfig {

    public static void setTimeout(Runnable runnable, int delay){
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                runnable.run();
            }
            catch (Exception e){
                System.err.println(e);
            }
        }).start();
    }

    @Autowired
    PasswordEncoder encoder;
    @Bean
    CommandLineRunner commandLineRunner(UserRepository repo, AppointmentRepository repoAppo) {

        return args -> {
            User user = new User("0","admin@example.com",encoder.encode("12345"), Roles.ADMIN, true);
            User olcay = new User("1","olcay@example.com",encoder.encode("12345"), Roles.USER , false);
            repo.saveAllAndFlush(List.of(user, olcay));

            System.out.println(user);

//            one.setDetails("Details1");
//            LocalDate localDate = LocalDate.of(2022, 4, 20);
//            Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
//            one.setDate(date);

//            setTimeout(() -> {
//                Appointment one = new Appointment(user);
//                one.setTitle("Titel1");
//                repoAppo.save(one);
//            }, 1000);
//            Appointment two = new Appointment();
//            repoAppo.saveAll(List.of(one,two));
        };
    }
}
