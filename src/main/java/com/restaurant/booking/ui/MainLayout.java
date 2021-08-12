package com.restaurant.booking.ui;

import com.restaurant.booking.ui.bookings.active.ActiveBookings;
import com.restaurant.booking.ui.bookings.newbooking.NewBooking;
import com.restaurant.booking.ui.bookings.pending.PendingRequests;
import com.restaurant.booking.ui.bookings.statuscheck.BookingStatus;
import com.restaurant.booking.ui.tables.ListView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;

@CssImport("./styles/shared-styles.css")
public class MainLayout extends AppLayout {

    public MainLayout() {
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("Booking system");
        logo.addClassName("logo");

        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(), logo);

        header.setDefaultVerticalComponentAlignment(
                FlexComponent.Alignment.CENTER);
        header.setWidth("100%");
        header.setHeight("50%");
        header.addClassName("header");

        addToNavbar(header);
    }

    private void createDrawer() {
        RouterLink newBookingLink = new RouterLink("New booking", NewBooking.class);
        newBookingLink.setHighlightCondition(HighlightConditions.sameLocation());

        addToDrawer(new VerticalLayout(
                newBookingLink,
                new RouterLink("Check booking status", BookingStatus.class),
                new RouterLink("Pending booking requests", PendingRequests.class),
                new RouterLink("Active bookings", ActiveBookings.class),
                new RouterLink("All tables", ListView.class)
        ));
    }
}
