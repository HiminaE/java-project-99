package hexlet.code.controller;


import hexlet.code.dto.LabelCreateDTO;
import hexlet.code.dto.LabelDTO;
import hexlet.code.dto.LabelUpdateDTO;
import hexlet.code.service.LabelService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
@RestController
@AllArgsConstructor
@RequestMapping("/api/labels")
public class LabelController {

    private final LabelService labelService;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<LabelDTO>> index() {
        List<LabelDTO> labels = labelService.getAll();
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(labels.size()))
                .body(labels);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public LabelDTO create(@Valid @RequestBody LabelCreateDTO model) {
        return labelService.create(model);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LabelDTO show(@PathVariable Long id) {
        return labelService.getById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public LabelDTO update(@Valid @RequestBody LabelUpdateDTO userData, @PathVariable Long id) {
        return labelService.update(id, userData);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        labelService.delete(id);
    }
}
