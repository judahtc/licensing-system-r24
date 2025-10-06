package com.regulator.license.service;

import com.regulator.license.model.License;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

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
        List<License> expiringLicenses = licenseService.getLicensesExpiringWithin(365)
                .stream()
                .map(dto -> {
                    // Convert DTO back to entity for this example
                    // In a real implementation, you'd modify the service to return entities
                    return null; // Placeholder
                })
                .toList();
        
        // This is a simplified version - in practice, you'd need to properly convert DTOs
        // For now, we'll use the DTO approach
        licenseService.getLicensesExpiringWithin(365).forEach(license -> {
            if (license.getYearsBeforeExpiry() <= 1) {
                try {
                    sendExpiryNotification(
                        license.getEmail(),
                        license.getCompanyName(),
                        license.getYearsBeforeExpiry()
                    );
                } catch (Exception e) {
                    // Log error but continue with other notifications
                    System.err.println("Failed to send email to: " + license.getEmail());
                }
            }
        });
    }
}