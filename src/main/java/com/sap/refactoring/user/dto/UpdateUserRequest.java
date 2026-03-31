package com.sap.refactoring.user.dto;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public class UpdateUserRequest {
	@NotBlank(message = "Name must not be blank")
	private String name;

	@NotEmpty(message = "User must have at least one role")
	private List<String> roles = new ArrayList<>();

	public UpdateUserRequest() {}

	public UpdateUserRequest(String name, List<String> roles) {
		this.name = name;
		setRoles(roles);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
