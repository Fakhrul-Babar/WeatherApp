package com.proit.application.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Daily {
    private List<String> time;
    private List<Integer> weathercode;

    @JsonProperty("temperature_2m_max")
    private List<Float> temperature2mMax;

    @JsonProperty("temperature_2m_min")
    private List<Float> temperature2mMin;

    private List<String> sunrise;
    private List<String> sunset;

    @JsonProperty("precipitation_probability_max")
    private List<Integer> precipitationProbabilityMax;

    @JsonProperty("windspeed_10m_max")
    private List<Float> windspeed10mMax;

    @JsonProperty("rain_sum")
    private List<Float> rainSum;
}
