package com.restaurant.booking.ui.bookings.newbooking;

import com.restaurant.booking.backend.controller.BookingRequestController;
import com.restaurant.booking.backend.controller.TableController;
import com.restaurant.booking.ui.MainLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "", layout = MainLayout.class)
public class NewBooking extends VerticalLayout {


    private final TableController tableController;
    private final BookingRequestController bookingRequestController;
    private NewBookingForm form;

    @Autowired
    public NewBooking(TableController tableController,
                        BookingRequestController bookingRequestController) {
        this.tableController = tableController;
        this.bookingRequestController = bookingRequestController;

        this.form = new NewBookingForm(tableController);
        form.addListener(NewBookingForm.SaveEvent.class, this::saveBooking);

        add(form);
    }

    private void saveBooking(NewBookingForm.SaveEvent event) {
        bookingRequestController.save(event.getBookingRequest());
        tableController.save(event.getTable());
        form.clearAllFields();
    }
}
