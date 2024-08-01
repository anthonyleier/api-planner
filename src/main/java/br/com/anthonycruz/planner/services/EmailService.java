package br.com.anthonycruz.planner.services;

import java.util.UUID;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    private String buildConfirmationLink(UUID participantID) {
        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/participants/")
                .path(participantID.toString())
                .path("/confirm")
                .toUriString();
    }

    public void sendConfirmationEmail(UUID participantID, String to) {
        String confirmationLink = this.buildConfirmationLink(participantID);
        String subject = "Confirm your participation";
        String text = "Please, use the link to confirm your participation: " + confirmationLink;
        this.sendEmail(to, subject, text);
    }
}
