package com.sap.refactoring.user.dto;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public class UserRequest {
	@NotBlank(message = "Name must not be blank")
	private String name;

	@NotBlank(message = "Email must not be blank")
	private String email;

	@NotEmpty(message = "User must have at least one role")
	private List<String> roles = new ArrayList<>();

	public UserRequest() {}

	public UserRequest(String name, String email, List<String> roles) {
		this.name = name;
		this.email = email;
		setRoles(roles);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<String> getRoles() {
		return new ArrayList<>(roles);
	}

	public void setRoles(List<String> roles) {
		if (roles == null) {
			this.roles = new ArrayList<>();
			return;
		}
		this.roles = new ArrayList<>(roles);
	}
}
