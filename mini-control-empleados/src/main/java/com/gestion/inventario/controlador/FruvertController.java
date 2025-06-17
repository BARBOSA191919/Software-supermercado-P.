package com.gestion.inventario.controlador;

import com.gestion.inventario.entidades.Carnes;
import com.gestion.inventario.entidades.Fruvert;
import com.gestion.inventario.repositorios.CategoriaRepository;
import com.gestion.inventario.servicio.FruvertService;
import com.gestion.inventario.util.paginacion.PageRender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.Path;
import java.nio.file.Paths;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
public class FruvertController {

    @Autowired
    private FruvertService fruvertService;

    @Autowired
    private CategoriaRepository categoriaRepository;

    // Metodo para listar Fruvert
    @GetMapping("/listarFruvert")
    public String listarFruvert(@RequestParam(name = "page", defaultValue = "0") int page, Model modelo) {
        Pageable pageRequest = PageRequest.of(page, 60);
        Page<Fruvert> fruverts = fruvertService.findAll(pageRequest);
        PageRender<Fruvert> pageRender = new PageRender<>("/listarFruvert", fruverts);
        modelo.addAttribute("titulo", "Listado de Fruvert");
        modelo.addAttribute("fruverts", fruverts);
        modelo.addAttribute("page", pageRender);
        return "Fruvert/listarFruvert";
    }

    // Metodo para mostrar el formulario de registro
    @GetMapping("/formFruvert")
    public String mostrarFormularioDeRegistrarFruvert(Model modelo) {
        Fruvert fruvert = new Fruvert();
        modelo.addAttribute("fruvert", fruvert);
        modelo.addAttribute("categorias", categoriaRepository.findAll());
        modelo.addAttribute("titulo", "Registro del Fruvert");
        return "Fruvert/formFruvert";
    }

    @GetMapping("/formFruvert/verificar-codigo/{codigo}")
    @ResponseBody
    public boolean verificarCodigo(@PathVariable String codigo, @RequestParam(required = false) Integer idActual) {
        // Si es una edición (idActual no es null)
        if (idActual != null) {
            Fruvert fruvertExistente = fruvertService.findByCodigo(codigo);
            // Retorna false si el código actual es el mismo que el que se está editando
            return fruvertExistente != null && !fruvertExistente.getId().equals(idActual);
        }
        // Si es nuevo producto, verifica normalmente
        return fruvertService.existeCodigo(codigo);
    }

    // Metodo para guardar un Fruvert (registrar o editar)
    @PostMapping("/formFruvert")
    public String guardarFruvert(@Valid Fruvert fruvert, BindingResult result, Model modelo, RedirectAttributes flash, SessionStatus status) {
        if (result.hasErrors()) {
            modelo.addAttribute("titulo", "Registro de Fruvert");
            return "Fruvert/formFruvert";
        }
        // Verificar si el código ya existe
        if (fruvertService.existeCodigo(fruvert.getCodigo())) {
            // Si estamos editando, obtenemos el ID actual
            Integer idActual = fruvert.getId();
            // Verificamos si el código ya existe y no es el mismo que el actual
            if (idActual == null || !fruvertService.findByCodigo(fruvert.getCodigo()).getId().equals(idActual)) {
                result.rejectValue("codigo", "error.codigo", "El código de barras ya está en uso.");
            }
        }

        // Verificar nuevamente si hay errores después de la validación del código
        if (result.hasErrors()) {
            modelo.addAttribute("titulo", "Registro de Fruvert");
            return "Fruvert/formFruvert";  // Retorna al formulario si hay errores
        }

        // Guardar el Fruvert (nuevo o existente)
        fruvertService.save(fruvert);
        status.setComplete();
        flash.addFlashAttribute("success", "Fruvert registrado o editado con éxito");
        return "redirect:/listarFruvert";
    }

    // Metodo para editar un Fruvert
    @GetMapping("/form/fruvert/{id}")
    public String editarFruvert(@PathVariable("id") Integer id, Model modelo, RedirectAttributes flash) {
        Fruvert fruvert = fruvertService.findOne(id);
        if (fruvert == null) {
            flash.addFlashAttribute("error", "El ID del fruvert no existe en la base de datos");
            return "redirect:/listarFruvert";
        }

        modelo.addAttribute("categorias", categoriaRepository.findAll());
        modelo.addAttribute("fruvert", fruvert);
        modelo.addAttribute("titulo", "Edición del Fruvert");
        return "Fruvert/formFruvert";
    }

    // Metodo para eliminar un Fruvert
    @GetMapping("/eliminar/fruvert/{id}")
    public String eliminarFruvert(@PathVariable(value = "id") Integer id, RedirectAttributes flash) {
        if (id > 0) {
            fruvertService.delete(id);
            flash.addFlashAttribute("success", "Fruvert eliminado con éxito");
        }
        return "redirect:/listarFruvert";
    }
}