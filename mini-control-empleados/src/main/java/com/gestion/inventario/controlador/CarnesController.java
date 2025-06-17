package com.gestion.inventario.controlador;

import com.gestion.inventario.entidades.Carnes;
import com.gestion.inventario.repositorios.CategoriaRepository;
import com.gestion.inventario.servicio.CarnesService;
import com.gestion.inventario.util.paginacion.PageRender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class CarnesController {

    @Autowired
    private CarnesService carnesService;

    @Autowired
    private CategoriaRepository categoriaRepository;

    // Listar todas las Carnes
    @GetMapping("/listarCarnes")
    public String listarCarnes(@RequestParam(name = "page", defaultValue = "0") int page, Model modelo) {
        Pageable pageRequest = PageRequest.of(page, 40);
        Page<Carnes> carnes = carnesService.findAll(pageRequest);
        PageRender<Carnes> pageRender = new PageRender<>("/listarCarnes", carnes);

        modelo.addAttribute("titulo", "Listado de Carnes");
        modelo.addAttribute("Carne", carnes);  // Cambié de "carnes" a "carne" para claridad
        modelo.addAttribute("page", pageRender);

        return "Carniceria/listarCarnes";  // Asegúrate de que esta vista exista
    }


    @GetMapping("/formCarnes")
    public String mostrarFormularioDeRegistrarCarnes(Model model) {
        Carnes carnes = new Carnes();
        model.addAttribute("Carne", carnes);
        model.addAttribute("categorias", categoriaRepository.findAll());
        model.addAttribute("titulo", "Registro de Carnes");
        return "Carniceria/formCarnes";  // Asegúrate de que esta vista exista
    }

    @GetMapping("/formCarnes/verificar-codigo/{codigo}")
    @ResponseBody
    public boolean verificarCodigo(@PathVariable String codigo, @RequestParam(required = false) Integer idActual) {
        // Si es una edición (idActual no es null)
        if (idActual != null) {
            Carnes carnesExistente = carnesService.findByCodigo(codigo);
            // Retorna false si el código actual es el mismo que el que se está editando
            return carnesExistente != null && !carnesExistente.getId().equals(idActual);
        }
        // Si es nuevo producto, verifica normalmente
        return carnesService.existeCodigo(codigo);
    }

    // Guardar Carnes
    @PostMapping("/formCarnes")
    public String guardarCarnes(@Valid @ModelAttribute("Carne") Carnes carnes, BindingResult result, Model modelo, RedirectAttributes flash, SessionStatus status) {
        // Verificar si hay errores de validación
        if (result.hasErrors()) {
            modelo.addAttribute("titulo", "Registro de Carnes");
            return "Carniceria/formCarnes";  // Retorna al formulario si hay errores
        }

        // Verificar si el código ya existe
        if (carnesService.existeCodigo(carnes.getCodigo())) {
            // Si estamos editando, obtenemos el ID actual
            Integer idActual = carnes.getId();
            // Verificamos si el código ya existe y no es el mismo que el actual
            if (idActual == null || !carnesService.findByCodigo(carnes.getCodigo()).getId().equals(idActual)) {
                result.rejectValue("codigo", "error.codigo", "El código de barras ya está en uso.");
            }
        }

        // Verificar nuevamente si hay errores después de la validación del código
        if (result.hasErrors()) {
            modelo.addAttribute("titulo", "Registro de Carnes");
            return "Carniceria/formCarnes";  // Retorna al formulario si hay errores
        }

        // Guardar el objeto Carnes
        carnesService.save(carnes);
        status.setComplete();
        flash.addFlashAttribute("success", "Carnes registrada o editada con éxito");
        return "redirect:/listarCarnes";
    }

    // Editar Carnes
    @GetMapping("/form/Carnes/{id}")
    public String editarCarnes(@PathVariable("id") Integer id, Model modelo, RedirectAttributes flash) {
        Carnes carnes = carnesService.findOne(id);
        if (carnes == null) {
            flash.addFlashAttribute("error", "El ID de la verdura no existe en la base de datos");
            return "redirect:/listarCarnes";
        }

        modelo.addAttribute("categorias", categoriaRepository.findAll());
        modelo.addAttribute("Carne", carnes); // Asegúrate de que este nombre sea correcto
        modelo.addAttribute("titulo", "Edición de las Carnes");
        return "Carniceria/formCarnes";  // Debe ser el mismo formulario para editar
    }

    // Eliminar Carnes
    @GetMapping("/eliminar/Carnes/{id}")
    public String eliminarCarnes(@PathVariable(value = "id") Integer id, RedirectAttributes flash) {
        if (id > 0) {
            carnesService.delete(id);
            flash.addFlashAttribute("success", "Carne eliminada con éxito");
        }
        return "redirect:/listarCarnes";
    }
}