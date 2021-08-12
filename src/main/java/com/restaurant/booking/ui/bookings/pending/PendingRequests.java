package com.restaurant.booking.ui.bookings.pending;

import com.restaurant.booking.backend.model.BookingRequest;
import com.restaurant.booking.backend.service.BookingRequestService;
import com.restaurant.booking.ui.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "pending_requests", layout = MainLayout.class)
public class PendingRequests extends HorizontalLayout {

    private Grid<BookingRequest> grid = new Grid<>(BookingRequest.class);
    private Button acceptButton = new Button("Accept");
    private Button denyButton = new Button("Deny");

    private final BookingRequestService bookingRequestService;
    private BookingRequest bookingRequest;

    @Autowired
    public PendingRequests(BookingRequestService bookingRequestService) {
        this.bookingRequestService = bookingRequestService;

        addClassName("active-requests");
        setSizeFull();

        configureGrid();
        configureButtons();

        VerticalLayout buttons = new VerticalLayout(acceptButton, denyButton);
        buttons.addClassName("accept-deny-buttons");
        add(grid, buttons);
        updateList();
    }

    private void configureGrid() {
        grid.addClassName("booking-grid");
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
            bookingRequestService.save(bookingRequest);
            updateList();
        });

        denyButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        denyButton.setEnabled(false);
        denyButton.addClickListener(e -> {
            bookingRequest.setStatus(BookingRequest.Status.DENIED);
            bookingRequestService.save(bookingRequest);
            updateList();
        });
    }

    private void updateList() {
        grid.setItems(bookingRequestService.getPendingRequests());
    }

    private void setBookingRequest(BookingRequest bookingRequest) {
        this.bookingRequest = bookingRequest;
    }
}
