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
import ru.shers.resit.entities.Teacher;
import ru.shers.resit.repository.TeacherRepository;
import ru.shers.resit.service.TeacherService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    private Teacher teacher;

    @BeforeEach
    public void setup() {
        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
    }

    @Test
    public void testGetAllTeachers() {
        // Подготовка данных
        when(teacherRepository.findAll()).thenReturn(Collections.singletonList(teacher));

        // Вызов метода
        List<Teacher> teachers = teacherService.getAllTeachers();

        // Проверка результата
        assertNotNull(teachers);
        assertEquals(1, teachers.size());
        assertEquals("John", teachers.get(0).getFirstName());
    }

    @Test
    public void testGetTeacherByIdSuccess() {
        // Подготовка данных
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));

        // Вызов метода
        Optional<Teacher> foundTeacher = teacherService.getTeacherById(1L);

        // Проверка результата
        assertTrue(foundTeacher.isPresent());
        assertEquals("John", foundTeacher.get().getFirstName());
    }

    @Test
    public void testGetTeacherByIdNotFound() {
        // Подготовка данных
        when(teacherRepository.findById(1L)).thenReturn(Optional.empty());

        // Вызов метода
        Optional<Teacher> foundTeacher = teacherService.getTeacherById(1L);

        // Проверка результата
        assertFalse(foundTeacher.isPresent());
    }

    @Test
    public void testSaveTeacher() {
        // Подготовка данных
        when(teacherRepository.save(any(Teacher.class))).thenReturn(teacher);

        // Вызов метода
        Teacher savedTeacher = teacherService.saveTeacher(teacher);

        // Проверка результата
        assertNotNull(savedTeacher);
        assertEquals("John", savedTeacher.getFirstName());
    }

    @Test
    public void testDeleteTeacher() {
        // Подготовка данных
        doNothing().when(teacherRepository).deleteById(1L);

        // Вызов метода
        teacherService.deleteTeacher(1L);

        // Проверка, что метод был вызван
        verify(teacherRepository, times(1)).deleteById(1L);
    }
}
