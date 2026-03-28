package com.sap.refactoring.user.dao;

import java.util.ArrayList;
import java.util.List;

import com.sap.refactoring.user.model.User;

public class UserDaoImpl implements UserDao {
	private final List<User> users = new ArrayList<>();

	@Override
	public void saveUser(User user) {
		users.add(copyUser(user));
	}

	@Override
	public List<User> getUsers() {
		List<User> userCopies = new ArrayList<>();
		for (User user : users) {
			userCopies.add(copyUser(user));
		}
		return userCopies;
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

		for (int i = 0; i < users.size(); i++) {
			User existingUser = users.get(i);
			if (userToUpdate.getName().equals(existingUser.getName())) {
				users.set(i, copyUser(userToUpdate));
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
				return copyUser(user);
			}
		}

		return null;
	}

	private User copyUser(User user) {
		if (user == null) {
			return null;
		}
		return new User(user.getName(), user.getEmail(), user.getRoles());
	}
}
