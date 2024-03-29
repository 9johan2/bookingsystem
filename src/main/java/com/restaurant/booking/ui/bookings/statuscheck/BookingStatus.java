package com.restaurant.booking.ui.bookings.statuscheck;

import com.restaurant.booking.backend.controller.BookingRequestController;
import com.restaurant.booking.ui.MainLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "booking_status", layout = MainLayout.class)
public class BookingStatus extends VerticalLayout {

    private final BookingRequestController bookingRequestController;

    private CheckBookingForm form;

    @Autowired
    public BookingStatus(BookingRequestController bookingRequestController) {
        this.bookingRequestController = bookingRequestController;

        H3 header = new H3("Check booking status");
        this.form = new CheckBookingForm(bookingRequestController);

        add(header, form);
    }
}
