package com.gestion.inventario.ServiceImpl;

import com.gestion.inventario.entidades.Rol;
import com.gestion.inventario.entidades.Usuario;
import com.gestion.inventario.repositorios.RolRepository;
import com.gestion.inventario.repositorios.UsuarioRepository;
import com.gestion.inventario.servicio.UsuarioService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Importación correcta

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceimpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private static final String ALGORITMO = "AES";

    public UsuarioServiceimpl(UsuarioRepository usuarioRepository, RolRepository rolRepository) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
    }

    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true) // Ahora debería funcionar
    public Page<Usuario> findAll(Pageable pageable) {
        return usuarioRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true) // Ahora debería funcionar
    public Page<Usuario> findAllBySearch(String search, Pageable pageable) {
        if (search != null && !search.isEmpty()) {
            if (pageable == null) {
                List<Usuario> resultados = usuarioRepository.findByUsernameContainingIgnoreCaseOrRolesNombreContainingIgnoreCase(search, search);
                return new PageImpl<>(resultados);
            }
            return usuarioRepository.findByUsernameContainingIgnoreCaseOrRolesNombreContainingIgnoreCase(search, search, pageable);
        }
        if (pageable == null) {
            return new PageImpl<>(usuarioRepository.findAll());
        }
        return usuarioRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true) // Ahora debería funcionar
    public Usuario obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void crearUsuario(String username, String password, List<String> roles) {
        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setPasswordDisplay(password);
        usuario.setPassword(password);

        Set<Rol> rolesSet = new HashSet<>();
        for (String rolNombre : roles) {
            Rol rol = rolRepository.findByNombre(rolNombre)
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + rolNombre));
            rolesSet.add(rol);
        }
        usuario.setRoles(rolesSet);

        usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public void crearUsuarioConPassword(Usuario usuario, List<String> roles) {
        usuario.setPasswordDisplay(usuario.getPassword());
        usuario.setPassword(usuario.getPassword());

        Set<Rol> rolesSet = new HashSet<>();
        for (String rolNombre : roles) {
            Rol rol = rolRepository.findByNombre(rolNombre)
                    .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + rolNombre));
            rolesSet.add(rol);
        }
        usuario.setRoles(rolesSet);

        usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public void actualizarRoles(Long id, List<String> roles) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Set<Rol> rolesAsignados = roles.stream()
                .map(rolName -> rolRepository.findByNombre(rolName)
                        .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + rolName)))
                .collect(Collectors.toSet());
        usuario.setRoles(rolesAsignados);
        usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public void eliminarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void guardarUsuario(Usuario usuario) {
        usuarioRepository.save(usuario);
    }

    @Override
    @Transactional(readOnly = true) // Ahora debería funcionar
    public List<Rol> listarTodosLosRoles() {
        return rolRepository.findAll();
    }

    public List<Usuario> obtenerUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios.stream().map(usuario -> {
            String passwordDescifrada = descifrarPassword(usuario.getPassword());
            return new Usuario(usuario.getUsername(), passwordDescifrada, usuario.getRoles());
        }).collect(Collectors.toList());
    }

    private String descifrarPassword(String passwordEncriptada) {
        return "contraseña en texto plano"; // Simplificado
    }
}