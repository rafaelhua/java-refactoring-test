package com.sap.refactoring.user.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.sap.refactoring.user.dao.UserDao;
import com.sap.refactoring.user.dao.UserDaoImpl;
import com.sap.refactoring.user.model.User;

@Service
public class UserServiceImpl implements UserService
{
	private final UserDao userDao;

	public UserServiceImpl()
	{
		this(new UserDaoImpl());
	}

	public UserServiceImpl(UserDao userDao)
	{
		this.userDao = userDao;
	}

	@Override
	public User createUser(String name, String email, List<String> roles)
	{
		validateUserInput(name, email, roles);

		if (emailExists(email)) {
			throw new IllegalStateException("A user with this email already exists");
		}

		User user = buildUser(name, email, roles);
		userDao.saveUser(user);
		return user;
	}

	@Override
	public User updateUser(String name, String email, List<String> roles)
	{
		validateUserInput(name, email, roles);

		User existingUser = userDao.findUser(name);
		if (existingUser == null) {
			throw new IllegalArgumentException("User not found");
		}

		User userToUpdate = buildUser(name, email, roles);
		if (emailAssignedToAnotherUser(name, email)) {
			throw new IllegalStateException("A user with this email already exists");
		}

		userDao.updateUser(userToUpdate);
		return userToUpdate;
	}

	@Override
	public void deleteUser(String name)
	{
		if (name == null || name.isBlank()) {
			throw new IllegalArgumentException("Name must not be blank");
		}

		User existingUser = userDao.findUser(name);
		if (existingUser == null) {
			throw new IllegalArgumentException("User not found");
		}

		userDao.deleteUser(existingUser);
	}

	@Override
	public List<User> getUsers()
	{
		List<User> users = userDao.getUsers();
		if (users == null) {
			return new ArrayList<>();
		}
		return users;
	}

	@Override
	public User findUser(String name)
	{
		if (name == null || name.isBlank()) {
			throw new IllegalArgumentException("Name must not be blank");
		}
		return userDao.findUser(name);
	}

	private User buildUser(String name, String email, List<String> roles)
	{
		User user = new User();
		user.setName(name);
		user.setEmail(email);
		user.setRoles(new ArrayList<>(roles));
		return user;
	}

	private void validateUserInput(String name, String email, List<String> roles)
	{
		if (name == null || name.isBlank()) {
			throw new IllegalArgumentException("Name must not be blank");
		}
		if (email == null || email.isBlank()) {
			throw new IllegalArgumentException("Email must not be blank");
		}
		if (roles == null || roles.isEmpty()) {
			throw new IllegalArgumentException("User must have at least one role");
		}
	}

	private boolean emailExists(String email)
	{
		List<User> users = getUsers();
		for (User user : users) {
			if (email.equals(user.getEmail())) {
				return true;
			}
		}
		return false;
	}

	private boolean emailAssignedToAnotherUser(String name, String email)
	{
		List<User> users = getUsers();
		for (User user : users) {
			if (email.equals(user.getEmail()) && !name.equals(user.getName())) {
				return true;
			}
		}
		return false;
	}
}
