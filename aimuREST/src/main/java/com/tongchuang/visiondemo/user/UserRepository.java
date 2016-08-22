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

public interface UserRepository extends JpaRepository<User, Integer>{
	
//	 @Query("SELECT u.userId, ur.role, ur.subjectId FROM user u, userRole ur WHERE u. = :id") 
//	 public List<Object[]>  getUserInfo(Integer userId);

	 @Query("SELECT u FROM User u WHERE u.username=:username and u.password=:password") 
	 public User  getUser(@Param("username")String userName, @Param("password")String password);
}
