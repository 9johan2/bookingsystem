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

    public BookingRequest() {
        this.id = UUID.randomUUID().toString();
        this.status = Status.PENDING;
    }

    public enum Status {
        APPROVED, DENIED, PENDING
    }
}
