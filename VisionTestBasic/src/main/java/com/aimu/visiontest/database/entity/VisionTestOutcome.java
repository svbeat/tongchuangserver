package com.aimu.visiontest.database.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.aimu.visiontest.dto.TestRunDto;

@Entity
@Table(name = "vision_test_outcome")
/*
CREATE TABLE vision_test_outcome (test_outcome_id  int NOT NULL AUTO_INCREMENT,
	creation_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, 
	last_update_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	test_id int NOT NULL,
	score_range_min int,
	score_range_max int,
	outcome VARCHAR(250), 
	message TEXT,
	PRIMARY KEY (test_outcome_id));
*/

public class VisionTestOutcome {

    @Id
    private long 	testOutcomeId;
    private Date	creationDate;
    private Date	lastUpdateDate;    
    private int		testId;
    private int		scoreRangeMin;
    private int		scoreRangeMax;
    private String	outcome;
    private String	message;
    
    
	public long getTestOutcomeId() {
		return testOutcomeId;
	}
	public void setTestOutcomeId(long testOutcomeId) {
		this.testOutcomeId = testOutcomeId;
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
	
	public int getTestId() {
		return testId;
	}
	public void setTestId(int testId) {
		this.testId = testId;
	}
	
	public int getScoreRangeMin() {
		return scoreRangeMin;
	}
	public void setScoreRangeMin(int scoreRangeMin) {
		this.scoreRangeMin = scoreRangeMin;
	}

	public int getScoreRangeMax() {
		return scoreRangeMax;
	}
	public void setScoreRangeMax(int scoreRangeMax) {
		this.scoreRangeMax = scoreRangeMax;
	}
	
	public String getOutcome() {
		return outcome;
	}
	public void setOutcome(String outcome) {
		this.outcome = outcome;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	@Override
	public String toString() {
		return "VisionTestOutcome [testOutcomeId=" + testOutcomeId + ", creationDate=" + creationDate
				+ ", lastUpdateDate=" + lastUpdateDate + ", testId=" + testId + ", scoreRangeMin=" + scoreRangeMin
				+ ", scoreRangeMax=" + scoreRangeMax + ", outcome=" + outcome + ", message=" + message + "]";
	}
	
	
    
}