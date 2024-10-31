package com.gamebuddy.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer","handler","friendShips"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    @Id
    @Column(updatable = false, nullable = false)
    @EqualsAndHashCode.Include
    private String userId;

    @Column(unique = true)
    @EqualsAndHashCode.Include
    private String userName;

    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Gender gender;
    private int age;
    private String profilePhoto;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isPremium = false;

    private Set<LanguagePreference> preferredLanguages;
    private Float averageRating;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private Set<FriendShip> friendShips;
}
