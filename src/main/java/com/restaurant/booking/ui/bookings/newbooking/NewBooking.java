package com.restaurant.booking.ui.bookings.newbooking;

import com.restaurant.booking.backend.controller.BookingRequestController;
import com.restaurant.booking.backend.controller.TableController;
import com.restaurant.booking.ui.MainLayout;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "", layout = MainLayout.class)
public class NewBooking extends VerticalLayout {


    private final TableController tableController;
    private final BookingRequestController bookingRequestController;
    private final NewBookingForm form;

    private Dialog dialog;
    private VerticalLayout dialogDisplay;
    private Text bookingId;

    @Autowired
    public NewBooking(TableController tableController,
                        BookingRequestController bookingRequestController) {
        this.tableController = tableController;
        this.bookingRequestController = bookingRequestController;
        addClassName("new-booking");

        H3 header = new H3("New booking reservation");

        this.form = new NewBookingForm(tableController);
        form.addListener(NewBookingForm.SaveEvent.class, this::saveBooking);

        configureDialog();

        add(header,form, dialog);
    }

    private void configureDialog() {
        this.dialog = new Dialog();
        this.dialogDisplay = new VerticalLayout();
        this.bookingId = new Text("");

        dialog.setWidth("550px");
        dialog.setHeight("250px");
        dialog.setCloseOnOutsideClick(false);

        Button closeButton = new Button("Close", e -> {
            dialog.close();
            UI.getCurrent().getPage().reload();
        });

        dialogDisplay.setAlignItems(Alignment.CENTER);
        dialogDisplay.add(new H3("Thank you for your booking!"));
        dialogDisplay.add(new Label("Your booking request has been sent for approval."));
        dialogDisplay.add(new Label("To check the status of your booking please use this booking id:"));
        dialogDisplay.add(bookingId);
        dialogDisplay.add(closeButton);

        dialog.add(dialogDisplay);
    }

    private void saveBooking(NewBookingForm.SaveEvent event) {
        bookingRequestController.save(event.getBookingRequest());
        tableController.addBooking(event.getTable(), event.getBookingRequest().getBookingTime());

        bookingId.setText(event.getBookingRequest().getId());

        dialog.open();
    }
}
