package com.restaurant.booking.ui.bookings.newbooking;

import com.restaurant.booking.backend.controller.BookingRequestController;
import com.restaurant.booking.backend.controller.TableController;
import com.restaurant.booking.ui.MainLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;


@Route(value = "", layout = MainLayout.class)
public class NewBooking extends VerticalLayout {


    private final TableController tableController;
    private final BookingRequestController bookingRequestController;
    private NewBookingForm form;

    private TextArea statusText;

    @Autowired
    public NewBooking(TableController tableController,
                        BookingRequestController bookingRequestController) {
        this.tableController = tableController;
        this.bookingRequestController = bookingRequestController;
        addClassName("new-booking");

        H3 header = new H3("New booking reservation");

        this.form = new NewBookingForm(tableController);
        form.addListener(NewBookingForm.SaveEvent.class, this::saveBooking);

        this.statusText = new TextArea();
        statusText.addClassName("status-text");
        statusText.setSizeFull();
        statusText.setReadOnly(true);


        add(header,form, statusText);
    }

    private void saveBooking(NewBookingForm.SaveEvent event) {
        bookingRequestController.save(event.getBookingRequest());
        tableController.save(event.getTable());
        statusText.setValue("Thank you for your booking!\nYour booking has been sent for approval.\nTo check the status of your booking please use this booking reference: \n" + event.getBookingRequest().getId());
        form.clearAllFields();
    }
}
