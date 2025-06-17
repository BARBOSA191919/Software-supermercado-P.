package com.gestion.inventario.servicio;

import com.gestion.inventario.entidades.Rol;
import com.gestion.inventario.entidades.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UsuarioService {

    List<Usuario> listarUsuarios();

    Page<Usuario> findAll(Pageable pageable);

    Page<Usuario> findAllBySearch(String search, Pageable pageable);

    Usuario obtenerUsuarioPorId(Long id);

    void crearUsuario(String username, String password, List<String> roles);

    void guardarUsuario(Usuario usuario);

    void eliminarUsuario(Long id);

    void actualizarRoles(Long id, List<String> roles);

    List<Rol> listarTodosLosRoles();

    void crearUsuarioConPassword(Usuario usuario, List<String> roles);
}