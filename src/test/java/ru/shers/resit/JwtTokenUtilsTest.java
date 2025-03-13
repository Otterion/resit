package ru.shers.resit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import ru.shers.resit.util.JwtTokenUtils;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class JwtTokenUtilsTest {

    @InjectMocks
    private JwtTokenUtils jwtTokenUtils;

    private UserDetails userDetails;

    @BeforeEach
    public void setup() {
        // Создаем тестового пользователя с ролью ROLE_USER
        userDetails = new User(
                "testuser",
                "password",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

    @Test
    public void testGenerateToken() {
        // Генерация токена
        String token = jwtTokenUtils.generateToken(userDetails);

        // Проверка, что токен не пустой
        assertNotNull(token);
        assertFalse(token.isEmpty());

        // Проверка, что токен содержит имя пользователя
        String username = jwtTokenUtils.getUsernameFromToken(token);
        assertEquals("testuser", username);

        // Проверка, что токен содержит роли
        List<String> roles = jwtTokenUtils.getRolesFromToken(token);
        assertNotNull(roles);
        assertEquals(1, roles.size());
        assertEquals("ROLE_USER", roles.get(0));
    }

    @Test
    public void testGetUsernameFromToken() {
        // Генерация токена
        String token = jwtTokenUtils.generateToken(userDetails);

        // Извлечение имени пользователя из токена
        String username = jwtTokenUtils.getUsernameFromToken(token);

        // Проверка результата
        assertEquals("testuser", username);
    }

    @Test
    public void testGetRolesFromToken() {
        // Генерация токена
        String token = jwtTokenUtils.generateToken(userDetails);

        // Извлечение ролей из токена
        List<String> roles = jwtTokenUtils.getRolesFromToken(token);

        // Проверка результата
        assertNotNull(roles);
        assertEquals(1, roles.size());
        assertEquals("ROLE_USER", roles.get(0));
    }

    @Test
    public void testGetClaimsFromToken() {
        // Генерация токена
        String token = jwtTokenUtils.generateToken(userDetails);

        // Извлечение claims из токена
        Claims claims = Jwts.parser()
                .setSigningKey("a1dcbc9bb584060a839af002a6b3ebd777f30193a07e6d8ca81a1b5bb74a92f1")
                .build()
                .parseClaimsJws(token)
                .getBody();

        // Проверка, что claims содержат имя пользователя и роли
        assertEquals("testuser", claims.getSubject());
        assertNotNull(claims.get("roles"));
        assertEquals("ROLE_USER", ((List<?>) claims.get("roles")).get(0));
    }

    @Test
    public void testTokenExpiration() {
        // Генерация токена
        String token = jwtTokenUtils.generateToken(userDetails);

        // Извлечение claims из токена
        Claims claims = Jwts.parser()
                .setSigningKey("a1dcbc9bb584060a839af002a6b3ebd777f30193a07e6d8ca81a1b5bb74a92f1")
                .build()
                .parseClaimsJws(token)
                .getBody();

        // Проверка, что токен не истек
        assertFalse(claims.getExpiration().before(new Date()));
    }
}