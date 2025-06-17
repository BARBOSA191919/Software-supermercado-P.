package com.gestion.inventario.controlador;

import com.gestion.inventario.ServiceImpl.VentaFacturaService;
import com.gestion.inventario.entidades.Contabilidad;
import com.gestion.inventario.entidades.Venta;
import com.gestion.inventario.servicio.ContabilidadService;

import com.gestion.inventario.servicio.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FacturaController {

    @Autowired
    private VentaService ventaService; // Asegúrate de tener este servicio

    @Autowired
    private VentaFacturaService ventaFacturaService; // Corregido: agregar 'private' y hacer 'Autowired'

    @GetMapping("/ventas/facturar/{id}")
    public ResponseEntity<byte[]> facturar(@PathVariable Integer id) {
        try {
            // Buscar la venta por ID
            Venta venta = ventaService.findById(id);

            if (venta == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // Generar PDF usando el servicio
            byte[] pdfBytes = ventaFacturaService.generarVentapdf(venta);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.inline().filename("factura_" + id + ".pdf").build());

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}