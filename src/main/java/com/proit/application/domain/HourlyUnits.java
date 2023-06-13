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
public class HourlyUnits {
    private String time;

    @JsonProperty("temperature_2m")
    private String temperature2m;

    @JsonProperty("apparent_temperature")
    private String apparentTemperature;

    @JsonProperty("windspeed_10m")
    private String windspeed10m;

    private String weathercode;

    @JsonProperty("precipitation_probability")
    private String precipitationProbability;

    private String rain;
}
