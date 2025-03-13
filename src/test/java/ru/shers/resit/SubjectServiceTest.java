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
import ru.shers.resit.entities.Subject;
import ru.shers.resit.repository.SubjectRepository;
import ru.shers.resit.service.SubjectService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class SubjectServiceTest {

    @Mock
    private SubjectRepository subjectRepository;

    @InjectMocks
    private SubjectService subjectService;

    private Subject subject;

    @BeforeEach
    public void setup() {
        subject = new Subject();
        subject.setId(1L);
        subject.setName("Mathematics");
    }

    @Test
    public void testGetAllSubjects() {
        // Подготовка данных
        when(subjectRepository.findAll()).thenReturn(Collections.singletonList(subject));

        // Вызов метода
        List<Subject> subjects = subjectService.getAllSubjects();

        // Проверка результата
        assertNotNull(subjects);
        assertEquals(1, subjects.size());
        assertEquals("Mathematics", subjects.get(0).getName());
    }

    @Test
    public void testCreateSubject() {
        // Подготовка данных
        when(subjectRepository.save(any(Subject.class))).thenReturn(subject);

        // Вызов метода
        Subject createdSubject = subjectService.createSubject(subject);

        // Проверка результата
        assertNotNull(createdSubject);
        assertEquals("Mathematics", createdSubject.getName());
    }

    @Test
    public void testDeleteSubject() {
        // Подготовка данных
        doNothing().when(subjectRepository).deleteById(1L);

        // Вызов метода
        subjectService.deleteSubject(1L);

        // Проверка, что метод был вызван
        verify(subjectRepository, times(1)).deleteById(1L);
    }
}
