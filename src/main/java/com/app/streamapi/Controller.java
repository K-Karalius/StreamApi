package com.app.streamapi;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Controller implements Initializable {

    private CustomTable table;  // custom table makes code a little cleaner
    private ObservableList<Person> data = FXCollections.observableArrayList();
    private final ObservableList<Person> copyOfData = FXCollections.observableArrayList();
    private String[] headers;   // size of 5 only

    private boolean FirstNameToLowerCase;
    private boolean LastNameToLowerCase;

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private ChoiceBox<String> cb_headers;
    @FXML
    private ChoiceBox<String> cb_sortingOrders;
    @FXML
    private TextField tf_filter;
    @FXML
    private Label label_tableSize;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        upload();
        setUpTable();

        setUpComboBoxes();
        updateTableSize();

        FirstNameToLowerCase = false;
        LastNameToLowerCase = false;
    }

    void setUpComboBoxes(){
        for(String header : headers){
            cb_headers.getItems().add(header);
        }

        cb_sortingOrders.getItems().addAll("Ascending","Descending");
    }
    void setUpTable(){
        table = new CustomTable(headers);
        anchorPane.getChildren().add(table);
        AnchorPane.setBottomAnchor(table, 10.0);
        AnchorPane.setLeftAnchor(table, 10.0);
        AnchorPane.setRightAnchor(table, 10.0);
        AnchorPane.setTopAnchor(table, 90.0);

        table.setItems(data);
    }
    void upload(){
        CSVReader reader;
        try {
            String csvLink = "https://klevas.mif.vu.lt/~ramdzm/MOCK_DATA.csv";
            URL url = new URL(csvLink);
            InputStreamReader inputStream = new InputStreamReader(url.openStream());
            CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
            reader = new CSVReaderBuilder(inputStream).withCSVParser(parser).build();

            String[] nextLine;
            if((nextLine = reader.readNext()) != null){
                headers = nextLine;
            }

            Person person;
            while ((nextLine = reader.readNext()) != null) {
                person = new Person();
                person.setData(nextLine);
                data.add(person);
                copyOfData.add(person);
            }
            reader.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    void updateTableSize(){
        label_tableSize.setText("Table size: " + data.size());
    }

    @FXML
    void toFilter(KeyEvent event){
        if(event.getCode() == KeyCode.ENTER){
            String selection = cb_headers.getValue();
            String filter = tf_filter.getText().trim().toLowerCase();
            if(selection != null && !filter.isEmpty()){
                data = data.stream()
                        .filter(person -> {
                            if (selection.equals(headers[0])) {
                                return person.getFirstName().toLowerCase().contains(filter);
                            } else if (selection.equals(headers[1])) {
                                return person.getLastName().toLowerCase().contains(filter);
                            } else if (selection.equals(headers[2])) {
                                return person.getEmail().contains(filter);
                            } else if (selection.equals(headers[3])) {
                                return person.getImageLink().contains(filter);
                            } else {
                                return person.getIpAddress().contains(filter);
                            }
                        })
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));

                updateTableSize();
                table.setItems(data);
            }

        }
    }


    @FXML
    void toSort(){
        String columnSelection = cb_headers.getValue();
        String orderSelection = cb_sortingOrders.getValue();
        if(columnSelection != null && orderSelection != null){
            Comparator<Person> comparator;
            if (columnSelection.equals(headers[0])) {
                comparator = Comparator.comparing(Person::getFirstName);
            } else if (columnSelection.equals(headers[1])) {
                comparator = Comparator.comparing(Person::getLastName);
            } else if (columnSelection.equals(headers[2])) {
                comparator = Comparator.comparing(Person::getEmail);
            } else if (columnSelection.equals(headers[3])) {
                comparator = Comparator.comparing(Person::getImageLink);
            } else {
                comparator = Comparator.comparing(Person::getIpAddress);
            }
            if(orderSelection.equals("Descending")) {
                comparator = comparator.reversed();
            }

           data = data.stream().sorted(comparator)
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
           table.setItems(data);
        }
    }

    @FXML
    void setFirstNameToLowerUpper() {
        if(FirstNameToLowerCase){
            data.replaceAll(person -> {
                person.setFirstName(person.getFirstName().toLowerCase());
                return person;
            });
            FirstNameToLowerCase = false;
        }else{
            data.replaceAll(person -> {
                person.setFirstName(person.getFirstName().toUpperCase());
                return person;
            });
            FirstNameToLowerCase = true;
        }
    }

    @FXML
    void setLastNameToLowerUpper() {
        if(LastNameToLowerCase){
            data.replaceAll(person -> {
                person.setLastName((person.getLastName().toLowerCase()));
                return person;
            });
            LastNameToLowerCase = false;
        }else{
            data.replaceAll(person -> {
                person.setLastName((person.getLastName().toUpperCase()));
                return person;
            });
            LastNameToLowerCase = true;
        }
    }

    @FXML
    void removeFilters(){
        data = FXCollections.observableArrayList(copyOfData);
        table.setItems(data);
        updateTableSize();
    }

    @FXML
    public void createMap(){
        Map<String, List<Person>> map = data.stream()
                .collect(Collectors.groupingBy(
                        person -> getFirstThreeDigits(person.getIpAddress())
                ));
        try {
            PrintWriter writer = new PrintWriter("output.txt");
            map.forEach((ipAddressPrefix, people) -> {
                writer.println("IP Address Prefix: " + ipAddressPrefix);
                people.forEach(person -> writer.println("Person: " + person.toString()));
                writer.println();
            });
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getFirstThreeDigits(String input) {
        String digits = input.replaceAll("[^0-9]", ""); // Remove non-digit characters
        if (digits.length() >= 3) {
            return digits.substring(0, 3);
        } else {
            return digits;
        }
    }
}