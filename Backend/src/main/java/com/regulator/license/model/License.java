package com.regulator.license.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "licenses")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "license_type")
public abstract class License {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Column(name = "company_name", nullable = false)
    private String companyName;
    
    @NotNull
    @Column(name = "issue_date", nullable = false)
    private LocalDate issueDate;
    
    @NotNull
    @Column(name = "latitude", nullable = false)
    private Double latitude;
    
    @NotNull
    @Column(name = "longitude", nullable = false)
    private Double longitude;
    
    @Email
    @NotBlank
    @Column(name = "email", nullable = false)
    private String email;
    
    @NotNull
    @Column(name = "application_fee", nullable = false, precision = 15, scale = 2)
    private BigDecimal applicationFee;
    
    @NotNull
    @Column(name = "license_fee", nullable = false, precision = 15, scale = 2)
    private BigDecimal licenseFee;
    
    @NotNull
    @Column(name = "validity_period", nullable = false)
    private Integer validityPeriod;
    
    public License() {}
    
    public License(String companyName, LocalDate issueDate, Double latitude, Double longitude, 
                  String email, BigDecimal applicationFee, BigDecimal licenseFee, Integer validityPeriod) {
        this.companyName = companyName;
        this.issueDate = issueDate;
        this.latitude = latitude;
        this.longitude = longitude;
        this.email = email;
        this.applicationFee = applicationFee;
        this.licenseFee = licenseFee;
        this.validityPeriod = validityPeriod;
    }
    
    public abstract BigDecimal getAnnualFrequencyFee();
    public abstract BigDecimal getAnnualUSFContribution();
    
    public int getYearsBeforeExpiry() {
        LocalDate expiryDate = issueDate.plusYears(validityPeriod);
        return (int) (expiryDate.toEpochDay() - LocalDate.now().toEpochDay()) / 365;
    }
    
    public void adjustApplicationFee(double percentage) {
        BigDecimal adjustment = applicationFee.multiply(BigDecimal.valueOf(percentage / 100));
        this.applicationFee = applicationFee.add(adjustment);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        License license = (License) obj;
        return Objects.equals(companyName, license.companyName) &&
               Objects.equals(issueDate, license.issueDate) &&
               Objects.equals(latitude, license.latitude) &&
               Objects.equals(longitude, license.longitude) &&
               Objects.equals(email, license.email) &&
               Objects.equals(applicationFee, license.applicationFee) &&
               Objects.equals(licenseFee, license.licenseFee) &&
               Objects.equals(validityPeriod, license.validityPeriod);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(companyName, issueDate, latitude, longitude, email, 
                          applicationFee, licenseFee, validityPeriod);
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    
    public LocalDate getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDate issueDate) { this.issueDate = issueDate; }
    
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public BigDecimal getApplicationFee() { return applicationFee; }
    public void setApplicationFee(BigDecimal applicationFee) { this.applicationFee = applicationFee; }
    
    public BigDecimal getLicenseFee() { return licenseFee; }
    public void setLicenseFee(BigDecimal licenseFee) { this.licenseFee = licenseFee; }
    
    public Integer getValidityPeriod() { return validityPeriod; }
    public void setValidityPeriod(Integer validityPeriod) { this.validityPeriod = validityPeriod; }
}