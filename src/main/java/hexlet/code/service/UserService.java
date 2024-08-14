package hexlet.code.service;

import hexlet.code.dto.UserCreateDTO;
import hexlet.code.dto.UserUpdateDTO;
import hexlet.code.dto.UserDTO;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.exception.AccessViolationException;
import hexlet.code.mapper.UserMapper;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
import hexlet.code.util.UserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder encoder;
    private final UserUtils userUtils;

    public List<UserDTO> getAll() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::map)
                .toList();
    }

    public UserDTO getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
        return userMapper.map(user);
    }

    public UserDTO create(UserCreateDTO data) {
        User newUser = userMapper.map(data);
        String encodedPassword = encoder.encode(data.getPassword());
        newUser.setPasswordDigest(encodedPassword);
        userRepository.save(newUser);
        return userMapper.map(newUser);
    }

    public UserDTO update(Long id, UserUpdateDTO data) {
        User user = checkUserAccess(id);
        userMapper.update(data, user);
        if (data.getPassword() != null) {
            String encodedPassword = encoder.encode(data.getPassword().get());
            user.setPasswordDigest(encodedPassword);
        }
        userRepository.save(user);
        return userMapper.map(user);
    }

    public void delete(Long id) {
        checkUserAccess(id);
        userRepository.deleteById(id);
    }

    private User checkUserAccess(Long id) {
        var currentUser = userUtils.getCurrentUser();
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
        if (!currentUser.equals(user)) {
            throw new AccessViolationException("You have no access to modify other users");
        }
        return user;
    }
}
