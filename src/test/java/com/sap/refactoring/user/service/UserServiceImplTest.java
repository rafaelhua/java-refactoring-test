package com.sap.refactoring.user.service;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.sap.refactoring.user.dao.UserDaoImpl;
import com.sap.refactoring.user.exception.DuplicateEmailException;
import com.sap.refactoring.user.exception.UserNotFoundException;
import com.sap.refactoring.user.model.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserServiceImplTest {
	private static final String TEST_USER_NAME = "Alice";
	private static final String TEST_USER_EMAIL = "alice@example.com";
	private static final List<String> TEST_USER_ROLES = List.of("admin");

	@Test
	void createUser_shouldSaveAndReturnUser() {
		UserServiceImpl userService = new UserServiceImpl(new UserDaoImpl());

		User createdUser = userService.createUser(TEST_USER_NAME, TEST_USER_EMAIL, TEST_USER_ROLES);

		assertThat(createdUser.getName()).isEqualTo(TEST_USER_NAME);
		assertThat(createdUser.getEmail()).isEqualTo(TEST_USER_EMAIL);
		assertThat(createdUser.getRoles()).containsExactly("admin");
	}

	@Test
	void createUser_shouldRejectDuplicateEmail() {
		UserServiceImpl userService = new UserServiceImpl(new UserDaoImpl());
		userService.createUser(TEST_USER_NAME, TEST_USER_EMAIL, TEST_USER_ROLES);

		DuplicateEmailException exception =
				assertThrows(
						DuplicateEmailException.class,
						() -> userService.createUser("Bob", TEST_USER_EMAIL, List.of("user")));

		assertEquals("A user with this email already exists", exception.getMessage());
	}

	@Test
	void findUser_shouldThrowWhenUserDoesNotExist() {
		UserServiceImpl userService = new UserServiceImpl(new UserDaoImpl());

		UserNotFoundException exception =
				assertThrows(UserNotFoundException.class, () -> userService.findUser("missing@example.com"));

		assertEquals("User not found", exception.getMessage());
	}

	@Test
	void createUser_shouldRejectBlankName() {
		UserServiceImpl userService = new UserServiceImpl(new UserDaoImpl());

		IllegalArgumentException exception =
				assertThrows(
						IllegalArgumentException.class,
						() -> userService.createUser("", TEST_USER_EMAIL, TEST_USER_ROLES));

		assertEquals("Name must not be blank", exception.getMessage());
	}

	@Test
	void updateUser_shouldThrowWhenUserDoesNotExist() {
		UserServiceImpl userService = new UserServiceImpl(new UserDaoImpl());

		UserNotFoundException exception =
				assertThrows(
						UserNotFoundException.class,
						() -> userService.updateUser("missing@example.com", "Missing", List.of("admin")));

		assertEquals("User not found", exception.getMessage());
	}

	@Test
	void deleteUser_shouldThrowWhenUserDoesNotExist() {
		UserServiceImpl userService = new UserServiceImpl(new UserDaoImpl());

		UserNotFoundException exception =
				assertThrows(UserNotFoundException.class, () -> userService.deleteUser("missing@example.com"));

		assertEquals("User not found", exception.getMessage());
	}

	@Test
	void updateUser_shouldKeepExistingEmail() {
		UserServiceImpl userService = new UserServiceImpl(new UserDaoImpl());
		userService.createUser(TEST_USER_NAME, TEST_USER_EMAIL, TEST_USER_ROLES);

		User updatedUser = userService.updateUser(TEST_USER_EMAIL, "Updated Name", List.of("user"));

		assertThat(updatedUser.getName()).isEqualTo("Updated Name");
		assertThat(updatedUser.getEmail()).isEqualTo(TEST_USER_EMAIL);
		assertThat(updatedUser.getRoles()).containsExactly("user");
	}
}
