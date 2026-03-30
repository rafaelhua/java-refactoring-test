package com.sap.refactoring.user.exception;

public final class ErrorMessages {
	public static final String USER_NOT_FOUND = "User not found";
	public static final String DUPLICATE_EMAIL = "A user with this email already exists";
	public static final String NAME_REQUIRED = "Name must not be blank";
	public static final String EMAIL_REQUIRED = "Email must not be blank";
	public static final String ROLE_REQUIRED = "User must have at least one role";

	private ErrorMessages() {}
}
