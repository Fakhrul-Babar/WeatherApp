package com.proit.application.views;

import com.proit.application.domain.Location;
import com.proit.application.domain.WeatherForecast;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class DailyForecastView extends VerticalLayout {

    public DailyForecastView() {
        setWidthFull();

    }

    public void showWeatherInfo(WeatherForecast weatherInfo, Location location) {
        if (weatherInfo == null || location == null) {
            setVisible(false);
            return;
        }

        add(getToolbar(location));
        setVisible(true);
    }

    private Component getToolbar(Location location) {
        H1 headerText = new H1("Weather Forecast For City: " + location.getName() + ", " + location.getCountry());

        var toolbar = new HorizontalLayout(headerText);
        toolbar.addClassName("toolbar");
        return toolbar;
    }
}
