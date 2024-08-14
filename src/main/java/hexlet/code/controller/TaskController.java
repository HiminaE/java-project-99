package hexlet.code.controller;



import hexlet.code.dto.TaskCreateDTO;
import hexlet.code.dto.TaskDTO;
import hexlet.code.dto.TaskParamDTO;
import hexlet.code.dto.TaskUpdateDTO;
import hexlet.code.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<TaskDTO>> index(TaskParamDTO params) {
        var tasks = taskService.getAll(params);
        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(tasks.size()))
                .body(tasks);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDTO create(@Valid @RequestBody TaskCreateDTO model) {
        return taskService.create(model);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDTO show(@PathVariable Long id) {
        return taskService.getById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDTO update(@Valid @RequestBody TaskUpdateDTO userData, @PathVariable Long id) {
        return taskService.update(id, userData);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        taskService.delete(id);
    }
}
