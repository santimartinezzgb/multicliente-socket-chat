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

    public void entrarSala(HandlerCliente manejador) {
        participantes.add(manejador);
    }

    public void abandonarSala(HandlerCliente manejador) {
        participantes.remove(manejador);
    }

    public int getSize() {
        return participantes.size();
    }

    public void enviarMensajeTodos(String mensaje) {
        for (HandlerCliente p : participantes) {
            p.enviar(mensaje);
        }
    }

    public void enviarMensajeMenosOrigen(HandlerCliente origen, String mensaje) {
        for (HandlerCliente p : participantes) {
            if (p != origen) {
                p.enviar(mensaje);
            }
        }
    }
}
