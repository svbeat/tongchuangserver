package com.aimu;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class AimuUiApplication {
	 
	 @Configuration
	  @Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
	  protected static class SecurityConfiguration extends WebSecurityConfigurerAdapter {
		 
		 @Autowired
		 DataSource dataSource;

		 
		 @Autowired
		 public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
			 auth.jdbcAuthentication().dataSource(dataSource)
			 .usersByUsernameQuery("select username,password, enabled from user where username=?")
			 .authoritiesByUsernameQuery("select u.username, ur.role from user u, user_role ur where u.username=? and u.userid=ur.userid");
		 }
		 
	    @Override
	    protected void configure(HttpSecurity http) throws Exception {
	      http
	        .httpBasic()
	      .and()
	        .authorizeRequests()
	          .antMatchers("/index.html", "/login.html", "/products.html", "/aboutus.html", "/").permitAll()
	          .anyRequest().authenticated();
	    }
	  }
	 
  @RequestMapping("/resource")
  public Map<String,Object> home() {
    Map<String,Object> model = new HashMap<String,Object>();
    model.put("id", UUID.randomUUID().toString());
    model.put("content", "Hello World");
    return model;
  }

  @RequestMapping("/user")
  public Principal user(Principal user) {
	  System.out.println("aimu msg: user");
    return user;
  }
  
  public static void main(String[] args) {
    SpringApplication.run(AimuUiApplication.class, args);
  }

}