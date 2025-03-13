package ru.shers.resit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.shers.resit.entities.Retake;
import ru.shers.resit.entities.Student;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RetakeRepository extends JpaRepository<Retake, Integer> {
    List<Retake> findByStudent(Student student);
}
