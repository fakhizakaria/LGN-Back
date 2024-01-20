package com.example.linguanaturemail.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.linguanaturemail.DTO.ContactForm;

import sendinblue.ApiClient;
import sendinblue.Configuration;
import sendinblue.auth.ApiKeyAuth;
import sibApi.TransactionalEmailsApi;
import sibModel.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    private String loadTemplateContent(String templatePath) throws IOException {
        String fullPath = "templates/" + templatePath;
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fullPath);
        if (inputStream != null) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                return reader.lines().collect(Collectors.joining("\n"));
            }
        }
        throw new FileNotFoundException("Template not found: " + fullPath);
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
            System.out.println("Exception occurred: " + e.getMessage());
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

        try {
            String emailTemplate = loadTemplateContent("contactForm.html");

            String emailContent = emailTemplate.replace("{{NAME}}", contactForm.getName())
                                               .replace("{{EMAIL}}", contactForm.getEmail())
                                               .replace("{{MESSAGE}}", contactForm.getMessage());

            sendSmtpEmail.setSender(sender);
            sendSmtpEmail.setTo(toList);
            sendSmtpEmail.setHtmlContent(emailContent);
            sendSmtpEmail.setSubject("Lingua Nature - Contact US");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sendSmtpEmail;
    }

    private SendSmtpEmail createConfirmationEmail(ContactForm contactForm) {
        SendSmtpEmail confirmationEmail = new SendSmtpEmail();
        SendSmtpEmailSender clientSender = new SendSmtpEmailSender();
        clientSender.setEmail("linguanature94@gmail.com");
        clientSender.setName("Lingua Nature");

        List<SendSmtpEmailTo> clientToList = new ArrayList<>();
        SendSmtpEmailTo clientTo = new SendSmtpEmailTo();
        clientTo.setEmail(contactForm.getEmail());
        clientTo.setName(contactForm.getName());
        clientToList.add(clientTo);

        try {
            String confirmationTemplate = loadTemplateContent("mailConfirmation.html");

            String confirmationContent = confirmationTemplate.replace("{{NAME}}", contactForm.getName())
                                                             .replace("{{MESSAGE}}", contactForm.getMessage());

            confirmationEmail.setSender(clientSender);
            confirmationEmail.setTo(clientToList);
            confirmationEmail.setHtmlContent(confirmationContent);
            confirmationEmail.setSubject("Lingua Nature - Contact Confirmation");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return confirmationEmail;
    }
}
