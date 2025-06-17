package com.gestion.inventario.controlador;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.gestion.inventario.entidades.Empleado;
import com.gestion.inventario.entidades.Turno;
import com.gestion.inventario.entidades.Usuario;
import com.gestion.inventario.servicio.EmpleadoService;
import com.gestion.inventario.servicio.TurnoService;
import com.gestion.inventario.servicio.UsuarioService;
import com.gestion.inventario.util.paginacion.PageRender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
@RequestMapping("/turnos")
public class TurnoController {

    @Autowired
    private TurnoService turnoService;

    @Autowired
    private EmpleadoService empleadoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private HttpServletRequest request;

    @PostMapping("/abrir")
    public ResponseEntity<Turno> abrirTurno(@RequestBody Turno turno) {
        Turno nuevoTurno = turnoService.abrirNuevoTurno(turno);
        return ResponseEntity.ok(nuevoTurno);
    }

    @PostMapping("/cerrar/{id}")
    public ResponseEntity<Void> cerrarTurno(@PathVariable Long id) {
        Turno turno = turnoService.obtenerTurnoPorId(id);
        if (turno != null) {
            turnoService.cerrarTurno(turno);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/listar")
    public String listarTurnos(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "search", required = false) String search,
            Model model) {
        page = Math.max(0, page);
        Pageable pageRequest = PageRequest.of(page, 5);
        Page<Turno> turnos = turnoService.findAllBySearch(search, pageRequest);
        if (turnos.getTotalElements() == 0 && page > 0) {
            return "redirect:/turnos/listar?search=" + (search != null ? search : "") + "&page=0";
        }

        PageRender<Turno> pageRender = new PageRender<>("/turnos/listar", turnos);
        List<Empleado> empleados = empleadoService.findAll();
        List<Usuario> usuarios = usuarioService.listarUsuarios();

        model.addAttribute("empleados", empleados);
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("turnos", turnos.getContent());
        model.addAttribute("page", pageRender);
        model.addAttribute("search", search);
        model.addAttribute("titulo", "Lista de Turnos");

        String acceptHeader = request.getHeader("X-Requested-With");
        if ("XMLHttpRequest".equals(acceptHeader)) {
            String fragment = request.getParameter("fragment");
            if ("pagination".equals(fragment)) {
                return "Turnos/Listarturno :: #pagination-container";
            }
            return "Turnos/Listarturno :: #table-container";
        }

        return "Turnos/Listarturno";
    }

    @GetMapping("/todos")
    @ResponseBody
    public ResponseEntity<String> obtenerTodosTurnos(@RequestParam(name = "search", required = false) String search) {
        try {
            List<Turno> turnos = turnoService.findAllBySearch(search, null).getContent();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            String json = mapper.writeValueAsString(turnos);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(json);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"error\":\"Error al generar JSON\"}");
        }
    }

    @PostMapping("/agregar")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> agregarTurno(
            @Valid @ModelAttribute Turno turno,
            BindingResult result,
            @RequestParam Long usuarioId) {
        Map<String, Object> response = new HashMap<>();

        if (result.hasErrors()) {
            StringBuilder errors = new StringBuilder();
            result.getAllErrors().forEach(error -> errors.append(error.getDefaultMessage()).append("; "));
            response.put("success", false);
            response.put("error", errors.toString());
            return ResponseEntity.badRequest().body(response);
        }

        try {
            Usuario usuario = usuarioService.obtenerUsuarioPorId(usuarioId);
            if (usuario == null) {
                response.put("success", false);
                response.put("error", "Usuario no encontrado");
                return ResponseEntity.badRequest().body(response);
            }

            turno.setUsuario(usuario);
            if (turnoService.existeTurnoParaUsuarioYFecha(usuario, turno.getFecha())) {
                response.put("success", false);
                response.put("error", "Ya existe un turno para este usuario en esta fecha");
                return ResponseEntity.badRequest().body(response);
            }

            turnoService.guardarTurno(turno);
            response.put("success", true);
            response.put("message", "Turno creado con éxito");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "Error al crear el turno: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/editar/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> editarTurno(
            @PathVariable Long id,
            @Valid @ModelAttribute Turno turno,
            BindingResult result,
            @RequestParam Long usuarioId) {
        Map<String, Object> response = new HashMap<>();

        if (result.hasErrors()) {
            StringBuilder errors = new StringBuilder();
            result.getAllErrors().forEach(error -> errors.append(error.getDefaultMessage()).append("; "));
            response.put("success", false);
            response.put("error", errors.toString());
            return ResponseEntity.badRequest().body(response);
        }

        try {
            Turno turnoExistente = turnoService.obtenerTurnoPorId(id);
            if (turnoExistente == null) {
                response.put("success", false);
                response.put("error", "Turno no encontrado");
                return ResponseEntity.badRequest().body(response);
            }

            Usuario usuario = usuarioService.obtenerUsuarioPorId(usuarioId);
            if (usuario == null) {
                response.put("success", false);
                response.put("error", "Usuario no encontrado");
                return ResponseEntity.badRequest().body(response);
            }

            if (!turnoExistente.getUsuario().getId().equals(usuario.getId()) &&
                    turnoService.existeTurnoParaUsuarioYFecha(usuario, turno.getFecha())) {
                response.put("success", false);
                response.put("error", "Ya existe un turno para este usuario en esta fecha");
                return ResponseEntity.badRequest().body(response);
            }

            turnoExistente.setUsuario(usuario);
            turnoExistente.setCaja(turno.getCaja());
            turnoExistente.setNombre(turno.getNombre());
            turnoExistente.setFecha(turno.getFecha());
            turnoExistente.setHoraInicio(turno.getHoraInicio());
            turnoExistente.setHoraSalida(turno.getHoraSalida());
            turnoExistente.setBaseDinero(turno.getBaseDinero());

            turnoService.guardarTurno(turnoExistente);
            response.put("success", true);
            response.put("message", "Turno actualizado con éxito");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "Error al actualizar el turno: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/eliminar/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> eliminarTurno(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            Turno turno = turnoService.obtenerTurnoPorId(id);
            if (turno == null) {
                response.put("success", false);
                response.put("error", "Turno no encontrado");
                return ResponseEntity.badRequest().body(response);
            }
            turnoService.eliminarTurno(id);
            response.put("success", true);
            response.put("message", "Turno eliminado con éxito");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "Error al eliminar el turno: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}