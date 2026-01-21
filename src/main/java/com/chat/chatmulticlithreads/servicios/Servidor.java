package com.chat.chatmulticlithreads.servicios;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Servidor {
    private static final int PUERTO = 8080;
    private static final int MAX_CLIENTES = 4;
    private static final AtomicInteger clientesConectados = new AtomicInteger(0);

    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(MAX_CLIENTES);
        System.out.println("Servidor iniciado en puerto " + PUERTO);
        SalaChat sala = new SalaChat("General");

        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            while (true) {
                Socket clienteSocket = serverSocket.accept();
                int numCliente = clientesConectados.incrementAndGet();
                System.out.println("Cliente #" + numCliente + " conectado: " + clienteSocket.getInetAddress());
                pool.execute(new HandlerCliente(clienteSocket, numCliente, sala));
            }
        } catch (IOException e) {
            System.err.println("Error en servidor: " + e.getMessage());
        } finally {
            pool.shutdown();
        }
    }
}
