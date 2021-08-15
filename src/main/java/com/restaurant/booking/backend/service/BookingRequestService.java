package com.restaurant.booking.backend.service;

import com.restaurant.booking.backend.model.BookingRequest;
import com.restaurant.booking.backend.repository.BookingRequestRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class BookingRequestService {

    private final BookingRequestRepository repository;
    private final MongoTemplate mongoTemplate;

    public List<BookingRequest> getBookings(BookingRequest.Status status) {
        if (status == BookingRequest.Status.PENDING) {
            return repository.getRequestsByStatus(BookingRequest.Status.PENDING);
        } else {
            Query query = new Query();
            query.addCriteria(Criteria.where("bookingTime").gt(LocalDateTime.now()));
            query.addCriteria(Criteria.where("status").ne(BookingRequest.Status.PENDING));
            return mongoTemplate.find(query, BookingRequest.class);
        }
    }

    public List<BookingRequest> getAllBookings() {
        return repository.findAll();
    }

    public Optional<BookingRequest> getByID(String id) {
        return repository.findById(id);
    }

    public void save(BookingRequest bookingRequest) {
        if (bookingRequest != null) {
            repository.save(bookingRequest);
        }
    }
}
