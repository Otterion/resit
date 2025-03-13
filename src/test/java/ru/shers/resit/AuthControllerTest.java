package ru.shers.resit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.shers.resit.controller.AuthController;
import ru.shers.resit.entities.User;
import ru.shers.resit.service.UserService;
import ru.shers.resit.util.JwtTokenUtils;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {
    MockMvc mockMvc;

    @Mock
    public UserService userService;

    @Mock
    private JwtTokenUtils jwtTokenUtils;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthController authController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    void testLogin_Success() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("testpassword");

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        when(userService.loadUserByUsername(user.getUsername())).thenReturn(userDetails);
        when(jwtTokenUtils.generateToken(userDetails)).thenReturn("testToken");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(content().string("testToken"));
    }

    @Test
    public void testLoginFailure() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("wrongpassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testRegistrationSuccess() throws Exception {
        // Подготовка данных
        User user = new User();
        user.setUsername("newuser");
        user.setPassword("newpassword");

        // Мокирование
        when(userService.createUser(any(User.class))).thenReturn(user);

        // Выполнение запроса и проверка результата
        mockMvc.perform(post("/auth/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("newuser"));
    }

    @Test
    public void testRegistrationFailure() throws Exception {
        // Подготовка данных
        User user = new User();
        user.setUsername("existinguser");
        user.setPassword("password");

        // Мокирование
        when(userService.createUser(any(User.class)))
                .thenThrow(new RuntimeException("Пользователь с таким именем уже есть"));

        // Выполнение запроса и проверка результата
        mockMvc.perform(post("/auth/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Пользователь с таким именем уже есть"));
    }
}
