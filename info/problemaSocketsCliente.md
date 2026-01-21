# Problema del primer cliente tratado diferente

## El fallo
El primer cliente que se conectaba era tratado de forma especial. Solo él tenía un hilo de escucha activo, los demás clientes no recibían mensajes correctamente y la lista de usuarios conectados no se actualizaba bien.

## La causa
Había una condición `if (sesionesActivas.size() == 1)` que solo iniciaba escucha para el primer socket. También existía una variable compartida `socketEscuchaActual` que provocaba conflictos.

## La solución
Ahora cada cliente tiene su propio hilo de escucha independiente:
- Se eliminó `socketEscuchaActual`
- Se llama `iniciarEscucha(sesion)` para **cada** cliente nuevo
- Cada hilo escucha solo su propio socket
- Cuando un socket se cierra, la sesión se elimina automáticamente

## Resultado
Todos los clientes funcionan igual. La lista de usuarios conectados se actualiza correctamente al entrar o salir cualquier cliente.

---

# Problema de mensajes duplicados en el chat

## El fallo
Al crear múltiples clientes locales, los mensajes del servidor (como "X se ha unido" o "Y ha abandonado la sala") aparecían duplicados múltiples veces en el chat general.

## La causa
Cada sesión local tenía su propio hilo escuchando del servidor. Cuando el servidor hacía broadcast de un mensaje a todos los clientes, cada hilo lo recibía y lo mostraba. Con 3 clientes locales, el mensaje aparecía 3 veces.

## La solución
Usar un **único hilo de escucha compartido** para el chat general:
- Se añadió `socketEscuchaCompartido` que comparten todas las sesiones locales
- Solo el primer cliente inicia el hilo de escucha, los demás reutilizan el mismo
- Si el socket compartido se cierra, el siguiente cliente crea uno nuevo

## Resultado
Los mensajes del chat general aparecen una sola vez, sin importar cuántos clientes locales estén conectados.

---

# Problema de mensajes de salida solo del primer cliente

## El fallo
Solo aparecía en el chat cuando el primer usuario abandonaba la sala. Los demás usuarios al salir no mostraban ningún mensaje. Además, el nombre del primer usuario no aparecía, solo ": ha abandonado la sala".

## La causa
Solo el socket compartido (el del primer cliente) estaba siendo escuchado. Cuando otros clientes se desconectaban, sus sockets no estaban activos en el hilo de escucha, por lo que sus mensajes de salida no se recibían en el chat.

## La solución
Cuando se cierra el socket compartido, asignar automáticamente otro socket activo:
- Detectar si el socket que se cierra es el `socketEscuchaCompartido`
- Si quedan sesiones activas, usar el socket del primer cliente restante
- Iniciar nuevo hilo de escucha con el nuevo socket compartido

## Resultado
Todos los mensajes de salida aparecen correctamente en el chat, sin importar qué cliente abandone la sala.

