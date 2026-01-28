package com.chat.chatmulticlithreads.servicios;

import javax.net.ServerSocketFactory;
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

            Servidor.clientes.add(this); // Agregar el cliente a la lista
            Servidor.broadcast(nombre + " se ha unido"); // Notificar a todos los clientes

            String mensaje;
            while ((mensaje = entrada.readLine()) != null) {
                if (mensaje.equals("/crear")){
                    Grupo grupo1 = new Grupo("nuevoGrupo");
                    Servidor.grupos.add(grupo1);
                    Servidor.broadcast(grupo1.nombre);
                }
                Servidor.broadcast(nombre.toUpperCase() + ": " + mensaje);
            }
        } catch (IOException e) {

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
