package com.proit.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proit.application.domain.Location;
import com.proit.application.domain.WeatherForecast;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class WeatherAppClient {
    private final String LOCATION_API = "https://geocoding-api.open-meteo.com/v1/search?name=%s&count=50&language=en&format=json";
    private final String WEATHER_FORECAST_API = "https://api.open-meteo.com/v1/forecast?" +
            "latitude=%s&longitude=%s" +
            "&hourly=temperature_2m,apparent_temperature,windspeed_10m,weathercode,precipitation_probability,rain" +
            "&daily=weathercode,temperature_2m_max,temperature_2m_min,sunrise,sunset,precipitation_probability_max,windspeed_10m_max,rain_sum" +
            "&timezone=auto";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public WeatherAppClient(
            RestTemplate restTemplate,
            ObjectMapper objectMapper
    ) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public List<Location> getLocations(String cityName) {
        String url = String.format(LOCATION_API, cityName);
        List<Location> locations = new ArrayList<>();

        try {
            ResponseEntity<Map> responseEntity = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), Map.class);
            if (!responseEntity.getBody().isEmpty()) {
                List<LinkedHashMap> results = (List<LinkedHashMap>) responseEntity.getBody().get("results");

                for (LinkedHashMap result : results) {
                    Location location = objectMapper.convertValue(result, Location.class);
                    locations.add(location);
                }
            }
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }

        return locations;
    }

    public WeatherForecast getWeatherForecastData(double latitude, double longitude) {
        String url = String.format(WEATHER_FORECAST_API, latitude, longitude);

        try {
            ResponseEntity<WeatherForecast> responseEntity = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), WeatherForecast.class);
            return responseEntity.getBody();
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
        }

        return null;
    }

}
