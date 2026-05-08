package com.dto;

import com.entity.User.UserRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
	private Long userId;
	@NotBlank(message="Name is required !")
	private String userName;
	@Email
	@NotBlank
	private String userEmail;
	private String userPhone;
	private UserRole userRole;
	private boolean userActive=false;
	@NotBlank(message = "Password is required")
	private String password;
}
