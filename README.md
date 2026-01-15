# chat-socket

Chat grupal (multi-cliente) basado en **sockets + threads** en Java.

## Requisito

- **Java 25** (compilar y ejecutar con la misma versión). Comprueba con:

```bash
java -version
javac -version
```

## Qué incluye

- `ChatServer`: servidor multihilo con pool fijo.
- `ClientHandler`: un hilo por cliente (vía `ExecutorService`).
- `ChatRoom`: sala de chat (broadcast) usando estructura concurrente.
- `ChatClient`: cliente interactivo con lectura/escritura simultánea (hilo lector + consola).

## Cómo compilar

```bash
cd /home/santi/Rober/chat-socket
javac -d out $(find src -name "*.java")
```

## Cómo ejecutar

Servidor:

```bash
java -cp out ChatServer
```

Cliente (en otra terminal, puedes abrir 2 o más):

```bash
java -cp out ChatClient
```

Opcionalmente:

```bash
java -cp out ChatClient 127.0.0.1 8080
```

## Atajo (recomendado)

Hay un script que compila y arranca el servidor con Java 25:

```bash
./run-java25.sh
```

## Uso

- Al entrar, el servidor pide tu nombre.
- Escribe mensajes normales para enviarlos al grupo.
- Comandos:
  - `/help`
  - `/salir` (o `salir`)
