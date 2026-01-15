import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Sala de chat simple (broadcast).
 *
 * Mantiene un conjunto de participantes y permite enviar mensajes a todos.
 */
public class ChatRoom {
    private final String nombre;
    private final Set<ClientHandler> participantes = ConcurrentHashMap.newKeySet();

    public ChatRoom(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void entrarSala(ClientHandler handler) {
        participantes.add(handler);
    }

    public void abandonarSala(ClientHandler handler) {
        participantes.remove(handler);
    }

    public int size() {
        return participantes.size();
    }

    public void broadcast(String mensaje) { // Enviar mensaje a todos los participantes
        for (ClientHandler p : participantes) {
            p.enviar(mensaje);
        }
    }

    public void broadcastExcept(ClientHandler origen, String mensaje) { // Enviar mensaje a todos excepto al origen
        for (ClientHandler p : participantes) {
            if (p != origen) {
                p.enviar(mensaje);
            }
        }
    }
}

