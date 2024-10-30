package com.gamebuddy.user.repository;

import com.gamebuddy.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, String> {

    User findByUserName(String userName);

    User findByUserId(String userId);

    boolean existsByUserId(String userId);

    boolean existsByUserName(String userName);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u WHERE " +
            "(u.age >= :minAge) AND " +
            "(u.age <= :maxAge) AND " +
            "(:genders IS NULL OR u.gender IN :genders)")
    List<User> findByCriteria(@Param("minAge") Integer minAge,
                              @Param("maxAge") Integer maxAge,
                              @Param("genders") List<String> genders);
}
