package com.regulator.license.service;

import com.regulator.license.dto.LicenseDTO;
import com.regulator.license.model.*;
import com.regulator.license.repository.LicenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LicenseService {
    
    @Autowired
    private LicenseRepository licenseRepository;
    
    public List<LicenseDTO> getAllLicenses() {
        return licenseRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public Optional<LicenseDTO> getLicenseById(Long id) {
        return licenseRepository.findById(id)
                .map(this::convertToDTO);
    }
    
    public LicenseDTO createLicense(LicenseDTO licenseDTO) {
        License license = convertToEntity(licenseDTO);
        License savedLicense = licenseRepository.save(license);
        return convertToDTO(savedLicense);
    }
    
    public Optional<LicenseDTO> updateLicense(Long id, LicenseDTO licenseDTO) {
        return licenseRepository.findById(id)
                .map(existingLicense -> {
                    updateLicenseFields(existingLicense, licenseDTO);
                    License savedLicense = licenseRepository.save(existingLicense);
                    return convertToDTO(savedLicense);
                });
    }
    
    public boolean deleteLicense(Long id) {
        if (licenseRepository.existsById(id)) {
            licenseRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public List<LicenseDTO> searchLicenses(String companyName) {
        return licenseRepository.findByCompanyNameContainingIgnoreCase(companyName)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public void adjustApplicationFee(Long id, double percentage) {
        licenseRepository.findById(id).ifPresent(license -> {
            license.adjustApplicationFee(percentage);
            licenseRepository.save(license);
        });
    }
    
    public boolean compareLicenses(Long id1, Long id2) {
        Optional<License> license1 = licenseRepository.findById(id1);
        Optional<License> license2 = licenseRepository.findById(id2);
        
        if (license1.isPresent() && license2.isPresent()) {
            return license1.get().equals(license2.get());
        }
        return false;
    }
    
    public List<LicenseDTO> getLicensesExpiringWithin(int days) {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(days);
        
        return licenseRepository.findLicensesExpiringBetween(startDate, endDate)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    private LicenseDTO convertToDTO(License license) {
        LicenseDTO dto = new LicenseDTO();
        dto.setId(license.getId());
        dto.setCompanyName(license.getCompanyName());
        dto.setIssueDate(license.getIssueDate());
        dto.setLatitude(license.getLatitude());
        dto.setLongitude(license.getLongitude());
        dto.setEmail(license.getEmail());
        dto.setApplicationFee(license.getApplicationFee());
        dto.setLicenseFee(license.getLicenseFee());
        dto.setValidityPeriod(license.getValidityPeriod());
        dto.setAnnualFrequencyFee(license.getAnnualFrequencyFee());
        dto.setAnnualUSFContribution(license.getAnnualUSFContribution());
        dto.setYearsBeforeExpiry(license.getYearsBeforeExpiry());
        
        if (license instanceof CellularTelecommunicationLicense) {
            dto.setLicenseType("CTL");
        } else if (license instanceof PublicRadioStationLicense) {
            dto.setLicenseType("PRSL");
        }
        
        return dto;
    }
    
    private License convertToEntity(LicenseDTO dto) {
        if ("CTL".equals(dto.getLicenseType())) {
            return new CellularTelecommunicationLicense(
                dto.getCompanyName(),
                dto.getIssueDate(),
                dto.getLatitude(),
                dto.getLongitude(),
                dto.getEmail()
            );
        } else if ("PRSL".equals(dto.getLicenseType())) {
            return new PublicRadioStationLicense(
                dto.getCompanyName(),
                dto.getIssueDate(),
                dto.getLatitude(),
                dto.getLongitude(),
                dto.getEmail(),
                dto.getValidityPeriod()
            );
        }
        throw new IllegalArgumentException("Invalid license type: " + dto.getLicenseType());
    }
    
    private void updateLicenseFields(License license, LicenseDTO dto) {
        license.setCompanyName(dto.getCompanyName());
        license.setIssueDate(dto.getIssueDate());
        license.setLatitude(dto.getLatitude());
        license.setLongitude(dto.getLongitude());
        license.setEmail(dto.getEmail());
        if (dto.getValidityPeriod() != null) {
            license.setValidityPeriod(dto.getValidityPeriod());
        }
    }
}