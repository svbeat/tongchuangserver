package com.tongchuang.visiondemo.perimetry;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.tongchuang.visiondemo.perimetry.entity.PerimetryTest;

public interface PerimetryTestRepository extends CrudRepository<PerimetryTest, Long> {
    
    @Query("SELECT count(1) FROM PerimetryTest p WHERE patientId = :id and IFNULL(deleted, 'N')<>'Y'") 
    Integer getTotalByPatientId(@Param("id") String patientId);
    
    @Query("SELECT p FROM PerimetryTest p WHERE patientId = :patientid and testId in :testids and IFNULL(deleted, 'N')<>'Y' order by creationDate asc") 
    List<PerimetryTest> getByPatientTestIds(@Param("patientid") String patientId, @Param("testids") List<Long> testIds);
    
    
    @Query("SELECT p FROM PerimetryTest p WHERE patientId = :patientid "
    		+ " and test_date>(UNIX_TIMESTAMP(subdate(current_date, :days))*1000) and IFNULL(deleted, 'N')<>'Y'") 
    List<PerimetryTest> getPastTests(@Param("patientid") String patientId, @Param("days") int days);
    
    @Query("SELECT p FROM PerimetryTest p WHERE patientId = :patientid and IFNULL(deleted, 'N')<>'Y' order by testDate desc") 
    List<PerimetryTest> findByPatientId(@Param("patientid") String patientId, Pageable pageable);
}
