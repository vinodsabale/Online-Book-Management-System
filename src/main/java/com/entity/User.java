package com.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "app_users")    // ← "user" se "app_users" karo — H2 reserved word fix
@Data
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

    private boolean userActive = true;
    @Column(nullable = false)
    private String password;
    public enum UserRole { ADMIN, MEMBER, LIBRARIAN }
 
}