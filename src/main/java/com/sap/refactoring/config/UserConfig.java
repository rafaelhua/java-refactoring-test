package com.sap.refactoring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sap.refactoring.user.dao.UserDao;
import com.sap.refactoring.user.dao.UserDaoImpl;
import com.sap.refactoring.user.service.UserService;
import com.sap.refactoring.user.service.UserServiceImpl;

@Configuration
public class UserConfig {
	@Bean
	public UserDao userDao() {
		return new UserDaoImpl();
	}

	@Bean
	public UserService userService(UserDao userDao) {
		return new UserServiceImpl(userDao);
	}
}
