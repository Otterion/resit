package ru.shers.resit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
import ru.shers.resit.controller.RetakeController;
import ru.shers.resit.entities.Retake;
import ru.shers.resit.service.RetakeService;

@ExtendWith(MockitoExtension.class)
public class RetakeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RetakeService retakeService;

    @InjectMocks
    private RetakeController retakeController;


    private ObjectMapper objectMapper;



    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(retakeController).build();

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Test
    public void testGetAllRetakes() throws Exception {
        Retake retake = new Retake();
        retake.setId(1);
        retake.setRetakeDate(LocalDate.of(2023, 10, 1));
        retake.setStatus("Pending");

        List<Retake> retakes = Collections.singletonList(retake);

        when(retakeService.getAllRetakes()).thenReturn(retakes);

        mockMvc.perform(get("/api/retakes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].retakeDate").value("2023-10-01"))
                .andExpect(jsonPath("$[0].status").value("Pending"));
    }

    @Test
    public void testGetRetakesForStudent() throws Exception {
        Retake retake = new Retake();
        retake.setId(1);
        retake.setRetakeDate(LocalDate.of(2023, 10, 1));
        retake.setStatus("Pending");

        List<Retake> retakes = Collections.singletonList(retake);

        when(retakeService.getRetakesForStudent(1L)).thenReturn(retakes);

        mockMvc.perform(get("/api/retakes/student/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].retakeDate").value("2023-10-01"))
                .andExpect(jsonPath("$[0].status").value("Pending"));
    }

    @Test
    public void testCreateRetake() throws Exception {
        Retake retake = new Retake();
        retake.setId(1);
        retake.setRetakeDate(LocalDate.of(2023, 10, 1));
        retake.setStatus("Pending");

        when(retakeService.createRetake(any(Retake.class))).thenReturn(retake);

        mockMvc.perform(post("/api/retakes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(retake)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.retakeDate").value("2023-10-01"))
                .andExpect(jsonPath("$.status").value("Pending"));
    }

    @Test
    public void testDeleteRetakeSuccess() throws Exception {
        doNothing().when(retakeService).deleteRetake(1);

        mockMvc.perform(delete("/api/retakes/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}