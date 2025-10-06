package com.regulator.license.controller;

import com.regulator.license.dto.LicenseDTO;
import com.regulator.license.service.LicenseService;
import com.regulator.license.service.ReportService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/licenses")
@CrossOrigin(origins = "http://localhost:3000")
public class LicenseController {
    
    @Autowired
    private LicenseService licenseService;
    
    @Autowired
    private ReportService reportService;
    
    @GetMapping
    public ResponseEntity<List<LicenseDTO>> getAllLicenses() {
        List<LicenseDTO> licenses = licenseService.getAllLicenses();
        return ResponseEntity.ok(licenses);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<LicenseDTO> getLicenseById(@PathVariable Long id) {
        return licenseService.getLicenseById(id)
                .map(license -> ResponseEntity.ok(license))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LicenseDTO> createLicense(@Valid @RequestBody LicenseDTO licenseDTO) {
        LicenseDTO createdLicense = licenseService.createLicense(licenseDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLicense);
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LicenseDTO> updateLicense(@PathVariable Long id, @Valid @RequestBody LicenseDTO licenseDTO) {
        return licenseService.updateLicense(id, licenseDTO)
                .map(license -> ResponseEntity.ok(license))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteLicense(@PathVariable Long id) {
        if (licenseService.deleteLicense(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<LicenseDTO>> searchLicenses(@RequestParam String companyName) {
        List<LicenseDTO> licenses = licenseService.searchLicenses(companyName);
        return ResponseEntity.ok(licenses);
    }
    
    @PutMapping("/{id}/adjust-fee")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> adjustApplicationFee(@PathVariable Long id, @RequestBody Map<String, Double> request) {
        Double percentage = request.get("percentage");
        if (percentage != null) {
            licenseService.adjustApplicationFee(id, percentage);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
    
    @GetMapping("/compare/{id1}/{id2}")
    public ResponseEntity<Map<String, Boolean>> compareLicenses(@PathVariable Long id1, @PathVariable Long id2) {
        boolean areEqual = licenseService.compareLicenses(id1, id2);
        return ResponseEntity.ok(Map.of("equal", areEqual));
    }
    
    @GetMapping("/expiring")
    public ResponseEntity<List<LicenseDTO>> getLicensesExpiringWithin(@RequestParam(defaultValue = "30") int days) {
        List<LicenseDTO> licenses = licenseService.getLicensesExpiringWithin(days);
        return ResponseEntity.ok(licenses);
    }
    
    @GetMapping("/reports/pdf")
    public ResponseEntity<byte[]> generatePdfReport() throws IOException {
        byte[] pdfData = reportService.generatePdfReport();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "licenses-report.pdf");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfData);
    }
    
    @GetMapping("/reports/excel")
    public ResponseEntity<byte[]> generateExcelReport() throws IOException {
        byte[] excelData = reportService.generateExcelReport();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "licenses-report.xlsx");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(excelData);
    }
    
    @GetMapping("/reports/csv")
    public ResponseEntity<String> generateCsvReport() throws IOException {
        String csvData = reportService.generateCsvReport();
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        headers.setContentDispositionFormData("attachment", "licenses-report.csv");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(csvData);
    }
}