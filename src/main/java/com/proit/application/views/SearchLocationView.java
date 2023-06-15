package com.proit.application.views;

import com.proit.application.domain.Location;
import com.proit.application.domain.WeatherForecast;
import com.proit.application.service.WeatherAppClient;
import com.proit.application.views.layout.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import jakarta.annotation.security.PermitAll;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@PermitAll
@PageTitle("Main")
@Route(value = "search", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class SearchLocationView extends VerticalLayout {

    Grid<Location> grid = new Grid<>(Location.class);
    TextField searchText = new TextField();

    private DailyForecastView dailyForecastView;

    private final WeatherAppClient weatherAppClient;

    public SearchLocationView(WeatherAppClient weatherAppClient) {
        this.weatherAppClient = weatherAppClient;

        addClassName("list-view");
        setSizeFull();

        configureGrid();

        dailyForecastView = new DailyForecastView();
        dailyForecastView.setSizeFull();
        dailyForecastView.showWeatherInfo(null, null);

        add(getToolbar(), getContent());
    }

    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, dailyForecastView);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, dailyForecastView);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private Component getToolbar() {
        searchText.setPlaceholder("Search by city...");
        searchText.setClearButtonVisible(true);
        searchText.setValueChangeMode(ValueChangeMode.LAZY);
        searchText.addValueChangeListener(e -> updateLocationList());

        Button addContactButton = new Button("Add City");
        addContactButton.addClickListener(click -> log.info("Add city clicked..."));

        var toolbar = new HorizontalLayout(searchText, addContactButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void configureGrid() {
        grid.addClassNames("location-grid");
        grid.setSizeFull();
        grid.setColumns();
        grid.addColumn(Location::getName).setHeader("Name");
        grid.addColumn(Location::getCountry).setHeader("Country");
        grid.addColumn(Location::getTimezone).setHeader("Timezone");
        grid.addColumn(Location::getLatitude).setHeader("Latitude");
        grid.addColumn(Location::getLongitude).setHeader("Longitude");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.setPageSize(10);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

        grid.asSingleSelect().addValueChangeListener(event -> {
                    dailyForecastView.showWeatherInfo(null, null);

                    Location location = event.getValue();
                    log.info("Selected location: {}", location);
                    WeatherForecast weatherForecastData = weatherAppClient.getWeatherForecastData(location.getLatitude(), location.getLongitude());

                    dailyForecastView.showWeatherInfo(weatherForecastData, location);
                    dailyForecastView.setVisible(true);
                }
        );
    }

    private void updateLocationList() {
        String searchedValue = searchText.getValue();
        if (searchedValue.isEmpty()) {
            Notification.show("Write a city name.").setPosition(Notification.Position.TOP_CENTER);
            return;
        }

        if (searchedValue.length() < 3) {
            Notification.show("Write at least 3 character of city name.").setPosition(Notification.Position.TOP_CENTER);
            return;
        }
        List<Location> locations = weatherAppClient.getLocations(searchText.getValue());
        grid.setItems(locations);
    }


}
