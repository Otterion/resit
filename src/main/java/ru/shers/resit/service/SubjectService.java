package ru.shers.resit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.shers.resit.entities.Subject;
import ru.shers.resit.repository.SubjectRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SubjectService {
    private final SubjectRepository subjectRepository;

    @Autowired
    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    public Optional<Subject> getSubjectByName(String name) {
        return subjectRepository.findByName(name);
    }

    public Subject createSubject(Subject subject) {
        return subjectRepository.save(subject);
    }

    public void deleteSubject(Long subjectId) {
        subjectRepository.deleteById(subjectId);
    }
}
