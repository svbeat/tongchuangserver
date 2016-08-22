package com.tongchuang.visiondemo.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import com.tongchuang.visiondemo.relationship.entity.Relationship;
import com.tongchuang.visiondemo.user.dto.User;
import com.tongchuang.visiondemo.user.dto.UserInfo;
import com.tongchuang.visiondemo.user.dto.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Integer>{
	
	 @Query("SELECT u FROM UserRole u WHERE u.userid = :id") 
	 public UserRole  getRoleByUserId(@Param("id")Integer userId);
	 
	 @Query("SELECT ur FROM User u, UserRole ur WHERE u.username = :username and u.userid=ur.userid") 
	 public UserRole  getRoleByUserName(@Param("username")String userName);

}
