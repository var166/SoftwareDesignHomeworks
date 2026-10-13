package andrei.usersservice;

import andrei.usersservice.model.User;
import andrei.usersservice.repository.UserQueryRepository;
import andrei.usersservice.service.UserQueryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserQueryServiceTest {

    @Mock
    private UserQueryRepository userQueryRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserQueryService userQueryService;

    @Test
    void authenticate_validCredentials_returnsUser() {
        User user = User.builder().username("alice").password("encodedPass").build();
        when(userQueryRepository.findByUsername("alice")).thenReturn(user);
        when(passwordEncoder.matches("rawPass", "encodedPass")).thenReturn(true);

        Optional<User> result = userQueryService.authenticate("alice", "rawPass");

        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("alice");
    }

    @Test
    void authenticate_wrongPassword_returnsEmpty() {
        User user = User.builder().username("alice").password("encodedPass").build();
        when(userQueryRepository.findByUsername("alice")).thenReturn(user);
        when(passwordEncoder.matches("wrong", "encodedPass")).thenReturn(false);

        Optional<User> result = userQueryService.authenticate("alice", "wrong");

        assertThat(result).isEmpty();
    }

    @Test
    void authenticate_userNotFound_returnsEmpty() {
        when(userQueryRepository.findByUsername("ghost")).thenReturn(null);

        Optional<User> result = userQueryService.authenticate("ghost", "pass");

        assertThat(result).isEmpty();
        verify(passwordEncoder, never()).matches(any(), any());
    }

    @Test
    void findAll_returnsAllUsers() {
        List<User> users = List.of(new User(), new User());
        when(userQueryRepository.findAll()).thenReturn(users);

        List<User> result = userQueryService.findAll();

        assertThat(result).hasSize(2);
    }

    @Test
    void findByUsername_delegatesToRepo() {
        User user = User.builder().username("alice").build();
        when(userQueryRepository.findByUsername("alice")).thenReturn(user);

        User result = userQueryService.findByUsername("alice");

        assertThat(result.getUsername()).isEqualTo("alice");
    }

    @Test
    void findByEmail_delegatesToRepo() {
        User user = User.builder().email("a@b.com").build();
        when(userQueryRepository.findByEmail("a@b.com")).thenReturn(user);

        User result = userQueryService.findByEmail("a@b.com");

        assertThat(result.getEmail()).isEqualTo("a@b.com");
    }

    @Test
    void findById_delegatesToRepo() {
        User user = User.builder().build();
        when(userQueryRepository.findById(1L)).thenReturn(user);

        User result = userQueryService.findById(1L);

        assertThat(result).isEqualTo(user);
    }
}
