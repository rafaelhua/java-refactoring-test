package com.sap.refactoring.user.dao;

import java.util.ArrayList;
import java.util.List;

import jakarta.transaction.Transactional;

import com.sap.refactoring.user.model.User;

public class UserDaoImpl implements UserDao
{
	private final ArrayList<User> users = new ArrayList<>();

	@Transactional
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
		try {
			for (User user : users) {
				if (user.getName().equals(userToDelete.getName())) {
					users.remove(user);
					return;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateUser(User userToUpdate) {
		try {
			for (User user : users) {
				if (user.getName().equals(userToUpdate.getName())) {
					user.setEmail(userToUpdate.getEmail());
					user.setRoles(userToUpdate.getRoles());
					return;
				}
			}
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	@Override
	public User findUser(String name) {
		try {
			for (User user : users) {
				if (user.getName().equals(name)) {
					return user;
				}
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
		return null;
	}
}
