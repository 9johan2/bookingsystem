package com.restaurant.booking.ui.bookings.newbooking;

import com.restaurant.booking.backend.controller.TableController;
import com.restaurant.booking.backend.model.BookingRequest;
import com.restaurant.booking.backend.model.Table;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.shared.Registration;

import java.util.List;


public class NewBookingForm extends VerticalLayout {
    private BookingRequest bookingRequest;

    TextField firstName = new TextField("First name");
    TextField lastName = new TextField("Last name");
    TextField email = new TextField("Email");
    IntegerField numOfPeople = new IntegerField("Number of people");
    ComboBox<Table> table = new ComboBox<>("Table");
    DateTimePicker bookingTime = new DateTimePicker("Time of visit");

    Button send = new Button("Send booking request");


    public NewBookingForm(TableController tableController) {
        addClassName("booking-form");

        FormLayout fields = new FormLayout();
        fields.add(firstName,
                lastName,
                email,
                numOfPeople,
                bookingTime,
                table);

//        configureFields();
        configureTableSelection(tableController);
        configureButtons();



        add(configureFields(), send);



    }

    private FormLayout configureFields() {
        FormLayout fields = new FormLayout();
        fields.setResponsiveSteps(new FormLayout.ResponsiveStep("1em", 1),
                new FormLayout.ResponsiveStep("15em", 2));

        firstName.setMaxWidth("35%");
        lastName.setMaxWidth("35%");
        email.setMaxWidth("35%");
        numOfPeople.setMaxWidth("35%");
        numOfPeople.setHasControls(true);
        bookingTime.setMaxWidth("35%");
        bookingTime.setMinWidth("250px");
        table.setMaxWidth("35%");

        fields.add(firstName, lastName, email, numOfPeople, bookingTime, table);
        return fields;
    }

    private void configureButtons() {
        send.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        send.setMaxWidth("200px");
        send.getElement().setAttribute("colspan", "2");
        send.addClickShortcut(Key.ENTER);

        send.addClickListener(event -> validateAndSave());

    }

    private void configureTableSelection(TableController tableController) {
        table.setItems(List.of());

        bookingTime.addValueChangeListener(listener -> {
            if (bookingTime.getValue() != null && numOfPeople.getValue() != null) {
                table.setItems(tableController.getTables(bookingTime.getValue(), numOfPeople.getValue()));
            }
        });
        numOfPeople.addValueChangeListener(listener -> {
            if (bookingTime.getValue() != null && numOfPeople.getValue() != null) {
                table.setItems(tableController.getTables(bookingTime.getValue(), numOfPeople.getValue()));
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
