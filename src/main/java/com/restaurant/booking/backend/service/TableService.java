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
        List<Table> allTables = getTables();
        List<Table> freeTables = getTables();
        freeTables.clear();

        for (Table table: allTables) {
            boolean free = false;
            if (table.getBookings().isEmpty()) {
                free = true;

            } else {
                for (LocalDateTime time: table.getBookings()) {
                    if (requestedTime.isBefore(time) && requestedTime.plusHours(1).isBefore(time)) {
                        free = true;
                        continue;
                    }
                    if (requestedTime.isAfter(time) && requestedTime.isAfter(time.plusHours(1))) {
                        free = true;
                    }
                }
            }

            if (free) {
                freeTables.add(table);
            }
        }

        freeTables.removeIf(table -> table.getAvailableSeats() < numOfPeople);

        return freeTables;

//        allTables.removeIf(new Predicate<Table>() {
//            @Override
//            public boolean test(Table table) {
//                for (LocalDateTime time: table.getBookings()) {
//                    if (requestedTime.isBefore(time) && requestedTime.plusHours(1).isBefore(time)) {
//                        return true;
//                    }
//                    if (!requestedTime.isAfter(time) && !requestedTime.isAfter(time.plusHours(1))) {
//                        return true;
//                    }
//                }
//                return false;
//            }
//        });
//        return allTables;

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
