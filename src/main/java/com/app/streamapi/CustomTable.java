package com.app.streamapi;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class CustomTable extends TableView<Person> {

    private String[] columnTitles;
    public CustomTable(String[] columnTitles){
        this.columnTitles = columnTitles;
        autosize();
        setEditable(false);
        setUpTableColumns();
    }

    public void setUpTableColumns(){
        addColumn(columnTitles[0], "firstName");
        addColumn(columnTitles[1], "lastName");
        addColumn(columnTitles[2], "email");
        addColumn(columnTitles[3], "imageLink");
        addColumn(columnTitles[4], "ipAddress");
    }
    public void addColumn(String title, String property){
        TableColumn<Person, String> column = new TableColumn<>(title);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        column.setSortable(false);
        column.setEditable(false);
        getColumns().add(column);
    }
}
