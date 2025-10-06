package com.regulator.license.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("CTL")
public class CellularTelecommunicationLicense extends License {
    
    private static final BigDecimal DEFAULT_APPLICATION_FEE = new BigDecimal("800.00");
    private static final BigDecimal DEFAULT_LICENSE_FEE = new BigDecimal("100000000.00");
    private static final Integer DEFAULT_VALIDITY_PERIOD = 15;
    private static final BigDecimal ANNUAL_USF_CONTRIBUTION = new BigDecimal("3000.00");
    
    public CellularTelecommunicationLicense() {
        super();
    }
    
    public CellularTelecommunicationLicense(String companyName, LocalDate issueDate, 
                                          Double latitude, Double longitude, String email) {
        super(companyName, issueDate, latitude, longitude, email, 
              DEFAULT_APPLICATION_FEE, DEFAULT_LICENSE_FEE, DEFAULT_VALIDITY_PERIOD);
    }
    
    @Override
    public BigDecimal getAnnualFrequencyFee() {
        return BigDecimal.ZERO;
    }
    
    @Override
    public BigDecimal getAnnualUSFContribution() {
        return ANNUAL_USF_CONTRIBUTION;
    }
}