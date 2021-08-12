package com.restaurant.booking.backend.controller;

import com.restaurant.booking.backend.model.BookingRequest;
import com.restaurant.booking.backend.service.BookingRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/activeRequests")
public class BookingRequestController {

    private final BookingRequestService service;

    @Autowired
    public BookingRequestController(BookingRequestService service) {
        this.service = service;
    }

    @GetMapping
    public List<BookingRequest> getAllRequests() {
        return service.getAllRequests();
    }

}
