package com.regulator.license.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("PRSL")
public class PublicRadioStationLicense extends License {
    
    private static final BigDecimal DEFAULT_APPLICATION_FEE = new BigDecimal("350.00");
    private static final BigDecimal DEFAULT_LICENSE_FEE = new BigDecimal("2000000.00");
    private static final BigDecimal ANNUAL_FREQUENCY_FEE = new BigDecimal("2000.00");
    
    public PublicRadioStationLicense() {
        super();
    }
    
    public PublicRadioStationLicense(String companyName, LocalDate issueDate, 
                                   Double latitude, Double longitude, String email, Integer validityPeriod) {
        super(companyName, issueDate, latitude, longitude, email, 
              DEFAULT_APPLICATION_FEE, DEFAULT_LICENSE_FEE, validityPeriod);
    }
    
    @Override
    public BigDecimal getAnnualFrequencyFee() {
        return ANNUAL_FREQUENCY_FEE;
    }
    
    @Override
    public BigDecimal getAnnualUSFContribution() {
        return BigDecimal.ZERO;
    }
}