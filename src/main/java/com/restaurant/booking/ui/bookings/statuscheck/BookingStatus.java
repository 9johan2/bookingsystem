package com.restaurant.booking.ui.bookings.statuscheck;

import com.restaurant.booking.backend.service.BookingRequestService;
import com.restaurant.booking.ui.MainLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "booking_status", layout = MainLayout.class)
public class BookingStatus extends VerticalLayout {

    private final BookingRequestService bookingRequestService;

    private CheckBookingForm form;

    @Autowired
    public BookingStatus(BookingRequestService bookingRequestService) {
        this.bookingRequestService = bookingRequestService;

        this.form = new CheckBookingForm(bookingRequestService);

        add(form);
    }
}
