package com.gestion.inventario.controlador;

import com.gestion.inventario.entidades.Factura_Electronica;
import com.gestion.inventario.servicio.Facturas_ElectronicaService;
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
@RequestMapping("/facturas")
public class Factura_ElectronicaController {

    @Autowired
    private Facturas_ElectronicaService facturasElectronicaService;

    // Listar todas las facturas electrónicas
    @GetMapping("/listar")
    public String listarFacturas(
            @RequestParam(name = "page", defaultValue = "0") int page,
            Model model) {

        Pageable pageRequest = PageRequest.of(page, 10);
        Page<Factura_Electronica> facturas = facturasElectronicaService.findAll(pageRequest);
        PageRender<Factura_Electronica> pageRender = new PageRender<>("/facturas/listar", facturas);

        model.addAttribute("facturas", facturas.getContent());
        model.addAttribute("factura", new Factura_Electronica()); // Para el formulario de nueva factura
        model.addAttribute("page", pageRender);
        model.addAttribute("titulo", "Lista de Facturas Electrónicas");

        return "FacturaElectronica/listarFacturasElectronica";
    }

    // Guardar una nueva factura electrónica
    @PostMapping("/agregar")
    public String agregarFactura(
            @Valid @ModelAttribute Factura_Electronica factura,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Error en los datos de la factura");
            return "redirect:/facturas/listar";
        }

        try {
            facturasElectronicaService.save(factura);
            redirectAttributes.addFlashAttribute("success", "Factura creada con éxito");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear la factura");
        }
        return "redirect:/facturas/listar";
    }

    // Editar una factura electrónica
    @PostMapping("/editar/{id}")
    public String editarFactura(
            @PathVariable Long id,
            @ModelAttribute Factura_Electronica factura,
            RedirectAttributes redirectAttributes) {

        try {
            Factura_Electronica facturaExistente = facturasElectronicaService.findOne(id);
            if (facturaExistente != null) {
                // Actualizar campos específicos
                facturaExistente.setNumeroFactura(factura.getNumeroFactura()); // Reemplaza con los campos reales
                facturaExistente.setFecha(factura.getFecha());
                facturaExistente.setXmlDocument(factura.getXmlDocument());
                // Añade otros campos según tu entidad Factura_Electronica

                facturasElectronicaService.save(facturaExistente);
                redirectAttributes.addFlashAttribute("success", "Factura actualizada con éxito");
            } else {
                redirectAttributes.addFlashAttribute("error", "Factura no encontrada");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar la factura");
        }
        return "redirect:/facturas/listar";
    }

    // Eliminar una factura electrónica
    @GetMapping("/eliminar/{id}")
    public String eliminarFactura(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes) {

        try {
            facturasElectronicaService.delete(id);
            redirectAttributes.addFlashAttribute("success", "Factura eliminada con éxito");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar la factura");
        }
        return "redirect:/facturas/listar";
    }
}