package com.example.mycalendar.mail;

import com.example.mycalendar.user.User;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.xml.crypto.OctetStreamData;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


//https://github.com/mkyong/spring-boot/tree/master/email
//https://myaccount.google.com/apppasswords?rapt=AEjHL4NafWbWRV4Ull1yRyyNt7zy9YjBc6w0N1-9Lv1yxQRo1k_MgZAwDA_-GLDQgBNUj7GPtnrVYI5F5_npTQw2KobCG_PlFA
@Component
public class MyJavaMailSender {

    private JavaMailSender mailSender;

    public MyJavaMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void send(User user, String subject, String description) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        helper.setTo(user.getEmail());
        helper.setSubject(subject);
        helper.setText(description, true);
        mailSender.send(mimeMessage);
    }

    public void sendFile( String subject, String description, String text) throws MessagingException, IOException {
        final ByteArrayOutputStream stream = createInMemoryDocument(text);
        final InputStreamSource attachment = new ByteArrayResource(stream.toByteArray());
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setTo("olcaygoeren@gmail.com");
        helper.setSubject(subject);
        helper.setText(description, true);
        helper.addAttachment("termin.ics", attachment );
        mailSender.send(mimeMessage);
    }

    private ByteArrayOutputStream createInMemoryDocument(String documentBody) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(documentBody.getBytes());
        return outputStream;
    }
}

