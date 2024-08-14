package hexlet.code.mapper;


import hexlet.code.dto.TaskCreateDTO;
import hexlet.code.dto.TaskDTO;
import hexlet.code.dto.TaskUpdateDTO;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.exception.ResourceNotFoundException;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;


@Mapper(
        uses = { JsonMapper.class, ReferenceMapper.class },
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class TaskMapper {

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private TaskStatusRepository statusRepository;

    @Mapping(target = "name", source = "title")
    @Mapping(target = "description", source = "content")
    @Mapping(source = "status", target = "taskStatus")
    @Mapping(source = "assigneeId", target = "assignee")
    @Mapping(source = "taskLabelIds", target = "labels")
    public abstract Task map(TaskCreateDTO taskCreateDTO);

    @Mapping(source = "name", target = "title")
    @Mapping(source = "description", target = "content")
    @Mapping(source = "taskStatus.slug", target = "status")
    @Mapping(source = "assignee.id", target = "assigneeId")
    @Mapping(source = "labels", target = "taskLabelIds")
    public abstract TaskDTO map(Task task);

    @Mapping(source = "title", target = "name")
    @Mapping(source = "content", target = "description")
    @Mapping(source = "status", target = "taskStatus.slug")
    @Mapping(source = "assigneeId", target = "assignee")
    @Mapping(source = "taskLabelIds", target = "labels")
    public abstract void update(TaskUpdateDTO dto, @MappingTarget Task task);


    public List<Long> toLabelIdList(List<Label> labels) {
        return labels.stream()
                .map(Label::getId)
                .toList();
    }

    public List<Label> toLabelList(List<Long> ids) {
        return new ArrayList<>(labelRepository.findAllById(ids));
    }

    public TaskStatus toTaskStatus(String statusSlug) {
        return statusRepository.findBySlug(statusSlug)
                .orElseThrow(() -> new ResourceNotFoundException("TaskStatus with slug " + statusSlug + " not found"));
    }

}
