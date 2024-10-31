package com.gamebuddy.user.repository;

import com.gamebuddy.user.model.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendRepository extends JpaRepository<Friend, String> {
}
