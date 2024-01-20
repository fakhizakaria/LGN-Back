package com.example.linguanaturemail.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.linguanaturemail.DTO.ContactForm;
import com.example.linguanaturemail.service.EmailService;

@RestController
@RequestMapping("/contact")
public class ContactController {

    private final EmailService emailService;

    public ContactController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/submit")
    public boolean submitContactForm(@RequestBody ContactForm contactForm) {
        return emailService.receiveContactForm(contactForm);
    }
    
}