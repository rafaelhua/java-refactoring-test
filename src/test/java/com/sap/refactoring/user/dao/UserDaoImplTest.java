package com.sap.refactoring.user.dao;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.sap.refactoring.user.model.User;

import static org.assertj.core.api.Assertions.assertThat;

class UserDaoImplTest {
	private static final String TEST_USER_NAME = "Alice";
	private static final String TEST_USER_EMAIL = "alice@example.com";
	private static final List<String> TEST_USER_ROLES = List.of("admin");

	@Test
	void saveUser_shouldStoreUser() {
		UserDaoImpl userDao = new UserDaoImpl();
		User user = new User(TEST_USER_NAME, TEST_USER_EMAIL, TEST_USER_ROLES);

		userDao.saveUser(user);

		assertThat(userDao.getUsers()).hasSize(1);
		assertThat(userDao.findUser(TEST_USER_EMAIL)).isNotNull();
		assertThat(userDao.findUser(TEST_USER_EMAIL).getEmail()).isEqualTo(TEST_USER_EMAIL);
	}

	@Test
	void updateUser_shouldChangeEmailAndRoles() {
		UserDaoImpl userDao = new UserDaoImpl();
		userDao.saveUser(new User(TEST_USER_NAME, TEST_USER_EMAIL, TEST_USER_ROLES));

		userDao.updateUser(TEST_USER_EMAIL, new User(TEST_USER_NAME, "alice@new.com", List.of("user")));

		User updatedUser = userDao.findUser("alice@new.com");
		assertThat(updatedUser).isNotNull();
		assertThat(updatedUser.getEmail()).isEqualTo("alice@new.com");
		assertThat(updatedUser.getRoles()).containsExactly("user");
	}

	@Test
	void deleteUser_shouldRemoveMatchingUser() {
		UserDaoImpl userDao = new UserDaoImpl();
		userDao.saveUser(new User(TEST_USER_NAME, TEST_USER_EMAIL, TEST_USER_ROLES));

		userDao.deleteUser(TEST_USER_EMAIL);

		assertThat(userDao.getUsers()).isEmpty();
		assertThat(userDao.findUser(TEST_USER_EMAIL)).isNull();
	}

	@Test
	void findUser_shouldReturnNullWhenEmailDoesNotExist() {
		UserDaoImpl userDao = new UserDaoImpl();

		User foundUser = userDao.findUser("missing@example.com");

		assertThat(foundUser).isNull();
	}
}
