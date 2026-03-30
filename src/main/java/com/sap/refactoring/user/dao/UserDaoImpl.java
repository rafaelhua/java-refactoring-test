package com.sap.refactoring.user.dao;

import java.util.ArrayList;
import java.util.List;

import com.sap.refactoring.user.model.User;

public class UserDaoImpl implements UserDao {
	private final List<User> users = new ArrayList<>();

	@Override
	public void saveUser(User user) {
		users.add(user);
	}

	@Override
	public List<User> getUsers() {
		return users;
	}

	@Override
	public void deleteUser(User userToDelete) {
		if (userToDelete == null || userToDelete.getName() == null) {
			return;
		}

		for (User user : users) {
			if (userToDelete.getName().equals(user.getName())) {
				users.remove(user);
				return;
			}
		}
	}

	@Override
	public void updateUser(User userToUpdate) {
		if (userToUpdate == null || userToUpdate.getName() == null) {
			return;
		}

		for (User existingUser : users) {
			if (userToUpdate.getName().equals(existingUser.getName())) {
				existingUser.setEmail(userToUpdate.getEmail());
				existingUser.setRoles(userToUpdate.getRoles());
				return;
			}
		}
	}

	@Override
	public User findUser(String name) {
		if (name == null) {
			return null;
		}

		for (User user : users) {
			if (name.equals(user.getName())) {
				return user;
			}
		}

		return null;
	}
}
