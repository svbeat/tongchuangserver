package com.tongchuang.visiondemo;

import org.springframework.http.HttpStatus;

public class ApiError {
 
    private String status;
    private String	code;
    private String message;
    private String error;
    
	public ApiError(String status, String code, String error, String message) {
		this.status = status;
		this.code = code;
		this.message = message;
		this.error = error;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
 
	

}