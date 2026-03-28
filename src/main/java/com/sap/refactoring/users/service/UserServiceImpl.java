package com.sap.refactoring.users.service;

import com.sap.refactoring.users.UserDao;

public class UserServiceImpl extends com.sap.refactoring.user.service.UserServiceImpl
		implements UserService {
	public UserServiceImpl() {
		super(UserDao.getUserDao());
	}

	public UserServiceImpl(UserDao userDao) {
		super(userDao);
	}
}
