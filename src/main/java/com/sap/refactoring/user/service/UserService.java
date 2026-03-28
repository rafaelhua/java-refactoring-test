package com.sap.refactoring.user.service;

import java.util.List;

import com.sap.refactoring.user.model.User;

public interface UserService {
	User createUser(String name, String email, List<String> roles);

	User updateUser(String name, String email, List<String> roles);

	void deleteUser(String name);

	List<User> getUsers();

	User findUser(String name);
}
