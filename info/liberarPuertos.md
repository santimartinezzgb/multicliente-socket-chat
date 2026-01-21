# Cómo liberar puertos ocupados (Error: Address already in use)

Es común encontrarse con el error `java.net.BindException: Address already in use` cuando intentas iniciar el servidor y el puerto (en este caso el **8080**) sigue ocupado por una ejecución anterior que no se cerró correctamente.

Aquí tienes los pasos para solucionarlo en Linux:

## 1. Identificar el proceso que usa el puerto
Usa el comando `lsof` para ver qué proceso tiene bloqueado el puerto:

```bash
lsof -i :8080
```

Esto te devolverá una tabla similar a esta:
```text
COMMAND   PID  USER   FD   TYPE DEVICE SIZE/OFF NODE NAME
java    84258 santi   39u  IPv6 380687      0t0  TCP *:http-alt (LISTEN)
```

Lo más importante aquí es el **PID** (identificador del proceso), que en este ejemplo es `84258`.

## 2. Terminar el proceso
Una vez que tienes el PID, puedes "matar" el proceso para liberar el puerto usando el comando `kill`:

```bash
kill -9 <PID>
```
*(Sustituye `<PID>` por el número que obtuviste en el paso anterior, por ejemplo: `kill -9 84258`)*

## Otros comandos útiles

### Usando fuser
También puedes identificar y matar el proceso en un solo comando con `fuser`:
```bash
fuser -k 8080/tcp
```

### Comprobar si el puerto se ha liberado
Puedes volver a ejecutar `lsof` para confirmar que ya no hay nada escuchando en ese puerto:
```bash
lsof -i :8080
```
Si el comando no devuelve nada, ¡el puerto está libre y listo para usarse!
