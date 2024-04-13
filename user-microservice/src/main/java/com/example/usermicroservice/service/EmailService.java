package com.example.usermicroservice.service;
import com.example.usermicroservice.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Component
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationEmail(User user, String siteURL)
            throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String subject = "Please verify your registration";
        String content = "Hello " + user.getFirstname() + ",\n\n"
                + "Please click the link below to verify your registration:\n\n"
                + siteURL + "/api/user/verify?code=" + user.getVerificationCode() + "\n\n"
                + "Thank you,\n"
                + user.getFirstname();

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("ghofrane.ferchichi@edu.isetcom.tn", "Sender Name"); // Change the sender name if needed
        helper.setTo(toAddress);
        helper.setSubject(subject);
        helper.setText(content);
        mailSender.send(message);
    }
}
