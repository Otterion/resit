package ru.shers.resit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.shers.resit.entities.Retake;
import ru.shers.resit.entities.Student;
import ru.shers.resit.repository.RetakeRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class RetakeService {
    private final RetakeRepository retakeRepository;

    @Autowired
    public RetakeService(RetakeRepository retakeRepository) {
        this.retakeRepository = retakeRepository;
    }

    public List<Retake> getAllRetakes() {
        return retakeRepository.findAll();
    }

    public Optional<Retake> getRetakeById(int id) {
        return retakeRepository.findById(id);
    }

    public List<Retake> getRetakesForStudent(Long studentId) {
        Student student = new Student();
        student.setId(studentId);
        return retakeRepository.findByStudent(student);
    }

    public Retake createRetake(Retake retake) {
        retake.setRetakeDate(LocalDate.now());
        return retakeRepository.save(retake);
    }

    public void deleteRetake(Integer retakeId) {
        retakeRepository.deleteById(retakeId);
    }
}
