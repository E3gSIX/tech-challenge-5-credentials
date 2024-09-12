package com.e3gsix.fiap.tech_challenge_5_credentials.model.entity;

import com.e3gsix.fiap.tech_challenge_5_credentials.model.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserTest {

    private User user;
    private static final String VALID_USERNAME = "user123";
    private static final String VALID_PASSWORD = "password123";
    private static final UserRole USER_ROLE = UserRole.USER;
    private static final UserRole ADMIN_ROLE = UserRole.ADMIN;
    private static final UUID USER_ID = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        user = new User(VALID_USERNAME, VALID_PASSWORD, USER_ROLE);
        user.setId(USER_ID);
    }

    @Test
    void setUsername_ShouldSetUsername_WhenUsernameIsValid() {
        user.setUsername("newUser");
        assertEquals("newUser", user.getUsername());
    }

    @Test
    void setUsername_ShouldThrowIllegalArgumentException_WhenUsernameIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> user.setUsername(null));
        assertEquals("Username não pode estar nulo nem vazio.", exception.getMessage());
    }

    @Test
    void setUsername_ShouldThrowIllegalArgumentException_WhenUsernameIsTooShort() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> user.setUsername("us"));
        assertEquals("Username deve ter tamanho de pelo menos 3 caracteres.", exception.getMessage());
    }

    @Test
    void setPassword_ShouldThrowIllegalArgumentException_WhenPasswordIsTooShort() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> user.setPassword("pass"));
        assertEquals("Senha deve ter tamanho de pelo menos 6 caracteres.", exception.getMessage());
    }

    @Test
    void setId_ShouldSetId_WhenIdIsValid() {
        UUID newId = UUID.randomUUID();
        user.setId(newId);
        assertEquals(newId, user.getId());
    }

    @Test
    void setId_ShouldThrowIllegalArgumentException_WhenIdIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> user.setId(null));
        assertEquals("ID não pode estar nulo.", exception.getMessage());
    }

    @Test
    void getAuthorities_ShouldGrantUserAuthority_WhenUserRoleIsUser() {
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Test
    void getAuthorities_ShouldGrantAdminAndUserAuthority_WhenUserRoleIsAdmin() {
        user.setRole(ADMIN_ROLE);
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        assertEquals(2, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Test
    void equalsAndHashCode_ShouldReturnTrue_WhenUsersHaveTheSameId() {
        User otherUser = new User(VALID_USERNAME, VALID_PASSWORD, USER_ROLE);
        otherUser.setId(USER_ID);
        assertEquals(user, otherUser);
        assertEquals(user.hashCode(), otherUser.hashCode());

        otherUser.setId(UUID.randomUUID());
        assertNotEquals(user, otherUser);
    }

    @Test
    void isAccountNonExpired_ShouldReturnTrue() {
        assertTrue(user.isAccountNonExpired());
    }

    @Test
    void isAccountNonLocked_ShouldReturnTrue() {
        assertTrue(user.isAccountNonLocked());
    }

    @Test
    void isCredentialsNonExpired_ShouldReturnTrue() {
        assertTrue(user.isCredentialsNonExpired());
    }

    @Test
    void isEnabled_ShouldReturnTrue() {
        assertTrue(user.isEnabled());
    }
}