package com.restaurant.booking.ui.bookings.newbooking;

import com.restaurant.booking.backend.service.BookingRequestService;
import com.restaurant.booking.backend.service.TableService;
import com.restaurant.booking.ui.MainLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "", layout = MainLayout.class)
public class NewBooking extends VerticalLayout {


    private final TableService tableService;
    private final BookingRequestService bookingRequestService;
    private NewBookingForm form;

    @Autowired
    public NewBooking(TableService tableService,
                        BookingRequestService bookingRequestService) {
        this.tableService = tableService;
        this.bookingRequestService = bookingRequestService;

        this.form = new NewBookingForm(tableService);
        form.addListener(NewBookingForm.SaveEvent.class, this::saveBooking);

        add(form);
    }

    private void saveBooking(NewBookingForm.SaveEvent event) {
        bookingRequestService.save(event.getBookingRequest());
        tableService.save(event.getTable());
        form.clearAllFields();
    }
}
