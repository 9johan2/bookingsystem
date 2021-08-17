package com.restaurant.booking.ui.bookings.statuscheck;

import com.restaurant.booking.backend.controller.BookingRequestController;
import com.restaurant.booking.backend.model.BookingRequest;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

import java.util.Optional;

public class CheckBookingForm extends VerticalLayout {

    private final TextField bookingId = new TextField("Booking reference");
    private final Button send = new Button("Send");
    private final Label result = new Label();

    public CheckBookingForm(BookingRequestController bookingRequestController) {
        bookingId.setWidth("350px");
        bookingId.setValueChangeMode(ValueChangeMode.EAGER);
        bookingId.addValueChangeListener(event -> send.setEnabled(!bookingId.isEmpty()));

        result.setWidthFull();

        send.setEnabled(false);
        send.addClickListener(buttonClickEvent -> {
            Optional<BookingRequest> booking = bookingRequestController.getBookingById(bookingId.getValue());
            if (booking.isEmpty()) {
                result.setText("Booking id does not match any booking");
            } else {
                BookingRequest.Status status = booking.get().getStatus();

                switch (status) {
                    case APPROVED:
                        result.setText("Your booking has been approved");
                        break;
                    case DENIED:
                        result.setText("Your booking has been denied, please contact customer support for further info");
                        break;
                    default:
                        result.setText("Your booking request is still being processed");
                }
            }
        });
        add(bookingId, send, result);
    }

}
