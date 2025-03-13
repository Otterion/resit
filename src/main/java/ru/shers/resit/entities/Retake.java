package ru.shers.resit.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Data
@Table(name = "retake")
public class Retake {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @Column(name = "retake_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate retakeDate;

    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
}
