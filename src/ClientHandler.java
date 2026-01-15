import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Maneja un cliente en un hilo separado.
 *
 * Protocolo (líneas):
 * - Al conectar, el servidor pide un nombre.
 * - Comandos:
 *   /salir  -> desconecta
 *   /help   -> ayuda
 * - Cualquier otra línea se emite a la sala (chat grupal).
 */
public class ClientHandler implements Runnable {
    private final Socket socket;
    private final int numeroCliente;
    private final ChatRoom room;

    private PrintWriter salida;
    private String nombre;
    private final AtomicBoolean running = new AtomicBoolean(true);

    public ClientHandler(Socket socket, int numeroCliente, ChatRoom room) {
        this.socket = socket;
        this.numeroCliente = numeroCliente;
        this.room = room;
    }

    @Override
    public void run() {
        try (
                BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter salida = new PrintWriter(socket.getOutputStream(), true)
        ) {
            // Guardar el PrintWriter para uso en room.broadcast
            this.salida = salida;

            salida.println("Bienvenido. Estás en la sala: " + room.getNombre());
            salida.println("Escribe tu nombre:");

            String n = entrada.readLine();
            if (n == null || n.trim().isEmpty()) {
                n = "Cliente" + numeroCliente;
            }
            nombre = n.trim();

            room.entrarSala(this);

            salida.println("✅ Conectado como '" + nombre + "'");
            room.broadcastExcept(this, "🔔 '" + nombre + "' se ha unido al chat. (" + room.size() + " en línea)");

            String mensaje;
            while (running.get() && (mensaje = (">> " + entrada.readLine())) != null) {
                String m = mensaje.trim();
                if (m.isEmpty()) continue;

                if (m.equalsIgnoreCase("salir")) {
                    salida.println("👋 Hasta pronto!");
                    break;
                }

                String line = "[" + nombre + "] " + mensaje;
                System.out.println(line);
                // Importante: no reenviar al mismo cliente; el cliente imprime su propio mensaje localmente.
                room.broadcastExcept(this, line);
            }

        } catch (IOException e) {
            System.err.println("Error con cliente #" + numeroCliente + ": " + e.getMessage());
        } finally {
            limpiar();
        }
    }

    public void enviar(String mensaje) {
        PrintWriter salida1 = this.salida;
        if (salida1 != null) {
            salida1.println(mensaje);
        }
    }

    private void limpiar() {
        if (!running.compareAndSet(true, false)) {
            return;
        }
        room.abandonarSala(this);
        if (nombre != null) {
            room.broadcast("🔕 '" + nombre.toUpperCase() + "' ha salido. (" + room.size() + " en línea)");
        }
        try {
            socket.close();
        } catch (IOException ignored) {
        }
        System.out.println("Cliente #" + numeroCliente + " desconectado");
    }
}
