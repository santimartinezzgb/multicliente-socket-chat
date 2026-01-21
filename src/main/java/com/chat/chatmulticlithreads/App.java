package com.chat.chatmulticlithreads;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Chat");
        dialog.setContentText("Tu nombre:");

        String nombre = dialog.showAndWait().orElse("").trim();
        if (nombre.isEmpty()) {
            return;
        }

        FXMLLoader loader = new FXMLLoader(App.class.getResource("index.fxml"));
        Scene scene = new Scene(loader.load(), 360, 640);
        MainController controller = loader.getController();
        controller.setNombreCliente(nombre);
        
        boolean conectado = controller.conectarAlServidor();
        if (!conectado) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de Conexión");
            alert.setHeaderText("No se pudo conectar al servidor");
            alert.setContentText("Asegúrate de que el servidor esté ejecutándose antes de iniciar el cliente.\n\nInicia primero LauncherServidor.java");
            alert.showAndWait();
            return;
        }

        stage.setTitle(nombre);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setOnCloseRequest(e -> controller.desconectar());
        stage.show();
    }
}
