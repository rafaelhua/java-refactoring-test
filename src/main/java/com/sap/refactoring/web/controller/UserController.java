package com.sap.refactoring.web.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/users")
public class UserController {
	private final com.sap.refactoring.user.controller.UserController delegate;

	public UserController() {
		this.delegate = new com.sap.refactoring.user.controller.UserController();
	}

	@GetMapping("add/")
	public ResponseEntity addUser(
			@RequestParam("name") String name,
			@RequestParam("email") String email,
			@RequestParam("role") List<String> roles) {
		return delegate.addUser(name, email, roles);
	}

	@GetMapping("update/")
	public ResponseEntity updateUser(
			@RequestParam("name") String name,
			@RequestParam("email") String email,
			@RequestParam("role") List<String> roles) {
		return delegate.updateUser(name, email, roles);
	}

	@GetMapping("delete/")
	public ResponseEntity deleteUser(
			@RequestParam("name") String name,
			@RequestParam("email") String email,
			@RequestParam("role") List<String> roles) {
		return delegate.deleteUser(name, email, roles);
	}

	@GetMapping("find/")
	public ResponseEntity getUsers() {
		return delegate.getUsers();
	}

	@GetMapping("search/")
	public ResponseEntity findUser(@RequestParam("name") String name) {
		return delegate.findUser(name);
	}
}
