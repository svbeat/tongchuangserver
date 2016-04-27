package com.tongchuang.visiondemo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface PerimetryTestRepository extends CrudRepository<PerimetryTest, Long> {

    List<PerimetryTest> findByPatientId(String patientId);
}
