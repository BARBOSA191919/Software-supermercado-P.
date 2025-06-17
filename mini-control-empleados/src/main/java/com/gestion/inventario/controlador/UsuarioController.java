package com.gestion.inventario.controlador;

import com.gestion.inventario.ServiceImpl.EmpleadoServiceImpl;
import com.gestion.inventario.ServiceImpl.UsuarioServiceimpl;
import com.gestion.inventario.entidades.Empleado;
import com.gestion.inventario.entidades.Rol;
import com.gestion.inventario.entidades.Usuario;
import com.gestion.inventario.repositorios.EmpleadoRepository;
import com.gestion.inventario.util.paginacion.PageRender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioServiceimpl usuarioService;

    @Autowired
    private EmpleadoServiceImpl empleadoService;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired
    private HttpServletRequest request; // Para detectar solicitudes AJAX

    // Listar todos los usuarios con soporte para AJAX y búsqueda
    @GetMapping("/listar")
    public String listarUsuarios(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "search", required = false) String search,
            Model model) {

        page = Math.max(0, page);
        Pageable pageRequest = PageRequest.of(page, 5);
        Page<Usuario> usuarios = usuarioService.findAllBySearch(search, pageRequest);

        if (usuarios.getTotalElements() == 0 && page > 0) {
            return "redirect:/usuarios/listar?search=" + (search != null ? search : "") + "&page=0";
        }

        PageRender<Usuario> pageRender = new PageRender<>("/usuarios/listar", usuarios);

        model.addAttribute("usuarios", usuarios.getContent());
        model.addAttribute("page", pageRender);
        model.addAttribute("search", search);
        model.addAttribute("roles", usuarioService.listarTodosLosRoles());
        model.addAttribute("empleados", empleadoService.findAll());
        model.addAttribute("titulo", "Lista de Usuarios");

        // Detectar si es una solicitud AJAX
        String acceptHeader = request.getHeader("X-Requested-With");
        if ("XMLHttpRequest".equals(acceptHeader)) {
            String fragment = request.getParameter("fragment");
            if ("pagination".equals(fragment)) {
                return "Roles/ListarUsuario :: #pagination-container";
            }
            return "Roles/ListarUsuario :: #table-container";
        }

        return "Roles/ListarUsuario";
    }

    // Agregar un usuario con AJAX
    @PostMapping("/agregar")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> agregarUsuario(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam List<String> roles) {

        Map<String, Object> response = new HashMap<>();
        try {
            usuarioService.crearUsuario(username, password, roles);
            response.put("success", true);
            response.put("message", "Usuario creado con éxito");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "Error al crear el usuario: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Editar un usuario con AJAX
    @PostMapping("/editar/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> editarUsuario(
            @PathVariable Long id,
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam List<String> roles) {

        Map<String, Object> response = new HashMap<>();
        try {
            Usuario usuario = usuarioService.obtenerUsuarioPorId(id);
            if (usuario != null) {
                usuario.setUsername(username);
                if (password != null && !password.isEmpty()) {
                    usuario.setPasswordDisplay(password);
                    usuario.setPassword(password);
                }
                usuarioService.actualizarRoles(id, roles);
                usuarioService.guardarUsuario(usuario);
                response.put("success", true);
                response.put("message", "Usuario actualizado con éxito");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("error", "Usuario no encontrado");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "Error al actualizar el usuario: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Eliminar un usuario con AJAX
    @GetMapping("/eliminar/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> eliminarUsuario(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Usuario usuario = usuarioService.obtenerUsuarioPorId(id);
            if (usuario != null) {
                usuarioService.eliminarUsuario(id);
                response.put("success", true);
                response.put("message", "Usuario eliminado con éxito");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("error", "Usuario no encontrado");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "Error al eliminar el usuario: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Obtener los roles disponibles para asignar a los usuarios
    @GetMapping("/roles")
    @ResponseBody
    public List<Rol> listarRoles() {
        return usuarioService.listarTodosLosRoles();
    }

    // Metodo para buscar empleados (usado en el autocompletado)
    @GetMapping("/buscar-empleados")
    @ResponseBody
    public List<Empleado> buscarEmpleados(@RequestParam String query) {
        return empleadoRepository.findByNombreContainingIgnoreCase(query);
    }
}