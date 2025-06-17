package com.gestion.inventario;

import com.gestion.inventario.entidades.Usuario;
import com.gestion.inventario.repositorios.UsuarioRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class WebSecurityConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance(); // Desactiva la codificación de contraseñas
	}

	@Bean
	public UserDetailsService userDetailsService(UsuarioRepository usuarioRepository) {
		return username -> {
			// Buscar el usuario desde la base de datos
			Usuario usuario = usuarioRepository.findByUsername(username);
			if (usuario == null) {
				throw new UsernameNotFoundException("Usuario no encontrado");
			}

			// Crear el UserDetails con los roles asociados del usuario
			List<GrantedAuthority> authorities = usuario.getRoles().stream()
					.map(role -> new SimpleGrantedAuthority("ROLE_" + role.getNombre()))
					.collect(Collectors.toList());

			// Usar la contraseña en texto plano (passwordDisplay) en lugar de la encriptada
			return new User(usuario.getUsername(), usuario.getPasswordDisplay(), authorities);
		};
	}


	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.csrf().ignoringAntMatchers("/reportes/factura/**") // CSRF deshabilitado para este endpoint
				.and()
				.authorizeRequests()
				.antMatchers("/v1/sendMessage").permitAll() // Permitir acceso sin autenticación
				// Permitir acceso a todos a las páginas principales y de login
				.antMatchers("/", "/login").permitAll()
				.antMatchers("/css/**", "/js/**", "/img/**", "/webfonts/**").permitAll()
				.antMatchers("/turnos/**").authenticated()
				// Solo el ADMIN y GERENTE pueden eliminar o modificar registros
				.antMatchers("/form/*", "/eliminar/*", "/proveedor/form/*", "/proveedor/eliminar/*")
				.hasAnyRole("ADMIN", "GERENTE")

				// Solo el ADMIN y GERENTE pueden agregar o editar
				.antMatchers("/editar/*", "/proveedor/form", "/agregar")
				.hasAnyRole("ADMIN", "GERENTE")

				// Cajeros y el Contador solo pueden ver ciertos datos
				.antMatchers("/ver/*", "/proveedor/ver/*")
				.hasAnyRole("ADMIN", "GERENTE", "CAJERO", "CONTADOR")

				// Cualquier otra solicitud requiere autenticación
				.anyRequest().authenticated()

				.and()
				.formLogin()
				.loginPage("/login")
				.defaultSuccessUrl("/principal", true) // Redirige a la página principal tras el login
				.permitAll()

				.and()
				.logout()
				.permitAll();

		return http.build();
	}
}