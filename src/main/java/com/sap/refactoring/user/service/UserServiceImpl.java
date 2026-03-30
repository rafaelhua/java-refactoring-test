package com.sap.refactoring.user.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sap.refactoring.user.dao.UserDao;
import com.sap.refactoring.user.dao.UserDaoImpl;
import com.sap.refactoring.user.exception.DuplicateEmailException;
import com.sap.refactoring.user.exception.ErrorMessages;
import com.sap.refactoring.user.exception.UserNotFoundException;
import com.sap.refactoring.user.model.User;

@Service
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

		if (emailExists(email)) {
			throw new DuplicateEmailException(ErrorMessages.DUPLICATE_EMAIL);
		}

		User user = buildUser(name, email, roles);
		userDao.saveUser(user);
		return user;
	}

	@Override
	public User updateUser(String name, String email, List<String> roles) {
		validateUserInput(name, email, roles);
		findExistingUser(name);

		if (emailAssignedToAnotherUser(name, email)) {
			throw new DuplicateEmailException(ErrorMessages.DUPLICATE_EMAIL);
		}

		User userToUpdate = buildUser(name, email, roles);
		userDao.updateUser(userToUpdate);
		return userToUpdate;
	}

	@Override
	public void deleteUser(String name) {
		User existingUser = findExistingUser(name);
		userDao.deleteUser(existingUser);
	}

	@Override
	public List<User> getUsers() {
		return userDao.getUsers();
	}

	@Override
	public User findUser(String name) {
		return findExistingUser(name);
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

	private User findExistingUser(String name) {
		validateName(name);

		User existingUser = userDao.findUser(name);
		if (existingUser == null) {
			throw new UserNotFoundException(ErrorMessages.USER_NOT_FOUND);
		}

		return existingUser;
	}

	private void validateName(String name) {
		if (name == null || name.isBlank()) {
			throw new IllegalArgumentException(ErrorMessages.NAME_REQUIRED);
		}
	}

	private boolean emailExists(String email) {
		for (User user : getUsers()) {
			if (email.equals(user.getEmail())) {
				return true;
			}
		}
		return false;
	}

	private boolean emailAssignedToAnotherUser(String name, String email) {
		for (User user : getUsers()) {
			if (email.equals(user.getEmail()) && !name.equals(user.getName())) {
				return true;
			}
		}
		return false;
	}
}
