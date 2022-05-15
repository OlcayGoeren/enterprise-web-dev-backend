package com.example.mycalendar;

//import com.example.mycalendar.mail.EmailServiceImpl;
import com.example.mycalendar.mail.MyJavaMailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class MycalendarApplication {

	@Autowired
	private MyJavaMailSender mailSender;

	public static void main(String[] args) {
		SpringApplication.run(MycalendarApplication.class, args);
	}


	@GetMapping
	public String sayHello(){
//		mailSender.send(user, "Hello World!", "Hello from Spring Boot Application");
		return "Hello World";
	}
}
