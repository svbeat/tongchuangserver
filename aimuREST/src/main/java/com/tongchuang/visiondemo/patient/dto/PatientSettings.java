package com.tongchuang.visiondemo.patient.dto;

import java.util.Map;

import com.tongchuang.visiondemo.common.Point;
import com.tongchuang.visiondemo.device.Intensity;
public class PatientSettings {   
	public static enum PatternType {P24_2, P30_2, PDEMO};
    public static enum ExamFieldOption {LEFT, RIGHT, BOTH};
	
    private Integer      version;
	private Map<String, Integer> initStimulusDBLeft;
	private Map<String, Integer> stimulusPrioritiesLeft;

	private Map<String, Integer> initStimulusDBRight;
	private Map<String, Integer> stimulusPrioritiesRight;
	
	private Integer     stimulateDuration;
	private Integer     stimulateInterval;
	private Integer		stimulateCountMax;

	private Integer     stimulusSpacing;
	private Integer     stimulusRadius;
	private Integer     fixationRadius;
	private Point       leftFixation;
	private Point       rightFixation;

	private String      stimulusSelectorClass;
	private String      stimulusRunnerClass;

	private PatternType patternType;
	
	private ExamFieldOption	examFieldOption;



	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Map<String, Integer> getInitStimulusDBLeft() {
		return initStimulusDBLeft;
	}

	public void setInitStimulusDBLeft(Map<String, Integer> initStimulusDBLeft) {
		this.initStimulusDBLeft = initStimulusDBLeft;
	}

	public Map<String, Integer> getStimulusPrioritiesLeft() {
		return stimulusPrioritiesLeft;
	}

	public void setStimulusPrioritiesLeft(Map<String, Integer> stimulusPrioritiesLeft) {
		this.stimulusPrioritiesLeft = stimulusPrioritiesLeft;
	}

	public Map<String, Integer> getInitStimulusDBRight() {
		return initStimulusDBRight;
	}

	public void setInitStimulusDBRight(Map<String, Integer> initStimulusDBRight) {
		this.initStimulusDBRight = initStimulusDBRight;
	}

	public Map<String, Integer> getStimulusPrioritiesRight() {
		return stimulusPrioritiesRight;
	}

	public void setStimulusPrioritiesRight(Map<String, Integer> stimulusPrioritiesRight) {
		this.stimulusPrioritiesRight = stimulusPrioritiesRight;
	}

	public Integer getStimulateDuration() {
		return stimulateDuration;
	}

	public void setStimulateDuration(Integer stimulateDuration) {
		this.stimulateDuration = stimulateDuration;
	}

	public Integer getStimulateInterval() {
		return stimulateInterval;
	}

	public void setStimulateInterval(Integer stimulateInterval) {
		this.stimulateInterval = stimulateInterval;
	}

	
	public Integer getStimulateCountMax() {
		return stimulateCountMax;
	}

	public void setStimulateCountMax(Integer stimulateCountMax) {
		this.stimulateCountMax = stimulateCountMax;
	}

	public Integer getStimulusSpacing() {
		return stimulusSpacing;
	}

	public void setStimulusSpacing(Integer stimulusSpacing) {
		this.stimulusSpacing = stimulusSpacing;
	}

	public Integer getStimulusRadius() {
		return stimulusRadius;
	}

	public void setStimulusRadius(Integer stimulusRadius) {
		this.stimulusRadius = stimulusRadius;
	}

	public Integer getFixationRadius() {
		return fixationRadius;
	}

	public void setFixationRadius(Integer fixationRadius) {
		this.fixationRadius = fixationRadius;
	}

	public Point getLeftFixation() {
		return leftFixation;
	}

	public void setLeftFixation(Point leftFixation) {
		this.leftFixation = leftFixation;
	}

	public Point getRightFixation() {
		return rightFixation;
	}

	public void setRightFixation(Point rightFixation) {
		this.rightFixation = rightFixation;
	}

	public String getStimulusSelectorClass() {
		return stimulusSelectorClass;
	}

	public void setStimulusSelectorClass(String stimulusSelectorClass) {
		this.stimulusSelectorClass = stimulusSelectorClass;
	}


	

	public String getStimulusRunnerClass() {
		return stimulusRunnerClass;
	}

	public void setStimulusRunnerClass(String stimulusRunnerClass) {
		this.stimulusRunnerClass = stimulusRunnerClass;
	}

	public PatternType getPatternType() {
		return patternType;
	}

	public void setPatternType(PatternType patternType) {
		this.patternType = patternType;
	}

	public ExamFieldOption getExamFieldOption() {
		return examFieldOption;
	}

	public void setExamFieldOption(ExamFieldOption examFieldOption) {
		this.examFieldOption = examFieldOption;
	}

	

}
