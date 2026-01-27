package com.chat.chatmulticlithreads;

import com.chat.chatmulticlithreads.servicios.Servidor;

/**
 * Launcher exclusivo para el servidor.
 * INICIAR ANTES DE CLIENTES
 */
public class LauncherServidor {
	public static void main(String[] args) {
		System.out.println("=== INICIANDO SERVIDOR DE CHAT ===");
		Servidor.main(args);
	}
}
