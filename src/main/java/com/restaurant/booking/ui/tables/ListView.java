package com.restaurant.booking.ui.tables;

import com.restaurant.booking.backend.model.Table;
import com.restaurant.booking.backend.service.TableService;
import com.restaurant.booking.ui.MainLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "tables", layout = MainLayout.class)
public class ListView extends VerticalLayout {

    private final TableService tableService;

    private Grid<Table> tableGrid = new Grid<>(Table.class);

    @Autowired
    public ListView(TableService tableService) {
        this.tableService = tableService;

        addClassName("list-view");
        setSizeFull();
        configureGrid();
        add(tableGrid);

        tableGrid.setItems(tableService.getTables());
    }

    private void configureGrid() {
        tableGrid.addClassName("table-grid");
        tableGrid.setSizeFull();
        tableGrid.setColumns("id", "availableSeats");
    }

}
