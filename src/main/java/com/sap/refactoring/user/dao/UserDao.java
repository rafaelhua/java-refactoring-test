package com.sap.refactoring.user.dao;

import java.util.List;

import com.sap.refactoring.user.model.User;

public interface UserDao {
	User saveUser(User user);

	List<User> getUsers();

	void deleteUser(String email);

	void updateUser(String email, User userToUpdate);

	User findUser(String email);
}
