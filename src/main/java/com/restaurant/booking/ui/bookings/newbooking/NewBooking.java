package com.restaurant.booking.ui.bookings.newbooking;

import com.restaurant.booking.backend.controller.BookingRequestController;
import com.restaurant.booking.backend.controller.TableController;
import com.restaurant.booking.ui.MainLayout;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "", layout = MainLayout.class)
public class NewBooking extends VerticalLayout {


    private final TableController tableController;
    private final BookingRequestController bookingRequestController;
    private NewBookingForm form;

    private Dialog dialog;

    @Autowired
    public NewBooking(TableController tableController,
                        BookingRequestController bookingRequestController) {
        this.tableController = tableController;
        this.bookingRequestController = bookingRequestController;
        addClassName("new-booking");

        H3 header = new H3("New booking reservation");

        this.form = new NewBookingForm(tableController);
        form.addListener(NewBookingForm.SaveEvent.class, this::saveBooking);

        this.dialog = new Dialog();
        dialog.setWidth("500px");
        dialog.setHeight("200px");


        add(header,form, dialog);
    }

    private void saveBooking(NewBookingForm.SaveEvent event) {
        bookingRequestController.save(event.getBookingRequest());
        tableController.save(event.getTable());
        dialog.add(new Text("Thank you for your booking!\nYour booking has been sent for approval.\nTo check the status of your booking please use this booking reference: \n" + event.getBookingRequest().getId()));
        dialog.open();
//        form.clearAllFields();
    }
}
