package hexlet.code.component;

import hexlet.code.dto.LabelCreateDTO;
import hexlet.code.dto.TaskStatusCreateDTO;
import hexlet.code.model.User;
import hexlet.code.repository.LabelRepository;
import hexlet.code.repository.TaskStatusRepository;
import hexlet.code.repository.UserRepository;
import hexlet.code.service.LabelService;
import hexlet.code.service.TaskStatusService;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final LabelRepository labelRepository;
    private final TaskStatusRepository statusRepository;
    private final TaskStatusService statusService;
    private final LabelService labelService;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        if (userRepository.findByEmail("hexlet@example.com").isEmpty()) {
            User user = new User();
            user.setEmail("hexlet@example.com");
            user.setFirstName("Admin");
            user.setLastName("Admin");
            user.setPasswordDigest(passwordEncoder.encode("qwerty"));
            userRepository.save(user);
        }

        Map<String, String> statuses = new HashMap<>(
                Map.of("draft", "Draft", "to_review", "ToRewiew",
                        "to_be_fixed", "ToBeFixed",
                        "to_publish", "ToPublish", "published", "Published")
        );

        TaskStatusCreateDTO statusData = new TaskStatusCreateDTO();
        for (Map.Entry<String, String> status : statuses.entrySet()) {
            if (statusRepository.findBySlug(status.getKey()).isEmpty()) {
                statusData.setSlug(status.getKey());
                statusData.setName(status.getValue());
                statusService.create(statusData);
            }
        }

        List<String> labels = new ArrayList<>(List.of("bug", "feature"));
        LabelCreateDTO labelData = new LabelCreateDTO();
        for (String label : labels) {
            if (labelRepository.findByName(label).isEmpty()) {
                labelData.setName(label);
                labelService.create(labelData);
            }
        }
    }

}
