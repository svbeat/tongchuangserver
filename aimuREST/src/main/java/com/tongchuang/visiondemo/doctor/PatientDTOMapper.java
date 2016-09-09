package com.tongchuang.visiondemo.doctor;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.tongchuang.visiondemo.ApplicationConstants.EntityStatus;
import com.tongchuang.visiondemo.patient.PatientConstants.Gender;
import com.tongchuang.visiondemo.patient.dto.PatientDTO;
import com.tongchuang.visiondemo.patient.entity.Patient;

public class PatientDTOMapper implements RowMapper {

	@Override
	public PatientDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
		Patient patient = new Patient();
		patient.setAddress(rs.getString("address"));
		patient.setBirthdate(rs.getDate("birthdate"));
		patient.setEmail(rs.getString("email"));
		patient.setGender(Gender.valueOf(rs.getString("gender")));
		patient.setName(rs.getString("name"));
		patient.setPatientId(rs.getString("patient_id"));
		patient.setPhone(rs.getString("phone"));
		patient.setStatus(EntityStatus.valueOf(rs.getString("status")));

		
		PatientDTO result = new PatientDTO(patient, null);
		result.setDpRelationshipId(rs.getInt("relationship_id"));
		
		return result;
	}

}
