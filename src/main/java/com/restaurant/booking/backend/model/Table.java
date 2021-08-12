package com.restaurant.booking.backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection="tables")
public class Table {

    @Id
    private int id;
    private int availableSeats;
    private List<LocalDateTime> bookings;

    public Table(int id, int availableSeats) {
        this.id = id;
        this.availableSeats = availableSeats;
        this.bookings = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Table: " + id + " - available seats: " + availableSeats;
    }
}
