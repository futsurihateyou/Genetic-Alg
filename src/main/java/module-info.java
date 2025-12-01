module com.optimization {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;

    opens com.optimization to javafx.fxml;
    exports com.optimization;
}