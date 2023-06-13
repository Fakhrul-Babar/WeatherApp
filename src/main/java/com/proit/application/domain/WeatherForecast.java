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
public class WeatherForecast {
    private Float latitude;
    private Float longitude;

    @JsonProperty("generationtime_ms")
    private Float generationtimeMs;

    @JsonProperty("utc_offset_seconds")
    private Integer utcOffsetSeconds;
    private String timezone;

    @JsonProperty("timezone_abbreviation")
    private String timezoneAbbreviation;
    private Float elevation;

    @JsonProperty("hourly_units")
    private HourlyUnits hourlyUnits;
    private Hourly hourly;

    @JsonProperty("daily_units")
    private DailyUnits dailyUnits;
    private Daily daily;
}
