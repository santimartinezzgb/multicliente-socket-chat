package com.chat.chatmulticlithreads.servicios;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChatCliente {
    private static final String HOST = "localhost";
    private static final int PUERTO = 8080;

    public static void main(String[] args) {

        try (
                Socket socket = new Socket(HOST, PUERTO);
                BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
                Scanner scanner = new Scanner(System.in)) {
            Thread reader = new Thread(() -> {
                try {
                    String linea;
                    while ((linea = entrada.readLine()) != null) {
                        System.out.println(linea);
                    }
                } catch (IOException e) {
                    System.err.println("Error de lectura: " + e.getMessage());
                }
            });

            // Hilo lector se ejecuta y envía los mensajes al servidor
            reader.setDaemon(true);
            reader.start();

            // Bucle principal para enviar mensajes al servidor
            while (true) {
                if (!scanner.hasNextLine()) {
                    break;
                }
                String msg = scanner.nextLine(); // Lee la entrada del cliente
                salida.println(msg); // Envía el mensaje al servidor
                if (msg.equalsIgnoreCase("salir")) {
                    break; // Si el cliente escribe "salir", sale del bucle
                }
            }

        } catch (IOException e) {
            System.err.println("Error de conexión: " + e.getMessage());
        }
    }
}
