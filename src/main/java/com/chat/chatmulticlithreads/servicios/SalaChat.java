package com.chat.chatmulticlithreads.servicios;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SalaChat {
    private final String nombre;
    private final Set<HandlerCliente> participantes = ConcurrentHashMap.newKeySet();

    public SalaChat(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    // Entrar a la sala
    public void entrarSala(HandlerCliente manejador) {
        participantes.add(manejador);
    }

    // Salir de la sala
    public void abandonarSala(HandlerCliente manejador) {
        participantes.remove(manejador);
    }

    // Obtener el tamaño de la sala
    public int getSize() {
        return participantes.size();
    }

    // Enviar mensaje a todos los participantes
    public void enviarMensajeTodos(String mensaje) {
        for (HandlerCliente p : participantes) {
            p.enviar(mensaje);
        }
    }

    // Enviar mensaje a todos menos a quien lo envía
    public void enviarMensajeMenosOrigen(HandlerCliente origen, String mensaje) {
        for (HandlerCliente p : participantes) {
            if (p != origen) {
                p.enviar(mensaje);
            }
        }
    }
}
