package ru.shers.resit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.shers.resit.entities.Retake;
import ru.shers.resit.service.RetakeService;

import java.util.List;

@RestController
@RequestMapping("/api/retakes")
public class RetakeController {
    private final RetakeService retakeService;

    @Autowired
    public RetakeController(RetakeService retakeService) {
        this.retakeService = retakeService;
    }

    @GetMapping
    public List<Retake> getAllRetakes() {
        return retakeService.getAllRetakes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Retake> getRetake(@PathVariable int id) {
        return retakeService.getRetakeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Retake>> getRetakesForStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(retakeService.getRetakesForStudent(studentId));
    }

    @PostMapping
    public ResponseEntity<Retake> createRetake(@RequestBody Retake retake) {
        return ResponseEntity.status(HttpStatus.CREATED).body(retakeService.createRetake(retake));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRetake(@PathVariable Integer id) {
        retakeService.deleteRetake(id);
        return ResponseEntity.noContent().build();
    }
}
