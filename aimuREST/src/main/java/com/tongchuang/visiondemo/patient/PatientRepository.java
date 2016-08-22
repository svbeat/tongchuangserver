package com.tongchuang.visiondemo.patient;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.tongchuang.visiondemo.patient.entity.Patient;
import com.tongchuang.visiondemo.patient.entity.PatientExamSettings;

public interface PatientRepository extends CrudRepository<Patient, String> {
	
	 @Query("SELECT p FROM Patient p where p.status<>'DELETED'")
	 public List<Patient> getPatients(Pageable pageable);
	 
	 @Query("SELECT p FROM Patient p, Relationship r where r.relationshipType='DOCTOR_PATIENT' "
	 		+ "and IFNULL(r.deleted, 'N')<>'Y' and r.subjectId=:doctorid "
	 		+ "and r.objectId=p.patientId and p.status<>'DELETED'")
	 public List<Patient> getPatientsByDoctorId(@Param("doctorid") String doctorId, Pageable pageable);
	
	 
	 @Query("SELECT count(1) FROM Patient p, Relationship r where r.relationshipType='DOCTOR_PATIENT' "
		 		+ "and r.deleted<>'Y' and r.subjectId=:doctorid "
		 		+ "and r.objectId=p.patientId and p.status<>'DELETED'")
	public Integer getTotalCountByDoctorId(@Param("doctorid") String doctorId);
	 
    @Query("SELECT count(1) FROM Patient p where p.status<>'DELETED'") 
    public Integer getTotalCount();
}
