package com.chat.chatmulticlithreads.servicios;

import java.io.PrintWriter;
import java.net.Socket;
import javafx.scene.layout.VBox;

public class SesionCliente {
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