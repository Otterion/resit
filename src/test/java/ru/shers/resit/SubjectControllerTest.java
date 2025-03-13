package ru.shers.resit;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.shers.resit.controller.SubjectController;
import ru.shers.resit.entities.Subject;
import ru.shers.resit.service.SubjectService;

@ExtendWith(MockitoExtension.class)
public class SubjectControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SubjectService subjectService;

    @InjectMocks
    private SubjectController subjectController;


    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(subjectController).build();
    }

    @Test
    public void testGetAllSubjects() throws Exception {
        Subject subject = new Subject();
        subject.setId(1L);
        subject.setName("Mathematics");

        List<Subject> subjects = Collections.singletonList(subject);

        when(subjectService.getAllSubjects()).thenReturn(subjects);

        mockMvc.perform(get("/api/subjects")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Mathematics"));
    }

    @Test
    public void testCreateSubject() throws Exception {
        Subject subject = new Subject();
        subject.setId(1L);
        subject.setName("Mathematics");

        when(subjectService.createSubject(any(Subject.class))).thenReturn(subject);

        mockMvc.perform(post("/api/subjects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(subject)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Mathematics"));
    }

    @Test
    public void testDeleteSubjectSuccess() throws Exception {
        doNothing().when(subjectService).deleteSubject(1L);
        mockMvc.perform(delete("/api/subjects/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
