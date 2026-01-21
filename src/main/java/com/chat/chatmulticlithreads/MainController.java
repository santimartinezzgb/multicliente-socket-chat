package com.chat.chatmulticlithreads;

import java.io.*;
import java.net.Socket;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class MainController {
    @FXML
    private VBox contenedor_chat_cuerpo;
    @FXML
    private TextField input_mensaje;
    @FXML
    private ScrollPane chat;
    @FXML
    private Label label_titulo_chat;

    private Socket socket;
    private PrintWriter salida;
    private String nombreCliente;

    public void setNombreCliente(String nombre) {
        this.nombreCliente = nombre;
        label_titulo_chat.setText(nombre);
    }

    public void conectarAlServidor() {
        try {
            socket = new Socket("localhost", 8080);
            salida = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            salida.println(nombreCliente);

            new Thread(() -> {
                try {
                    String msg;
                    while ((msg = entrada.readLine()) != null) {
                        String m = msg;
                        Platform.runLater(() -> agregarMensaje(m));
                    }
                } catch (IOException e) {
                }
            }).start();
        } catch (IOException e) {
            agregarMensaje("Error: " + e.getMessage());
        }
    }

    @FXML
    private void enviarMensaje() {
        String msg = input_mensaje.getText().trim();
        if (!msg.isEmpty() && salida != null) {
            salida.println(nombreCliente.toUpperCase() + " → " + msg);
            agregarMensajePropio(msg);
            input_mensaje.clear();
        }
    }

    private void agregarMensajePropio(String texto) {
        Label label = new Label(texto);
        label.setWrapText(true);
        label.getStyleClass().add("mensaje");
        label.getStyleClass().add("mensaje_enviado");
        contenedor_chat_cuerpo.getChildren().add(label);
        chat.setVvalue(1.0);
    }

    private void agregarMensaje(String texto) {
        if (texto.startsWith(nombreCliente.toUpperCase() + " → ")) {
            return;
        }
        Label label = new Label(texto);
        label.setWrapText(true);
        label.getStyleClass().add("mensaje");
        label.getStyleClass().add("mensaje_recibido");
        contenedor_chat_cuerpo.getChildren().add(label);
        chat.setVvalue(1.0);
    }

    public void desconectar() {
        try {
            if (salida != null)
                salida.println("salir");
            if (socket != null)
                socket.close();
        } catch (IOException ignored) {
        }
    }
}
