package com.chat.chatmulticlithreads;

import javafx.application.Application;

/**
 * Launcher de la aplicación de chat multicliente.
 * Cada ejecución abre una nueva ventana de chat que se conecta al servidor.
 * El servidor se levanta automáticamente con la primera instancia.
 */
public class Launcher {
	public static void main(String[] args) {
		// Lanzar una nueva instancia del cliente
		Application.launch(App.class, args);
	}
}
