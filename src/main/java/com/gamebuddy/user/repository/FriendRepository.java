package com.gamebuddy.user.repository;

import com.gamebuddy.user.model.FriendShip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendRepository extends JpaRepository<FriendShip, String> {
}
