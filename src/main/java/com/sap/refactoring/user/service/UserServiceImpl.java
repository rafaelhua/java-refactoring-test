package com.sap.refactoring.user.service;

import java.util.List;

import com.sap.refactoring.user.dao.UserDao;
import com.sap.refactoring.user.dao.UserDaoImpl;
import com.sap.refactoring.user.exception.DuplicateEmailException;
import com.sap.refactoring.user.exception.ErrorMessages;
import com.sap.refactoring.user.exception.UserNotFoundException;
import com.sap.refactoring.user.model.User;

public class UserServiceImpl implements UserService {
	private final UserDao userDao;

	public UserServiceImpl() {
		this(new UserDaoImpl());
	}

	public UserServiceImpl(UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public User createUser(String name, String email, List<String> roles) {
		validateUserInput(name, email, roles);

		User user = buildUser(name, email, roles);
		userDao.saveUser(user);
		return user;
	}

	@Override
	public User updateUser(String currentEmail, String name, String email, List<String> roles) {
		validateUserInput(name, email, roles);
		findExistingUser(currentEmail);

		User userToUpdate = buildUser(name, email, roles);
		userDao.updateUser(currentEmail, userToUpdate);
		return userToUpdate;
	}

	@Override
	public void deleteUser(String email) {
		findExistingUser(email);
		userDao.deleteUser(email);
	}

	@Override
	public List<User> getUsers() {
		return userDao.getUsers();
	}

	@Override
	public User findUser(String email) {
		return findExistingUser(email);
	}

	private User buildUser(String name, String email, List<String> roles) {
		return new User(name, email, roles);
	}

	private void validateUserInput(String name, String email, List<String> roles) {
		validateName(name);
		if (email == null || email.isBlank()) {
			throw new IllegalArgumentException(ErrorMessages.EMAIL_REQUIRED);
		}
		if (roles == null || roles.isEmpty()) {
			throw new IllegalArgumentException(ErrorMessages.ROLE_REQUIRED);
		}
	}

	private User findExistingUser(String email) {
		validateLookupEmail(email);

		User existingUser = userDao.findUser(email);
		if (existingUser == null) {
			throw new UserNotFoundException(ErrorMessages.USER_NOT_FOUND);
		}

		return existingUser;
	}

	private void validateLookupEmail(String email) {
		if (email == null || email.isBlank()) {
			throw new IllegalArgumentException(ErrorMessages.EMAIL_REQUIRED);
		}
	}

	private void validateName(String name) {
		if (name == null || name.isBlank()) {
			throw new IllegalArgumentException(ErrorMessages.NAME_REQUIRED);
		}
	}
}
