package com.example.mycalendar;

//import com.example.mycalendar.mail.EmailServiceImpl;
import com.example.mycalendar.mail.MyJavaMailSender;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;

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
		return "Hello World";
	}

	@GetMapping(path = "hello")
	public void hello() throws IOException, MessagingException {
		CloseableHttpClient client = HttpClientBuilder.create().build();
		HttpPost request = new HttpPost("http://localhost:3300/download");
		StringEntity params = new StringEntity("details={\"name\":\"xyz\",\"age\":\"20\"} ");
		request.addHeader("content-type", "application/x-www-form-urlencoded");
		request.setEntity(params);
		HttpResponse response = client.execute(request);
		HttpEntity entity = response.getEntity();
		InputStream inputStream = entity.getContent();
		String result = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
		inputStream.close();
		mailSender.sendFile("Ist hier eine file", "dies ist meine description", result);
	}
}
