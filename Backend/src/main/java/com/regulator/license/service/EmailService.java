package com.regulator.license.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Service
public class EmailService {
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Autowired
    private LicenseService licenseService;
    
    public void sendExpiryNotification(String to, String companyName, int yearsBeforeExpiry) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("License Expiry Notification");
        message.setText(String.format(
            "Dear %s,\n\n" +
            "This is to notify you that your license will expire in %d years.\n" +
            "Please ensure to renew your license before expiry.\n\n" +
            "Best regards,\n" +
            "Telecommunications Regulator",
            companyName, yearsBeforeExpiry
        ));
        
        mailSender.send(message);
    }
    
    @Scheduled(cron = "0 0 9 * * MON") // Every Monday at 9 AM
    public void sendWeeklyExpiryNotifications() {
        licenseService.getLicensesExpiringWithin(365).forEach(license -> {
            if (license.getYearsBeforeExpiry() <= 1) {
                try {
                    sendExpiryNotification(
                        license.getEmail(),
                        license.getCompanyName(),
                        license.getYearsBeforeExpiry()
                    );
                } catch (Exception e) {
                    System.err.println("Failed to send email to: " + license.getEmail());
                }
            }
        });
    }
}