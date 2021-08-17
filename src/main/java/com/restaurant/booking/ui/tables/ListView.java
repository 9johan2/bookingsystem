package com.restaurant.booking.ui.tables;

import com.restaurant.booking.backend.controller.TableController;
import com.restaurant.booking.backend.model.Table;
import com.restaurant.booking.ui.MainLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "tables", layout = MainLayout.class)
public class ListView extends VerticalLayout {

    private final TableController tableController;

    private Grid<Table> tableGrid = new Grid<>(Table.class);

    @Autowired
    public ListView(TableController tableController) {
        this.tableController = tableController;

        addClassName("list-view");
        setSizeFull();
        configureGrid();

        H3 header = new H3("All tables");
        add(header, tableGrid);

        tableGrid.setItems(tableController.getTables(null, null));
    }

    private void configureGrid() {
        tableGrid.addClassName("table-grid");
        tableGrid.setSizeFull();
        tableGrid.setColumns("id", "availableSeats");
    }
}
