package com.proit.application.views;

import com.proit.application.domain.Location;
import com.proit.application.domain.WeatherForecast;
import com.proit.application.security.AuthenticationService;
import com.proit.application.service.UserFavoriteLocationService;
import com.proit.application.service.WeatherAppClient;
import com.proit.application.views.layout.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@AnonymousAllowed
@PageTitle("Main")
@Route(value = "search", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class SearchLocationView extends VerticalLayout {

    Grid<Location> grid = new Grid<>(Location.class);
    TextField searchText = new TextField();

    private TextField gridFilterTextField = new TextField();

    private DailyForecastView dailyForecastView;

    private final WeatherAppClient weatherAppClient;
    private final AuthenticationService authenticationService;
    private final UserFavoriteLocationService userFavoriteLocationService;

    public SearchLocationView(
            WeatherAppClient weatherAppClient,
            AuthenticationService authenticationService,
            UserFavoriteLocationService userFavoriteLocationService) {
        this.weatherAppClient = weatherAppClient;
        this.authenticationService = authenticationService;
        this.userFavoriteLocationService = userFavoriteLocationService;

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

        Button searchButton = new Button("Search");
        searchButton.addClickListener(click -> getLocations());
        searchButton.addClickShortcut(Key.ENTER);

        var toolbar = new HorizontalLayout(searchText, searchButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void configureGrid() {
        grid.addClassNames("location-grid");
        grid.setSizeFull();
        grid.setColumns();
        grid.addColumn("name").setHeader("Name");

        // Add the column with the custom renderer
        grid.addColumn(getLocationDetailsRenderer()).setHeader("Details");
        grid.addColumn(getAddToFavorite()).setHeader("Favorite");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.setPageSize(10);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

        configureGridFilterTextField();

        // location row select action
        grid.asSingleSelect().addValueChangeListener(event -> {
                    if (!authenticationService.isUserLogIn()) {
                        Notification authNotification = Notification.show("Please login to view weather forecast");
                        authNotification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                        authNotification.setPosition(Notification.Position.TOP_CENTER);
                        return;
                    }
                    dailyForecastView.showWeatherInfo(null, null);

                    Location location = event.getValue();
                    log.info("Selected location: {}", location);
                    if (location == null) return;
                    WeatherForecast weatherForecastData = weatherAppClient.getWeatherForecastData(location.getLatitude(), location.getLongitude());

                    dailyForecastView.showWeatherInfo(weatherForecastData, location);
                }
        );
    }

    private LitRenderer<Location> getLocationDetailsRenderer() {
        String template = "<div>${item.admin}<br/>${item.country}<br/>Coordinates: ${item.latitude}, ${item.longitude}</div>";

        return LitRenderer.<Location>of(template)
                .withProperty("admin", Location::getAdminDetails)
                .withProperty("country", Location::getCountry)
                .withProperty("latitude", Location::getLatitude)
                .withProperty("longitude", Location::getLongitude);
    }

    private ComponentRenderer<Component, Location> getAddToFavorite() {
        return new ComponentRenderer<Component, Location>(location -> {
            Button addToFavoriteButton = new Button("Add to Favorite");
            addToFavoriteButton.addClickListener(click -> addLocationToUserFavorite(location));
            return addToFavoriteButton;
        });
    }

    private void configureGridFilterTextField() {
        gridFilterTextField.setPlaceholder("Filter by name");
        gridFilterTextField.setValueChangeMode(ValueChangeMode.EAGER);
        gridFilterTextField.addValueChangeListener(e -> onGridFilterTextFieldValueChange(e.getValue()));

        grid.appendHeaderRow()
                .getCell(grid.getColumnByKey("name"))
                .setComponent(gridFilterTextField);
    }

    private void onGridFilterTextFieldValueChange(String value) {
//        dataView.refreshAll();
    }

    private void getLocations() {
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

    private void addLocationToUserFavorite(Location location) {
        log.info("Select location: {} -- for add to favorite", location);
        if (!authenticationService.isUserLogIn()) {
            Notification authNotification = Notification.show("Please login to add location as favorite");
            authNotification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            authNotification.setPosition(Notification.Position.TOP_CENTER);
            return;
        }

        if (userFavoriteLocationService.existsByLocationIdAndUsername(location.getId(), authenticationService.getLogedInUsername())) {
            Notification.show("This location is already added as favorite").setPosition(Notification.Position.TOP_CENTER);
            return;
        }
        userFavoriteLocationService.saveUserFavoriteLocation(location);
        Notification.show("Location '" + location.getAdminDetails() + "' is added as favorite").setPosition(Notification.Position.TOP_CENTER);
    }

}
