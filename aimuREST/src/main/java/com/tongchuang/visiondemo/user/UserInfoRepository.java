package com.tongchuang.visiondemo.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.tongchuang.visiondemo.user.dto.UserInfo;

public interface UserInfoRepository {
	
	 @Query("SELECT u.userId, ur.role, ur.subjectId FROM user u, userRole ur WHERE u. = :id") 
	 public UserInfo getUserInfo(Integer userId);

}
