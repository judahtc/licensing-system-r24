package com.regulator.license.service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.opencsv.CSVWriter;
import com.regulator.license.dto.LicenseDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

@Service
public class ReportService {
    
    @Autowired
    private LicenseService licenseService;
    
    public byte[] generatePdfReport() throws IOException {
        List<LicenseDTO> licenses = licenseService.getAllLicenses();
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        
        document.add(new Paragraph("License Management Report"));
        
        Table table = new Table(6);
        table.addHeaderCell("Company Name");
        table.addHeaderCell("License Type");
        table.addHeaderCell("Issue Date");
        table.addHeaderCell("Email");
        table.addHeaderCell("Application Fee");
        table.addHeaderCell("Years Before Expiry");
        
        for (LicenseDTO license : licenses) {
            table.addCell(license.getCompanyName());
            table.addCell(license.getLicenseType());
            table.addCell(license.getIssueDate().toString());
            table.addCell(license.getEmail());
            table.addCell(license.getApplicationFee().toString());
            table.addCell(license.getYearsBeforeExpiry().toString());
        }
        
        document.add(table);
        document.close();
        
        return baos.toByteArray();
    }
    
    public byte[] generateExcelReport() throws IOException {
        List<LicenseDTO> licenses = licenseService.getAllLicenses();
        
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Licenses");
        
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Company Name");
        headerRow.createCell(1).setCellValue("License Type");
        headerRow.createCell(2).setCellValue("Issue Date");
        headerRow.createCell(3).setCellValue("Email");
        headerRow.createCell(4).setCellValue("Application Fee");
        headerRow.createCell(5).setCellValue("Years Before Expiry");
        
        int rowNum = 1;
        for (LicenseDTO license : licenses) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(license.getCompanyName());
            row.createCell(1).setCellValue(license.getLicenseType());
            row.createCell(2).setCellValue(license.getIssueDate().toString());
            row.createCell(3).setCellValue(license.getEmail());
            row.createCell(4).setCellValue(license.getApplicationFee().doubleValue());
            row.createCell(5).setCellValue(license.getYearsBeforeExpiry());
        }
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos);
        workbook.close();
        
        return baos.toByteArray();
    }
    
    public String generateCsvReport() throws IOException {
        List<LicenseDTO> licenses = licenseService.getAllLicenses();
        
        StringWriter stringWriter = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(stringWriter);
        
        String[] header = {"Company Name", "License Type", "Issue Date", "Email", "Application Fee", "Years Before Expiry"};
        csvWriter.writeNext(header);
        
        for (LicenseDTO license : licenses) {
            String[] data = {
                license.getCompanyName(),
                license.getLicenseType(),
                license.getIssueDate().toString(),
                license.getEmail(),
                license.getApplicationFee().toString(),
                license.getYearsBeforeExpiry().toString()
            };
            csvWriter.writeNext(data);
        }
        
        csvWriter.close();
        return stringWriter.toString();
    }
}