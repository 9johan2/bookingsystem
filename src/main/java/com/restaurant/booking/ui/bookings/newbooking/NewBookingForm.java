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
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.DateTimeRangeValidator;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.validator.IntegerRangeValidator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.function.SerializablePredicate;
import com.vaadin.flow.shared.Registration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;


public class NewBookingForm extends VerticalLayout {
    private BookingRequest bookingRequest;

    TextField firstName = new TextField("First name");
    TextField lastName = new TextField("Last name");
    TextField email = new TextField("Email");
    IntegerField numOfPeople = new IntegerField("Number of people");
    ComboBox<Table> table = new ComboBox<>("Table");
    DateTimePicker bookingTime = new DateTimePicker("Time of visit");
    Binder<BookingRequest> binder = new Binder<>();

    Button send = new Button("Send booking request");


    public NewBookingForm(TableController tableController) {
        addClassName("booking-form");

        configureTableSelection(tableController);
        configureButtons();

        add(configureFields(), send);
    }

    private FormLayout configureFields() {
        FormLayout fields = new FormLayout();
        fields.setResponsiveSteps(new FormLayout.ResponsiveStep("1em", 1),
                new FormLayout.ResponsiveStep("15em", 2));

        // Size layout and value change mode
        firstName.setMaxWidth("35%");
        firstName.setMinWidth("100px");
        firstName.getStyle().set("marginRight", "10px");
        firstName.setValueChangeMode(ValueChangeMode.EAGER);

        lastName.setMaxWidth("35%");
        lastName.setMinWidth("100px");
        lastName.getStyle().set("marginRight", "10px");
        lastName.setValueChangeMode(ValueChangeMode.EAGER);

        email.setMaxWidth("35%");
        email.setMinWidth("100px");
        email.getStyle().set("marginRight", "10px");
        email.setValueChangeMode(ValueChangeMode.EAGER);

        numOfPeople.setMaxWidth("35%");
        numOfPeople.setMinWidth("100px");
        numOfPeople.getStyle().set("marginRight", "10px");
        numOfPeople.setHasControls(true);
        numOfPeople.setValueChangeMode(ValueChangeMode.EAGER);

        bookingTime.setMaxWidth("35%");
        bookingTime.setMinWidth("250px");
        bookingTime.getStyle().set("marginRight", "10px");
        LocalDateTime min = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        bookingTime.setMin(min);


        table.setMaxWidth("35%");
        table.setMinWidth("250px");
        table.setReadOnly(true);

        fields.add(firstName, lastName, email, numOfPeople, bookingTime, table);

        //Validation
            // First name
        binder.forField(firstName)
                .withValidator(new StringLengthValidator(
                        "Please enter your first name", 1, null))
                .bind(BookingRequest::getFirstName, BookingRequest::setFirstName);

            // Last name
        binder.forField(lastName)
                .withValidator(new StringLengthValidator(
                        "Please enter your family name", 1, null))
                .bind(BookingRequest::getLastName, BookingRequest::setLastName);


            // Email
        binder.forField(email)
                .withValidator(new StringLengthValidator(
                        "Please enter your email", 1, null))
                .withValidator(new EmailValidator("Incorrect email adress"))
                .bind(BookingRequest::getEmail, BookingRequest::setEmail);


            // Number of people
        binder.forField(numOfPeople)
                .withValidator(new IntegerRangeValidator("There must be at least one person included in your booking"
                        , 1, null))
                .bind(BookingRequest::getNumOfPeople, BookingRequest::setNumOfPeople);

            //Booking time
        binder.forField(bookingTime)
                .withValidator(new DateTimeRangeValidator(
                        "Please chose a valid date and time for your visit"
                        , LocalDateTime.now(), null))
                .bind(BookingRequest::getBookingTime, BookingRequest::setBookingTime);

            //Table
        SerializablePredicate<Table> tableSelected = value -> table.getValue() != null ;
        binder.forField(table)
                .withValidator(tableSelected, "No tables available for your desired time and group size")
                .bind(BookingRequest::getTable, BookingRequest::setTable);

        return fields;
    }

    private void configureButtons() {
        send.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        send.setMaxWidth("200px");
        send.getElement().setAttribute("colspan", "2");
        send.addClickShortcut(Key.ENTER);

        binder.addStatusChangeListener(e -> send.setEnabled(binder.isValid()));

        send.addClickListener(event -> validateAndSave());

    }

    private void configureTableSelection(TableController tableController) {
        table.setItems(List.of());

        bookingTime.addValueChangeListener(listener -> {
            if (bookingTime.getValue() != null && numOfPeople.getValue() != null) {
                List<Table> tables = tableController.getTables(bookingTime.getValue(), numOfPeople.getValue());
                table.setItems(tables);
                if (!tables.isEmpty()) {
                    table.setValue(tables.get(0));
                }
            }
        });
        numOfPeople.addValueChangeListener(listener -> {
            if (bookingTime.getValue() != null && numOfPeople.getValue() != null) {
                List<Table> tables = tableController.getTables(bookingTime.getValue(), numOfPeople.getValue());
                table.setItems(tables);
                if (!tables.isEmpty()) {
                    table.setValue(tables.get(0));
                }
            }
        });
    }

    private void validateAndSave() {
        try {
            bookingRequest = new BookingRequest();
            binder.writeBeanIfValid(bookingRequest);

            fireEvent(new SaveEvent(this, bookingRequest));

        } catch (Exception e) {
            e.printStackTrace();
        }
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
