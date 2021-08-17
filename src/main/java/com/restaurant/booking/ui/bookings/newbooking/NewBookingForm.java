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

import java.time.LocalDateTime;
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

        bookingRequest = new BookingRequest();

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

        table.setMaxWidth("35%");
        table.setMinWidth("250px");

        fields.add(firstName, lastName, email, numOfPeople, bookingTime, table);

        //Validation
        firstName.setRequiredIndicatorVisible(true);
        binder.forField(firstName)
                .withValidator(new StringLengthValidator(
                        "Please enter your first name", 1, null))
                .bind(BookingRequest::getFirstName, BookingRequest::setFirstName);

        lastName.setRequiredIndicatorVisible(true);
        binder.forField(lastName)
                .withValidator(new StringLengthValidator(
                        "Please enter your family name", 1, null))
                .bind(BookingRequest::getLastName, BookingRequest::setLastName);

        email.setRequiredIndicatorVisible(true);
        binder.forField(email)
                .withValidator(new StringLengthValidator(
                        "Please enter your email", 1, null))
                .withValidator(new EmailValidator("Incorrect email adress"))
                .bind(BookingRequest::getEmail, BookingRequest::setEmail);

        numOfPeople.setRequiredIndicatorVisible(true);
        binder.forField(numOfPeople)
                .withValidator(new IntegerRangeValidator("There must be at least one person included in your booking"
                        , 1, null))
                .bind(BookingRequest::getNumOfPeople, BookingRequest::setNumOfPeople);

        bookingTime.setRequiredIndicatorVisible(true);
        binder.forField(bookingTime)
                .withValidator(new DateTimeRangeValidator(
                        "Please chose the desired date for your visit"
                        , LocalDateTime.now(), LocalDateTime.now().plusYears(1)))
                .bind(BookingRequest::getBookingTime, BookingRequest::setBookingTime);

        SerializablePredicate<Table> tableSelected = value -> table.getValue() != null;
        table.setRequiredIndicatorVisible(true);
        binder.forField(table)
                .withNullRepresentation(null)
                .withValidator(tableSelected, "Please choose a table. If no tables are visible there are no free tables matching your desired date and group size")
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
            binder.writeBeanIfValid(bookingRequest);
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
