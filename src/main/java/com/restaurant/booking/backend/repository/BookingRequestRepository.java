package com.restaurant.booking.backend.repository;

import com.restaurant.booking.backend.model.BookingRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BookingRequestRepository extends MongoRepository<BookingRequest, String> {

    List<BookingRequest> getRequestsByStatus(BookingRequest.Status status);
}
