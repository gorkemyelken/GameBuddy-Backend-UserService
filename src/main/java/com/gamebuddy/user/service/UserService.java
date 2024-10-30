package com.gamebuddy.user.service;

import com.gamebuddy.user.dto.UserCreateDTO;
import com.gamebuddy.user.dto.UserUpdateDTO;
import com.gamebuddy.user.dto.UserViewDTO;
import com.gamebuddy.user.dto.auth.RegisterResponse;
import com.gamebuddy.user.exception.results.*;
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

    public DataResult<RegisterResponse> registerUser(UserCreateDTO userCreateDTO) {
        String validationMessage = isValidUser(userCreateDTO);
        if (validationMessage != null) {
            return new ErrorDataResult<>(validationMessage);
        }
        User user = createUser(userCreateDTO);
        userRepository.save(user);
        RegisterResponse registerResponse = new RegisterResponse();
        registerResponse.setUserId(user.getUserId());
        return new SuccessDataResult<>(registerResponse, "User added successfully.");
    }

    public DataResult<UserViewDTO> findByUsername(String username) {
        if(!checkIfUserNameExists(username)){
            return new ErrorDataResult<>("User not found.");
        }else{
            User user = userRepository.findByUserName(username);
            UserViewDTO userViewDTO = modelMapper.map(user, UserViewDTO.class);
            return new SuccessDataResult<>(userViewDTO, "User found successfully.");
        }
    }

    public DataResult<UserViewDTO> updateUser(String userId, UserUpdateDTO userUpdateDTO) {
        if(!checkIfUserIdExists(userId)){
            return new ErrorDataResult<>("User not found.");
        }
        User user = userRepository.findByUserId(userId);

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

        if (userUpdateDTO.getProfilePhoto() != null && user.isPremium()) {
            user.setProfilePhoto(userUpdateDTO.getProfilePhoto());
        }

        if(userUpdateDTO.getPreferredLanguages() != null){
            user.setPreferredLanguages(userUpdateDTO.getPreferredLanguages());
        }

        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        UserViewDTO userViewDTO = modelMapper.map(user, UserViewDTO.class);
        return new SuccessDataResult<>(userViewDTO, "User updated successfully.");
    }

    public DataResult<UserViewDTO> getUserByUserId(String userId) {
        if(!checkIfUserIdExists(userId)){
            return new ErrorDataResult<>("User not found.");
        }
        User user = userRepository.findByUserId(userId);
        UserViewDTO userViewDTO = modelMapper.map(user, UserViewDTO.class);
        return new SuccessDataResult<>(userViewDTO, "User found successfully.");
    }

    public DataResult<List<UserViewDTO>> getAllUsers() {
        List<User> users = userRepository.findAll();
        if(users.isEmpty()){
            return new ErrorDataResult<>("Users not found.");
        }
        List<UserViewDTO> userViewDTOs = users.stream()
                .map(user -> modelMapper.map(user, UserViewDTO.class))
                .collect(Collectors.toList());
        return new SuccessDataResult<>(userViewDTOs, "All users retrieved successfully.");
    }

    public Result deleteUser(String userId) {
        if(!checkIfUserIdExists(userId)){
            return new ErrorResult("User not found.");
        }
        userRepository.deleteById(userId);
        return new SuccessResult("User deleted " + userId);
    }

    public DataResult<UserViewDTO> makeUserPremium(String userId) {
        if(!checkIfUserIdExists(userId)){
            return new ErrorDataResult<>("User not found.");
        }
        User user = userRepository.findByUserId(userId);
        user.setPremium(true);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        UserViewDTO userViewDTO = modelMapper.map(user, UserViewDTO.class);
        return new SuccessDataResult<>(userViewDTO, "User has been made premium.");
    }

    public DataResult<List<UserViewDTO>> getUsersByCriteria(Integer minAge, Integer maxAge, List<String> genders) {
        int effectiveMinAge = (minAge != null) ? minAge : 0;
        int effectiveMaxAge = (maxAge != null) ? maxAge : 100;
        List<String> effectiveGenders = (genders != null && !genders.isEmpty()) ? genders : List.of("MALE", "FEMALE", "OTHER");

        List<User> filteredUsers = userRepository.findByCriteria(effectiveMinAge, effectiveMaxAge, effectiveGenders);
        if(filteredUsers.isEmpty()){
            return new ErrorDataResult<>("Users not found.");
        }

        List<UserViewDTO> userViewDTOs = filteredUsers.stream()
                .map(user -> modelMapper.map(user, UserViewDTO.class))
                .collect(Collectors.toList());

        return new SuccessDataResult<>(userViewDTOs, "Filtered users retrieved successfully.");
    }

    public Result matchPassword(String userName, String password) {
        if(!checkIfUserNameExists(userName)){
            return new ErrorResult("User not found.");
        }
        User user = userRepository.findByUserName(userName);

        boolean matches = passwordEncoder.matches(password, user.getPassword());
        if(matches){
            return new SuccessResult();
        }else{
            return new ErrorResult();
        }
    }

    private String isValidUser(UserCreateDTO userCreateDTO) {
        if (checkIfUserNameExists(userCreateDTO.getUserName())) {
            return "UserName already exists.";
        }

        if (checkIfEmailExists(userCreateDTO.getEmail())) {
            return "Email already exists.";
        }

        return null;
    }

    private User createUser(UserCreateDTO userCreateDTO) {
        User user = modelMapper.map(userCreateDTO, User.class);
        user.setUserId(UUID.randomUUID().toString());
        user.setPassword(userCreateDTO.getPassword());
        user.setPremium(false);
        user.setProfilePhoto(null);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(null);
        user.setPreferredLanguages(null);
        return user;
    }

    private boolean checkIfUserNameExists(String userName) {
        return this.userRepository.existsByUserName(userName);
    }

    private boolean checkIfEmailExists(String email) {
        return this.userRepository.existsByEmail(email);
    }

    private boolean checkIfUserIdExists(String userId) {
        return this.userRepository.existsByUserId(userId);
    }
}

