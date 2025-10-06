package com.regulator.license.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class LicenseDTO {
    private Long id;
    
    @NotBlank(message = "Company name is required")
    private String companyName;
    
    @NotNull(message = "Issue date is required")
    private LocalDate issueDate;
    
    @NotNull(message = "Latitude is required")
    @DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90")
    @DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90")
    private Double latitude;
    
    @NotNull(message = "Longitude is required")
    @DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180")
    @DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180")
    private Double longitude;
    
    @Email(message = "Valid email is required")
    @NotBlank(message = "Email is required")
    private String email;
    
    @NotBlank(message = "License type is required")
    private String licenseType;
    
    private Integer validityPeriod;
    private BigDecimal applicationFee;
    private BigDecimal licenseFee;
    private BigDecimal annualFrequencyFee;
    private BigDecimal annualUSFContribution;
    private Integer yearsBeforeExpiry;
    
    public LicenseDTO() {}
    
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
    
    public String getLicenseType() { return licenseType; }
    public void setLicenseType(String licenseType) { this.licenseType = licenseType; }
    
    public Integer getValidityPeriod() { return validityPeriod; }
    public void setValidityPeriod(Integer validityPeriod) { this.validityPeriod = validityPeriod; }
    
    public BigDecimal getApplicationFee() { return applicationFee; }
    public void setApplicationFee(BigDecimal applicationFee) { this.applicationFee = applicationFee; }
    
    public BigDecimal getLicenseFee() { return licenseFee; }
    public void setLicenseFee(BigDecimal licenseFee) { this.licenseFee = licenseFee; }
    
    public BigDecimal getAnnualFrequencyFee() { return annualFrequencyFee; }
    public void setAnnualFrequencyFee(BigDecimal annualFrequencyFee) { this.annualFrequencyFee = annualFrequencyFee; }
    
    public BigDecimal getAnnualUSFContribution() { return annualUSFContribution; }
    public void setAnnualUSFContribution(BigDecimal annualUSFContribution) { this.annualUSFContribution = annualUSFContribution; }
    
    public Integer getYearsBeforeExpiry() { return yearsBeforeExpiry; }
    public void setYearsBeforeExpiry(Integer yearsBeforeExpiry) { this.yearsBeforeExpiry = yearsBeforeExpiry; }
}