package com.gestion.inventario.controlador;

import com.gestion.inventario.entidades.Contabilidad;
import com.gestion.inventario.servicio.ContabilidadService;
import com.gestion.inventario.util.paginacion.PageRender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/contabilidad")
public class ContabilidadController {

    @Autowired
    private ContabilidadService contabilidadService;

    // Listar registros contables
    @GetMapping("/listar")
    public String listarContabilidad(
            @RequestParam(name = "page", defaultValue = "0") int page,
            Model model) {

        Pageable pageRequest = PageRequest.of(page, 6);
        Page<Contabilidad> contabilidades = contabilidadService.findAll(pageRequest);
        PageRender<Contabilidad> pageRender = new PageRender<>("/contabilidad/listar", contabilidades);

        model.addAttribute("contabilidades", contabilidades.getContent());
        model.addAttribute("contabilidad", new Contabilidad()); // Para el formulario de nuevo registro
        model.addAttribute("page", pageRender);
        model.addAttribute("titulo", "Listado de registros contables");

        return "Contabilidad/contabilidad_listar";
    }

    // Guardar un nuevo registro contable
    @PostMapping("/agregar")
    public String agregarContabilidad(
            @Valid @ModelAttribute Contabilidad contabilidad,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Error en los datos del registro contable");
            return "redirect:/contabilidad/listar";
        }

        try {
            contabilidadService.save(contabilidad);
            redirectAttributes.addFlashAttribute("success", "Registro contable creado con éxito");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear el registro contable");
        }
        return "redirect:/contabilidad/listar";
    }

    // Editar un registro contable
    @PostMapping("/editar/{id}")
    public String editarContabilidad(
            @PathVariable Long id,
            @ModelAttribute Contabilidad contabilidad,
            RedirectAttributes redirectAttributes) {

        try {
            Contabilidad contabilidadExistente = contabilidadService.findOne(id);
            if (contabilidadExistente != null) {
                // Actualizar campos específicos
                contabilidadExistente.setTotalEgresos(contabilidad.getTotalEgresos()); // Reemplaza con los campos reales
                contabilidadExistente.setTotalEgresos(contabilidad.getTotalEgresos());
                contabilidadExistente.setFecha(contabilidad.getFecha());
                contabilidadExistente.setGanancias(contabilidad.getGanancias());

                // Añade otros campos según tu entidad Contabilidad

                contabilidadService.save(contabilidadExistente);
                redirectAttributes.addFlashAttribute("success", "Registro contable actualizado con éxito");
            } else {
                redirectAttributes.addFlashAttribute("error", "Registro contable no encontrado");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar el registro contable");
        }
        return "redirect:/contabilidad/listar";
    }

    // Eliminar un registro contable
    @GetMapping("/eliminar/{id}")
    public String eliminarContabilidad(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        try {
            contabilidadService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Registro contable eliminado con éxito");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el registro contable");
        }
        return "redirect:/contabilidad/listar";
    }
}