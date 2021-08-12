package com.restaurant.booking.backend.repository;

import com.restaurant.booking.backend.model.Table;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TableRepository extends MongoRepository<Table, Integer> {

}
