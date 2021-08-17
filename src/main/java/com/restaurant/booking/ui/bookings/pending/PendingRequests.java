package com.restaurant.booking.ui.bookings.pending;

import com.restaurant.booking.backend.controller.BookingRequestController;
import com.restaurant.booking.backend.model.BookingRequest;
import com.restaurant.booking.ui.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "pending_requests", layout = MainLayout.class)
public class PendingRequests extends VerticalLayout {

    private Grid<BookingRequest> grid = new Grid<>(BookingRequest.class);
    private Button acceptButton = new Button("Accept");
    private Button denyButton = new Button("Deny");

    private final BookingRequestController bookingRequestController;
    private BookingRequest bookingRequest;

    @Autowired
    public PendingRequests(BookingRequestController bookingRequestController) {
        this.bookingRequestController = bookingRequestController;

        addClassName("pending-requests");
        setSizeFull();

        H3 header = new H3("Pending booking requests");

        configureGrid();
        configureButtons();

        VerticalLayout buttons = new VerticalLayout(acceptButton, denyButton);
        buttons.addClassName("button-layout");

        Div content = new Div(grid, buttons);
        content.setSizeFull();
        content.addClassName("content");

        add(header, content);
        updateList();
    }

    private void configureGrid() {
        grid.addClassName("pending-grid");
        grid.setSizeFull();
        grid.setColumns("id", "firstName",
                "lastName", "email", "numOfPeople", "table", "bookingTime", "status");

        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event -> {
            setBookingRequest(event.getValue());
            acceptButton.setEnabled(true);
            denyButton.setEnabled(true);
        });
    }

    private void configureButtons() {
        acceptButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        acceptButton.setEnabled(false);
        acceptButton.addClickListener(e -> {
            bookingRequest.setStatus(BookingRequest.Status.APPROVED);
            bookingRequestController.update(bookingRequest);
            updateList();
        });

        denyButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        denyButton.setEnabled(false);
        denyButton.addClickListener(e -> {
            bookingRequest.setStatus(BookingRequest.Status.DENIED);
            bookingRequestController.update(bookingRequest);
            updateList();
        });
    }

    private void updateList() {
        grid.setItems(bookingRequestController.getBooking(BookingRequest.Status.PENDING));
    }

    private void setBookingRequest(BookingRequest bookingRequest) {
        this.bookingRequest = bookingRequest;
    }
}
