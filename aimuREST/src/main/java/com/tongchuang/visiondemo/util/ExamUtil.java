package com.tongchuang.visiondemo.util;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.tongchuang.visiondemo.common.Point;
import com.tongchuang.visiondemo.device.Intensity;
import com.tongchuang.visiondemo.device.dto.DeviceSettings;
import com.tongchuang.visiondemo.patient.dto.PatientSettings;
import com.tongchuang.visiondemo.patient.dto.PatientSettings.PatternType;

public class ExamUtil {
	private static String calibration = 
			"10,1000,237,255,255,55;11,1000,254,221,255,55;12,1000,252,202,255,55;13,1000,251,186,255,54;14,1000,251,170,255,54;15,1000,251,154,255,54;16,1000,250,143,255,54;17,1000,246,134,255,54;18,1000,247,123,255,54;19,1000,247,114,255,54;20,1000,246,105,255,54;21,1000,245,98,255,53;22,1000,245,91,255,53;23,1000,244,85,255,53;24,1000,244,81,255,53;25,1000,244,76,255,53;26,1000,247,72,255,53;27,1000,247,69,255,53;28,1000,209,70,255,53;29,1000,197,67,255,53;30,1000,197,65,255,53;31,1000,191,63,255,53;32,1000,191,61,255,53;33,1000,191,60,255,53;34,1000,191,59,255,53;35,1000,255,56,255,53;36,1000,191,58,255,53;37,1000,81,62,255,53;38,1000,255,55,255,53;39,1000,191,56,255,53;40,1000,255,54,255,53";
	
	private static String[] positionCodesLeft = {
			"q1r1c1","q1r1c2","q1r1c3","q1r1c4","q1r1c5",
            "q1r2c1","q1r2c2","q1r2c3","q1r2c4",
            "q1r3c1","q1r3c2","q1r3c3",
            "q1r4c1","q1r4c2",
            "q2r1c1","q2r1c2","q2r1c3","q2r1c4",
            "q2r2c1","q2r2c2","q2r2c3","q2r2c4",
            "q2r3c1","q2r3c2","q2r3c3",
            "q2r4c1","q2r4c2",
            "q3r1c1","q3r1c2","q3r1c3","q3r1c4",
            "q3r2c1","q3r2c2","q3r2c3","q3r2c4",
            "q3r3c1","q3r3c2","q3r3c3",
            "q3r4c1","q3r4c2",
            "q4r1c1","q4r1c2","q4r1c3","q4r1c4","q4r1c5",
            "q4r2c1","q4r2c2","q4r2c3","q4r2c4",
            "q4r3c1","q4r3c2","q4r3c3",
            "q4r4c1","q4r4c2"};

	private static int[] priorityLeft = {
			1, 1, 1, 2, 2,
            1, 1, 2, 2,
            1, 2, 2,
            2, 2,
            1, 1, 2, 2,
            1, 1, 2, 2,
            1, 2, 2, 
            2, 2,
            1, 1, 2, 2,
            1, 1, 2, 2,
            1, 2, 2,
            2, 2,     
			1, 1, 1, 2, 2,
            1, 1, 2, 2,
            1, 2, 2,
            2, 2};

	private static int[] initLeft = {
			25, 25, 25, 25, 25,
            25, 25, 25, 25,
            25, 25, 25,
            25, 25,
			25, 25, 25, 25, 
            25, 25, 25, 25,
            25, 25, 25,
            25, 25,
			25, 25, 25, 25, 
            25, 25, 25, 25,
            25, 25, 25,
            25, 25, 
			25, 25, 25, 25, 25,
            25, 25, 25, 25,
            25, 25, 25,
            25, 25};
	
	private static String[] positionCodesRight = {"q1r1c1","q1r1c2","q1r1c3","q1r1c4",
            "q1r2c1","q1r2c2","q1r2c3","q1r2c4",
            "q1r3c1","q1r3c2","q1r3c3",
            "q1r4c1","q1r4c2",
            "q2r1c1","q2r1c2","q2r1c3","q2r1c4","q2r1c5",
            "q2r2c1","q2r2c2","q2r2c3","q2r2c4",
            "q2r3c1","q2r3c2","q2r3c3",
            "q2r4c1","q2r4c2",
            "q3r1c1","q3r1c2","q3r1c3","q3r1c4","q3r1c5",
            "q3r2c1","q3r2c2","q3r2c3","q3r2c4",
            "q3r3c1","q3r3c2","q3r3c3",
            "q3r4c1","q3r4c2",
            "q4r1c1","q4r1c2","q4r1c3","q4r1c4",
            "q4r2c1","q4r2c2","q4r2c3","q4r2c4",
            "q4r3c1","q4r3c2","q4r3c3",
            "q4r4c1","q4r4c2"};

