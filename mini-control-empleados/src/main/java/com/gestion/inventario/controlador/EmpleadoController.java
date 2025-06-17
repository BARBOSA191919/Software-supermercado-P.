package com.gestion.inventario.controlador;

import com.gestion.inventario.entidades.Empleado;
import com.gestion.inventario.servicio.EmpleadoService;
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
@RequestMapping("/empleados")
public class EmpleadoController {

    @Autowired
    private EmpleadoService empleadoService;

    @Autowired
    private HttpServletRequest request;

    @GetMapping("/listar")
    public String listarEmpleados(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "search", required = false) String search,
            Model model) {

        page = Math.max(0, page);
        Pageable pageRequest = PageRequest.of(page, 5);
        Page<Empleado> empleados = empleadoService.findAllBySearch(search, pageRequest);

        if (empleados.getTotalElements() == 0 && page > 0) {
            return "redirect:/empleados/listar?search=" + (search != null ? search : "") + "&page=0";
        }

        PageRender<Empleado> pageRender = new PageRender<>("/empleados/listar", empleados);

        model.addAttribute("empleados", empleados.getContent());
        model.addAttribute("page", pageRender);
        model.addAttribute("search", search);
        model.addAttribute("titulo", "Gestión de Empleados");

        String acceptHeader = request.getHeader("X-Requested-With");
        if ("XMLHttpRequest".equals(acceptHeader)) {
            String fragment = request.getParameter("fragment");
            if ("pagination".equals(fragment)) {
                return "Empleados/ListarEmpleado :: #pagination-container";
            }
            return "Empleados/ListarEmpleado :: #table-container";
        }

        return "Empleados/ListarEmpleado";
    }

    @GetMapping("/todos")
    @ResponseBody
    public List<Empleado> obtenerTodosEmpleados(@RequestParam(name = "search", required = false) String search) {
        return empleadoService.findAllBySearch(search, null).getContent();
    }

    @PostMapping("/agregar")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> guardarEmpleado(@ModelAttribute Empleado empleado) {
        Map<String, Object> response = new HashMap<>();
        try {
            empleadoService.guardarEmpleado(empleado);
            response.put("success", true);
            response.put("message", "Empleado creado con éxito");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "Error al crear el empleado: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/editar/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> actualizarEmpleado(
            @PathVariable Long id,
            @ModelAttribute Empleado empleado) {
        Map<String, Object> response = new HashMap<>();
        try {
            Empleado existingEmpleado = empleadoService.obtenerEmpleadoPorId(id)
                    .orElseThrow(() -> new IllegalArgumentException("Empleado no encontrado con ID: " + id));
            empleado.setId(id);
            empleadoService.guardarEmpleado(empleado);
            response.put("success", true);
            response.put("message", "Empleado actualizado con éxito");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "Error al actualizar el empleado: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/eliminar/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> eliminarEmpleado(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Empleado empleado = empleadoService.obtenerEmpleadoPorId(id)
                    .orElseThrow(() -> new IllegalArgumentException("Empleado no encontrado con ID: " + id));
            empleadoService.eliminarEmpleado(id);
            response.put("success", true);
            response.put("message", "Empleado eliminado con éxito");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "Error al eliminar el empleado: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}