package com.aimu.visiontest.response;

public class TestResultResponse {
	private long	testRunId;
	private int		score;
	private String	outcome;
	private	String	message;
	
	
	
	public TestResultResponse(long testRunId) {
		this.testRunId = testRunId;
	}
	
	public long getTestRunId() {
		return testRunId;
	}
	public void setTestRunId(int testRunId) {
		this.testRunId = testRunId;
	}
	
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
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
	
}
