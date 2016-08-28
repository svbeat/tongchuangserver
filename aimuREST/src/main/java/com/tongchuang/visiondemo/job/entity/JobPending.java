package com.tongchuang.visiondemo.job.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.tongchuang.visiondemo.ApplicationConstants.EntityStatus;
import com.tongchuang.visiondemo.job.JobConstants.JobCode;
import com.tongchuang.visiondemo.job.JobConstants.JobStatus;
import com.tongchuang.visiondemo.patient.PatientConstants.Gender;

@Entity
@Table(name = "job_pending")
/*
 * CREATE TABLE job_pending (job_pending_id int(11) not null auto_increment,
	creation_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, 
	last_update_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	created_by int(11),
	last_updated_by int(11),
	job_code varchar(30),
	status varchar(30),	
	details text,
	batch_id varchar(60),
	primary key (job_pending_id)) DEFAULT CHARSET=utf8;
 */
public class JobPending {
	
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer 		jobPendingId;    
    private Date			creationDate;    
    private Date			lastUpdateDate;
    private Integer			createdBy;
    private Integer			lastUpdatedBy;
    @Enumerated(EnumType.STRING)
    private JobCode			jobCode;
    private String			details;
    private String			batchId;
    @Enumerated(EnumType.STRING)
    private JobStatus		status = JobStatus.PENDING;
    
    
	public Integer getJobPendingId() {
		return jobPendingId;
	}
	public void setJobPendingId(Integer jobPendingId) {
		this.jobPendingId = jobPendingId;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}
	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	public Integer getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}
	public Integer getLastUpdatedBy() {
		return lastUpdatedBy;
	}
	public void setLastUpdatedBy(Integer lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
	public JobCode getJobCode() {
		return jobCode;
	}
	public void setJobCode(JobCode jobCode) {
		this.jobCode = jobCode;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public String getBatchId() {
		return batchId;
	}
	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}
	public JobStatus getStatus() {
		return status;
	}
	public void setStatus(JobStatus status) {
		this.status = status;
	}
    
}
