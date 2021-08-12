package com.restaurant.booking.backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Document(collection = "bookingRequest")
public class BookingRequest {

    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private int numOfPeople;
    private Table table;
    private LocalDateTime bookingTime;
    private Status status;


    // The constructor will create a booking and automatically add the booking time to the
    // list of "booked times" belonging to the table.
    public BookingRequest(String firstName,
                          String lastName,
                          String email,
                          int numOfPeople,
                          Table table,
                          LocalDateTime bookingTime) {

        this.id = UUID.randomUUID().toString();
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.numOfPeople = numOfPeople;
        this.table = table;
        this.table.getBookings().add(bookingTime);

        this.bookingTime = bookingTime;
        this.status = Status.PENDING;

    }

    public enum Status {
        APPROVED, DENIED, PENDING
    }

}


