package com.restaurant.booking.backend.service;

import com.restaurant.booking.backend.model.Table;
import com.restaurant.booking.backend.repository.TableRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service
public class TableService{

    TableRepository tableRepository;

    public List<Table> getTables() {
        return tableRepository.findAll();
    }

    // Will create a list with all tables and then remove the tables that does not match with the number of people
    // in the booking or the booking time. This method assumes that a restaurant visit takes 2 hours.
    public List<Table> getAvailableTables(LocalDateTime requestedTime, int numOfPeople) {
        List<Table> tables = getTables();

        tables.removeIf(new Predicate<Table>() {
            @Override
            public boolean test(Table table) {
                for (LocalDateTime time: table.getBookings()) {
                    if (requestedTime.equals(time)) {
                        return true;
                    }
                    if (requestedTime.isBefore(time) && (requestedTime.plusHours(1).isAfter(time) || requestedTime.plusHours(1).equals(time))) {
                        return true;
                    }
                    if (requestedTime.isAfter(time) && (!requestedTime.isAfter(time.plusHours(1)) || requestedTime.equals(time.plusHours(1)) )) {
                        return true;
                    }
                }
                return false;
            }
        });
        tables.removeIf(table -> table.getAvailableSeats() < numOfPeople);

        return tables;
    }

    // Method used for getting all possible tableSizes from the database
    public Set<Integer> getTableSizes() {
        Set<Integer> tableSizes = new HashSet<>();

        getTables().forEach(table -> tableSizes.add(table.getAvailableSeats()));

        return tableSizes;
    }

    public void save(Table table) {
        if (table != null) {
            tableRepository.save(table);
        }
    }

    @PostConstruct
    public void addTestData() {
        if (tableRepository.count() == 0) {
            tableRepository.saveAll(Arrays.asList(
                    new Table(1, 2),
                    new Table(2, 2),
                    new Table(3, 4),
                    new Table(4, 6)
            ));

        }
    }
}
