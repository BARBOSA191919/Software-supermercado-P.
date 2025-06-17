package com.gestion.inventario;

import com.gestion.inventario.entidades.Contabilidad;
import com.gestion.inventario.repositorios.ContabilidadRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.PostMapping;

import java.sql.Date;

@SpringBootTest
class MiniControlEmpleadosApplicationTests {

	@Test
	void contextLoads() {
	}
	@Autowired
	private ContabilidadRepository contabilidadRepository;



}
