package com.gestion.inventario.controlador;

import com.gestion.inventario.entidades.Proveedor;
import com.gestion.inventario.servicio.ProveedorService;
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
@RequestMapping("/proveedores")
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;

    @Autowired
    private HttpServletRequest request; // Para detectar solicitudes AJAX

    // Listar proveedores con soporte para AJAX
    @GetMapping("/listar")
    public String listarProveedores(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "search", required = false) String search,
            Model model) {

        page = Math.max(0, page);
        Pageable pageRequest = PageRequest.of(page, 5);
        Page<Proveedor> proveedores = proveedorService.findAllBySearch(search, pageRequest);

        if (proveedores.getTotalElements() == 0 && page > 0) {
            return "redirect:/proveedores/listar?search=" + (search != null ? search : "") + "&page=0";
        }

        PageRender<Proveedor> pageRender = new PageRender<>("/proveedores/listar", proveedores);

        model.addAttribute("proveedores", proveedores.getContent());
        model.addAttribute("proveedor", new Proveedor());
        model.addAttribute("page", pageRender);
        model.addAttribute("search", search);
        model.addAttribute("titulo", "Lista de Proveedores");

        if (proveedores.getTotalElements() == 0) {
            model.addAttribute("info", "No se encontraron proveedores con el criterio de búsqueda.");
        }

        // Detectar si es una solicitud AJAX
        String acceptHeader = request.getHeader("X-Requested-With");
        if ("XMLHttpRequest".equals(acceptHeader)) {
            return "Proveedores/listarProveedores :: #table-container"; // Solo la tabla
        }

        return "Proveedores/listarProveedores";
    }

    // Guardar un nuevo proveedor con AJAX
    @PostMapping("/agregar")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> agregarProveedor(@ModelAttribute Proveedor proveedor) {
        Map<String, Object> response = new HashMap<>();
        try {
            proveedorService.save(proveedor);
            response.put("success", true);
            response.put("message", "Proveedor creado con éxito");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "Error al crear el proveedor: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Editar un proveedor con AJAX
    @PostMapping("/editar/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> editarProveedor(@PathVariable Long id, @ModelAttribute Proveedor proveedor) {
        Map<String, Object> response = new HashMap<>();
        try {
            Proveedor proveedorExistente = proveedorService.findOne(id);
            if (proveedorExistente != null) {
                proveedorExistente.setNombreProveedor(proveedor.getNombreProveedor());
                proveedorExistente.setDireccion(proveedor.getDireccion());
                proveedorExistente.setTelefono(proveedor.getTelefono());
                proveedorExistente.setEmail(proveedor.getEmail());
                proveedorService.save(proveedorExistente);
                response.put("success", true);
                response.put("message", "Proveedor actualizado con éxito");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("error", "Proveedor no encontrado");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "Error al actualizar el proveedor: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Eliminar un proveedor con AJAX
    @GetMapping("/eliminar/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> eliminarProveedor(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Proveedor proveedor = proveedorService.findOne(id);
            if (proveedor != null) {
                proveedorService.delete(id);
                response.put("success", true);
                response.put("message", "Proveedor eliminado con éxito");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("error", "Proveedor no encontrado");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "Error al eliminar el proveedor: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Endpoint para obtener todos los proveedores (para el PDF)
    @GetMapping("/todos")
    @ResponseBody
    public List<Proveedor> obtenerTodosProveedores(@RequestParam(name = "search", required = false) String search) {
        return proveedorService.findAllBySearch(search, null).getContent();
    }
}