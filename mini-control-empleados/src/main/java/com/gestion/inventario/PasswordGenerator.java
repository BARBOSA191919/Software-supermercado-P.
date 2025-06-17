package com.gestion.inventario;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {

	public static void main(String[] args) {
		String rawPassword = "12345";
		System.out.println("Contraseña en texto plano: " + rawPassword);
	}

	
}
