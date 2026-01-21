# Chat Multicliente con Sockets

Chat grupal en tiempo real usando **sockets + threads** en Java con interfaz **JavaFX**.

## Requisitos

- Java 21
- Maven

## Componentes

- **Servidor**: gestiona conexiones concurrentes con pool de threads
- **HandlerCliente**: hilo independiente por cada cliente conectado
- **Cliente JavaFX**: interfaz gráfica con chat en tiempo real

## Ejecución

**Servidor:**
- Click derecho en `LauncherServidor.java` → Run

**Clientes** (en diferentes terminales o IDEs):
- Click derecho en `Launcher.java` → Run
- Ingresa tu nombre cuando se solicite

## Compilación

```bash
javac -d out $(find src -name "*.java")
```

O con Maven:
```bash
mvn clean compile
```

## Uso

1. Inicia el servidor (puerto 8080)
2. Conecta clientes (puedes abrir múltiples instancias)
3. Escribe mensajes en la interfaz
4. Los mensajes se envían a todos los conectados



## Arquitectura

```
Servidor (puerto 8080)
  ↓
HandlerCliente (hilo por cliente)
  ↓
Broadcast → Todos los clientes
```

**Flujo:**
1. Cliente conecta y envía nombre
2. Servidor notifica: "Usuario se ha unido"
3. Mensajes formato: `[NOMBRE]: mensaje`
4. Al salir: "Usuario se ha ido"
