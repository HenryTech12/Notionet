package com.taskmanager.app.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MailService {

    @Value("${spring.mail.username}")
    private String from;
    @Autowired
    private JavaMailSender javaMailSender;

    public void sendMessage(MailInfo mailInfo) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject(mailInfo.getSubject());
        simpleMailMessage.setText(mailInfo.getMessage());
        simpleMailMessage.setTo(mailInfo.getTo());
        simpleMailMessage.setFrom(from);

        javaMailSender.send(simpleMailMessage);
        log.info("mail sent ...");

    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MailInfo {

        private String subject;
        private String message;
        private String to;
    }
}
