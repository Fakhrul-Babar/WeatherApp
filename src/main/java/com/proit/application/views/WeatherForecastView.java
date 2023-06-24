package com.proit.application.views;

import com.proit.application.domain.CurrentWeather;
import com.proit.application.domain.DailyWeatherForecast;
import com.proit.application.domain.Location;
import com.proit.application.domain.WeatherForecast;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class WeatherForecastView extends VerticalLayout {

    String degreeSymbol = "\u00B0";

    public WeatherForecastView() {
        setWidthFull();

    }

    public void showWeatherInfo(WeatherForecast weatherForecast, Location location) {
        if (weatherForecast == null || location == null) {
            setVisible(false);
            removeAll();
            return;
        }

        add(getToolbar(location), createCurrentWeatherContent(weatherForecast.getCurrentWeather(), location), createWeeklyWeatherContent(weatherForecast, location));
        setVisible(true);
    }

    private Component getToolbar(Location location) {
        H2 headerText = new H2("Weather Forecast For: " + location.getName() + ", " + location.getCountry());

        Button closeButton = new Button(new Icon("lumo", "cross"));
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        closeButton.addClassNames(LumoUtility.BorderColor.ERROR);
        closeButton.addClickListener(e -> {
            setVisible(false);
            removeAll();
        });

        HorizontalLayout toolbar = new HorizontalLayout();
        toolbar.add(headerText, closeButton);
        toolbar.setJustifyContentMode(JustifyContentMode.BETWEEN);
        toolbar.setWidthFull();
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private Component createCurrentWeatherContent(CurrentWeather currentWeather, Location location) {
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
        locationDiv.setText(location.getName());

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
//        containerDiv.getStyle().set("margin-top", "140px");
        containerDiv.add(forecastContainerDiv);

        return containerDiv;
    }

    private Component createWeeklyWeatherContent(WeatherForecast weatherForecast, Location location) {

        List<DailyWeatherForecast> dailyWeatherForecasts = convertWeatherForecastToDailyForecast(weatherForecast);

        HorizontalLayout horizontalLayout = new HorizontalLayout();

        for (DailyWeatherForecast dailyWeatherForecast: dailyWeatherForecasts) {
            Component containerDiv = createDayWeatherContent(dailyWeatherForecast, location);
            horizontalLayout.add(containerDiv);
        }

        horizontalLayout.setMaxWidth("100%");
        horizontalLayout.setWidth("560px");

        Scroller scroller = new Scroller();
        scroller.setScrollDirection(Scroller.ScrollDirection.HORIZONTAL);
        horizontalLayout.getStyle().set("display", "inline-flex");
        scroller.setContent(horizontalLayout);

        Div forecastContainerDiv = new Div();
        forecastContainerDiv.addClassName("forecast-container");
        forecastContainerDiv.add(horizontalLayout);

        Div containerDiv = new Div();
        containerDiv.addClassName("container");
//        containerDiv.getStyle().set("margin-top", "-50px");
        containerDiv.add(forecastContainerDiv);



        return containerDiv;
    }

    private Component createDayWeatherContent(DailyWeatherForecast dailyWeatherForecast, Location location) {
        Div forecastDiv = new Div();
        forecastDiv.addClassName("forecast");

        Div forecastHeaderDiv = new Div();
        forecastHeaderDiv.addClassName("forecast-header");

        Div dayDiv = new Div();
        dayDiv.addClassName("day");
        dayDiv.setText(dailyWeatherForecast.getTime().getDayOfWeek().name());

        forecastHeaderDiv.add(dayDiv);

        Div forecastContentDiv = new Div();
        forecastContentDiv.addClassName("forecast-content");

        Div forecastIconDiv = new Div();
        forecastIconDiv.addClassName("forecast-icon");

        Image forecastImage = new Image("icons/icon-3.svg", "Weather Icon");
        forecastImage.setWidth("48px");

        forecastIconDiv.add(forecastImage);

        Div degreeDiv = new Div();
        degreeDiv.addClassName("degree");
        degreeDiv.setText(dailyWeatherForecast.getTemperature2mMax() + " " + degreeSymbol + "C");

        Span smallSpan = new Span();
        smallSpan.setText(dailyWeatherForecast.getTemperature2mMin() + " " + degreeSymbol + "C");

        forecastContentDiv.add(forecastIconDiv, degreeDiv, smallSpan);

        forecastDiv.add(forecastHeaderDiv, forecastContentDiv);

        return forecastDiv;
    }

    private List<DailyWeatherForecast> convertWeatherForecastToDailyForecast(WeatherForecast weatherForecast) {
        List<DailyWeatherForecast> dailyForecastList = new ArrayList<>();

        for (int i = 0; i < weatherForecast.getDaily().getTime().size(); i++) {
            DailyWeatherForecast dailyForecast = new DailyWeatherForecast()
                    .setCurrentWeather(weatherForecast.getCurrentWeather())
                    .setTime(weatherForecast.getDaily().getTime().get(i))
                    .setWeathercode(weatherForecast.getDaily().getWeathercode().get(i))
                    .setTemperature2mMax(weatherForecast.getDaily().getTemperature2mMax().get(i))
                    .setTemperature2mMin(weatherForecast.getDaily().getTemperature2mMin().get(i))
                    .setTemperatureUnit(weatherForecast.getDailyUnits().getTemperature2mMax())
                    .setSunrise(weatherForecast.getDaily().getSunrise().get(i))
                    .setSunset(weatherForecast.getDaily().getSunset().get(i))
                    .setPrecipitationProbabilityMax(weatherForecast.getDaily().getPrecipitationProbabilityMax().get(i))
                    .setWindspeed10mMax(weatherForecast.getDaily().getWindspeed10mMax().get(i))
                    .setWindspeedUnit(weatherForecast.getDailyUnits().getWindspeed10mMax())
                    .setRainSum(weatherForecast.getDaily().getRainSum().get(i))
                    .setRainUnit(weatherForecast.getDailyUnits().getRainSum());

            dailyForecastList.add(dailyForecast);
        }
        return dailyForecastList;
    }

}

