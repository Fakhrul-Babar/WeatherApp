package com.proit.application.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Location {
    private Long id;
    private String name;
    private Double latitude;
    private Double longitude;
    private Double elevation;

    @JsonProperty("feature_code")
    private String featureCode;

    @JsonProperty("country_code")
    private String countryCode;

    private String timezone;

    @JsonProperty("country_id")
    private Long countryId;
    private String country;

    private String admin1;
    private String admin2;

    public String getAdminDetails() {
        return new StringBuilder(getAdmin2() != null ? getAdmin2().concat(", ") : "").append(getAdmin1()).toString();
    }
}