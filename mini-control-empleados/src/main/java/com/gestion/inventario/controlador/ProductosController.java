package com.gestion.inventario.controlador;

import com.gestion.inventario.entidades.Categoria;
import com.gestion.inventario.entidades.Producto;
import com.gestion.inventario.repositorios.CategoriaRepository;
import com.gestion.inventario.repositorios.ProductosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/productos")
public class ProductosController {
    @Autowired
    private ProductosRepository productosRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping(value = "/agregar")
    public String agregarProducto(Model model) {

        model.addAttribute("producto", new Producto());
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "productos/agregar_producto"; // debe corresponder a /templates/productos/agregar_producto.html
    }

    @GetMapping(value = "/mostrar")
    public String mostrarProductos(Model model) {
        model.addAttribute("productos", productosRepository.findAll());
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "productos/ver_productos";
    }

    @PostMapping(value = "/eliminar")
    public String eliminarProducto(@RequestParam Integer id, RedirectAttributes redirectAttrs) {
        productosRepository.deleteById(id);
        redirectAttrs
                .addFlashAttribute("mensaje", "Eliminado correctamente")
                .addFlashAttribute("clase", "warning");
        return "redirect:/productos/mostrar";
    }

    // Editar un producto
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Integer id, Model model) {
        Producto producto = productosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        model.addAttribute("producto", producto);
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "productos/ver_productos"; // Usar la misma vista para editar
    }

    @PostMapping("/editar/{id}")
    public String editarProducto(@PathVariable Integer id, @Valid @ModelAttribute Producto producto, BindingResult bindingResult, RedirectAttributes redirectAttrs) {
        if (bindingResult.hasErrors()) {
            return "productos/ver_productos"; // Retornar a la vista de productos si hay errores
        }

        Producto productoExistente = productosRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        // Actualizar campos del producto existente
        productoExistente.setNombre(producto.getNombre());
        productoExistente.setCodigo(producto.getCodigo());
        productoExistente.setPrecio(producto.getPrecio());
        productoExistente.setExistencia(producto.getExistencia());
        productoExistente.setIva(producto.getIva());
        productoExistente.setDescuento(producto.getDescuento());
        productoExistente.setPrecio_final(producto.getPrecio_final());
        productoExistente.setCategoria(producto.getCategoria());

        productosRepository.save(productoExistente);
        redirectAttrs.addFlashAttribute("mensaje", "Producto actualizado correctamente")
                .addFlashAttribute("clase", "success");
        return "redirect:/productos/mostrar"; // Redirigir a la lista de productos
    }

    @PostMapping(value = "/agregar")
    public String guardarProducto(@ModelAttribute @Valid Producto producto, BindingResult bindingResult, RedirectAttributes redirectAttrs) {
        if (bindingResult.hasErrors()) {
            return "productos/agregar_producto";
        }
        if (productosRepository.findByCodigo(producto.getCodigo()) != null) {
            redirectAttrs
                    .addFlashAttribute("mensaje", "Ya existe un producto con ese código")
                    .addFlashAttribute("clase", "warning");
            return "redirect:/productos/agregar";
        }

        Categoria categoria = categoriaRepository.findById(producto.getCategoria().getId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        producto.setCategoria(categoria);

        productosRepository.save(producto);
        redirectAttrs
                .addFlashAttribute("mensaje", "Agregado correctamente")
                .addFlashAttribute("clase", "success");
        return "redirect:/productos/agregar";
    }
}