package andrei.usersservice;

import andrei.usersservice.model.User;
import andrei.usersservice.model.UserRole;
import andrei.usersservice.repository.UserCommandRepository;
import andrei.usersservice.service.UserCommandService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserCommandServiceTest {

    @Mock
    private UserCommandRepository userCommandRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserCommandService userCommandService;

    @Test
    void register_savesUserWithEncodedPassword() {
        when(passwordEncoder.encode("pass")).thenReturn("encodedPass");
        User saved = User.builder().username("alice").email("a@b.com").password("encodedPass").userRole(UserRole.USER).build();
        when(userCommandRepository.save(any(User.class))).thenReturn(saved);

        User result = userCommandService.register("alice", "a@b.com", "pass");

        verify(passwordEncoder).encode("pass");
        verify(userCommandRepository).save(any(User.class));
        assertThat(result.getPassword()).isEqualTo("encodedPass");
        assertThat(result.getUserRole()).isEqualTo(UserRole.USER);
    }

    @Test
    void updateUserRole_userExists_updatesAndReturnsTrue() {
        when(userCommandRepository.findById(1L)).thenReturn(Optional.of(new User()));

        boolean result = userCommandService.updateUserRole(1L, "ADMIN");

        verify(userCommandRepository).updateUserRole(1L, UserRole.ADMIN);
        assertThat(result).isTrue();
    }

    @Test
    void updateUserRole_userNotFound_returnsFalse() {
        when(userCommandRepository.findById(99L)).thenReturn(Optional.empty());

        boolean result = userCommandService.updateUserRole(99L, "ADMIN");

        verify(userCommandRepository, never()).updateUserRole(anyLong(), any());
        assertThat(result).isFalse();
    }

    @Test
    void updateUserRole_invalidRole_returnsFalse() {
        boolean result = userCommandService.updateUserRole(1L, "INVALID_ROLE");

        verify(userCommandRepository, never()).findById(anyLong());
        assertThat(result).isFalse();
    }

    @Test
    void updatePassword_userExists_updatesAndReturnsTrue() {
        when(userCommandRepository.findById(1L)).thenReturn(Optional.of(new User()));
        when(passwordEncoder.encode("newPass")).thenReturn("encodedNew");

        boolean result = userCommandService.updatePassword(1L, "newPass");

        verify(userCommandRepository).updatePassword(1L, "encodedNew");
        assertThat(result).isTrue();
    }

    @Test
    void updatePassword_userNotFound_returnsFalse() {
        when(userCommandRepository.findById(99L)).thenReturn(Optional.empty());

        boolean result = userCommandService.updatePassword(99L, "newPass");

        verify(userCommandRepository, never()).updatePassword(anyLong(), anyString());
        assertThat(result).isFalse();
    }

    @Test
    void delete_userExists_deletesAndReturnsTrue() {
        when(userCommandRepository.findById(1L)).thenReturn(Optional.of(new User()));

        boolean result = userCommandService.delete(1L);

        verify(userCommandRepository).deleteById(1L);
        assertThat(result).isTrue();
    }

    @Test
    void delete_userNotFound_returnsFalse() {
        when(userCommandRepository.findById(99L)).thenReturn(Optional.empty());

        boolean result = userCommandService.delete(99L);

        verify(userCommandRepository, never()).deleteById(anyLong());
        assertThat(result).isFalse();
    }
}
