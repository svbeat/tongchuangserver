package com.tongchuang.visiondemo.security;

public class AuthResponse {
	private long	expiresTime;
	private String	token;
	
	public long getExpiresTime() {
		return expiresTime;
	}
	public void setExpiresTime(long expiresTime) {
		this.expiresTime = expiresTime;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
	
}
