package com.tongchuang.visiondemo.patient;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.tongchuang.visiondemo.device.entity.Device;
import com.tongchuang.visiondemo.patient.dto.PatientSettings;
import com.tongchuang.visiondemo.patient.entity.PatientExamSettings;
import com.tongchuang.visiondemo.patient.entity.PatientExamSettingsCompositePK;
import com.tongchuang.visiondemo.patient.entity.PatientExamSettingsLog;
import com.tongchuang.visiondemo.user.dto.UserInfo;

public interface PatientExamSettingsLogRepository extends CrudRepository<PatientExamSettingsLog, Integer> {


}
