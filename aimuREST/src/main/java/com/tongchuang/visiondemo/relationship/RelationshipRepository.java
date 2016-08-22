package com.tongchuang.visiondemo.relationship;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.tongchuang.visiondemo.doctor.entity.Doctor;
import com.tongchuang.visiondemo.patient.entity.Patient;
import com.tongchuang.visiondemo.patient.entity.PatientExamSettings;
import com.tongchuang.visiondemo.relationship.entity.Relationship;

public interface RelationshipRepository extends CrudRepository<Relationship, Integer> {
}
