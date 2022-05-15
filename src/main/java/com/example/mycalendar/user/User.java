package com.example.mycalendar.user;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    @Column(columnDefinition = "CHAR(32)")
    private String id;


    @NotNull
    @Column(unique = true)
    private String email;

    @NotNull
    private Roles roles;

    @NotNull
    @Column(nullable = false)
    private String username;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private boolean isBlocked = false;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private boolean verified = false;

    public User(String email, String username, String password, Roles roles) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public User(String id, String email, String username, String password, Roles roles) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.roles = roles;
    }
}
