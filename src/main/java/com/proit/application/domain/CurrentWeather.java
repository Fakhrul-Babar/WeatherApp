package com.proit.application.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CurrentWeather {
    private Double temperature;
    private Double windspeed;
    private Double winddirection;
    private Integer weathercode;

    @JsonProperty("is_day")
    private Integer isDay;
    private LocalDateTime time;
}
