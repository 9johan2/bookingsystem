package com.restaurant.booking.ui.bookings.active;

import com.restaurant.booking.backend.controller.BookingRequestController;
import com.restaurant.booking.backend.model.BookingRequest;
import com.restaurant.booking.ui.MainLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "active-bookings", layout = MainLayout.class)
public class ActiveBookings extends VerticalLayout {

    private final BookingRequestController bookingRequestController;

    private final Grid<BookingRequest> grid = new Grid<>(BookingRequest.class);

    @Autowired
    public ActiveBookings(BookingRequestController bookingRequestController) {
        this.bookingRequestController = bookingRequestController;

        setSizeFull();
        configureGrid();
        add(grid);
        updateList();
    }

    private void configureGrid() {
        grid.setClassName("active-bookings");
        grid.setSizeFull();
        grid.setColumns("id", "firstName",
                "lastName", "email", "numOfPeople", "table", "bookingTime", "status");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private void updateList() {
        grid.setItems(bookingRequestController.getBooking(BookingRequest.Status.APPROVED));
    }

}
