module com.chat.chatmulticlithreads {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.graphics;


    opens com.chat.chatmulticlithreads to javafx.fxml;
    exports com.chat.chatmulticlithreads;
}