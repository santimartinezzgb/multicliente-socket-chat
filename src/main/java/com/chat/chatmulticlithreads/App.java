package com.chat.chatmulticlithreads;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

public class App extends Application {
    private static boolean servidorIniciado = false;

    @Override
    public void start(Stage stage) throws IOException {
        if (!servidorIniciado) {
            new Thread(() -> com.chat.chatmulticlithreads.servicios.Servidor.main(null)).start();
            servidorIniciado = true;
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
            }
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Chat");
        dialog.setHeaderText("¿Cómo te llamas?");
        dialog.setContentText("Tu nombre:");

        String nombre = dialog.showAndWait().orElse("").trim();
        if (nombre.isEmpty()) {
            return;
        }

        FXMLLoader loader = new FXMLLoader(App.class.getResource("index.fxml"));
        Scene scene = new Scene(loader.load(), 380, 650);
        MainController controller = loader.getController();
        controller.setNombreCliente(nombre);
        controller.conectarAlServidor();

        stage.setTitle(nombre);
        stage.setScene(scene);
        stage.setOnCloseRequest(e -> controller.desconectar());
        stage.show();
    }
}
