package com.restaurant.booking.backend.controller;

import com.restaurant.booking.backend.model.Table;
import com.restaurant.booking.backend.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/tables")
public class TableController {

    private final TableService tableService;

    @Autowired
    public TableController(TableService tableService) {
        this.tableService = tableService;
    }

    @GetMapping()
    public List<Table> getTables(
            @RequestParam(value = "requestedTime", required = false)LocalDateTime requestedTime,
            @RequestParam(value = "numOfPeople", required = false) Integer numOfPeople
            ) {
        if (requestedTime != null && numOfPeople != null) {
            return tableService.getAvailableTables(requestedTime, numOfPeople);
        }
        return tableService.getTables();
    }

    @PutMapping
    public void save(@RequestParam("table") Table table) {
        if (table != null) {
            tableService.save(table);
        }
    }
}
