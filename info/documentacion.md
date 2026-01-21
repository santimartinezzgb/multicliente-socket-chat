# Documentación para público general

Este documento describe, de forma sencilla y directa, qué hace la aplicación y cómo está pensada. Desarrollé este proyecto como ejercicio práctico de programación; el texto explica las decisiones principales sin tecnicismos.

## ¿Qué hace la aplicación?
Es un chat que puede funcionar en dos modos: aplicaciones que actúan como servidor (aceptan conexiones) y aplicaciones que actúan como cliente (se conectan a un servidor). También incluye una pequeña interfaz gráfica para uso local.

## Idea general de funcionamiento
- Un componente controla la parte visual (ventana y botones). Ese componente solicita acciones a la lógica del chat, pero no realiza el trabajo pesado.
- La parte de red se encarga de aceptar conexiones, recibir mensajes y reenviarlos a los participantes de una sala.
- Las piezas están separadas para que cada una sea fácil de entender y, si hace falta, de modificar.

## Diseño y modularidad
- Modularidad significa dividir el proyecto en piezas con responsabilidades claras: interfaz, controladores y servicios de red.
- Esa separación facilita cambios futuros: por ejemplo, mejorar la interfaz sin rehacer la lógica que reparte mensajes.

## Por qué se eligió este enfoque
- Permite comprobar cada parte por separado, lo que ayuda a localizar y arreglar errores.
- Facilita añadir funcionalidades (historial, autenticación) sin rehacer toda la aplicación.

## Cómo probarlo de forma simple
- Compilar y ejecutar desde la carpeta del proyecto con las herramientas habituales de Java. Con Maven:

```bash
mvn clean compile javafx:run
```

- También es posible empaquetar y ejecutar con `java -jar` una vez generado el artefacto.


