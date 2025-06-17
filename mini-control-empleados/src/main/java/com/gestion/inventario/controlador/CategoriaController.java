package com.gestion.inventario.controlador;

import com.gestion.inventario.entidades.*;
import com.gestion.inventario.repositorios.CarnesRepository;
import com.gestion.inventario.repositorios.FruvertRepository;
import com.gestion.inventario.repositorios.ProductosRepository;
import com.gestion.inventario.repositorios.VerduraRepository;
import com.gestion.inventario.servicio.CategoriaService;
import com.gestion.inventario.util.paginacion.PageRender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private ProductosRepository productosRepository;

    @Autowired
    private VerduraRepository verduraRepository;

    @Autowired
    private FruvertRepository fruvertRepository;

    @Autowired
    private CarnesRepository carnesRepository;

    @GetMapping("/api/categorias/{id}/productos")
    @ResponseBody
    public ResponseEntity<?> obtenerProductosPorCategoria(
            @PathVariable(value = "id") Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            // Buscar la categoría por ID
            Categoria categoria = categoriaService.findOne(id);
            if (categoria == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("mensaje", "No se encontró la categoría con ID: " + id));
            }

            // Configurar paginación
            Pageable pageable = PageRequest.of(page, size);

            // Obtener productos paginados por categoría
            Page<Producto> productosPage = productosRepository.findByCategoria(categoria, pageable);
            Page<Fruvert> fruvertPage = fruvertRepository.findByCategoria(categoria, pageable);
            Page<Verdura> verduraPage = verduraRepository.findByCategoria(categoria, pageable);
            Page<Carnes> carnesPage = carnesRepository.findByCategoria(categoria, pageable);

            // Mapear a DTOs
            List<Map<String, Object>> productosDTO = mapToDTO(productosPage.getContent(), Producto.class);
            List<Map<String, Object>> fruvertDTO = mapToDTO(fruvertPage.getContent(), Fruvert.class);
            List<Map<String, Object>> verduraDTO = mapToDTO(verduraPage.getContent(), Verdura.class);
            List<Map<String, Object>> carnesDTO = mapToDTO(carnesPage.getContent(), Carnes.class);

            // Crear respuesta
            Map<String, Object> response = new HashMap<>();
            response.put("nombreCategoria", categoria.getNombre());
            response.put("productos", productosDTO);
            response.put("fruvert", fruvertDTO);
            response.put("verdura", verduraDTO);
            response.put("carne", carnesDTO);
            response.put("total", productosPage.getTotalElements() + fruvertPage.getTotalElements() +
                    verduraPage.getTotalElements() + carnesPage.getTotalElements());
            response.put("totalPages", Math.max(productosPage.getTotalPages(), Math.max(fruvertPage.getTotalPages(),
                    Math.max(verduraPage.getTotalPages(), carnesPage.getTotalPages()))));
            response.put("currentPage", page);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);

        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("mensaje", "Error al procesar la solicitud");
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }


    @GetMapping("/api/productos/buscar")
    @ResponseBody
    public ResponseEntity<?> buscarProductos(
            @RequestParam(value = "query", required = false, defaultValue = "") String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            // Configurar paginación
            Pageable pageable = PageRequest.of(page, size);

            // Buscar en cada tipo de producto
            Page<Producto> productosPage = productosRepository.findByNombreContainingIgnoreCaseOrCodigoContainingIgnoreCase(query, query, pageable);
            Page<Fruvert> fruvertPage = fruvertRepository.findByNombreContainingIgnoreCaseOrCodigoContainingIgnoreCase(query, query, pageable);
            Page<Verdura> verduraPage = verduraRepository.findByNombreContainingIgnoreCaseOrCodigoContainingIgnoreCase(query, query, pageable);
            Page<Carnes> carnesPage = carnesRepository.findByNombreContainingIgnoreCaseOrCodigoContainingIgnoreCase(query, query, pageable);

            // Mapear a DTOs
            List<Map<String, Object>> productosDTO = mapToDTO(productosPage.getContent(), Producto.class);
            List<Map<String, Object>> fruvertDTO = mapToDTO(fruvertPage.getContent(), Fruvert.class);
            List<Map<String, Object>> verduraDTO = mapToDTO(verduraPage.getContent(), Verdura.class);
            List<Map<String, Object>> carnesDTO = mapToDTO(carnesPage.getContent(), Carnes.class);

            // Combinar todos los resultados
            List<Map<String, Object>> allProducts = new ArrayList<>();
            allProducts.addAll(productosDTO);
            allProducts.addAll(fruvertDTO);
            allProducts.addAll(verduraDTO);
            allProducts.addAll(carnesDTO);

            // Crear respuesta
            Map<String, Object> response = new HashMap<>();
            response.put("productos", allProducts); // Aquí devolvemos todos los productos combinados bajo la clave "productos"
            response.put("total", productosPage.getTotalElements() + fruvertPage.getTotalElements() +
                    verduraPage.getTotalElements() + carnesPage.getTotalElements());
            response.put("totalPages", Math.max(productosPage.getTotalPages(), Math.max(fruvertPage.getTotalPages(),
                    Math.max(verduraPage.getTotalPages(), carnesPage.getTotalPages()))));
            response.put("currentPage", page);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(response);

        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("mensaje", "Error al buscar productos");
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }

    // Método auxiliar para mapear entidades a DTOs
    private List<Map<String, Object>> mapToDTO(List<?> entities, Class<?> entityType) {
        List<Map<String, Object>> dtos = new ArrayList<>();
        for (Object entity : entities) {
            Map<String, Object> dto = new HashMap<>();
            if (entity instanceof Producto) {
                Producto p = (Producto) entity;
                dto.put("id", p.getId());
                dto.put("codigo", p.getCodigo());
                dto.put("nombre", p.getNombre());
                dto.put("precio", p.getPrecio());
                dto.put("existencia", p.getExistencia());
                dto.put("iva", p.getIva());
                dto.put("descuento", p.getDescuento());
                dto.put("precio_final", p.getPrecio_final());
                dto.put("categoria", p.getCategoria() != null ? Map.of("id", p.getCategoria().getId(), "nombre", p.getCategoria().getNombre()) : null);
            } else if (entity instanceof Fruvert) {
                Fruvert f = (Fruvert) entity;
                dto.put("id", f.getId());
                dto.put("codigo", f.getCodigo());
                dto.put("nombre", f.getNombre());
                dto.put("precio", f.getPrecio());
                dto.put("existencia", f.getExistencia());
                dto.put("iva", f.getIva());
                dto.put("descuento", f.getDescuento());
                dto.put("precio_final", f.getPrecio_final());
                dto.put("categoria", f.getCategoria() != null ? Map.of("id", f.getCategoria().getId(), "nombre", f.getCategoria().getNombre()) : null);
            } else if (entity instanceof Verdura) {
                Verdura v = (Verdura) entity;
                dto.put("id", v.getId());
                dto.put("codigo", v.getCodigo());
                dto.put("nombre", v.getNombre());
                dto.put("precio", v.getPrecio());
                dto.put("existencia", v.getExistencia());
                dto.put("iva", v.getIva());
                dto.put("descuento", v.getDescuento());
                dto.put("precio_final", v.getPrecio_final());
                dto.put("categoria", v.getCategoria() != null ? Map.of("id", v.getCategoria().getId(), "nombre", v.getCategoria().getNombre()) : null);
            } else if (entity instanceof Carnes) {
                Carnes c = (Carnes) entity;
                dto.put("id", c.getId());
                dto.put("codigo", c.getCodigo());
                dto.put("nombre", c.getNombre());
                dto.put("precio", c.getPrecio());
                dto.put("existencia", c.getExistencia());
                dto.put("iva", c.getIva());
                dto.put("descuento", c.getDescuento());
                dto.put("precio_final", c.getPrecio_final());
                dto.put("categoria", c.getCategoria() != null ? Map.of("id", c.getCategoria().getId(), "nombre", c.getCategoria().getNombre()) : null);
            }
            dtos.add(dto);
        }
        return dtos;
    }


    @GetMapping("/listarCategorias")
    @PreAuthorize("hasAnyRole('GERENTE', 'ENCARGADO')")
    public String listarCategorias(@RequestParam(name = "page", defaultValue = "0") int page, Model modelo) {
        Pageable pageRequest = PageRequest.of(page, 8);
        Page<Categoria> categorias = categoriaService.findAll(pageRequest);
        PageRender<Categoria> pageRender = new PageRender<>("/listarCategorias", categorias);

        modelo.addAttribute("titulo", "Listado de Categorías");
        modelo.addAttribute("categorias", categorias);
        modelo.addAttribute("page", pageRender);

        return "Categorias/listarCategorias";
    }

    @GetMapping("/ver/categoria/{id}")
    @PreAuthorize("hasAnyRole('GERENTE', 'ENCARGADO')")
    public String verDetallesCategoria(@PathVariable(value = "id") Long id,
                                       Map<String, Object> modelo, RedirectAttributes flash) {
        Categoria categoria = categoriaService.findOne(id);
        if (categoria == null) {
            flash.addFlashAttribute("error", "La categoría no existe en la base de datos");
            return "redirect:/categorias";
        }

        modelo.put("categoria", categoria);
        modelo.put("titulo", "Detalles de la Categoría");
        return "Categorias/verCategoria";
    }

    @PostMapping("/api/categorias")
    @PreAuthorize("hasAnyRole('GERENTE', 'ENCARGADO')")
    @ResponseBody
    public ResponseEntity<?> crearCategoriaAPI(@RequestBody Categoria categoria) {
        try {
            if (categoria.getNombre() == null || categoria.getNombre().isEmpty()) {
                return ResponseEntity.badRequest().body("El nombre de la categoría es requerido");
            }
            categoriaService.save(categoria);
            return ResponseEntity.ok(categoria);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear la categoría: " + e.getMessage());
        }
    }

    @GetMapping("/api/categorias")
    @ResponseBody
    public ResponseEntity<?> listarCategoriasAPI() {
        try {
            List<Categoria> categorias = categoriaService.findAll();
            return ResponseEntity.ok(categorias);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener las categorías: " + e.getMessage());
        }
    }

    @DeleteMapping("/api/categorias/{id}")
    @PreAuthorize("hasAnyRole('GERENTE', 'ENCARGADO')")
    @ResponseBody
    public ResponseEntity<?> eliminarCategoriaAPI(@PathVariable(value = "id") Long id) {
        try {
            Categoria categoria = categoriaService.findOne(id);
            if (categoria == null) {
                return ResponseEntity.notFound().build();
            }
            categoriaService.delete(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("No se pudo eliminar la categoría: " + e.getMessage());
        }
    }

    @GetMapping("/eliminar/categoria/{id}")
    @PreAuthorize("hasRole('GERENTE')")
    public String eliminarCategoria(@PathVariable(value = "id") Long id, RedirectAttributes flash) {
        if (id > 0) {
            categoriaService.delete(id);
            flash.addFlashAttribute("success", "Categoría eliminada con éxito");
        }
        return "redirect:/categorias";
    }

    @PutMapping("/api/categorias/{id}")
    @PreAuthorize("hasAnyRole('GERENTE', 'ENCARGADO')")
    @ResponseBody
    public ResponseEntity<?> actualizarCategoriaAPI(@PathVariable(value = "id") Long id,
                                                    @RequestBody Categoria categoriaActualizada) {
        try {
            // Buscar la categoría existente
            Categoria categoriaExistente = categoriaService.findOne(id);
            if (categoriaExistente == null) {
                return ResponseEntity.notFound().build();
            }

            // Validar los datos
            if (categoriaActualizada.getNombre() == null || categoriaActualizada.getNombre().isEmpty()) {
                return ResponseEntity.badRequest().body("El nombre de la categoría es requerido");
            }

            // Actualizar los campos
            categoriaExistente.setNombre(categoriaActualizada.getNombre());
            categoriaExistente.setCodigo(categoriaActualizada.getCodigo());

            // Guardar los cambios
            categoriaService.save(categoriaExistente);

            return ResponseEntity.ok(categoriaExistente);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body("Error al actualizar la categoría: " + e.getMessage());
        }
    }
}