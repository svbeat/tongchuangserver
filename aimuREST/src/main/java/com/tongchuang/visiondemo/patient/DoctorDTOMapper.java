package com.tongchuang.visiondemo.patient;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import com.tongchuang.visiondemo.doctor.dto.DoctorDTO;
import com.tongchuang.visiondemo.doctor.entity.Doctor;
import com.tongchuang.visiondemo.patient.PatientConstants.Gender;
import com.tongchuang.visiondemo.patient.dto.PatientDTO;

public class DoctorDTOMapper implements RowMapper {

	@Override
	public DoctorDTO mapRow(ResultSet rs, int rowNum) throws SQLException{ 
		Doctor  doctor = new Doctor();
		doctor.setDescription(rs.getString("description"));
		doctor.setDoctorId(rs.getInt("doctor_id"));
		doctor.setEmail(rs.getString("email"));
		String gender = rs.getString("gender");
		doctor.setGender(gender==null?null:Gender.valueOf(gender));
		doctor.setHospitalId(rs.getInt("hospital_id"));
		doctor.setName(rs.getString("name"));
		doctor.setPhone(rs.getString("phone"));

		DoctorDTO result = new DoctorDTO(doctor, null);
		result.setDpRelationshipId(rs.getInt("relationship_id"));
		
		return result;
		
	}

}
