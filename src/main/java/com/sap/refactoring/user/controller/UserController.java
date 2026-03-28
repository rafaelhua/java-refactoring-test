package com.sap.refactoring.user.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.sap.refactoring.user.service.UserService;
import com.sap.refactoring.user.service.UserServiceImpl;

@Controller
@RequestMapping("/users")
public class UserController {
	private final UserService userService;

	public UserController() {
		this(new UserServiceImpl());
	}

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("add/")
	public ResponseEntity addUser(
			@RequestParam("name") String name,
			@RequestParam("email") String email,
			@RequestParam("role") List<String> roles) {
		return ResponseEntity.ok(userService.createUser(name, email, roles));
	}

	@GetMapping("update/")
	public ResponseEntity updateUser(
			@RequestParam("name") String name,
			@RequestParam("email") String email,
			@RequestParam("role") List<String> roles) {
		return ResponseEntity.ok(userService.updateUser(name, email, roles));
	}

	@GetMapping("delete/")
	public ResponseEntity deleteUser(
			@RequestParam("name") String name,
			@RequestParam("email") String email,
			@RequestParam("role") List<String> roles) {
		userService.deleteUser(name);
		return ResponseEntity.ok().build();
	}

	@GetMapping("find/")
	public ResponseEntity getUsers() {
		return ResponseEntity.ok(userService.getUsers());
	}

	@GetMapping("search/")
	public ResponseEntity findUser(@RequestParam("name") String name) {
		return ResponseEntity.ok(userService.findUser(name));
	}
}
