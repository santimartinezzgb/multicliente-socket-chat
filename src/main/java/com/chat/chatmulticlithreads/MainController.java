package com.chat.chatmulticlithreads;

import java.io.*;
import java.net.Socket;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
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
            salida.println("[" + nombreCliente.toUpperCase() + "]: " + msg);
            agregarMensajePropio(msg);
            input_mensaje.clear();
        }
    }

    private void agregarMensajePropio(String texto) {
        Label label = new Label(texto); // Mensaje enviado
        label.setWrapText(true); // Permitir salto de línea
        label.getStyleClass().add("mensaje"); // Clase CSS para mensajes
        label.getStyleClass().add("mensaje_enviado"); // Clase CSS para mensajes enviados

        HBox contenedor = new HBox(label); // Contenedor para alinear el mensaje
        contenedor.setAlignment(Pos.CENTER_RIGHT);
        contenedor_chat_cuerpo.getChildren().add(contenedor); // Agregar al contenedor principal
        chat.setVvalue(1.0); // Desplazar al final del chat
    }

    private void agregarMensaje(String texto) {
        if (texto.startsWith("[" + nombreCliente.toUpperCase() + "]: ")) { // Ignorar mensajes propios
            return;
        }
        Label label = new Label(texto); // Mensaje recibido
        label.setWrapText(true);// Permitir salto de línea
        label.getStyleClass().add("mensaje"); // Clase CSS para mensajes

        HBox contenedor = new HBox(label); // Contenedor para alinear el mensaje

        // Detectar mensajes del sistema (unirse/salir) y centrarlos
        if (texto.contains("se ha unido") || texto.contains("se ha ido")) {
            label.getStyleClass().add("mensaje_info"); // Clase CSS para mensajes del sistema
            contenedor.setAlignment(Pos.CENTER);
        } else {
            label.getStyleClass().add("mensaje_recibido"); // Clase CSS para mensajes recibidos
            contenedor.setAlignment(Pos.CENTER_LEFT);
        }

        contenedor_chat_cuerpo.getChildren().add(contenedor); // Agregar al contenedor principal
        chat.setVvalue(1.0); // Desplazar al final del chat
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
