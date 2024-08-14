package hexlet.code.service;

import hexlet.code.dto.TaskStatusCreateDTO;
import hexlet.code.dto.TaskStatusDTO;
import hexlet.code.dto.TaskStatusUpdateDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.TaskStatusMapper;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.TaskStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskStatusService {


    private final TaskStatusRepository taskStatusRepository;

    private final TaskStatusMapper taskStatusMapper;

    public List<TaskStatusDTO> getAll() {
        List<TaskStatus> statuses = taskStatusRepository.findAll();
        return statuses.stream()
                .map(t -> taskStatusMapper.map(t))
                .toList();
    }

    public TaskStatusDTO getById(Long id) {
        TaskStatus status = taskStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task status with id " + id + "not found"));
        return taskStatusMapper.map(status);
    }

    public TaskStatusDTO create(TaskStatusCreateDTO data) {
        TaskStatus newStatus = taskStatusMapper.map(data);
        taskStatusRepository.save(newStatus);
        return taskStatusMapper.map(newStatus);
    }

    public TaskStatusDTO update(Long id, TaskStatusUpdateDTO data) {
        TaskStatus status = taskStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task status with id " + id + "not found"));
        taskStatusMapper.update(data, status);
        taskStatusRepository.save(status);
        return taskStatusMapper.map(status);
    }

    public void delete(Long id) {
        TaskStatus status = taskStatusRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task status with id " + id + "not found"));

        taskStatusRepository.deleteById(id);
    }
}
