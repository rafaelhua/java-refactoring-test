package com.sap.refactoring.user.controller;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import com.sap.refactoring.user.dto.UpdateUserRequest;
import com.sap.refactoring.user.dto.UserRequest;
import com.sap.refactoring.user.dto.UserResponse;
import com.sap.refactoring.user.model.User;
import com.sap.refactoring.user.service.UserService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
	private static final String TEST_USER_NAME = "Alice";
	private static final String TEST_USER_EMAIL = "alice@example.com";
	private static final List<String> TEST_USER_ROLES = List.of("admin");

	@Mock
	private UserService userService;

	private UserController userController;

	@BeforeEach
	void setUp() {
		userController = new UserController(userService);
	}

	@Test
	void addUser_shouldReturnCreatedUserResponse() {
		UserRequest request = new UserRequest(TEST_USER_NAME, TEST_USER_EMAIL, TEST_USER_ROLES);
		User savedUser = new User(TEST_USER_NAME, TEST_USER_EMAIL, TEST_USER_ROLES);

		when(userService.createUser(TEST_USER_NAME, TEST_USER_EMAIL, TEST_USER_ROLES)).thenReturn(savedUser);

		ResponseEntity<UserResponse> response = userController.createUser(request);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getName()).isEqualTo(TEST_USER_NAME);
		assertThat(response.getBody().getEmail()).isEqualTo(TEST_USER_EMAIL);
	}

	@Test
	void getUsers_shouldReturnSavedUsers() {
		when(userService.getUsers()).thenReturn(List.of(new User(TEST_USER_NAME, TEST_USER_EMAIL, TEST_USER_ROLES)));

		ResponseEntity<List<UserResponse>> response = userController.getUsers();

		assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody()).hasSize(1);
		assertThat(response.getBody().get(0).getName()).isEqualTo(TEST_USER_NAME);
	}

	@Test
	void deleteUser_shouldReturnOkStatus() {
		ResponseEntity<Void> response = userController.deleteUser(TEST_USER_EMAIL);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
	}

	@Test
	void findUser_shouldReturnUserResponse() {
		when(userService.findUser(TEST_USER_EMAIL)).thenReturn(new User(TEST_USER_NAME, TEST_USER_EMAIL, TEST_USER_ROLES));

		ResponseEntity<UserResponse> response = userController.findUser(TEST_USER_EMAIL);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getName()).isEqualTo(TEST_USER_NAME);
		assertThat(response.getBody().getEmail()).isEqualTo(TEST_USER_EMAIL);
	}

	@Test
	void updateUser_shouldReturnUpdatedUserResponse() {
		UpdateUserRequest request = new UpdateUserRequest(TEST_USER_NAME, List.of("user"));
		User updatedUser = new User(TEST_USER_NAME, TEST_USER_EMAIL, List.of("user"));

		when(userService.updateUser(TEST_USER_EMAIL, TEST_USER_NAME, List.of("user"))).thenReturn(updatedUser);

		ResponseEntity<UserResponse> response = userController.updateUser(TEST_USER_EMAIL, request);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
		assertThat(response.getBody()).isNotNull();
		assertThat(response.getBody().getName()).isEqualTo(TEST_USER_NAME);
		assertThat(response.getBody().getEmail()).isEqualTo(TEST_USER_EMAIL);
	}
}
