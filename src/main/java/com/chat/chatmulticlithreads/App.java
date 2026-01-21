package com.chat.chatmulticlithreads;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        Thread serverThread = new Thread(() -> {
            com.chat.chatmulticlithreads.servicios.Servidor.main(null);
        });

        serverThread.setDaemon(true);
        serverThread.start();

        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("index.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 700);
        stage.setTitle("Chat multicliente");
        stage.setScene(scene);
        stage.show();
    }

}
