package com.sap.refactoring.user.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.sap.refactoring.user.exception.DuplicateEmailException;
import com.sap.refactoring.user.exception.ErrorMessages;
import com.sap.refactoring.user.model.User;

@Repository
public class UserDaoImpl implements UserDao {
	private final Map<String, User> users = new HashMap<>();

	@Override
	public synchronized void saveUser(User user) {
		if (user == null || user.getEmail() == null) {
			return;
		}

		if (users.containsKey(user.getEmail())) {
			throw new DuplicateEmailException(ErrorMessages.DUPLICATE_EMAIL);
		}

		users.put(user.getEmail(), user);
	}

	@Override
	public synchronized List<User> getUsers() {
		return new ArrayList<>(users.values());
	}

	@Override
	public synchronized void deleteUser(String email) {
		if (email == null) {
			return;
		}

		users.remove(email);
	}

	@Override
	public synchronized void updateUser(String currentEmail, User userToUpdate) {
		if (currentEmail == null || userToUpdate == null || userToUpdate.getEmail() == null) {
			return;
		}

		User existingUser = users.get(currentEmail);
		if (existingUser == null) {
			return;
		}

		String updatedEmail = userToUpdate.getEmail();
		if (!currentEmail.equals(updatedEmail) && users.containsKey(updatedEmail)) {
			throw new DuplicateEmailException(ErrorMessages.DUPLICATE_EMAIL);
		}

		users.remove(currentEmail);
		users.put(updatedEmail, userToUpdate);
	}

	@Override
	public synchronized User findUser(String email) {
		if (email == null) {
			return null;
		}

		return users.get(email);
	}

	@Override
	public synchronized void clearUsers() {
		users.clear();
	}
}
