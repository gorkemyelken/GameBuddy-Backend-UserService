package com.gamebuddy.user.service;

import com.gamebuddy.user.dto.UserCreateDTO;
import com.gamebuddy.user.dto.UserUpdateDTO;
import com.gamebuddy.user.dto.UserViewDTO;
import com.gamebuddy.user.dto.auth.LoginResponse;
import com.gamebuddy.user.exception.UserNotFoundException;
import com.gamebuddy.user.model.User;
import com.gamebuddy.user.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserViewDTO registerUser(UserCreateDTO userCreateDTO) {
        User user = modelMapper.map(userCreateDTO, User.class);
        user.setId(UUID.randomUUID().toString());
        user.setPassword(userCreateDTO.getPassword());
        user.setPremium(false);
        user.setProfilePhoto(null);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(null);
        userRepository.save(user);
        return modelMapper.map(user, UserViewDTO.class);
    }

    public UserViewDTO findByUsername(String username) {
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));



        return modelMapper.map(user, UserViewDTO.class);
    }

    public UserViewDTO updateUser(String userId, UserUpdateDTO userUpdateDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        if (userUpdateDTO.getUserName() != null) {
            user.setUserName(userUpdateDTO.getUserName());
        }

        if (userUpdateDTO.getEmail() != null) {
            user.setEmail(userUpdateDTO.getEmail());
        }

        if (userUpdateDTO.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userUpdateDTO.getPassword()));
        }

        if (userUpdateDTO.getGender() != null) {
            user.setGender(userUpdateDTO.getGender());
        }

        if (userUpdateDTO.getAge() != null) {
            user.setAge(userUpdateDTO.getAge());
        }

        if (userUpdateDTO.getProfilePhoto() != null) {
            user.setProfilePhoto(userUpdateDTO.getProfilePhoto());
        }

        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);
        return modelMapper.map(user, UserViewDTO.class);
    }

    public UserViewDTO getUserById(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        return modelMapper.map(user, UserViewDTO.class);
    }

    public List<UserViewDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> modelMapper.map(user, UserViewDTO.class))
                .collect(Collectors.toList());
    }

    public void deleteUser(String userId) {
        if (!userRepository.existsById(userId)) {
            throw new UserNotFoundException("User not found with id: " + userId);
        }
        userRepository.deleteById(userId);
    }

    public UserViewDTO makeUserPremium(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        user.setPremium(true);
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);
        return modelMapper.map(user, UserViewDTO.class);
    }

    public List<UserViewDTO> getUsersByCriteria(Integer minAge, Integer maxAge, List<String> genders) {
        int effectiveMinAge = (minAge != null) ? minAge : 0;
        int effectiveMaxAge = (maxAge != null) ? maxAge : 100;
        List<String> effectiveGenders = (genders != null && !genders.isEmpty()) ? genders : List.of("MALE", "FEMALE", "OTHER");

        List<User> filteredUsers = userRepository.findByCriteria(effectiveMinAge, effectiveMaxAge, effectiveGenders);

        return filteredUsers.stream()
                .map(user -> modelMapper.map(user, UserViewDTO.class))
                .collect(Collectors.toList());
    }

    public Boolean matchPassword(String username, String password) {
        User user = userRepository.findByUserName(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));

        boolean matches = passwordEncoder.matches(password, user.getPassword());
        return matches;
    }
}

