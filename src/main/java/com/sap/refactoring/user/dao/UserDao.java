package com.sap.refactoring.user.dao;

import java.util.List;

import com.sap.refactoring.user.model.User;

public interface UserDao {
	void saveUser(User user);

	List<User> getUsers();

	void deleteUser(String email);

	void updateUser(String currentEmail, User userToUpdate);

	User findUser(String email);
}
