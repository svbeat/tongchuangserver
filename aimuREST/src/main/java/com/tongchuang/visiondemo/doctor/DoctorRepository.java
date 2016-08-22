package com.tongchuang.visiondemo.doctor;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.tongchuang.visiondemo.doctor.entity.Doctor;
import com.tongchuang.visiondemo.patient.entity.Patient;
import com.tongchuang.visiondemo.patient.entity.PatientExamSettings;

public interface DoctorRepository extends CrudRepository<Doctor, Integer> {
	
	 @Query("SELECT p FROM Doctor p where IFNULL(p.deleted, 'N')<>'Y'")
	 public List<Doctor> getDoctors(Pageable pageable);
	 
    @Query("SELECT count(1) FROM Doctor p where IFNULL(p.deleted, 'N')<>'Y'") 
    Integer getTotalCount();
    
	 @Query("SELECT d FROM Doctor d, Relationship r where r.relationshipType='DOCTOR_PATIENT' "
		 		+ "and IFNULL(r.deleted, 'N')<>'Y' and r.subjectId=d.doctorId "
		 		+ "and r.objectId=:patientid and IFNULL(d.deleted, 'N')<>'Y'")
	 public List<Doctor> getDoctorsByPatientId(@Param("patientid") String patientId);
}
