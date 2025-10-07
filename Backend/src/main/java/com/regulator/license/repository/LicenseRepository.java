package com.regulator.license.repository;

import com.regulator.license.model.License;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface LicenseRepository extends JpaRepository<License, Long> {
    
    List<License> findByCompanyNameContainingIgnoreCase(String companyName);
    
    @Query("SELECT l FROM License l WHERE l.issueDate BETWEEN ?1 AND ?2")
    List<License> findByIssueDateBetween(LocalDate startDate, LocalDate endDate);
    
    @Query("SELECT l FROM License l WHERE l.issueDate BETWEEN :startDate AND :endDate")
    List<License> findLicensesExpiringBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT l FROM License l WHERE TYPE(l) = ?1")
    List<License> findByLicenseType(Class<?> licenseType);
}