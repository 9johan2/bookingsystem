package com.restaurant.booking.ui.bookings.statuscheck;

import com.restaurant.booking.backend.controller.BookingRequestController;
import com.restaurant.booking.backend.model.BookingRequest;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import java.util.Optional;

// FIXME: 2021-08-10 The UI should be redesigned to display information in a nicer way
public class CheckBookingForm extends VerticalLayout {

    private final TextField bookingId = new TextField("Booking reference");
    private final Button send = new Button("Send");
    private final TextField result = new TextField("Booking status");

    public CheckBookingForm(BookingRequestController bookingRequestController) {
        send.setEnabled(false);
        result.setEnabled(false);
        result.setWidthFull();

        bookingId.addValueChangeListener(event -> send.setEnabled(!bookingId.isEmpty()));

        send.addClickListener(buttonClickEvent -> {
            Optional<BookingRequest> booking = bookingRequestController.getBookingById(bookingId.getValue());
            if (booking.isEmpty()) {
                result.setValue("Booking id does not match any booking");
            } else {
                BookingRequest.Status status = booking.get().getStatus();

                switch (status) {
                    case APPROVED:
                        result.setValue("Your booking has been approved");
                        break;
                    case DENIED:
                        result.setValue("Your booking has been denied, please contact customer support for further info");
                        break;
                    default:
                        result.setValue("Your booking request is still being processed");
                }
            }
        });
        add(bookingId, send, result);
    }

}
