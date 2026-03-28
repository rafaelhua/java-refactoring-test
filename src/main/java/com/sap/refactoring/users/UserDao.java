package com.sap.refactoring.users;

public class UserDao extends com.sap.refactoring.user.dao.UserDaoImpl
{
	private static final UserDao INSTANCE = new UserDao();

	public static UserDao getUserDao()
	{
		return INSTANCE;
	}
}
