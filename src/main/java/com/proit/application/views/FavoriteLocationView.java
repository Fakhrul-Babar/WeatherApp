package com.proit.application.views;

import com.proit.application.domain.CurrentWeather;
import com.proit.application.entity.UserFavoriteLocationEntity;
import com.proit.application.security.AuthenticationService;
import com.proit.application.service.UserFavoriteLocationService;
import com.proit.application.service.WeatherAppClient;
import com.proit.application.util.WeatherUtil;
import com.proit.application.views.layout.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Paragraph;
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

    public FavoriteLocationView(
            AuthenticationService authenticationService,
            UserFavoriteLocationService userFavoriteLocationService,
            WeatherAppClient weatherAppClient) {
        this.authenticationService = authenticationService;
        this.userFavoriteLocationService = userFavoriteLocationService;
        this.weatherAppClient = weatherAppClient;


        // Create the main div container
        container = new Div();
        container.addClassName("fav-weather-container");

        // Generate and populate the weather data
        createFavoriteLocationContent(container);

        add(container);
    }

    private void createFavoriteLocationContent(Div container) {
        container.getElement().removeAllChildren();
        List<UserFavoriteLocationEntity> favoriteLocations = userFavoriteLocationService.getFavoriteLocationByUser(authenticationService.getLogedInUsername());

        for (UserFavoriteLocationEntity favoriteLocation : favoriteLocations) {
            Div weatherDiv = createWeatherDiv(favoriteLocation);
            container.add(weatherDiv);
        }
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
