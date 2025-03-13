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
import ru.shers.resit.entities.Retake;
import ru.shers.resit.repository.RetakeRepository;
import ru.shers.resit.service.RetakeService;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class RetakeServiceTest {

    @Mock
    private RetakeRepository retakeRepository;

    @InjectMocks
    private RetakeService retakeService;

    private Retake retake;

    @BeforeEach
    public void setup() {
        retake = new Retake();
        retake.setId(1);
        retake.setRetakeDate(LocalDate.of(2023, 10, 1));
        retake.setStatus("Pending");
    }

    @Test
    public void testGetAllRetakes() {
        when(retakeRepository.findAll()).thenReturn(Collections.singletonList(retake));

        List<Retake> retakes = retakeService.getAllRetakes();

        assertNotNull(retakes);
        assertEquals(1, retakes.size());
        assertEquals("Pending", retakes.get(0).getStatus());
    }

    @Test
    public void testCreateRetake() {
        when(retakeRepository.save(any(Retake.class))).thenReturn(retake);

        Retake createdRetake = retakeService.createRetake(retake);

        assertNotNull(createdRetake);
        assertEquals("Pending", createdRetake.getStatus());
    }

    @Test
    public void testDeleteRetake() {
        doNothing().when(retakeRepository).deleteById(1);

        retakeService.deleteRetake(1);

        verify(retakeRepository, times(1)).deleteById(1);
    }
}
