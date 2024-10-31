package com.gamebuddy.user.service;

import com.gamebuddy.user.dto.FriendCreateDTO;
import com.gamebuddy.user.dto.UserCreateDTO;
import com.gamebuddy.user.dto.UserUpdateDTO;
import com.gamebuddy.user.dto.UserViewDTO;
import com.gamebuddy.user.dto.auth.RegisterResponse;
import com.gamebuddy.user.exception.results.*;
import com.gamebuddy.user.model.FriendShip;
import com.gamebuddy.user.model.Gender;
import com.gamebuddy.user.model.User;
import com.gamebuddy.user.repository.FriendRepository;
import com.gamebuddy.user.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    private final FriendRepository friendRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, FriendRepository friendRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.friendRepository = friendRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public DataResult<RegisterResponse> registerUser(UserCreateDTO userCreateDTO) {
        LOGGER.info("[registerUser] UserCreateDTO: {}",userCreateDTO);
        String validationMessage = isValidUser(userCreateDTO);
        if (validationMessage != null) {
            return new ErrorDataResult<>(validationMessage);
        }
        LOGGER.info("[registerUser] User valid.");
        User user = createUser(userCreateDTO);
        userRepository.save(user);
        LOGGER.info("[registerUser] User added to database. User: {}", user);
        RegisterResponse registerResponse = new RegisterResponse();
        registerResponse.setUserId(user.getUserId());
        return new SuccessDataResult<>(registerResponse, "User added successfully.");
    }

    public Result addFriend(FriendCreateDTO friendCreateDTO) {
        LOGGER.info("[addFriend] FriendCreateDTO: {}",friendCreateDTO);
        User user = userRepository.findByUserId(friendCreateDTO.getUserId());
        LOGGER.info("[addFriend] User: {}",user);
        User friend = userRepository.findByUserId(friendCreateDTO.getFriendId());
        LOGGER.info("[addFriend] Friend: {}",friend);

        if (user == null || friend == null) {
            return new ErrorResult("User not found.");
        }

        createAndSaveFriendship(user, friendCreateDTO.getFriendId());
        createAndSaveFriendship(friend, friendCreateDTO.getUserId());

        return new SuccessResult("Friendship completed.");
    }

    public DataResult<List<UserViewDTO>> getFriends(String userId){
        LOGGER.info("[getFriends] UserId: {}",userId);
        List<FriendShip> friendShips = friendRepository.findByUserId(userId);

        List<User> friendList = new ArrayList<>();
        for (FriendShip friendShip : friendShips){
            User user = this.userRepository.findByUserId(friendShip.getFriendShipId());
            friendList.add(user);
        }
        List<UserViewDTO> userViewDTOs = friendList.stream()
                .map(user -> modelMapper.map(user, UserViewDTO.class))
                .collect(Collectors.toList());
        return new SuccessDataResult<>(userViewDTOs, "All friends retrieved successfully.");

    }

    private void createAndSaveFriendship(User user, String friendId) {
        FriendShip newFriendship = new FriendShip();
        newFriendship.setFriendShipId(UUID.randomUUID().toString());
        newFriendship.setUser(user);
        newFriendship.setFriendId(friendId);

        user.getFriendShips().add(newFriendship);
        friendRepository.save(newFriendship);
        userRepository.save(user);
    }

    public DataResult<UserViewDTO> findByUsername(String userName) {
        LOGGER.info("[findByUsername] UserName: {}",userName);
        if(!checkIfUserNameExists(userName)){
            return new ErrorDataResult<>("User not found.");
        }else{
            User user = userRepository.findByUserName(userName);
            UserViewDTO userViewDTO = modelMapper.map(user, UserViewDTO.class);
            return new SuccessDataResult<>(userViewDTO, "User found successfully.");
        }
    }

    public DataResult<UserViewDTO> updateUser(String userId, UserUpdateDTO userUpdateDTO) {
        LOGGER.info("[updateUser] UserId: {} , UserUpdateDTO: {}",userId,userUpdateDTO);
        if(!checkIfUserIdExists(userId)){
            return new ErrorDataResult<>("User not found.");
        }
        User user = userRepository.findByUserId(userId);

        if (userUpdateDTO.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(userUpdateDTO.getPassword()));
            LOGGER.info("[updateUser] Password is changed.");
        }

        if (userUpdateDTO.getGender() != null) {
            user.setGender(userUpdateDTO.getGender());
            LOGGER.info("[updateUser] Gender is changed. New gender is {}", userUpdateDTO.getGender());
            if(userUpdateDTO.getGender() == Gender.MALE){
                user.setProfilePhoto("https://yyamimarlik.s3.eu-north-1.amazonaws.com/Male+Avatar.png");
                LOGGER.info("[updateUser] Profile photo is changed. MALE");
            }
            if(userUpdateDTO.getGender() == Gender.FEMALE){
                user.setProfilePhoto("https://yyamimarlik.s3.eu-north-1.amazonaws.com/Female+Avatar.png");
                LOGGER.info("[updateUser] Profile photo is changed. FEMALE");
            }
        }

        if (userUpdateDTO.getAge() != null) {
            user.setAge(userUpdateDTO.getAge());
            LOGGER.info("[updateUser] Age is changed. New age is {}", userUpdateDTO.getAge());
        }

        if (userUpdateDTO.getProfilePhoto() != null && user.isPremium()) {
            user.setProfilePhoto(userUpdateDTO.getProfilePhoto());
            LOGGER.info("[updateUser] ProfilePhoto is changed. New profilePhoto is {}", userUpdateDTO.getProfilePhoto());
        }

        if(userUpdateDTO.getPreferredLanguages() != null){
            user.setPreferredLanguages(userUpdateDTO.getPreferredLanguages());
            LOGGER.info("[updateUser] PreferredLanguages is changed. New preferredLanguages is {}", userUpdateDTO.getPreferredLanguages());
        }

        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        LOGGER.info("[updateUser] User added to database. User: {}", user);
        UserViewDTO userViewDTO = modelMapper.map(user, UserViewDTO.class);
        return new SuccessDataResult<>(userViewDTO, "User updated successfully.");
    }

    public DataResult<UserViewDTO> getUserByUserId(String userId) {
        LOGGER.info("[getUserByUserId] UserId: {}",userId);
        if(!checkIfUserIdExists(userId)){
            return new ErrorDataResult<>("User not found.");
        }
        User user = userRepository.findByUserId(userId);
        UserViewDTO userViewDTO = modelMapper.map(user, UserViewDTO.class);
        return new SuccessDataResult<>(userViewDTO, "User found successfully.");
    }

    public DataResult<List<UserViewDTO>> getAllUsers() {
        LOGGER.info("[getAllUsers]");
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
        LOGGER.info("[deleteUser] UserId: {}",userId);
        if(!checkIfUserIdExists(userId)){
            return new ErrorResult("User not found.");
        }
        userRepository.deleteById(userId);
        return new SuccessResult("User deleted " + userId);
    }

    public DataResult<UserViewDTO> makeUserPremium(String userId) {
        LOGGER.info("[makeUserPremium] UserId: {}",userId);
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

    public DataResult<List<UserViewDTO>> getUsersByCriteria(Integer minAge, Integer maxAge, Float minRating, Float maxRating, List<String> genders) {
        int effectiveMinAge = (minAge != null) ? minAge : 0;
        int effectiveMaxAge = (maxAge != null) ? maxAge : 100;
        float effectiveMinRating = (minRating != null) ? minRating : 0;
        float effectiveMaxRating = (maxRating != null) ? maxRating : 5;
        List<String> effectiveGenders = (genders != null && !genders.isEmpty()) ? genders : List.of("MALE", "FEMALE", "OTHER");

        List<User> filteredUsers = userRepository.findByCriteria(effectiveMinAge, effectiveMaxAge, effectiveMinRating, effectiveMaxRating, effectiveGenders);
        if(filteredUsers.isEmpty()){
            return new ErrorDataResult<>("Users not found.");
        }

        List<UserViewDTO> userViewDTOs = filteredUsers.stream()
                .map(user -> modelMapper.map(user, UserViewDTO.class))
                .collect(Collectors.toList());

        return new SuccessDataResult<>(userViewDTOs, "Filtered users retrieved successfully.");
    }

    public Result matchPassword(String userName, String password) {
        LOGGER.info("[matchPassword] UserName: {}",userName);
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
        if(!checkIfEmailValid(userCreateDTO.getEmail())){
            return "Email not valid.";
        }

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
        user.setProfilePhoto("https://yyamimarlik.s3.eu-north-1.amazonaws.com/gamebuddy-logo.png");
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

    private boolean checkIfEmailValid(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email != null && email.matches(emailRegex);
    }

}