	private static int[] priorityRight = {
			1, 1, 1, 2, 
            1, 1, 2, 2,
            1, 2, 2,
            2, 2,
            1, 1, 2, 2, 2,
            1, 1, 2, 2,
            1, 2, 2,
            2, 2,
            1, 1, 2, 2, 2,
            1, 1, 2, 2,
            1, 2, 2,
            2, 2,     
			1, 1, 1, 2, 
            1, 1, 2, 2,
            1, 2, 2,
            2, 2};

	private static int[] initRight = {
			25, 25, 25, 25, 
            25, 25, 25, 25,
            25, 25, 25,
            25, 25,
			25, 25, 25, 25, 25,
            25, 25, 25, 25,
            25, 25, 25,
            25, 25,
			25, 25, 25, 25, 25,
            25, 25, 25, 25,
            25, 25, 25,
            25, 25, 
			25, 25, 25, 25, 
            25, 25, 25, 25,
            25, 25, 25,
            25, 25};
	
//	public static void main(String[] args) {
//		patientSettingsTest();
//	}
	
	public static void deviceSettingsTest() {

        String[] rawIntensities = calibration.split(";");
        Map<Integer, Intensity> intensities = new HashMap<Integer, Intensity>();
        for (int i = 0; i < rawIntensities.length; i++) {
            String[] data = rawIntensities[i].split(",");
            int db = Integer.parseInt(data[0]);
            Intensity intensity = new Intensity(db, Integer.parseInt(data[1])/1000f,
                    Integer.parseInt(data[2]), Integer.parseInt(data[3]), Integer.parseInt(data[4]), Integer.parseInt(data[5]));
            
            intensities.put(db, intensity);
        }
        
		DeviceSettings deviceSetting = new DeviceSettings();
		deviceSetting.setIntensities(intensities);
		deviceSetting.setFixationRadius(6);
		deviceSetting.setLeftFixation(new Point(320, 320));
		deviceSetting.setRightFixation(new Point(640, 320));
		deviceSetting.setStimulateDuration(500);
		deviceSetting.setStimulateInterval(1000);
		deviceSetting.setStimulusRadius(6);
		deviceSetting.setStimulusSpacing(30);
		deviceSetting.setTextDisplaySize(20);
		
		Gson gson = new Gson();
		System.out.println(gson.toJson(deviceSetting));
	}
	
	public static void patientSettingsTest() {
		PatientSettings patientSettings = new PatientSettings();
		
		Map<String, Integer> initStimulusDBLeft = new HashMap<String, Integer>();
		Map<String, Integer> stimulusPrioritiesLeft = new HashMap<String, Integer>();
		Map<String, Integer> initStimulusDBRight = new HashMap<String, Integer>();
		Map<String, Integer> stimulusPrioritiesRight = new HashMap<String, Integer>();
		
		for (int i = 0; i < positionCodesLeft.length; i++) {
			initStimulusDBLeft.put(positionCodesLeft[i], initLeft[i]);
			stimulusPrioritiesLeft.put(positionCodesLeft[i], priorityLeft[i]);
		}
		
		for (int i = 0; i < positionCodesRight.length; i++) {
			initStimulusDBRight.put(positionCodesRight[i], initRight[i]);
			stimulusPrioritiesRight.put(positionCodesRight[i], priorityRight[i]);
		}
		
		//patientSettings.setFixationRadius(fixationRadius);
		patientSettings.setInitStimulusDBLeft(initStimulusDBLeft);
		patientSettings.setStimulusPrioritiesLeft(stimulusPrioritiesLeft);
		patientSettings.setInitStimulusDBRight(initStimulusDBRight);
		patientSettings.setStimulusPrioritiesRight(stimulusPrioritiesRight);
		//patientSettings.setLeftFixation(leftFixation);
		patientSettings.setPatternType(PatternType.P24_2);
		//patientSettings.setRightFixation(rightFixation);
		//patientSettings.setStimulateDuration(stimulateDuration);
		patientSettings.setStimulusRunnerClass("DefaultStimulusRunnerImpl");
		
		patientSettings.setStimulusPrioritiesLeft(stimulusPrioritiesLeft);
		patientSettings.setStimulusSelectorClass("DefaultStimulusSelectorImpl");
		
		Gson gson = new Gson();
		String jsonString = gson.toJson(patientSettings);
		
		PatientSettings p2 = gson.fromJson(jsonString, PatientSettings.class);
		System.out.println(jsonString);
		
	}
}
