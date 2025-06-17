package com.gestion.inventario.controlador;

import com.gestion.inventario.entidades.Fruvert;
import com.gestion.inventario.entidades.Verdura;
import com.gestion.inventario.repositorios.CategoriaRepository;
import com.gestion.inventario.servicio.VerduraService;
import com.gestion.inventario.util.paginacion.PageRender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Map;

@Controller
public class VerduraController {

    @Autowired
    private VerduraService verduraService;

    @Autowired
    private CategoriaRepository categoriaRepository;


    // Listar todas las verduras
    @GetMapping("/listarVerduras")
    public String listarVerduras(@RequestParam(name = "page", defaultValue = "0") int page, Model modelo) {
        Pageable pageRequest = PageRequest.of(page, 40);
        Page<Verdura> verduras = verduraService.findAll(pageRequest);
        PageRender<Verdura> pageRender = new PageRender<>("/listarVerduras", verduras);

        modelo.addAttribute("titulo", "Listado de Verduras");
        modelo.addAttribute("verdura", verduras);  // Cambié de "verdura" a "verduras" para claridad
        modelo.addAttribute("page", pageRender);

        return "Fruvert/listarVerduras";  // Asegúrate de que esta vista exista
    }

    @GetMapping("/formVerduras")
    public String mostrarFormularioDeRegistrarVerdura(Model model) {
        Verdura verdura = new Verdura();
        model.addAttribute("verdura", verdura);
        model.addAttribute("categorias", categoriaRepository.findAll());
        model.addAttribute("titulo", "Registro de Verduras");
        return "Fruvert/formVerduras";  // Asegúrate de que esta vista exista
    }

    @GetMapping("/formVerdura/verificar-codigo/{codigo}")
    @ResponseBody
    public boolean verificarCodigo(@PathVariable String codigo, @RequestParam(required = false) Integer idActual) {
        // Si es una edición (idActual no es null)
        if (idActual != null) {
            Verdura verduraExistente = verduraService.findByCodigo(codigo);
            // Retorna false si el código actual es el mismo que el que se está editando
            return verduraExistente != null && !verduraExistente.getId().equals(idActual);
        }
        // Si es nuevo producto, verifica normalmente
        return verduraService.existeCodigo(codigo);
    }

    // Guardar Verdura
    @PostMapping("/formVerduras")
    public String guardarVerdura(@Valid Verdura verdura, BindingResult result, Model modelo, RedirectAttributes flash, SessionStatus status) {
        if (result.hasErrors()) {
            modelo.addAttribute("titulo", "Registro de Verduras");
            return "Fruvert/formVerduras";  // Retorna al formulario si hay errores
        }
        // Verificar si el código ya existe
        if (verduraService.existeCodigo(verdura.getCodigo())) {
            // Si estamos editando, obtenemos el ID actual
            Integer idActual = verdura.getId();
            // Verificamos si el código ya existe y no es el mismo que el actual
            if (idActual == null || !verduraService.findByCodigo(verdura.getCodigo()).getId().equals(idActual)) {
                result.rejectValue("codigo", "error.codigo", "El código de barras ya está en uso.");
            }
        }

        // Verificar nuevamente si hay errores después de la validación del código
        if (result.hasErrors()) {
            modelo.addAttribute("titulo", "Registro de Verduras");
            return "Fruvert/formVerduras";  // Retorna al formulario si hay errores
        }


        verduraService.save(verdura);
        status.setComplete();
        flash.addFlashAttribute("success", "Verdura registrada o editada con éxito");
        return "redirect:/listarVerduras";
    }

    // Editar Verdura
    @GetMapping("/form/verduras/{id}")
    public String editarVerdura(@PathVariable("id") Integer id, Model modelo, RedirectAttributes flash) {
        Verdura verdura = verduraService.findOne(id);
        if (verdura == null) {
            flash.addFlashAttribute("error", "El ID de la verdura no existe en la base de datos");
            return "redirect:/listarVerduras";
        }

        modelo.addAttribute("categorias", categoriaRepository.findAll());
        modelo.addAttribute("verdura", verdura);
        modelo.addAttribute("titulo", "Edición de la Verdura");
        return "Fruvert/formVerduras";  // Debe ser el mismo formulario para editar
    }

    // Eliminar Verdura
    @GetMapping("/eliminar/verduras/{id}")
    public String eliminarVerdura(@PathVariable(value = "id") Integer id, RedirectAttributes flash) {
        if (id > 0) {
            verduraService.delete(id);
            flash.addFlashAttribute("success", "Verdura eliminada con éxito");
        }
        return "redirect:/listarVerduras";
    }
}
