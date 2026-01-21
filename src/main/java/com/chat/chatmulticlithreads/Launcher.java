package com.chat.chatmulticlithreads;

import javafx.application.Application;

/**
 * Launcher del cliente de chat.
 * Cada ejecución abre una nueva ventana de chat que se conecta al servidor.
 * 
 * IMPORTANTE: Primero debes ejecutar LauncherServidor.java para iniciar el servidor,
 * y luego ejecutar este Launcher.java para conectar clientes.
 */
public class Launcher {
	public static void main(String[] args) {
		// Lanzar una nueva instancia del cliente
		Application.launch(App.class, args);
	}
}
