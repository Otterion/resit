package ru.shers.resit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.shers.resit.entities.Role;
import ru.shers.resit.entities.User;
import ru.shers.resit.repository.RoleRepository;
import ru.shers.resit.repository.UserRepository;
import ru.shers.resit.service.RoleService;
import ru.shers.resit.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleService roleService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    public void setup() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("password");
        user.setRoles(Collections.emptyList());

    }

    @Test
    public void testLoadUserByUsernameSuccess() {
        // Подготовка данных
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        // Вызов метода
        UserDetails userDetails = userService.loadUserByUsername("testuser");

        // Проверка результата
        assertNotNull(userDetails);
        assertEquals("testuser", userDetails.getUsername());
    }

    @Test
    public void testLoadUserByUsernameNotFound() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername("testuser");
        });
    }

    @Test
    public void testCreateUserSuccess() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(roleService.getUserRole()).thenReturn(new Role());
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(user);

        assertNotNull(createdUser);
        assertEquals("testuser", createdUser.getUsername());
        assertEquals("encodedPassword", createdUser.getPassword());
    }

    @Test
    public void testCreateUserAlreadyExists() {
        // Подготовка данных
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        // Проверка исключения
        assertThrows(RuntimeException.class, () -> {
            userService.createUser(user);
        });
    }

    @Test
    public void testDeleteUserSuccess() {
        // Подготовка данных
        doNothing().when(userRepository).deleteById(1L);

        // Вызов метода
        userService.deleteUser(1L);

        // Проверка, что метод был вызван
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testFindByIdSuccess() {
        // Подготовка данных
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Вызов метода
        Optional<User> foundUser = userService.findById(1L);

        // Проверка результата
        assertTrue(foundUser.isPresent());
        assertEquals("testuser", foundUser.get().getUsername());
    }

    @Test
    public void testFindByIdNotFound() {
        // Подготовка данных
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Вызов метода
        Optional<User> foundUser = userService.findById(1L);

        // Проверка результата
        assertFalse(foundUser.isPresent());
    }

    @Test
    public void testUpdateUserSuccess() {
        // Подготовка данных
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newpassword")).thenReturn("encodedNewPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Обновление данных пользователя
        user.setPassword("newpassword");

        // Вызов метода
        User updatedUser = userService.updateUser(1L, user);

        // Проверка результата
        assertNotNull(updatedUser);
        assertEquals("encodedNewPassword", updatedUser.getPassword());
    }

    @Test
    public void testUpdateUserNotFound() {
        // Подготовка данных
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Проверка исключения
        assertThrows(RuntimeException.class, () -> {
            userService.updateUser(1L, user);
        });
    }

    @Test
    public void testFindAllUsers() {
        // Подготовка данных
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("user2");

        List<User> users = List.of(user1, user2);

        // Мокирование
        when(userRepository.findAll()).thenReturn(users);

        // Вызов метода
        List<User> foundUsers = userService.findAll();

        // Проверка результата
        assertNotNull(foundUsers);
        assertEquals(2, foundUsers.size());
        assertEquals("user1", foundUsers.get(0).getUsername());
        assertEquals("user2", foundUsers.get(1).getUsername());
    }

    @Test
    public void testFindByUsernameSuccess() {
        // Подготовка данных
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        // Вызов метода
        Optional<User> foundUser = userService.findByUsername("testuser");

        // Проверка результата
        assertTrue(foundUser.isPresent());
        assertEquals("testuser", foundUser.get().getUsername());
    }

    @Test
    public void testFindByUsernameNotFound() {
        // Подготовка данных
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        // Вызов метода
        Optional<User> foundUser = userService.findByUsername("testuser");

        // Проверка результата
        assertFalse(foundUser.isPresent());
    }
}
