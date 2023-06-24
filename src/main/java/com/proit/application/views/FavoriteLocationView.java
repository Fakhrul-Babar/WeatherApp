package com.proit.application.views;

import com.proit.application.domain.CurrentWeather;
import com.proit.application.entity.UserFavoriteLocationEntity;
import com.proit.application.security.AuthenticationService;
import com.proit.application.service.UserFavoriteLocationService;
import com.proit.application.service.WeatherAppClient;
import com.proit.application.util.WeatherUtil;
import com.proit.application.views.layout.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;
import lombok.extern.slf4j.Slf4j;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@PermitAll
@PageTitle("Favorite Location | Weather App")
@Route(value = "favorite-location", layout = MainLayout.class)
public class FavoriteLocationView extends VerticalLayout {

    private final AuthenticationService authenticationService;
    private final UserFavoriteLocationService userFavoriteLocationService;
    private final WeatherAppClient weatherAppClient;

    Div container;
    String degreeSymbol = "\u00B0";

    public FavoriteLocationView(
            AuthenticationService authenticationService,
            UserFavoriteLocationService userFavoriteLocationService,
            WeatherAppClient weatherAppClient) {
        this.authenticationService = authenticationService;
        this.userFavoriteLocationService = userFavoriteLocationService;
        this.weatherAppClient = weatherAppClient;


        // Create the main div container
        container = new Div();
//        container.addClassName("fav-weather-container");

        // Generate and populate the weather data
        createFavoriteLocationContent(container);

        add(container);
    }

    private void createFavoriteLocationContent(Div container) {
        container.getElement().removeAllChildren();
        List<UserFavoriteLocationEntity> favoriteLocations = userFavoriteLocationService.getFavoriteLocationByUser(authenticationService.getLogedInUsername());

        for (UserFavoriteLocationEntity favoriteLocation : favoriteLocations) {
            Component weatherDiv = createCurrentWeatherContent(favoriteLocation);
            container.add(weatherDiv);
        }
    }

    private Component createCurrentWeatherContent(UserFavoriteLocationEntity favoriteLocation) {
        CurrentWeather currentWeather = weatherAppClient.getCurrentWeatherForecastData(favoriteLocation.getLatitude(), favoriteLocation.getLongitude());

        Div dayDiv = new Div();
        dayDiv.addClassName("day");
        dayDiv.setText(currentWeather.getTime().getDayOfWeek().name());

        Div dateDiv = new Div();
        dateDiv.addClassName("date");
        dateDiv.setText(currentWeather.getTime().format(DateTimeFormatter.ofPattern("MMMM d")));

        Div forecastHeaderDiv = new Div();
        forecastHeaderDiv.addClassName("forecast-header");
        forecastHeaderDiv.add(dayDiv, dateDiv);

        Div forecastContentDiv = new Div();
        forecastContentDiv.addClassName("forecast-content");

        Div locationDiv = new Div();
        locationDiv.addClassName("location");
        locationDiv.setText(favoriteLocation.getLocationDetails());

        Div degreeDiv = new Div();
        degreeDiv.addClassName("degree");

        Div numDiv = new Div();
        numDiv.addClassName("num");
        numDiv.setText(currentWeather.getTemperature() + " " + degreeSymbol + "C");

        Div forecastIconDiv = new Div();
        forecastIconDiv.addClassName("forecast-icon");
        Image forecastImage = new Image("icons/icon-1.svg", "Weather Icon");
        forecastImage.setWidth("90px");

        forecastIconDiv.add(forecastImage);

        degreeDiv.add(numDiv, forecastIconDiv);

        Span umbrellaSpan = new Span();
        umbrellaSpan.add(new Image("icon-umberella.png", "Umbrella Icon"));
        umbrellaSpan.add("20%");

        Span windSpan = new Span();
        windSpan.add(new Image("icon-wind.png", "Wind Icon"));
        windSpan.add(currentWeather.getWindspeed() + "km/h");

        Span compassSpan = new Span();
        compassSpan.add(new Image("icon-compass.png", "Compass Icon"));
        compassSpan.add("East");

        forecastContentDiv.add(locationDiv, degreeDiv, umbrellaSpan, windSpan, compassSpan);

        Div todayDiv = new Div();
        todayDiv.addClassName("today");
        todayDiv.addClassName("forecast");
        todayDiv.add(forecastHeaderDiv, forecastContentDiv);

        Div forecastContainerDiv = new Div();
        forecastContainerDiv.addClassName("forecast-container");
        forecastContainerDiv.add(todayDiv);

        Div containerDiv = new Div();
        containerDiv.addClassName("container");
        containerDiv.getStyle().set("margin-top", "20px");
        containerDiv.add(forecastContainerDiv);
        containerDiv.setWidth(33, Unit.PERCENTAGE);

        return containerDiv;
    }


    private Div createWeatherDiv(UserFavoriteLocationEntity favoriteLocation) {
        // Create a div for weather data
        Div weatherDiv = new Div();
        weatherDiv.addClassName("fav-weather-div");

        // Populate the weather data
        CurrentWeather currentWeatherForecastData = weatherAppClient.getCurrentWeatherForecastData(favoriteLocation.getLatitude(), favoriteLocation.getLongitude());
//        weatherDiv.setText("Weather Data For: " + favoriteLocation.getLocationDetails());

        Button closeButton = new Button(new Icon("lumo", "cross"));
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        closeButton.addClassNames(LumoUtility.BorderColor.ERROR);
        closeButton.addClickListener(e -> {
            userFavoriteLocationService.deleteUserFavoriteLocation(favoriteLocation.getId());
            createFavoriteLocationContent(container);
        });

        H4 headerText = new H4("Current Weather Forecast For: " + favoriteLocation.getLocationDetails());
        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.add(headerText, closeButton);
        toolbar.setJustifyContentMode(JustifyContentMode.BETWEEN);
        toolbar.setWidthFull();
        toolbar.addClassName("toolbar");

        weatherDiv.add(toolbar);

        Div dayTimeDiv = new Div();
        dayTimeDiv.add(new Paragraph(currentWeatherForecastData.getTime().getDayOfWeek().name()));
        dayTimeDiv.add(new Paragraph(currentWeatherForecastData.getTime().format(DateTimeFormatter.ofPattern("MMMM d"))));
        dayTimeDiv.add(new Paragraph("Weather: " + WeatherUtil.getWeatherMessage(currentWeatherForecastData.getWeathercode())));
        dayTimeDiv.add(new Paragraph("Temperature: " + currentWeatherForecastData.getTemperature()));

        weatherDiv.add(dayTimeDiv);
        return weatherDiv;
    }

}
