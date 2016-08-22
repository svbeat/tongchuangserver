package com.tongchuang.visiondemo.relationship.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.tongchuang.visiondemo.ApplicationConstants.EntityDeleted;
import com.tongchuang.visiondemo.ApplicationConstants.RelationshipType;
import com.tongchuang.visiondemo.patient.PatientConstants.Gender;

@Entity
@Table(name = "relationship")
/*
CREATE TABLE relationship (relationship_id int(11) not null auto_increment,
	creation_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, 
	last_update_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	created_by int(11),
	last_updated_by int(11),
	relationship_type varchar(30),	
	subject_id	varchar(60),
	object_id varchar(60),
	deleted char(1),
	primary key (relationship_id)) DEFAULT CHARSET=utf8;
 */
public class Relationship {
	
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int 		relationshipId;
    
    private Date		creationDate;    
    private Date		lastUpdateDate;
    private Integer		createdBy;
    private Integer		lastUpdatedBy;
    
    @Enumerated(EnumType.STRING)
    private RelationshipType	relationshipType;
    private String		subjectId;
    private String		objectId;
    
    @Enumerated(EnumType.STRING)
    private EntityDeleted	deleted;

	public int getRelationshipId() {
		return relationshipId;
	}

	public void setRelationshipId(int relationshipId) {
		this.relationshipId = relationshipId;
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

	public RelationshipType getRelationshipType() {
		return relationshipType;
	}

	public void setRelationshipType(RelationshipType relationshipType) {
		this.relationshipType = relationshipType;
	}

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public EntityDeleted getDeleted() {
		return deleted;
	}

	public void setDeleted(EntityDeleted deleted) {
		this.deleted = deleted;
	}
    
    
}