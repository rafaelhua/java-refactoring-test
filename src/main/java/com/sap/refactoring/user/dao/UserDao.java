package com.sap.refactoring.user.dao;

import java.util.List;

import com.sap.refactoring.user.model.User;

public interface UserDao
{
	void saveUser(User user);

	List<User> getUsers();

	void deleteUser(User userToDelete);

	void updateUser(User userToUpdate);

	User findUser(String name);
}
