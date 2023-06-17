package com.proit.application.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class DailyWeatherForecast {
    private LocalDate time;
    private Integer weathercode;
    private Float temperature2mMax;
    private Float temperature2mMin;
    private String temperatureUnit;
    private String sunrise;
    private String sunset;
    private Integer precipitationProbabilityMax;
    private Float windspeed10mMax;
    private String windspeedUnit;
    private Float rainSum;
    private String rainUnit;

    private CurrentWeather currentWeather;
}
