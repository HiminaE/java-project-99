package hexlet.code.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class TaskCreateDTO {

    private Long id;
    private String title;
    private Integer index;
    private String content;
    private String status;
    private Long assigneeId;
    private List<Long> taskLabelIds;
    private LocalDate createdAt;

}
