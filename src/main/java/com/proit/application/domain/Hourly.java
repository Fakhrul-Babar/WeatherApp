package com.proit.application.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Hourly {
    private List<LocalDateTime> time;

    @JsonProperty("temperature_2m")
    private List<Double> temperature2m;

    @JsonProperty("apparent_temperature")
    private List<Double> apparentTemperature;

    @JsonProperty("windspeed_10m")
    private List<Double> windspeed10m;

    @JsonProperty("weathercode")
    private List<Integer> weathercode;

    @JsonProperty("precipitation_probability")
    private List<Integer> precipitationProbability;

    private List<Double> rain;
}
