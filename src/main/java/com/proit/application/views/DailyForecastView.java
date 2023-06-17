package com.proit.application.views;

import com.proit.application.domain.DailyWeatherForecast;
import com.proit.application.domain.Location;
import com.proit.application.domain.WeatherForecast;
import com.proit.application.util.WeatherUtil;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DailyForecastView extends VerticalLayout {

    public DailyForecastView() {
        setWidthFull();

    }

    public void showWeatherInfo(WeatherForecast weatherForecast, Location location) {
        if (weatherForecast == null || location == null) {
            setVisible(false);
            removeAll();
            return;
        }

        List<DailyWeatherForecast> dailyWeatherForecasts = convertWeatherForecastToDailyForecast(weatherForecast);

        Grid<DailyWeatherForecast> dailyForecastGrid = new Grid<>(DailyWeatherForecast.class, false);

        configureWeatherForecastGrid(dailyForecastGrid);
        dailyForecastGrid.setItems(dailyWeatherForecasts);

        add(getToolbar(location), dailyForecastGrid);
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

    private void configureWeatherForecastGrid(Grid<DailyWeatherForecast> dailyForecastGrid) {
        String dayTemplate = "<div>${item.day}<br/>${item.date}</div>";
        dailyForecastGrid.addColumn(
                LitRenderer.<DailyWeatherForecast>of(dayTemplate)
                        .withProperty("day", forecast -> forecast.getTime().getDayOfWeek().name())
                        .withProperty("date", forecast -> forecast.getTime().format(DateTimeFormatter.ofPattern("MMMM d")))
        ).setHeader("Day");

        String weatherTemplate = "<div>${item.message}</div>";
        dailyForecastGrid.addColumn(
                LitRenderer.<DailyWeatherForecast>of(weatherTemplate)
                        .withProperty("message", forecast -> WeatherUtil.getWeatherMessage(forecast.getWeathercode()))
        ).setHeader("Weather");

        dailyForecastGrid.addColumn(dailyForecast -> String.format("%s | %s %s", dailyForecast.getTemperature2mMax(), dailyForecast.getTemperature2mMin(), dailyForecast.getTemperatureUnit())).setHeader("Temperature");
        dailyForecastGrid.addColumn(dailyForecast -> String.format("%s %s", dailyForecast.getWindspeed10mMax(), dailyForecast.getWindspeedUnit())).setHeader("Wind");
        dailyForecastGrid.addColumn(dailyForecast -> String.format("%s %s", dailyForecast.getRainSum(), dailyForecast.getRainUnit())).setHeader("Rain");

        dailyForecastGrid.getColumns().forEach(column -> {
            column.setAutoWidth(true);
            column.setSortable(false);
        });
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

    public <T> Collection<List<T>> prepareChunks(List<T> inputList, int chunkSize) {
        AtomicInteger counter = new AtomicInteger();
        return inputList.stream().collect(Collectors.groupingBy(it -> counter.getAndIncrement() / chunkSize)).values();
    }

    public static <T extends Collection<E>, E> List<List<T>> separateList(List<T> originalList, int chunkSize) {
        return IntStream.range(0, originalList.size())
                .boxed()
                .collect(Collectors.groupingBy(index -> index / chunkSize))
                .values()
                .stream()
                .map(indices -> indices.stream()
                        .map(originalList::get)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

}
