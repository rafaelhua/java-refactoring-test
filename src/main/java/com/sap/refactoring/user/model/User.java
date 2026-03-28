package com.sap.refactoring.user.model;

import java.util.ArrayList;
import java.util.List;

public class User {
	private String name;
	private String email;
	private List<String> roles = new ArrayList<>();

	public User() {}

	public User(String name, String email, List<String> roles) {
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
