package com.chat.chatmulticlithreads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class MainController {
    @FXML
    private VBox contenedor_chat_cuerpo;
    @FXML
    private VBox lista_clientes;
    @FXML
    private TextField input_nuevo_nombre;
    @FXML
    private ScrollPane chat;
    @FXML
    private Label label_titulo_chat;

    private final List<SesionCliente> sesionesActivas = new ArrayList<>();
    private final String HOST = "localhost";
    private final int PORT = 8080;
    private Socket socketEscuchaCompartido = null;

    @FXML
    private void conectarNuevoCliente() {
        String nombre = input_nuevo_nombre.getText().trim();
        if (nombre.isEmpty())
            return;

        try {
            Socket socket = new Socket(HOST, PORT);
            PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);

            // Enviar el nombre al servidor inmediatamente para cumplir con el protocolo
            salida.println(nombre);

            // Creamos la sesión pero la tarjeta se creará después
            SesionCliente sesion = new SesionCliente(nombre, salida, null, socket);
            sesionesActivas.add(sesion);

            // Usar un único hilo de escucha compartido para el chat general
            if (socketEscuchaCompartido == null || socketEscuchaCompartido.isClosed()) {
                socketEscuchaCompartido = socket;
                iniciarEscuchaCompartida();
            }

            crearTarjetaCliente(sesion);
            // Reconstruir la lista visible para evitar inconsistencias
            actualizarListaClientes();
            input_nuevo_nombre.clear();

        } catch (IOException e) {
            agregarMensaje("Error al conectar a " + nombre + ": " + e.getMessage());
        }
    }

    private void iniciarEscuchaCompartida() {
        if (socketEscuchaCompartido == null) return;

        new Thread(() -> {
            try (BufferedReader entrada = new BufferedReader(new InputStreamReader(socketEscuchaCompartido.getInputStream()))) {
                String linea;
                while ((linea = entrada.readLine()) != null) {
                    final String msg = linea;
                    Platform.runLater(() -> {
                        if (msg.startsWith("[SYSTEM_COUNT]: ")) {
                            try {
                                String count = msg.replace("[SYSTEM_COUNT]: ", "").trim();
                                label_titulo_chat.setText("Sala General (" + count + " en línea)");
                            } catch (Exception ignored) {
                            }
                        } else {
                            agregarMensaje(msg);
                            chat.setVvalue(1.0);
                        }
                    });
                }
            } catch (Exception e) {
                // Conexión cerrada o error en el socket compartido
            } finally {
                // Marcar que el socket compartido se cerró
                socketEscuchaCompartido = null;
            }
        }, "Escucha-Compartida").start();
    }

    private void crearTarjetaCliente(SesionCliente sesion) {

        if (sesionesActivas.size() > 4) {
            alertaError("Máximo de clientes alcanzado",
                    "Se han alcanzado el número máximo de clientes. No se permite una cantidad de clientes superior a 4.");
            return;
        }

        // Crear la tarjeta del cliente
        VBox tarjetaCliente = new VBox();
        tarjetaCliente.getStyleClass().add("tarjeta_cliente");
        sesion.setTarjeta(tarjetaCliente);

        // Crear el nombre del cliente
        Label labelNombre = new Label(sesion.getNombre());
        labelNombre.getStyleClass().add("nombre_cliente");

        // Crear el area de entrada
        HBox areaEntrada = new HBox(5);
        TextField campoEntrada = new TextField();
        campoEntrada.setPromptText("Mensaje...");
        campoEntrada.getStyleClass().add("campo_entrada");
        HBox.setHgrow(campoEntrada, javafx.scene.layout.Priority.ALWAYS);

        // Crear el boton de enviar
        Button botonEnviar = new Button("Enviar");
        botonEnviar.getStyleClass().add("boton_enviar");

        // Crear la accion de enviar
        Runnable enviarMensaje = () -> {
            String msg = campoEntrada.getText().trim();
            if (msg.isEmpty())
                return;

            if (msg.equalsIgnoreCase("salir")) {
                sesion.getSalida().println("salir");

                // Eliminar la sesión y reconstruir la lista
                Platform.runLater(() -> {
                    sesionesActivas.remove(sesion);
                    actualizarListaClientes();
                });

                try {
                    sesion.getSocket().close();
                } catch (IOException ignored) {
                }
            } else {
                String nombre = sesion.getNombre().toUpperCase();
                String mensajeFinal = nombre + " → " + msg;
                sesion.getSalida().println(mensajeFinal);
            }
            campoEntrada.clear();
        };

        // Asignar la accion al campo de entrada y al boton
        campoEntrada.setOnAction(e -> enviarMensaje.run());
        botonEnviar.setOnAction(e -> enviarMensaje.run());

        // Agregar los componentes a la tarjeta
        areaEntrada.getChildren().addAll(campoEntrada, botonEnviar);
        tarjetaCliente.getChildren().addAll(labelNombre, areaEntrada);

    }


    private void actualizarListaClientes() {
        Platform.runLater(() -> {
            lista_clientes.getChildren().clear();
            for (SesionCliente s : sesionesActivas) {
                if (s.getTarjeta() == null) {
                    crearTarjetaCliente(s);
                }
                if (s.getTarjeta() != null) {
                    lista_clientes.getChildren().add(s.getTarjeta());
                }
            }
        });
    }

    private void agregarMensaje(String texto) {

        // Crear el contenedor del mensaje
        VBox contenedorMensaje = new VBox(2);

        // Crear el label del mensaje
        Label label = new Label(texto);
        label.setWrapText(true);
        label.getStyleClass().addAll("mensaje", "mensaje_recibido");

        // Agregar el label al contenedor
        contenedorMensaje.getChildren().add(label);

        // Agregar el contenedor al cuerpo del chat
        contenedor_chat_cuerpo.getChildren().add(contenedorMensaje);
    }

    private void alertaError(String titulo, String texto) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(texto);
        alert.showAndWait();
    }

    // Clase interna para gestionar la sesión de cada cliente local
    private static class SesionCliente {
        private final String nombre;
        private final PrintWriter salida;
        private final Socket socket;
        private VBox tarjeta;

        public SesionCliente(String nombre, PrintWriter salida, VBox tarjeta, Socket socket) {
            this.nombre = nombre;
            this.salida = salida;
            this.tarjeta = tarjeta;
            this.socket = socket;
        }

        public String getNombre() {
            return nombre;
        }

        public PrintWriter getSalida() {
            return salida;
        }

        public VBox getTarjeta() {
            return tarjeta;
        }

        public void setTarjeta(VBox tarjeta) {
            this.tarjeta = tarjeta;
        }

        public Socket getSocket() {
            return socket;
        }
    }
}
