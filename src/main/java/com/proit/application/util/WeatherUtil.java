package com.proit.application.util;

import java.util.HashMap;
import java.util.Map;

public class WeatherUtil {

    private static final Map<Integer, String> weatherCodeToMessageMap;

    static {
        weatherCodeToMessageMap = mapWeatherCodeToMessage();
    }

    private static Map<Integer, String> mapWeatherCodeToMessage() {

        HashMap<Integer, String> map = new HashMap<>();
        map.put(0, "Clear Sky");
        map.put(1, "Mainly Clear");
        map.put(2, "Partly Cloudy");
        map.put(3, "Overcast");

        map.put(45, "Fog");
        map.put(48, "Depositing Rime Fog");

        map.put(51, "Light Drizzle");
        map.put(53, "Moderate Drizzle");
        map.put(55, "Dense Drizzle");
        map.put(56, "Light Freezing Drizzle");
        map.put(57, "Dense Freezing Drizzle");

        map.put(61, "Slight Rain");
        map.put(63, "Moderate Rain");
        map.put(65, "Heavy Rain");
        map.put(66, "Light Freezing Rain");
        map.put(67, "Heavy Freezing Rain");

        map.put(71, "Slight Snowfall");
        map.put(73, "Moderate Snowfall");
        map.put(75, "Heavy Snowfall");
        map.put(77, "Snow Grains");

        map.put(80, "Slight Rain Showers");
        map.put(81, "Moderate Rain Showers");
        map.put(82, "Violent Rain Showers");

        map.put(85, "Slight Snow Showers");
        map.put(86, "Heavy Snow Showers");

        map.put(95, "Slight or Moderate Thunderstorm");
        map.put(96, "Thunderstorm with Slight Hail");
        map.put(99, "Thunderstorm with Heavy Hail");

        return map;
    }


    public static String getWeatherMessage(int weatherCode) {
        return weatherCodeToMessageMap.get(weatherCode);
    }
}
