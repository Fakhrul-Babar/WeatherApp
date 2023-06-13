package com.proit.application.views;

import com.proit.application.service.WeatherAppClient;
import com.proit.application.views.layout.MainLayout;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

@PageTitle("Main")
@Route(value = "search", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class SearchLocationView extends HorizontalLayout {

    private TextField name;
    private Button findLocation;
    private Button findWeatherForecust;

    private final WeatherAppClient weatherAppClient;

    public SearchLocationView(WeatherAppClient weatherAppClient) {
        this.weatherAppClient = weatherAppClient;
        name = new TextField("City");
        findLocation = new Button("Get Location");
        findLocation.addClickListener(e -> {
            Notification.show("Get Location by city: " + name.getValue()).setPosition(Notification.Position.TOP_END);
            weatherAppClient.getLocations(name.getValue());
        });
        findLocation.addClickShortcut(Key.ENTER);

        findWeatherForecust = new Button("Get Weather Forecast");
        findWeatherForecust.addClickListener(e -> {
            weatherAppClient.getWeatherForecastData(23.7104, 90.40744);
        });

        setMargin(true);
        setVerticalComponentAlignment(Alignment.END, name, findLocation);

        add(name, findLocation, findWeatherForecust);
    }

}
