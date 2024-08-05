package hexlet.code.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.TaskCreateDTO;
import hexlet.code.model.Label;
import hexlet.code.model.Task;
import hexlet.code.model.TaskStatus;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.util.Generator;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskStatusRepository statusRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private Generator generator;

    private Task testTask;

    private TaskStatus testStatus;

    private Label testLabel;

    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token;

    @BeforeEach
    public void setUp() {
        testTask = Instancio.of(generator.getTaskModel())
                .create();

        User testUser = Instancio.of(generator.getUserModel())
                .create();
        userRepository.save(testUser);

        testStatus = Instancio.of(generator.getStatusModel())
                .create();
        statusRepository.save(testStatus);

        testLabel = Instancio.of(generator.getLabelModel())
                .create();
        labelRepository.save(testLabel);

        testTask.setTaskStatus(testStatus);
        testTask.setAssignee(testUser);
        testTask.setLabels(new ArrayList<>(List.of(testLabel)));
        taskRepository.save(testTask);

        token = jwt().jwt(builder -> builder.subject("hexlet@example.com"));
    }

    @Test
    public void testShow() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/tasks/" + testTask.getId()).with(token))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println(body);
        assertThatJson(body).and(
                a -> a.node("title").isEqualTo(testTask.getName()),
                a -> a.node("index").isEqualTo(testTask.getIndex()),
                a -> a.node("content").isEqualTo(testTask.getDescription()),
                a -> a.node("status").isEqualTo(testTask.getTaskStatus().getSlug()),
                a -> a.node("assigneeId").isEqualTo(testTask.getAssignee().getId())//,
                //a -> a.node("taskLabelIds").isArray()
        );
    }

    @Test
    public void testIndex() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/tasks").with(token)
                        .param("titleCont", testTask.getName())
                        .param("assigneeId", String.valueOf(testTask.getAssignee().getId()))
                        .param("status", testStatus.getSlug())
                        .param("labelId", String.valueOf(testLabel.getId())))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void testCreate() throws Exception {
        var taskStatus = statusRepository.findBySlug("draft").get();
        var label = labelRepository.findByName("feature").get();
        var data = new TaskCreateDTO();
        String name = "New Task Name";
        data.setTitle(name);
        data.setStatus(taskStatus.getSlug());
        data.setTaskLabelIds(List.of(label.getId()));

        mockMvc.perform(post("/api/tasks")
                        .content(om.writeValueAsString(data))
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(token))
                .andExpect(status()
                        .isCreated());
    }

    @Test
    public void testUpdate() throws Exception {
        Map<String, String> data = new HashMap<>(Map.of("title", "newTitle"));

        MockHttpServletRequestBuilder request = put("/api/tasks/" + testTask.getId()).with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(data));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        Task updatedTask = taskRepository.findById(testTask.getId()).orElse(null);
        assertThat(updatedTask).isNotNull();
        assertThat(updatedTask.getName()).isEqualTo("newTitle");
        assertThat(updatedTask.getIndex()).isEqualTo(testTask.getIndex());
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete("/api/tasks/" + testTask.getId()).with(token))
                .andExpect(status().isNoContent());

        Task destroyedTask = taskRepository.findById(testTask.getId()).orElse(null);
        assertThat(destroyedTask).isNull();
    }

}