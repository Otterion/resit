package ru.shers.resit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.shers.resit.controller.TeacherController;
import ru.shers.resit.entities.Teacher;
import ru.shers.resit.service.TeacherService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class TeacherControllerTest {
    private MockMvc mockMvc;

    @Mock
    private TeacherService teacherService;

    @InjectMocks
    private TeacherController teacherController;


    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(teacherController).build();
    }

    @Test
    public void testGetAllTeachers() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");

        List<Teacher> teachers = Collections.singletonList(teacher);

        when(teacherService.getAllTeachers()).thenReturn(teachers);

        mockMvc.perform(get("/api/teachers")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"));
    }

    @Test
    public void testGetTeacherByIdSuccess() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");

        when(teacherService.getTeacherById(1L)).thenReturn(Optional.of(teacher));

        mockMvc.perform(get("/api/teachers/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    public void testGetTeacherByIdNotFound() throws Exception {
        when(teacherService.getTeacherById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/teachers/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateTeacher() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");

        when(teacherService.saveTeacher(any(Teacher.class))).thenReturn(teacher);

        mockMvc.perform(post("/api/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teacher)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    public void testUpdateTeacherSuccess() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");

        when(teacherService.getTeacherById(1L)).thenReturn(Optional.of(teacher));
        when(teacherService.saveTeacher(any(Teacher.class))).thenReturn(teacher);

        mockMvc.perform(put("/api/teachers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teacher)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    public void testUpdateTeacherNotFound() throws Exception {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");

        when(teacherService.getTeacherById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/teachers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teacher)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteTeacherSuccess() throws Exception {
        when(teacherService.getTeacherById(1L)).thenReturn(Optional.of(new Teacher()));

        mockMvc.perform(delete("/api/teachers/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
