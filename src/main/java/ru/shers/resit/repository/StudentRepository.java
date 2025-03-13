package ru.shers.resit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.shers.resit.entities.Student;
import ru.shers.resit.entities.User;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
}
