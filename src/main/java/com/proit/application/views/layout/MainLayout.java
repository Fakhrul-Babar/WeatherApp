package com.proit.application.views.layout;

import com.proit.application.security.AuthenticationService;
import com.proit.application.views.SearchLocationView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.security.core.userdetails.UserDetails;

public class MainLayout extends AppLayout {

    private final AuthenticationService authenticationService;

    public MainLayout(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("Weather App");
        logo.addClassNames(
                LumoUtility.FontSize.LARGE,
                LumoUtility.Margin.MEDIUM);

        Button logout;
        if(authenticationService.isUserLogIn()) {
            String username = authenticationService.getAuthenticatedUser().map(UserDetails::getUsername).orElse("");
            logout = new Button("Log out " + username, e -> authenticationService.logout());
        }else {
            logout = new Button("Log In");
            logout.addClickListener(a -> logout.getUI().ifPresent(u -> u.navigate("/login")));
        }

        var header = new HorizontalLayout(new DrawerToggle(), logo, logout);

        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(logo);
        header.setWidthFull();
        header.addClassNames(
                LumoUtility.Padding.Vertical.NONE,
                LumoUtility.Padding.Horizontal.MEDIUM);

        addToNavbar(header);
    }

    private void createDrawer() {
        addToDrawer(new VerticalLayout(
                new RouterLink("Search Location", SearchLocationView.class)
        ));
    }
}
