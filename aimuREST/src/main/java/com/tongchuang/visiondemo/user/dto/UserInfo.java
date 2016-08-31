package com.tongchuang.visiondemo.user.dto;

public class UserInfo {
	public static enum Role {ADMIN, DOCTOR, PATIENT};
	
	private int		userId;
	private Role	role;
	private String	subjectId;
	
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	
	
	
}
