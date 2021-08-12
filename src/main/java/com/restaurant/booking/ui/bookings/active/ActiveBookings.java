package com.restaurant.booking.ui.bookings.active;

import com.restaurant.booking.backend.model.BookingRequest;
import com.restaurant.booking.backend.service.BookingRequestService;
import com.restaurant.booking.ui.MainLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "active-bookings", layout = MainLayout.class)
public class ActiveBookings extends VerticalLayout {

    private final BookingRequestService bookingRequestService;

    private Grid<BookingRequest> grid = new Grid<>(BookingRequest.class);

    @Autowired
    public ActiveBookings(BookingRequestService bookingRequestService) {
        this.bookingRequestService = bookingRequestService;

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
        grid.setItems(bookingRequestService.getActiveRequests());
    }
}
