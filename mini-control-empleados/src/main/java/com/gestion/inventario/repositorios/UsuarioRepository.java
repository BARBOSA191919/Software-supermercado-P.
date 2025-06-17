package com.gestion.inventario.repositorios;

import com.gestion.inventario.entidades.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByUsername(String username);

    @Query("SELECT u FROM Usuario u LEFT JOIN u.roles r WHERE " +
            "LOWER(u.username) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(r.nombre) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Usuario> findByUsernameContainingIgnoreCaseOrRolesNombreContainingIgnoreCase(
            @Param("search") String search, @Param("search") String search2, Pageable pageable);

    @Query("SELECT u FROM Usuario u LEFT JOIN u.roles r WHERE " +
            "LOWER(u.username) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(r.nombre) LIKE LOWER(CONCAT('%', :search, '%'))")
    List<Usuario> findByUsernameContainingIgnoreCaseOrRolesNombreContainingIgnoreCase(
            @Param("search") String search, @Param("search") String search2);
}