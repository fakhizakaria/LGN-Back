package com.example.linguanaturemail.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.linguanaturemail.DTO.ContactForm;
import com.example.linguanaturemail.DTO.EmailRequest;

import sendinblue.ApiClient;
import sendinblue.Configuration;
import sendinblue.auth.ApiKeyAuth;
import sibApi.TransactionalEmailsApi;
import sibModel.CreateSmtpEmail;
import sibModel.SendSmtpEmail;
import sibModel.SendSmtpEmailReplyTo;
import sibModel.SendSmtpEmailSender;
import sibModel.SendSmtpEmailTo;

@Service
public class EmailService {
    @Value("${brevo.apiKey}")
    private String apiKey;

    private ApiClient configureBrevoApiClient() {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        ApiKeyAuth apiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("api-key");
        apiKeyAuth.setApiKey(apiKey);
        return defaultClient;
    }

    public boolean sendEmail(EmailRequest emailRequest) {
        try {
            ApiClient defaultClient = configureBrevoApiClient();
            TransactionalEmailsApi api = new TransactionalEmailsApi();

            ApiKeyAuth apiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("api-key");
            apiKeyAuth.setApiKey(apiKey);

            SendSmtpEmailSender sender = new SendSmtpEmailSender();
            sender.setEmail(emailRequest.getSenderEmail());
            sender.setName(emailRequest.getSenderName());

            SendSmtpEmailTo to = new SendSmtpEmailTo();
            to.setEmail(emailRequest.getReceiverEmail());
            to.setName(emailRequest.getReceiverName());

            SendSmtpEmail sendSmtpEmail = new SendSmtpEmail();
            sendSmtpEmail.setSender(sender);
            sendSmtpEmail.setTo(List.of(to));


            CreateSmtpEmail response = api.sendTransacEmail(sendSmtpEmail);

            return response != null;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean receiveContactForm(ContactForm contactForm) {
        try {
            ApiClient defaultClient = configureBrevoApiClient();
            TransactionalEmailsApi api = new TransactionalEmailsApi();

            ApiKeyAuth apiKeyAuth = (ApiKeyAuth) defaultClient.getAuthentication("api-key");
            apiKeyAuth.setApiKey(apiKey);

            SendSmtpEmail sendEmailToLinguaNature = createEmailToLinguaNature(contactForm);
            CreateSmtpEmail linguaNatureResponse = api.sendTransacEmail(sendEmailToLinguaNature);
            System.out.println("Email to Lingua Nature Sent: " + linguaNatureResponse.toString());

            SendSmtpEmail confirmationEmail = createConfirmationEmail(contactForm);
            CreateSmtpEmail confirmationResponse = api.sendTransacEmail(confirmationEmail);
            System.out.println("Confirmation Email Sent: " + confirmationResponse.toString());

            return true;
        } catch (Exception e) {
            System.out.println("Exception occurred:- " + e.getMessage());
            return false;
        }
    }

    
    private SendSmtpEmail createEmailToLinguaNature(ContactForm contactForm) {
        SendSmtpEmail sendSmtpEmail = new SendSmtpEmail();
        
        SendSmtpEmailSender sender = new SendSmtpEmailSender();
        sender.setEmail(contactForm.getEmail());
        sender.setName(contactForm.getName());
    
        List<SendSmtpEmailTo> toList = new ArrayList<>();
        SendSmtpEmailTo to = new SendSmtpEmailTo();
        to.setEmail("linguanature94@gmail.com");
        to.setName("Lingua Nature");
        toList.add(to);
    
        SendSmtpEmailReplyTo replyTo = new SendSmtpEmailReplyTo();
        replyTo.setEmail("fakhizakaria94@gmail.com");
    
        Properties headers = new Properties();
        headers.setProperty("Some-Custom-Name", "unique-id-1234");
        Properties params = new Properties();
        params.setProperty("parameter", contactForm.getEmail());
        params.setProperty("subject", "New Subject");
    
        sendSmtpEmail.setSender(sender);
        sendSmtpEmail.setTo(toList);
        sendSmtpEmail.setHtmlContent("<html><body><h1>This is my first transactional email " +
                                contactForm.getMessage() + "</h1></body></html>");
        sendSmtpEmail.setSubject("Lingua Nature - Contact US");
        sendSmtpEmail.setReplyTo(replyTo);
    
        sendSmtpEmail.setHeaders(headers);
        sendSmtpEmail.setParams(params);
    
        return sendSmtpEmail;
    }
    
    private SendSmtpEmail createConfirmationEmail(ContactForm contactForm) {
        SendSmtpEmail confirmationEmail = new SendSmtpEmail();
        
        SendSmtpEmailSender clientSender = new SendSmtpEmailSender();
        clientSender.setEmail("linguanature94@gmail.com");
        clientSender.setName("Lingua Nature");
    
        List<SendSmtpEmailTo> clientToList = new ArrayList<>();
        SendSmtpEmailTo clientTo = new SendSmtpEmailTo();
        clientTo.setEmail(contactForm.getEmail()); // Client's email
        clientTo.setName(contactForm.getName()); // Client's name
        clientToList.add(clientTo);
    
        confirmationEmail.setSender(clientSender);
        confirmationEmail.setTo(clientToList);
        confirmationEmail.setHtmlContent("<html><body><h1>Thank you for contacting us!</h1><p>We have received your message:</p><p><strong>Name:</strong> " +
            contactForm.getName() + "</p><p><strong>Message:</strong> " + contactForm.getMessage() + "</p><p>We will get back to you soon.</p></body></html>");
        confirmationEmail.setSubject("Lingua Nature - Contact Confirmation");
        return confirmationEmail;
    }
        
}