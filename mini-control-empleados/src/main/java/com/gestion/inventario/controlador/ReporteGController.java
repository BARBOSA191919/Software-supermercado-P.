package com.gestion.inventario.controlador;

import com.gestion.inventario.entidades.*;
import com.gestion.inventario.repositorios.*;
import com.gestion.inventario.servicio.CategoriaService;
import com.gestion.inventario.servicio.ReporteGService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


import java.time.LocalDate;
import java.util.*;


@Controller
public class ReporteGController {

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private ReporteGService reporteService;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private InventarioRepository inventarioRepository;

    @Autowired
    private ProductosRepository productosRepository;

    @Autowired
    private VerduraRepository verduraRepository;

    @Autowired
    private CarnesRepository carnesRepository;

    @Autowired
    private FruvertRepository fruvertRepository;

    @GetMapping("/reporteInventario")
    public String mostrarReporte(Model model) {
        // Agregar datos que quieras mostrar en el HTML
        model.addAttribute("titulo", "Mi Reporte");
        model.addAttribute("fecha", LocalDate.now());

        return "ReporteGeneral/ReporteInventario";
    }

    @GetMapping("/reporteTurno")
    public String mostrarReporteTurno(Model model) {
        // Agregar datos que quieras mostrar en el HTML
        model.addAttribute("titulo", "Mi Reporte");
        model.addAttribute("fecha", LocalDate.now());

        return "ReporteGeneral/ReporteTurno";
    }

    @GetMapping("/api/reporteG/categorias")
    public ResponseEntity<?> getCategorias() {
        try {
            List<Categoria> categorias = categoriaService.findAll();
            System.out.println("Categorías encontradas: " + categorias.size()); // Log para depuración
            return ResponseEntity.ok(categorias);
        } catch (Exception e) {
            System.err.println("Error al obtener categorías: " + e.getMessage()); // Log para depuración
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener las categorías: " + e.getMessage());
        }
    }


    @GetMapping("/categorias/{categoriaId}")
    public String listarPorCategoria(@PathVariable Long categoriaId,
                                     @RequestParam(defaultValue = "0") int page,
                                     Model model) {
        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        Pageable pageable = PageRequest.of(page, 40);
        Page<Reporte> productos = reporteService.findByCategoria(categoria, pageable); // Cambiado findAll a findByCategoria

        model.addAttribute("reporte", productos);
        model.addAttribute("categoriaActual", categoria);
        return "ReporteGeneral/ReporteInventario";
    }


    @GetMapping("/api/inventario/porCategoria")
    public ResponseEntity<?> obtenerProductosPorCategoria(@RequestParam Long categoriaId) {
        try {
            Categoria categoria = categoriaService.findOne(categoriaId);
            if (categoria == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("mensaje", "No se encontró la categoría con ID: " + categoriaId));
            }

            List<Map<String, Object>> result = new ArrayList<>();

            // Obtener y procesar productos
            List<Carnes> carnes = carnesRepository.findByCategoria(categoria);
            if (carnes != null) {
                for (Carnes carne : carnes) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("codigo", carne.getCodigo());
                    map.put("nombre", carne.getNombre());
                    map.put("existencia", carne.getExistencia());
                    map.put("precio", carne.getPrecio());
                    map.put("categoria", new HashMap<String, Object>() {{
                        put("id", carne.getCategoria().getId());
                        put("nombre", carne.getCategoria().getNombre());
                    }});
                    result.add(map);
                }
            }

            // Obtener y procesar fruvert
            List<Fruvert> fruverts = fruvertRepository.findByCategoria(categoria);
            if (fruverts != null) {
                for (Fruvert fruvert : fruverts) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("codigo", fruvert.getCodigo());
                    map.put("nombre", fruvert.getNombre());
                    map.put("existencia", fruvert.getExistencia());
                    map.put("precio", fruvert.getPrecio());
                    map.put("categoria", new HashMap<String, Object>() {{
                        put("id", fruvert.getCategoria().getId());
                        put("nombre", fruvert.getCategoria().getNombre());
                    }});
                    result.add(map);
                }
            }

            // Obtener y procesar verduras
            List<Verdura> verduras = verduraRepository.findByCategoria(categoria);
            if (verduras != null) {
                for (Verdura verdura : verduras) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("codigo", verdura.getCodigo());
                    map.put("nombre", verdura.getNombre());
                    map.put("existencia", verdura.getExistencia());
                    map.put("precio", verdura.getPrecio());
                    map.put("categoria", new HashMap<String, Object>() {{
                        put("id", verdura.getCategoria().getId());
                        put("nombre", verdura.getCategoria().getNombre());
                    }});
                    result.add(map);
                }
            }

            // Obtener y procesar verduras
            List<Producto> productos = productosRepository.findByCategoria(categoria);
            if (productos != null) {
                for (Producto producto : productos) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("codigo", producto.getCodigo());
                    map.put("nombre", producto.getNombre());
                    map.put("existencia", producto.getExistencia());
                    map.put("precio", producto.getPrecio());
                    map.put("categoria", new HashMap<String, Object>() {{
                        put("id", producto.getCategoria().getId());
                        put("nombre", producto.getCategoria().getNombre());
                    }});
                    result.add(map);
                }
            }

            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al procesar la solicitud: " + e.getMessage()));
        }
    }

    private void agregarProductosALista(List<?> productos, List<Map<String, Object>> result) {
        if (productos != null) {
            for (Object prod : productos) {
                Map<String, Object> map = new HashMap<>();
                // Usando reflection o cast según el tipo
                if (prod instanceof Producto) {
                    Producto p = (Producto) prod;
                    map.put("id", p.getId());
                    map.put("codigo", p.getCodigo());
                    map.put("nombre", p.getNombre());
                    map.put("stock", p.getExistencia()); // Mapear existencia a stock
                    map.put("precio", p.getPrecio());
                    map.put("categoria", p.getCategoria().getNombre());
                }
                // Agregar casos similares para otros tipos
                result.add(map);
            }
        }
    }


}

