package com.e3gsix.fiap.tech_challenge_5_credentials.model.entity;

import com.e3gsix.fiap.tech_challenge_5_credentials.model.enums.UserRole;
import com.e3gsix.fiap.tech_challenge_5_credentials.security.SecurityUtils;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity(name = "users")
@Table(
        indexes = {
                @Index(name = "username_index", columnList = "username", unique = true)
        }
)
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String username;
    private String password;
    private UserRole role;

    private final int MIN_USERNAME_LENGTH = 3;
    private final int MIN_PASSWORD_LENGTH = 6;

    public User() {
    }

    public User(String username, String password, UserRole role) {
        this.setUsername(username);
        this.setPassword(password);
        this.setRole(role);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID value) {
        if (value == null) {
            throw new IllegalArgumentException("ID não pode estar nulo.");
        }

        this.id = value;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String value) {
        if (value == null || value.isEmpty() || value.isBlank()) {
            throw new IllegalArgumentException("Username não pode estar nulo nem vazio.");
        }

        if (value.length() < MIN_USERNAME_LENGTH) {
            throw new IllegalArgumentException("Username deve ter tamanho de pelo menos " +
                    MIN_USERNAME_LENGTH + " caracteres.");
        }

        this.username = value;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String value) {
        if (value.length() < MIN_PASSWORD_LENGTH) {
            throw new IllegalArgumentException("Senha deve ter tamanho de pelo menos " +
                    MIN_PASSWORD_LENGTH + " caracteres.");
        }

        this.password = SecurityUtils.encrypt(value);
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole value) {
        this.role = value;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == UserRole.ADMIN)
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        else return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
