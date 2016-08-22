package com.tongchuang.visiondemo.user.dto;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_role")
public class UserRole {
	@Id
	private int		userRoleId;
	private long	userid;
	
    @Enumerated(EnumType.STRING)
	private UserInfo.Role	role;
	private String	subjectId;
	
	public int getUserRoleId() {
		return userRoleId;
	}
	public void setUserRoleId(int userRoleId) {
		this.userRoleId = userRoleId;
	}
	
	

	
	public long getUserid() {
		return userid;
	}
	public void setUserid(long userid) {
		this.userid = userid;
	}
	public UserInfo.Role getRole() {
		return role;
	}
	public void setRole(UserInfo.Role role) {
		this.role = role;
	}
	
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	
	
}

