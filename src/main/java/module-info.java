module com.chat.chatmulticlithreads {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.chat.chatmulticlithreads to javafx.fxml;
    exports com.chat.chatmulticlithreads;
}