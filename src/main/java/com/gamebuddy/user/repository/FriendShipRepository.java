package com.gamebuddy.user.repository;

import com.gamebuddy.user.model.FriendShip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface FriendShipRepository extends JpaRepository<FriendShip, String> {
    Set<FriendShip> findByUser_UserId(String userId);
}
