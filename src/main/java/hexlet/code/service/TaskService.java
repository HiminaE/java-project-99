package hexlet.code.service;



import hexlet.code.dto.TaskCreateDTO;
import hexlet.code.dto.TaskDTO;
import hexlet.code.dto.TaskParamDTO;
import hexlet.code.dto.TaskUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskMapper;
import hexlet.code.model.Task;
import hexlet.code.repository.TaskRepository;
import hexlet.code.specification.TaskSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;

    private final TaskMapper taskMapper;

    private final TaskSpecification taskSpecification;

    public List<TaskDTO> getAll(TaskParamDTO params) {
        Specification<Task> spec = taskSpecification.build(params);
        List<Task> tasks = taskRepository.findAll(spec);
        return tasks.stream()
                .map(taskMapper::map)
                .toList();
    }

    public TaskDTO getById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + "not found"));
        return taskMapper.map(task);
    }

    public TaskDTO create(TaskCreateDTO data) {
        Task newStatus = taskMapper.map(data);
        taskRepository.save(newStatus);
        return taskMapper.map(newStatus);
    }

    public TaskDTO update(Long id, TaskUpdateDTO data) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + "not found"));
        taskMapper.update(data, task);
        taskRepository.save(task);
        return taskMapper.map(task);
    }

    public void delete(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task with id " + id + "not found"));

        taskRepository.deleteById(id);
    }
}
