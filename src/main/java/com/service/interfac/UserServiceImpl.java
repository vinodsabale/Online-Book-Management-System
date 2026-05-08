package com.service.interfac;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dto.UserDTO;
import com.entity.User;
import com.exception.BadRequestException;
import com.exception.ResourceNotFoundException;
import com.repository.UserRepository;
import com.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;  // ADD THIS

    public static UserDTO toDTO(User u) {
        return UserDTO.builder()
                .userId(u.getUserId())
                .userName(u.getUserName())
                .userEmail(u.getUserEmail())
                .userPhone(u.getUserPhone())
                .userRole(u.getRole())
                .userActive(u.isUserActive())
                .build();
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                    new ResourceNotFoundException("User not found: " + id));
        user.setUserActive(false);
        userRepository.save(user);
    }

    @Override
    public Page<UserDTO> getAllUsers(String keyword, int page, int size,
                                     String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return userRepository.searchUsers(keyword, pageable).map(UserServiceImpl::toDTO);
    }

    @Override
    public long getTotalUsersCount() {
        return userRepository.count();
    }

    @Override
    public UserDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(UserServiceImpl::toDTO)
                .orElseThrow(() ->
                    new ResourceNotFoundException("User not found with id: " + id));
    }

    @Override
    @Transactional
    public UserDTO saveUser(UserDTO dto) {
        if (userRepository.existsByUserEmail(dto.getUserEmail()))
            throw new BadRequestException("Email already registered: " + dto.getUserEmail());
        return toDTO(userRepository.save(toEntity(dto)));
    }

    private User toEntity(UserDTO dto) {
        return User.builder()
                .userId(dto.getUserId())
                .userName(dto.getUserName())
                .userEmail(dto.getUserEmail())
                .userPhone(dto.getUserPhone())
                .password(passwordEncoder.encode(dto.getPassword()))  // ADD THIS
                .role(dto.getUserRole() != null ? dto.getUserRole() : User.UserRole.MEMBER)
                .userActive(true)
                .build();
    }

    @Override
    @Transactional
    public UserDTO updateUser(Long id, UserDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                    new ResourceNotFoundException("User not found with id: " + id));
        user.setUserName(dto.getUserName());
        user.setRole(dto.getUserRole());
        return toDTO(userRepository.save(user));
    }
}