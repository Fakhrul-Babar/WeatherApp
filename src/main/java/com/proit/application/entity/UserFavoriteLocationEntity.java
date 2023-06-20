package com.proit.application.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "USER_FAVORITE_LOCATION")
public class UserFavoriteLocationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "USER_ID")
    @ManyToOne
    private UserEntity user;

    private Long locationId;
    private String locationDetails;
    private Double latitude;
    private Double longitude;
}
