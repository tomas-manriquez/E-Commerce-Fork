package com.example.ecommerce.controllers;

import com.example.ecommerce.entities.OpinionEntity;
import com.example.ecommerce.services.OpinionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/opiniones")
public class OpinionController {

    @Autowired
    private OpinionService opinionService;

    @GetMapping
    public List<OpinionEntity> getAllOpinions() {
        return opinionService.getAllOpinions();
    }

    @GetMapping("/{id}")
    public Optional<OpinionEntity> getOpinionById(@PathVariable String id) {
        return opinionService.getOpinionById(id);
    }

    @PostMapping("/create")
    public OpinionEntity createOpinion(@RequestBody OpinionEntity opinion) {
        return opinionService.createOpinion(opinion);
    }

    @PutMapping("/{id}")
    public OpinionEntity updateOpinion(@PathVariable String id, @RequestBody OpinionEntity opinion) {
        return opinionService.updateOpinion(id, opinion);
    }

    @DeleteMapping("/{id}")
    public void deleteOpinion(@PathVariable String id) {
        opinionService.deleteOpinion(id);
    }
}