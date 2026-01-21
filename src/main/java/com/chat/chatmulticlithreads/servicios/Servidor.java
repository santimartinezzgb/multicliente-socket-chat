package com.chat.chatmulticlithreads.servicios;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Servidor {
    private static final int PUERTO = 8080;
    public static final Set<HandlerCliente> clientes = ConcurrentHashMap.newKeySet();

    public static void main(String[] args) {
        ExecutorService pool = Executors.newCachedThreadPool();
        System.out.println("Servidor iniciado en puerto " + PUERTO);

        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Cliente conectado: " + socket.getInetAddress());
                pool.execute(new HandlerCliente(socket));
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static void broadcast(String mensaje) {
        clientes.forEach(c -> c.enviar(mensaje));
    }
}
