package com.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.entity.User;
import com.entity.User.UserRole;


public interface UserRepository extends JpaRepository<User,Long>{
	
	@Query("SELECT u FROM User u WHERE " +
		       "(:keyword IS NULL OR LOWER(u.userName) LIKE LOWER(CONCAT('%',:keyword,'%')) " +
		       "OR LOWER(u.userEmail) LIKE LOWER(CONCAT('%',:keyword,'%'))) " +
		       "AND u.userActive = true " +
		       "ORDER BY u.userName ASC")
		Page<User> searchUsers(@Param("keyword") String keyword, Pageable pageable);
	 // ✅ CORRECT:
	 Optional<User> findByUserEmail(String email);
	 Page<User> findByRoleAndUserActiveTrue(UserRole role, Pageable pageable);
	 boolean existsByUserEmail(String email);
	 Optional<User> findByVerificationToken(String token);
}
