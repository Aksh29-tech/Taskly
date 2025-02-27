package com.taskly.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {
    @Value("${spring.mail.host}")
    private String mailHost;

    @Value("${spring.mail.port}")
    private int mailPort;

    @Value("${spring.mail.username}")
    private String mailUsername;

    @Value("${spring.mail.password}")
    private String mailPassword;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailHost); // smtp.gmail.com
        mailSender.setPort(mailPort); // 587 for STARTTLS
        mailSender.setUsername(mailUsername); // Your email username
        mailSender.setPassword(mailPassword); // Your email password

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true"); // Enable authentication
        props.put("mail.smtp.starttls.enable", "true"); // Enable STARTTLS
        props.put("mail.smtp.starttls.required", "true"); // Enforce STARTTLS
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com"); // Trust the SMTP server

        return mailSender;
    }
}
