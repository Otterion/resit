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
import ru.shers.resit.entities.Student;
import ru.shers.resit.repository.StudentRepository;
import ru.shers.resit.service.StudentService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    private Student student;

    @BeforeEach
    public void setup() {
        student = new Student();
        student.setId(1L);
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setGroupName("Group A");
    }

    @Test
    public void testGetAllStudents() {
        // Подготовка данных
        when(studentRepository.findAll()).thenReturn(Collections.singletonList(student));

        // Вызов метода
        List<Student> students = studentService.getAllStudents();

        // Проверка результата
        assertNotNull(students);
        assertEquals(1, students.size());
        assertEquals("John", students.get(0).getFirstName());
    }

    @Test
    public void testGetStudentByIdSuccess() {
        // Подготовка данных
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        // Вызов метода
        Optional<Student> foundStudent = studentService.getStudentById(1L);

        // Проверка результата
        assertTrue(foundStudent.isPresent());
        assertEquals("John", foundStudent.get().getFirstName());
    }

    @Test
    public void testGetStudentByIdNotFound() {
        // Подготовка данных
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        // Вызов метода
        Optional<Student> foundStudent = studentService.getStudentById(1L);

        // Проверка результата
        assertFalse(foundStudent.isPresent());
    }

    @Test
    public void testCreateStudent() {
        // Подготовка данных
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        // Вызов метода
        Student createdStudent = studentService.createStudent(student);

        // Проверка результата
        assertNotNull(createdStudent);
        assertEquals("John", createdStudent.getFirstName());
    }

    @Test
    public void testDeleteStudent() {
        // Подготовка данных
        doNothing().when(studentRepository).deleteById(1L);

        // Вызов метода
        studentService.deleteStudent(1L);

        // Проверка, что метод был вызван
        verify(studentRepository, times(1)).deleteById(1L);
    }
}