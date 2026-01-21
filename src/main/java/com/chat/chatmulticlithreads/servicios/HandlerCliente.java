package com.chat.chatmulticlithreads.servicios;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class HandlerCliente implements Runnable {
    private final Socket socket;
    private PrintWriter salida;
    private String nombre;

    public HandlerCliente(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            salida = new PrintWriter(socket.getOutputStream(), true);

            nombre = entrada.readLine();
            if (nombre == null || nombre.isEmpty())
                nombre = "Usuario";

            Servidor.clientes.add(this);
            Servidor.broadcast(nombre + " se ha unido");

            String mensaje;
            while ((mensaje = entrada.readLine()) != null) {
                if (mensaje.equalsIgnoreCase("salir"))
                    break;
                Servidor.broadcast(nombre.toUpperCase() + ": " + mensaje);
            }
        } catch (IOException e) {
            // Cliente desconectado
        } finally {
            Servidor.clientes.remove(this);
            if (nombre != null) {
                Servidor.broadcast(nombre + " se ha ido");
            }
            try {
                socket.close();
            } catch (IOException ignored) {
            }
        }
    }

    public void enviar(String mensaje) {
        if (salida != null)
            salida.println(mensaje);
    }
}
