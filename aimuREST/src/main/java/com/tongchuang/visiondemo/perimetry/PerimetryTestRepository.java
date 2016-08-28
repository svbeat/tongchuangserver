package com.tongchuang.visiondemo.perimetry;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface PerimetryTestRepository extends CrudRepository<PerimetryTest, Long> {

    List<PerimetryTest> findByPatientId(String patientId, Pageable pageable);
    
    @Query("SELECT count(1) FROM PerimetryTest p WHERE patientId = :id") 
    Integer getTotalByPatientId(@Param("id") String patientId);
    
    @Query("SELECT p FROM PerimetryTest p WHERE patientId = :patientid and testId in :testids order by creationDate asc") 
    List<PerimetryTest> getByPatientTestIds(@Param("patientid") String patientId, @Param("testids") List<Long> testIds);
}
