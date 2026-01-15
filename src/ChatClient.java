/**
 * Cliente de chat.
 *
 * - Un hilo lee del servidor y lo imprime.
 * - El hilo principal lee del teclado y envía.
 */
private static final String HOST = "localhost";
private static final int PUERTO = 8080;

void main() {

    try (
            Socket socket = new Socket(HOST, PUERTO);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in)
    ) {
        // Hilo lector: imprime todo lo que llega del servidor.
        Thread reader = new Thread(() -> {
            try {
                String line;
                while ((line = entrada.readLine()) != null) {
                    IO.println(line);
                }
            } catch (IOException e) {
                // Silencioso: normalmente ocurre al cerrar el socket.
            }
        }, "server-reader");
        reader.setDaemon(true);
        reader.start();

        // Hilo principal: envía lo que escribe el usuario.
        while (true) {
            if (!scanner.hasNextLine()) {
                break;
            }
            String msg = scanner.nextLine();
            salida.println(msg);
            if (msg.equalsIgnoreCase("salir")) {
                break;
            }
        }

    } catch (IOException e) {
        System.err.println("Error de conexión: " + e.getMessage());
    }
}

