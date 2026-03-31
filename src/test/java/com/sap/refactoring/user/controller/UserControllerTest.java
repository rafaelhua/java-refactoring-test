package com.sap.refactoring.user.controller;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import com.sap.refactoring.user.dao.UserDaoImpl;
import com.sap.refactoring.user.dto.UpdateUserRequest;
import com.sap.refactoring.user.dto.UserRequest;
import com.sap.refactoring.user.dto.UserResponse;
import com.sap.refactoring.user.service.UserServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;

class UserControllerTest {
	private static final String TEST_USER_NAME = "Alice";
	private static final String TEST_USER_EMAIL = "alice@example.com";
	private static final List<String> TEST_USER_ROLES = List.of("admin");

	@Test
	void addUser_shouldReturnCreatedUserResponse() {
		UserController userController = new UserController(new UserServiceImpl(new UserDaoImpl()));
		UserRequest request = new UserRequest(TEST_USER_NAME, TEST_USER_EMAIL, TEST_USER_ROLES);

		ResponseEntity<UserResponse> response = userController.createUser(request);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getName()).isEqualTo(TEST_USER_NAME);
		assertThat(response.getBody().getEmail()).isEqualTo(TEST_USER_EMAIL);
	}

	@Test
	void getUsers_shouldReturnSavedUsers() {
		UserController userController = new UserController(new UserServiceImpl(new UserDaoImpl()));
		userController.createUser(new UserRequest(TEST_USER_NAME, TEST_USER_EMAIL, TEST_USER_ROLES));

		ResponseEntity<List<UserResponse>> response = userController.getUsers();

		assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody()).hasSize(1);
		assertThat(response.getBody().get(0).getName()).isEqualTo(TEST_USER_NAME);
	}

	@Test
	void deleteUser_shouldReturnOkStatus() {
		UserController userController = new UserController(new UserServiceImpl(new UserDaoImpl()));
		userController.createUser(new UserRequest(TEST_USER_NAME, TEST_USER_EMAIL, TEST_USER_ROLES));

		ResponseEntity<Void> response = userController.deleteUser(TEST_USER_EMAIL);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
	}

	@Test
	void findUser_shouldReturnUserResponse() {
		UserController userController = new UserController(new UserServiceImpl(new UserDaoImpl()));
		userController.createUser(new UserRequest(TEST_USER_NAME, TEST_USER_EMAIL, TEST_USER_ROLES));

		ResponseEntity<UserResponse> response = userController.findUser(TEST_USER_EMAIL);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getName()).isEqualTo(TEST_USER_NAME);
		assertThat(response.getBody().getEmail()).isEqualTo(TEST_USER_EMAIL);
	}

	@Test
	void updateUser_shouldReturnUpdatedUserResponse() {
		UserController userController = new UserController(new UserServiceImpl(new UserDaoImpl()));
		userController.createUser(new UserRequest(TEST_USER_NAME, TEST_USER_EMAIL, TEST_USER_ROLES));

		ResponseEntity<UserResponse> response =
				userController.updateUser(TEST_USER_EMAIL, new UpdateUserRequest(TEST_USER_NAME, List.of("user")));

		assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getName()).isEqualTo(TEST_USER_NAME);
		assertThat(response.getBody().getEmail()).isEqualTo(TEST_USER_EMAIL);
	}
}
