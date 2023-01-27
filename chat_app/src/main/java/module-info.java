module fr.insa.chat_app {
    requires javafx.controls;
    requires javafx.fxml;
	requires java.sql;
	requires java.desktop;
	requires javafx.base;
	requires javafx.graphics;

    opens fr.insa.chat_app to javafx.fxml;
    exports fr.insa.chat_app;
}
