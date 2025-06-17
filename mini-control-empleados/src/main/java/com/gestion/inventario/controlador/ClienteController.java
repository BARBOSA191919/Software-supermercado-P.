package com.gestion.inventario.controlador;

import com.gestion.inventario.entidades.Cliente;
import com.gestion.inventario.servicio.ClienteService;
import com.gestion.inventario.util.paginacion.PageRender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private HttpServletRequest request; // Para detectar solicitudes AJAX

    // Listar todos los clientes con soporte para AJAX
    @GetMapping("/listar")
    public String listarClientes(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "search", required = false) String search,
            Model model) {

        page = Math.max(0, page);
        Pageable pageRequest = PageRequest.of(page, 5);
        Page<Cliente> clientes = clienteService.findAllBySearch(search, pageRequest);

        if (clientes.getTotalElements() == 0 && page > 0) {
            return "redirect:/clientes/listar?search=" + (search != null ? search : "") + "&page=0";
        }

        PageRender<Cliente> pageRender = new PageRender<>("/clientes/listar", clientes);

        model.addAttribute("clientes", clientes.getContent());
        model.addAttribute("cliente", new Cliente());
        model.addAttribute("page", pageRender);
        model.addAttribute("search", search);
        model.addAttribute("titulo", "Lista de Clientes");

        if (clientes.getTotalElements() == 0) {
            model.addAttribute("info", "No se encontraron clientes con el criterio de búsqueda.");
        }

        // Detectar si es una solicitud AJAX
        String acceptHeader = request.getHeader("X-Requested-With");
        if ("XMLHttpRequest".equals(acceptHeader)) {
            String fragment = request.getParameter("fragment");
            if ("pagination".equals(fragment)) {
                return "Clientes/listarClientes :: #pagination-container";
            }
            return "Clientes/listarClientes :: #table-container";
        }

        return "Clientes/listarClientes";
    }

    // Agregar un cliente con AJAX
    @PostMapping("/agregar")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> agregarCliente(
            @Valid @ModelAttribute Cliente cliente,
            BindingResult result) {

        Map<String, Object> response = new HashMap<>();
        if (result.hasErrors()) {
            response.put("success", false);
            response.put("error", "Error en los datos del cliente");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            clienteService.save(cliente);
            response.put("success", true);
            response.put("message", "Cliente creado con éxito");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "Error al crear el cliente: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Editar un cliente con AJAX
    @PostMapping("/editar/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> editarCliente(
            @PathVariable Long id,
            @ModelAttribute Cliente cliente) {

        Map<String, Object> response = new HashMap<>();
        try {
            Cliente clienteExistente = clienteService.findOne(id);
            if (clienteExistente != null) {
                clienteExistente.setNombreCliente(cliente.getNombreCliente());
                clienteExistente.setApellido(cliente.getApellido());
                clienteExistente.setCedula(cliente.getCedula());
                clienteService.save(clienteExistente);
                response.put("success", true);
                response.put("message", "Cliente actualizado con éxito");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("error", "Cliente no encontrado");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "Error al actualizar el cliente: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Eliminar un cliente con AJAX
    @GetMapping("/eliminar/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> eliminarCliente(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Cliente cliente = clienteService.findOne(id);
            if (cliente != null) {
                clienteService.delete(id);
                response.put("success", true);
                response.put("message", "Cliente eliminado con éxito");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("error", "Cliente no encontrado");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "Error al eliminar el cliente: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Endpoint para obtener todos los clientes (para el PDF)
    @GetMapping("/todos")
    @ResponseBody
    public List<Cliente> obtenerTodosClientes(@RequestParam(name = "search", required = false) String search) {
        return clienteService.findAllBySearch(search, null).getContent();
    }
}