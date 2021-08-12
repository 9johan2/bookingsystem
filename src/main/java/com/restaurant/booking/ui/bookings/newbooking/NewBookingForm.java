package com.restaurant.booking.ui.bookings.newbooking;

import com.restaurant.booking.backend.model.BookingRequest;
import com.restaurant.booking.backend.model.Table;
import com.restaurant.booking.backend.service.TableService;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.shared.Registration;

import java.util.List;


public class NewBookingForm extends FormLayout {
    private BookingRequest bookingRequest;

    TextField firstName = new TextField("First name");
    TextField lastName = new TextField("Last name");
    TextField email = new TextField("Email");
    IntegerField numOfPeople = new IntegerField("Number of people");
    ComboBox<Table> table = new ComboBox<>("Table");
    DateTimePicker bookingTime = new DateTimePicker("Time of visit");

    Button send = new Button("Send booking request");

    // FIXME: 2021-08-11 There are probably better classes than these ones to display plain text...
    H1 statusText = new H1();
    H2 textarea = new H2();


    public NewBookingForm(TableService tableService) {
        addClassName("booking-form");

        numOfPeople.setHasControls(true);
        configureTables(tableService);
        configureButtons();

        add(firstName,
            lastName,
            email,
            numOfPeople,
            bookingTime,
            table,
            send,
            statusText,
            textarea
        );

    }

    private void configureButtons() {
        send.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        send.addClickShortcut(Key.ENTER);

        send.addClickListener(event -> validateAndSave());

    }
    
    private void configureTables(TableService tableService) {
        table.setItems(List.of());

        bookingTime.addValueChangeListener(listener -> {
            if (bookingTime.getValue() != null && numOfPeople.getValue() != null) {
                table.setItems(tableService.getAvailableTables(bookingTime.getValue(), numOfPeople.getValue()));
            }
        });
        numOfPeople.addValueChangeListener(listener -> {
            if (bookingTime.getValue() != null && numOfPeople.getValue() != null) {
                table.setItems(tableService.getAvailableTables(bookingTime.getValue(), numOfPeople.getValue()));
            }
        });
    }

    private void validateAndSave() {
        try {
            BookingRequest request = new BookingRequest(
                    firstName.getValue(),
                    lastName.getValue(),
                    email.getValue(),
                    numOfPeople.getValue(),
                    table.getValue(),
                    bookingTime.getValue()
            );

            setBookingRequest(request);
            fireEvent(new SaveEvent(this, bookingRequest));

            clearAllFields();
            statusText.setText("Thank you for your booking!");
            textarea.setText("Your booking has been sent for approval.\nTo check the status of your booking use this booking reference: " + request.getId());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void clearAllFields() {
        firstName.clear();
        lastName.clear();
        email.clear();
        numOfPeople.clear();
        bookingTime.clear();
        table.clear();
    }

    public void setBookingRequest(BookingRequest bookingRequest) {
        this.bookingRequest = bookingRequest;
    }

    // Events
    public static abstract class NewBookingFormEvent extends ComponentEvent<NewBookingForm> {
        private BookingRequest bookingRequest;
        private Table table;

        protected NewBookingFormEvent(NewBookingForm source, BookingRequest bookingRequest) {
            super(source, false);
            this.bookingRequest = bookingRequest;
            this.table = bookingRequest.getTable();
        }

        public BookingRequest getBookingRequest() {
            return bookingRequest;
        }

        public Table getTable() {
            return this.table;
        }
    }

    public static class SaveEvent extends NewBookingFormEvent {
        SaveEvent(NewBookingForm source, BookingRequest bookingRequest) {
            super(source, bookingRequest);
        }
    }
    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
