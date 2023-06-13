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
public class DailyUnits {
    private String time;
    private String weathercode;

    @JsonProperty("temperature_2m_max")
    private String temperature2mMax;

    @JsonProperty("temperature_2m_min")
    private String temperature2mMin;

    private String sunrise;
    private String sunset;

    @JsonProperty("precipitation_probability_max")
    private String precipitationProbabilityMax;

    @JsonProperty("windspeed_10m_max")
    private String windspeed10mMax;

    @JsonProperty("rain_sum")
    private String rainSum;
}
