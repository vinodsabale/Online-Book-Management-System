package com.service;
import org.springframework.data.domain.Page;


import com.dto.UserDTO;
public interface UserService {
	 Page<UserDTO> getAllUsers(String keyword, int page, int size, String sortBy, String sortDir);
	    UserDTO getUserById(Long id);
	    UserDTO saveUser(UserDTO userDTO);
	    UserDTO updateUser(Long id, UserDTO userDTO);
	    void deleteUser(Long id);
	    long getTotalUsersCount();
	    boolean verifyEmail(String token);
}