package com.sap.refactoring.user.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.sap.refactoring.user.dao.UserDao;
import com.sap.refactoring.user.exception.ErrorMessages;
import com.sap.refactoring.user.exception.UserNotFoundException;
import com.sap.refactoring.user.model.User;

@Service
public class UserServiceImpl implements UserService {
	private final UserDao userDao;

	public UserServiceImpl(UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public User createUser(String name, String email, List<String> roles) {
		validateUserInput(name, email, roles);

		User user = buildUser(name, email, roles);
		return userDao.saveUser(user);
	}

	@Override
	public User updateUser(String email, String name, List<String> roles) {
		User existingUser = findExistingUser(email);
		validateUpdateInput(name, roles);

		User userToUpdate = buildUser(name, existingUser.getEmail(), roles);
		userDao.updateUser(email, userToUpdate);
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
		if (!StringUtils.hasText(email)) {
			throw new IllegalArgumentException(ErrorMessages.EMAIL_REQUIRED);
		}
		if (roles == null || roles.isEmpty()) {
			throw new IllegalArgumentException(ErrorMessages.ROLE_REQUIRED);
		}
	}

	private void validateUpdateInput(String name, List<String> roles) {
		validateName(name);
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
		if (!StringUtils.hasText(email)) {
			throw new IllegalArgumentException(ErrorMessages.EMAIL_REQUIRED);
		}
	}

	private void validateName(String name) {
		if (!StringUtils.hasText(name)) {
			throw new IllegalArgumentException(ErrorMessages.NAME_REQUIRED);
		}
	}
}
