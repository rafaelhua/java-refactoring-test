package com.sap.refactoring.user.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sap.refactoring.user.dto.UserRequest;
import com.sap.refactoring.user.dto.UserResponse;
import com.sap.refactoring.user.model.User;
import com.sap.refactoring.user.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("add/")
	public ResponseEntity<UserResponse> addUser(
			@RequestParam("name") String name,
			@RequestParam("email") String email,
			@RequestParam("role") List<String> roles) {
		UserRequest request = new UserRequest(name, email, roles);
		User user = userService.createUser(
				request.getName(), request.getEmail(), request.getRoles());
		return ResponseEntity.ok(toUserResponse(user));
	}

	@GetMapping("update/")
	public ResponseEntity<UserResponse> updateUser(
			@RequestParam("name") String name,
			@RequestParam("email") String email,
			@RequestParam("role") List<String> roles) {
		UserRequest request = new UserRequest(name, email, roles);
		User user = userService.updateUser(
				request.getName(), request.getEmail(), request.getRoles());
		return ResponseEntity.ok(toUserResponse(user));
	}

	@GetMapping("delete/")
	public ResponseEntity<Void> deleteUser(
			@RequestParam("name") String name,
			@RequestParam("email") String email,
			@RequestParam("role") List<String> roles) {
		userService.deleteUser(name);
		return ResponseEntity.ok().build();
	}

	@GetMapping("find/")
	public ResponseEntity<List<UserResponse>> getUsers() {
		return ResponseEntity.ok(toUserResponses(userService.getUsers()));
	}

	@GetMapping("search/")
	public ResponseEntity<UserResponse> findUser(@RequestParam("name") String name) {
		return ResponseEntity.ok(toUserResponse(userService.findUser(name)));
	}

	private UserResponse toUserResponse(User user) {
		return new UserResponse(user.getName(), user.getEmail(), user.getRoles());
	}

	private List<UserResponse> toUserResponses(List<User> users) {
		List<UserResponse> responses = new ArrayList<>();
		for (User user : users) {
			responses.add(toUserResponse(user));
		}
		return responses;
	}
}
