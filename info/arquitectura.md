# Arquitectura del proyecto

Resumen conciso de la arquitectura y decisiones de diseño.

- Visión general:
  - Aplicación cliente/servidor para chat, con opción GUI (JavaFX) y clientes por línea de comandos.
  - Código separado en paquetes: interfaz, controladores y servicios de red (`servicios`).

- Componentes principales:
  - `App` (JavaFX): arranca la UI y lanza el servidor internamente en segundo plano.
  - `MainController`: controla la UI, gestiona sesiones locales y muestra mensajes.
  - `Servidor`: escucha conexiones, crea `HandlerCliente` por conexión.
  - `HandlerCliente`: gestiona comunicación con un cliente, recibe y reenvía mensajes.
  - `SalaChat`: mantiene participantes y reparte mensajes de forma thread-safe.

- Principios de diseño:
  - Separación de responsabilidades: cada clase tiene una única responsabilidad clara.
  - Concurrencia controlada: pool fijo de hilos en servidor y `ConcurrentHashMap` para participantes.
  - Simplicidad: comportamiento explícito y mínimo código necesario para cumplir requisitos.

- Puntos de extensión:
  - Sustituir `SalaChat` por una implementación persistente.
  - Añadir autenticación o cifrado de mensajes.
  - Desacoplar servidor de la UI para desplegarlo como servicio independiente.

