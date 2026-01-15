import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Servidor de chat multihilo basado en sockets.
 *
 * - Acepta múltiples clientes concurrentemente usando un pool de hilos.
 * - Cada cliente se maneja en un Runnable independiente.
 * - Permite chat grupal (broadcast) en una "sala" por defecto.
 */
public class ChatServer {
    private static final int PUERTO = 8080;
    private static final int MAX_CLIENTES = 10;
    private static final AtomicInteger clientesConectados = new AtomicInteger(0);

    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(MAX_CLIENTES);

        System.out.println("Servidor multihilo iniciado en puerto " + PUERTO);
        System.out.println("Pool de hilos: " + MAX_CLIENTES);

        ChatRoom room = new ChatRoom("general");

        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            while (true) {
                Socket clienteSocket = serverSocket.accept();
                int numCliente = clientesConectados.incrementAndGet();

                System.out.println("Cliente #" + numCliente + " conectado: " + clienteSocket.getInetAddress());

                pool.execute(new ClientHandler(clienteSocket, numCliente, room));
            }
        } catch (IOException e) {
            System.err.println("Error en servidor: " + e.getMessage());
        } finally {
            pool.shutdown();
        }
    }
}

