package com.chat.chatmulticlithreads.servicios;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * - Maneja la comunicación con un cliente específico en la sala de chat.
 * - Permite recibir mensajes del cliente y enviar mensajes a todos los clientes
 * en la sala.
 * - Cada instancia de esta clase se ejecuta en su propio hilo.
 */

public class HandlerCliente implements Runnable {
    private final Socket socket;
    private final int numeroCliente;
    private final SalaChat sala;

    private PrintWriter salida;
    private String nombre;
    private final AtomicBoolean enEjecucion = new AtomicBoolean(true);

    public HandlerCliente(Socket socket, int numeroCliente, SalaChat sala) {
        this.socket = socket;
        this.numeroCliente = numeroCliente;
        this.sala = sala;
    }

    @Override
    public void run() {
        String palabraSalida = "salir";
        String mensajeSalida = " ha abandonado la sala.";

        try (

                BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter salida = new PrintWriter(socket.getOutputStream(), true)) {

            this.salida = salida;
            salida.println("Bienvenid@s a la sala: " + sala.getNombre());

            String linea = entrada.readLine();
            if (linea == null || linea.trim().isEmpty())
                linea = "Cliente" + numeroCliente;
            nombre = linea.trim();

            sala.entrarSala(this);
            sala.enviarMensajeTodos("'" + nombre + "' se ha unido al chat.");

            String mensaje;
            while ((mensaje = entrada.readLine()) != null) {
                String m = mensaje.trim();
                if (m.isEmpty())
                    continue;
                if (m.equalsIgnoreCase(palabraSalida)) {
                    salida.println(nombre.toUpperCase() + mensajeSalida);
                    break;
                }
                sala.enviarMensajeTodos(m);
            }

        } catch (IOException e) {
            System.err.println("Error con cliente #" + numeroCliente + ": " + e.getMessage());
        } finally {
            limpiar();
        }
    }

    public void enviar(String mensaje) {
        PrintWriter salidaActual = this.salida;
        if (salidaActual != null)
            salidaActual.println(mensaje);
    }

    private void limpiar() {
        if (!enEjecucion.compareAndSet(true, false))
            return;

        sala.abandonarSala(this);

        if (nombre != null) {
            String msgSalida = nombre.toUpperCase() + " ha abandonado la sala.";
            sala.enviarMensajeTodos(msgSalida);
        }
        try {
            socket.close();
        } catch (IOException ignored) {
            System.err.println("Error al cerrar el socket del cliente #" + numeroCliente);
        }

        System.out.println("Cliente #" + numeroCliente + " desconectado");
    }
}
