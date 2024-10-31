package com.gamebuddy.user.service;

import com.gamebuddy.user.dto.FriendShipViewDTO;
import com.gamebuddy.user.exception.results.DataResult;
import com.gamebuddy.user.exception.results.ErrorDataResult;
import com.gamebuddy.user.exception.results.SuccessDataResult;
import com.gamebuddy.user.model.FriendShip;
import com.gamebuddy.user.repository.FriendShipRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FriendShipService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FriendShipService.class);
    private FriendShipRepository friendShipRepository;

    private final ModelMapper modelMapper;

    public FriendShipService(FriendShipRepository friendShipRepository, ModelMapper modelMapper) {
        this.friendShipRepository = friendShipRepository;
        this.modelMapper = modelMapper;
    }

    public DataResult<List<FriendShipViewDTO>> getAllFriendShips() {
        LOGGER.info("[getAllFriendShips]");
        List<FriendShip> friendShips = friendShipRepository.findAll();
        if(friendShips.isEmpty()){
            return new ErrorDataResult<>("FriendShips not found.");
        }
        List<FriendShipViewDTO> friendShipViewDTOS = friendShips.stream()
                .map(friendShip -> modelMapper.map(friendShip, FriendShipViewDTO.class))
                .collect(Collectors.toList());
        return new SuccessDataResult<>(friendShipViewDTOS, "All friendShips retrieved successfully.");
    }
}
