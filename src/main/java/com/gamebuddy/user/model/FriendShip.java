package com.gamebuddy.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Use only explicitly included fields
public class FriendShip {

    @Id
    @Column(updatable = false, nullable = false)
    @EqualsAndHashCode.Include // Include friendShipId in hashCode/equals
    private String friendShipId;

    @EqualsAndHashCode.Include // Include friendId in hashCode/equals
    private String friendId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    @JsonIgnore // Prevent serialization issues
    private User user;
}