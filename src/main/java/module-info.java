module com.optimization {
    requires javafx.controls;
    requires javafx.fxml;  // Если используете FXML (в вашем коде не обязательно, но добавьте на всякий случай)
    requires org.json;     // Для библиотеки JSON

    opens com.optimization to javafx.fxml;  // Если используете FXML, иначе можно убрать
    exports com.optimization;  // Экспортирует ваш пакет для доступа
}