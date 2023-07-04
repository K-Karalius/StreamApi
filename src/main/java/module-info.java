module com.app.streamapi {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.opencsv;


    opens com.app.streamapi to javafx.fxml;
    exports com.app.streamapi;
}