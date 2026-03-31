package com.sap.refactoring.user.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import com.sap.refactoring.user.exception.ErrorMessages;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserIntegrationTest {
	private static final String TEST_USER_NAME = "Alice";
	private static final String TEST_USER_EMAIL = "alice@example.com";
	private static final String TEST_USER_ROLE = "admin";

	@Autowired
	private MockMvc mockMvc;

	@Test
	void createUser_shouldReturnUserResponse() throws Exception {
		mockMvc.perform(
						post("/users")
								.contentType(MediaType.APPLICATION_JSON)
								.content(userRequestJson(TEST_USER_NAME, TEST_USER_EMAIL, TEST_USER_ROLE)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.name").value(TEST_USER_NAME))
				.andExpect(jsonPath("$.email").value(TEST_USER_EMAIL))
				.andExpect(jsonPath("$.roles[0]").value(TEST_USER_ROLE));
	}

	@Test
	void createUser_shouldReturnConflictForDuplicateEmail() throws Exception {
		mockMvc.perform(
						post("/users")
								.contentType(MediaType.APPLICATION_JSON)
								.content(userRequestJson(TEST_USER_NAME, "duplicate@example.com", TEST_USER_ROLE)))
				.andExpect(status().isCreated());

		mockMvc.perform(
						post("/users")
								.contentType(MediaType.APPLICATION_JSON)
								.content(userRequestJson("Bob", "duplicate@example.com", "user")))
				.andExpect(status().isConflict())
				.andExpect(content().string(ErrorMessages.DUPLICATE_EMAIL));
	}

	@Test
	void searchUser_shouldReturnNotFoundForMissingUser() throws Exception {
		mockMvc.perform(get("/users/missing@example.com"))
				.andExpect(status().isNotFound())
				.andExpect(content().string(ErrorMessages.USER_NOT_FOUND));
	}

	@Test
	void searchUser_shouldReturnUserResponseWhenUserExists() throws Exception {
		mockMvc.perform(
						post("/users")
								.contentType(MediaType.APPLICATION_JSON)
								.content(userRequestJson("Carol", "carol@example.com", "user")))
				.andExpect(status().isCreated());

		mockMvc.perform(get("/users/carol@example.com"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("Carol"))
				.andExpect(jsonPath("$.email").value("carol@example.com"))
				.andExpect(jsonPath("$.roles[0]").value("user"));
	}

	@Test
	void findUsers_shouldReturnSavedUsers() throws Exception {
		mockMvc.perform(
						post("/users")
								.contentType(MediaType.APPLICATION_JSON)
								.content(userRequestJson("Dave", "dave@example.com", TEST_USER_ROLE)))
				.andExpect(status().isCreated());

		mockMvc.perform(get("/users"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].name").exists());
	}

	@Test
	void createUser_shouldReturnBadRequestForBlankName() throws Exception {
		mockMvc.perform(
						post("/users")
								.contentType(MediaType.APPLICATION_JSON)
								.content(userRequestJson("", "bad@example.com", TEST_USER_ROLE)))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(ErrorMessages.NAME_REQUIRED));
	}

	@Test
	void deleteUser_shouldReturnOkWhenUserExists() throws Exception {
		mockMvc.perform(
						post("/users")
								.contentType(MediaType.APPLICATION_JSON)
								.content(userRequestJson("Eve", "eve@example.com", "user")))
				.andExpect(status().isCreated());

		mockMvc.perform(delete("/users/eve@example.com")).andExpect(status().isNoContent());
	}

	@Test
	void updateUser_shouldReturnUpdatedUserResponse() throws Exception {
		mockMvc.perform(
						post("/users")
								.contentType(MediaType.APPLICATION_JSON)
								.content(userRequestJson(TEST_USER_NAME, TEST_USER_EMAIL, TEST_USER_ROLE)))
				.andExpect(status().isCreated());

		mockMvc.perform(
						put("/users/" + TEST_USER_EMAIL)
								.contentType(MediaType.APPLICATION_JSON)
								.content(updateUserRequestJson(TEST_USER_NAME, "user")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value(TEST_USER_NAME))
				.andExpect(jsonPath("$.email").value(TEST_USER_EMAIL))
				.andExpect(jsonPath("$.roles[0]").value("user"));
	}

	private String userRequestJson(String name, String email, String role) {
		return """
				{
				  "name": "%s",
				  "email": "%s",
				  "roles": ["%s"]
				}
				""".formatted(name, email, role);
	}

	private String updateUserRequestJson(String name, String role) {
		return """
				{
				  "name": "%s",
				  "roles": ["%s"]
				}
				""".formatted(name, role);
	}
}
