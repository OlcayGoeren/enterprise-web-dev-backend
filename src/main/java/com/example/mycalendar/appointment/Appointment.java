package com.example.mycalendar.appointment;

import com.example.mycalendar.user.Roles;
import com.example.mycalendar.user.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "appointments")
@Data
public class Appointment {
    @NotNull
    @OneToOne
    private  User user;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(columnDefinition = "CHAR(32)")
    private  String id;

    @NotNull
    private  String title;

    @NotNull
    private  String details;

    private String date;

    private String ort;

    private Intervall intervall;

    private String[] emailList;

    private String von;
    private String bis;

    private boolean fromBetreiber = true;

    public Appointment(User user) {
        this.user = user;
    }
}
