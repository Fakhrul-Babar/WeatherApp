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
}