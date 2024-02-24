package com.galaxy.backend.controllers;

import com.galaxy.backend.dtos.SeguradoDTO;
import com.galaxy.backend.dtos.SeguradoPageDTO;
import com.galaxy.backend.models.Segurado;
import com.galaxy.backend.services.SeguradoService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping( "api/segurados")
@CrossOrigin(origins = "http://localhost:4200")
public class SeguradoController {

    private final SeguradoService seguradoService;

    public SeguradoController(SeguradoService seguradoService) {
        this.seguradoService = seguradoService;
    }

    @PostMapping
    public ResponseEntity<SeguradoDTO> create(@RequestBody SeguradoDTO data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(seguradoService.save(data));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Segurado>> getAll() {
        return ResponseEntity.ok(seguradoService.findAll());
    }

    @GetMapping
    public ResponseEntity<SeguradoPageDTO> list(@RequestParam(defaultValue = "0") @PositiveOrZero int pageNumber, @RequestParam(defaultValue = "10") @Positive @Max(100) int pageSize) {
        return ResponseEntity.ok(seguradoService.listAll(pageNumber, pageSize));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeguradoDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(seguradoService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SeguradoDTO> update(@PathVariable Long id, @RequestBody SeguradoDTO data) {
        return ResponseEntity.ok().body(seguradoService.update(id, data));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return seguradoService.delete(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
