package com.sap.refactoring.user.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

	@PostMapping
	public ResponseEntity<UserResponse> createUser(@RequestBody UserRequest request) {
		User user = userService.createUser(
				request.getName(), request.getEmail(), request.getRoles());
		return ResponseEntity.status(HttpStatus.CREATED).body(toUserResponse(user));
	}

	@PutMapping("/{email}")
	public ResponseEntity<UserResponse> updateUser(
			@PathVariable String email, @RequestBody UserRequest request) {
		User user = userService.updateUser(
				email, request.getName(), request.getEmail(), request.getRoles());
		return ResponseEntity.ok(toUserResponse(user));
	}

	@DeleteMapping("/{email}")
	public ResponseEntity<Void> deleteUser(@PathVariable String email) {
		userService.deleteUser(email);
		return ResponseEntity.noContent().build();
	}

	@GetMapping
	public ResponseEntity<List<UserResponse>> getUsers() {
		return ResponseEntity.ok(toUserResponses(userService.getUsers()));
	}

	@GetMapping("/{email}")
	public ResponseEntity<UserResponse> findUser(@PathVariable String email) {
		return ResponseEntity.ok(toUserResponse(userService.findUser(email)));
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
