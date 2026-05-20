package com.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "app_users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotBlank
    private String userName;

    @Email
    @Column(unique = true, nullable = false)
    private String userEmail;

    private String userPhone;

    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.MEMBER;

    @Column(name = "user_active")
    private boolean userActive = true;

    @Column(nullable = false)
    private String password;

    private String verificationToken;

    @Column(name = "is_verified")
    private boolean verified = false;   // ← "isVerified" नाही, "verified" कर

    public enum UserRole { ADMIN, MEMBER, LIBRARIAN }
}