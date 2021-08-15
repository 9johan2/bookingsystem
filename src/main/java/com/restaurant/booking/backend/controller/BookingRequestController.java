package com.restaurant.booking.backend.controller;

import com.restaurant.booking.backend.model.BookingRequest;
import com.restaurant.booking.backend.service.BookingRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/bookings")
public class BookingRequestController {

    private final BookingRequestService service;

    @Autowired
    public BookingRequestController(BookingRequestService service) {
        this.service = service;
    }

    @GetMapping
    public List<BookingRequest> getAllBookings() {
        return service.getAllBookings();
    }

    @GetMapping("/status")
    public List<BookingRequest> getBooking(
            @RequestParam(value = "status", required = false) BookingRequest.Status status) {

        return service.getBookings(status);
    }

    @GetMapping("/id")
    public Optional<BookingRequest> getBookingById(
            @RequestParam(value = "id", required = false) String id) {

        return service.getByID(id);
    }

    @PostMapping
    public void save(BookingRequest bookingRequest) {
        service.save(bookingRequest);
    }

    @PutMapping
    public void update(BookingRequest bookingRequest) {
        service.save(bookingRequest);
    }


}
