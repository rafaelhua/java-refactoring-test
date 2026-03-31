package com.sap.refactoring.user.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.sap.refactoring.user.exception.DuplicateEmailException;
import com.sap.refactoring.user.exception.ErrorMessages;
import com.sap.refactoring.user.model.User;

@Repository
public class UserDaoImpl implements UserDao {
	private final Map<String, User> users = new ConcurrentHashMap<>();

	@Override
	public synchronized User saveUser(User user) {
		if (user == null || !StringUtils.hasText(user.getEmail())) {
			return null;
		}

		if (users.containsKey(user.getEmail())) {
			throw new DuplicateEmailException(ErrorMessages.DUPLICATE_EMAIL);
		}

		users.put(user.getEmail(), user);
		return user;
	}

	@Override
	public List<User> getUsers() {
		return new ArrayList<>(users.values());
	}

	@Override
	public void deleteUser(String email) {
		if (!StringUtils.hasText(email)) {
			return;
		}

		users.remove(email);
	}

	@Override
	public synchronized void updateUser(String email, User userToUpdate) {
		if (!StringUtils.hasText(email)
				|| userToUpdate == null
				|| !StringUtils.hasText(userToUpdate.getName())) {
			return;
		}

		User existingUser = users.get(email);
		if (existingUser == null) {
			return;
		}

		userToUpdate.setEmail(existingUser.getEmail());
		users.put(email, userToUpdate);
	}

	@Override
	public User findUser(String email) {
		if (!StringUtils.hasText(email)) {
			return null;
		}

		return users.get(email);
	}
}
