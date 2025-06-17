package com.gestion.inventario.controlador;

import com.gestion.inventario.entidades.*;
import com.gestion.inventario.repositorios.*;
import com.gestion.inventario.servicio.DevolucionesService;
import com.gestion.inventario.util.paginacion.PageRender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/devoluciones")
public class DevolucionesController {

    @Autowired
    private DevolucionesService devolucionesService;

    @Autowired
    private ProductosRepository productosRepository;

    @Autowired
    private FruvertRepository fruvertRepository;

    @Autowired
    private VerduraRepository verduraRepository;

    @Autowired
    private CarnesRepository carnesRepository;

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private HttpServletRequest request; // Para detectar solicitudes AJAX

    // Métodos de búsqueda existentes (sin cambios)
    @GetMapping("/buscarProductos")
    @ResponseBody
    public List<Map<String, Object>> buscarProductos(@RequestParam String q, @RequestParam String tipo) {
        // Código existente sin cambios
        List<Map<String, Object>> resultados = new ArrayList<>();
        switch (tipo) {
            case "PRODUCTO":
                productosRepository.findByNombreContainingOrCodigoContaining(q, q).forEach(producto -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("codigo", producto.getCodigo());
                    item.put("nombre", producto.getNombre());
                    item.put("precio", producto.getPrecio_final());
                    resultados.add(item);
                });
                break;
            case "FRUTA":
                fruvertRepository.findByNombreContainingOrCodigoContaining(q, q).forEach(fruta -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("codigo", fruta.getCodigo());
                    item.put("nombre", fruta.getNombre());
                    item.put("precio", fruta.getPrecio_final());
                    resultados.add(item);
                });
                break;
            case "VERDURA":
                verduraRepository.findByNombreContainingOrCodigoContaining(q, q).forEach(verdura -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("codigo", verdura.getCodigo());
                    item.put("nombre", verdura.getNombre());
                    item.put("precio", verdura.getPrecio_final());
                    resultados.add(item);
                });
                break;
            case "CARNE":
                carnesRepository.findByNombreContainingOrCodigoContaining(q, q).forEach(carne -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("codigo", carne.getCodigo());
                    item.put("nombre", carne.getNombre());
                    item.put("precio", carne.getPrecio_final());
                    resultados.add(item);
                });
                break;
        }
        return resultados;
    }

    @GetMapping("/buscarProductosPorVenta")
    @ResponseBody
    public List<Map<String, Object>> buscarProductosPorVenta(@RequestParam Integer idVenta) {
        // Código existente sin cambios
        try {
            Venta venta = ventaRepository.findById(idVenta)
                    .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
            List<Map<String, Object>> resultado = new ArrayList<>();
            for (ProductoVendido producto : venta.getProductos()) {
                Map<String, Object> item = new HashMap<>();
                item.put("codigo", producto.getCodigo());
                item.put("nombre", producto.getNombre());
                item.put("precio", producto.getPrecio_final());
                item.put("cantidad", producto.getCantidad());
                resultado.add(item);
            }
            return resultado;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @GetMapping("/listar")
    public String listarDevoluciones(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "motivo", required = false) String motivo,
            @RequestParam(name = "fechaInicio", required = false) String fechaInicio,
            @RequestParam(name = "fechaFin", required = false) String fechaFin,
            Model model) {

        page = Math.max(0, page);
        Pageable pageRequest = PageRequest.of(page, 5);
        Page<Devoluciones> devoluciones = devolucionesService.findAllBySearchAndMotivoAndFecha(
                search, motivo, fechaInicio, fechaFin, pageRequest);

        if (devoluciones.getTotalElements() == 0 && page > 0) {
            return "redirect:/devoluciones/listar?search=" +
                    (search != null ? search : "") +
                    "&motivo=" + (motivo != null ? motivo : "") +
                    "&fechaInicio=" + (fechaInicio != null ? fechaInicio : "") +
                    "&fechaFin=" + (fechaFin != null ? fechaFin : "") +
                    "&page=0";
        }

        PageRender<Devoluciones> pageRender = new PageRender<>("/devoluciones/listar", devoluciones);

        List<Producto> productos = (List<Producto>) productosRepository.findAll();
        List<Fruvert> frutas = (List<Fruvert>) fruvertRepository.findAll();
        List<Verdura> verduras = (List<Verdura>) verduraRepository.findAll();
        List<Carnes> carnes = (List<Carnes>) carnesRepository.findAll();

        model.addAttribute("productos", productos);
        model.addAttribute("frutas", frutas);
        model.addAttribute("verduras", verduras);
        model.addAttribute("carnes", carnes);
        model.addAttribute("devoluciones", devoluciones.getContent());
        model.addAttribute("devolucion", new Devoluciones());
        model.addAttribute("search", search);
        model.addAttribute("motivo", motivo);
        model.addAttribute("fechaInicio", fechaInicio);
        model.addAttribute("fechaFin", fechaFin);
        model.addAttribute("page", pageRender);
        model.addAttribute("titulo", "Lista de Devoluciones");

        if (devoluciones.getTotalElements() == 0) {
            model.addAttribute("info", "No se encontraron resultados con los filtros aplicados");
        }

        // Detectar si es una solicitud AJAX
        String acceptHeader = request.getHeader("X-Requested-With");
        if ("XMLHttpRequest".equals(acceptHeader)) {
            return "Devoluciones/listarDevoluciones :: #table-container"; // Solo la tabla
        }

        return "Devoluciones/listarDevoluciones";
    }

    @PostMapping("/agregar")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> agregarDevoluciones(
            @Valid @ModelAttribute Devoluciones devoluciones,
            @RequestParam("tipoProducto") String tipoProducto,
            @RequestParam("codigoProducto") String codigoProducto,
            @RequestParam("cantidad") Integer cantidad,
            @RequestParam("motivo") String motivo,
            BindingResult result) {

        Map<String, Object> response = new HashMap<>();

        if (result.hasErrors()) {
            response.put("success", false);
            response.put("error", "Error en los datos de la devolución");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            Float precioUnitario = 0.0F;
            String nombreProducto = "";
            boolean productoValido = false;

            switch (tipoProducto) {
                case "PRODUCTO":
                    Producto producto = productosRepository.findByCodigo(codigoProducto);
                    if (producto != null) {
                        precioUnitario = producto.getPrecio_final();
                        nombreProducto = producto.getNombre();
                        productoValido = true;
                    }
                    break;
                case "FRUTA":
                    Fruvert fruta = fruvertRepository.findByCodigo(codigoProducto);
                    if (fruta != null) {
                        precioUnitario = fruta.getPrecio_final();
                        nombreProducto = fruta.getNombre();
                        productoValido = true;
                    }
                    break;
                case "VERDURA":
                    Verdura verdura = verduraRepository.findByCodigo(codigoProducto);
                    if (verdura != null) {
                        precioUnitario = verdura.getPrecio_final();
                        nombreProducto = verdura.getNombre();
                        productoValido = true;
                    }
                    break;
                case "CARNE":
                    Carnes carne = carnesRepository.findByCodigo(codigoProducto);
                    if (carne != null) {
                        precioUnitario = carne.getPrecio_final();
                        nombreProducto = carne.getNombre();
                        productoValido = true;
                    }
                    break;
            }

            if (!productoValido) {
                response.put("success", false);
                response.put("error", "El producto seleccionado no coincide con el tipo de producto elegido.");
                return ResponseEntity.badRequest().body(response);
            }

            if (precioUnitario > 0) {
                Float precioFinal = precioUnitario * cantidad;

                devoluciones.setCodigoProducto(codigoProducto);
                devoluciones.setNombreProducto(nombreProducto);
                devoluciones.setTipoProducto(Devoluciones.TipoProducto.valueOf(tipoProducto));
                devoluciones.setPrecio(precioUnitario);
                devoluciones.setPrecio_final(precioFinal);
                devoluciones.setCantidad(cantidad);
                devoluciones.setMotivo(motivo);

                devolucionesService.save(devoluciones);

                if (!motivo.equals("Vencido o defectuoso") && !motivo.equals("producto incompleto")
                        && !motivo.equals("error en el pedido") && !motivo.equals("mal funcionamiento")
                        && !motivo.equals("baja calidad")) {
                    sumarAlInventario(tipoProducto, codigoProducto, cantidad);
                }

                response.put("success", true);
                response.put("message", "Devolución creada con éxito y stock actualizado");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("error", "Producto no encontrado");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "Error al crear la devolución: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/editar/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> editarDevolucion(
            @PathVariable Long id,
            @Valid @ModelAttribute Devoluciones devolucion,
            @RequestParam("tipoProducto") String tipoProducto,
            @RequestParam("codigoProducto") String codigoProducto,
            @RequestParam("cantidad") Integer cantidad,
            @RequestParam("motivo") String motivo,
            BindingResult result) {

        Map<String, Object> response = new HashMap<>();

        if (result.hasErrors()) {
            response.put("success", false);
            response.put("error", "Error en los datos de la devolución");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            Devoluciones devolucionExistente = devolucionesService.findOne(id);
            if (devolucionExistente == null) {
                response.put("success", false);
                response.put("error", "Devolución no encontrada");
                return ResponseEntity.badRequest().body(response);
            }

            if (!motivo.equals("Vencido o defectuoso") && !motivo.equals("producto incompleto")
                    && !motivo.equals("error en el pedido") && !motivo.equals("mal funcionamiento")
                    && !motivo.equals("baja calidad")) {
                restarDelInventario(
                        devolucionExistente.getTipoProducto().toString(),
                        devolucionExistente.getCodigoProducto(),
                        devolucionExistente.getCantidad()
                );
            }

            Float precioUnitario = 0.0F;
            String nombreProducto = "";
            boolean productoValido = false;

            switch (tipoProducto) {
                case "PRODUCTO":
                    Producto producto = productosRepository.findByCodigo(codigoProducto);
                    if (producto != null) {
                        precioUnitario = producto.getPrecio_final();
                        nombreProducto = producto.getNombre();
                        productoValido = true;
                    }
                    break;
                case "FRUTA":
                    Fruvert fruta = fruvertRepository.findByCodigo(codigoProducto);
                    if (fruta != null) {
                        precioUnitario = fruta.getPrecio_final();
                        nombreProducto = fruta.getNombre();
                        productoValido = true;
                    }
                    break;
                case "VERDURA":
                    Verdura verdura = verduraRepository.findByCodigo(codigoProducto);
                    if (verdura != null) {
                        precioUnitario = verdura.getPrecio_final();
                        nombreProducto = verdura.getNombre();
                        productoValido = true;
                    }
                    break;
                case "CARNE":
                    Carnes carne = carnesRepository.findByCodigo(codigoProducto);
                    if (carne != null) {
                        precioUnitario = carne.getPrecio_final();
                        nombreProducto = carne.getNombre();
                        productoValido = true;
                    }
                    break;
            }

            if (!productoValido) {
                response.put("success", false);
                response.put("error", "El producto seleccionado no coincide con el tipo de producto elegido.");
                return ResponseEntity.badRequest().body(response);
            }

            if (precioUnitario > 0) {
                Float precioFinal = precioUnitario * cantidad;

                devolucionExistente.setCodigoProducto(codigoProducto);
                devolucionExistente.setNombreProducto(nombreProducto);
                devolucionExistente.setTipoProducto(Devoluciones.TipoProducto.valueOf(tipoProducto));
                devolucionExistente.setPrecio(precioUnitario);
                devolucionExistente.setPrecio_final(precioFinal);
                devolucionExistente.setCantidad(cantidad);
                devolucionExistente.setFecha(devolucion.getFecha());
                devolucionExistente.setMotivo(motivo);

                if (!motivo.equals("Vencido o defectuoso") && !motivo.equals("producto incompleto")
                        && !motivo.equals("error en el pedido") && !motivo.equals("mal funcionamiento")
                        && !motivo.equals("baja calidad")) {
                    sumarAlInventario(tipoProducto, codigoProducto, cantidad);
                }

                devolucionesService.save(devolucionExistente);

                response.put("success", true);
                response.put("message", "Devolución actualizada con éxito y stock actualizado");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("error", "Producto no encontrado");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "Error al actualizar la devolución: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/eliminar/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> eliminarDevolucion(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        try {
            Devoluciones devolucion = devolucionesService.findOne(id);
            if (devolucion != null) {
                devolucionesService.delete(id);
                response.put("success", true);
                response.put("message", "Devolución eliminada con éxito");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("error", "Devolución no encontrada");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "Error al eliminar la devolución: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // Agregar este método al controlador existente (DevolucionesController.java)
    @GetMapping("/todas")
    @ResponseBody
    public List<Devoluciones> obtenerTodasDevoluciones(
            @RequestParam(name = "search", required = false) String search,
            @RequestParam(name = "motivo", required = false) String motivo,
            @RequestParam(name = "fechaInicio", required = false) String fechaInicio,
            @RequestParam(name = "fechaFin", required = false) String fechaFin) {
        // Obtener todas las devoluciones sin paginación, aplicando filtros
        return devolucionesService.findAllBySearchAndMotivoAndFecha(search, motivo, fechaInicio, fechaFin, null)
                .getContent();
    }

    // Métodos privados sin cambios
    private void sumarAlInventario(String tipoProducto, String codigoProducto, Integer cantidad) {
        switch (tipoProducto) {
            case "PRODUCTO":
                Producto producto = productosRepository.findByCodigo(codigoProducto);
                if (producto != null) {
                    producto.sumarExistencia(cantidad);
                    productosRepository.save(producto);
                }
                break;
            case "FRUTA":
                Fruvert fruta = fruvertRepository.findByCodigo(codigoProducto);
                if (fruta != null) {
                    fruta.sumarExistencia(cantidad);
                    fruvertRepository.save(fruta);
                }
                break;
            case "VERDURA":
                Verdura verdura = verduraRepository.findByCodigo(codigoProducto);
                if (verdura != null) {
                    verdura.sumarExistencia(cantidad);
                    verduraRepository.save(verdura);
                }
                break;
            case "CARNE":
                Carnes carne = carnesRepository.findByCodigo(codigoProducto);
                if (carne != null) {
                    carne.sumarExistencia(cantidad);
                    carnesRepository.save(carne);
                }
                break;
        }
    }

    private void restarDelInventario(String tipoProducto, String codigoProducto, Integer cantidad) {
        switch (tipoProducto) {
            case "PRODUCTO":
                Producto producto = productosRepository.findByCodigo(codigoProducto);
                if (producto != null) {
                    producto.restarExistencia(Float.valueOf(cantidad));
                    productosRepository.save(producto);
                }
                break;
            case "FRUTA":
                Fruvert fruta = fruvertRepository.findByCodigo(codigoProducto);
                if (fruta != null) {
                    fruta.restarExistencia(Float.valueOf(cantidad));
                    fruvertRepository.save(fruta);
                }
                break;
            case "VERDURA":
                Verdura verdura = verduraRepository.findByCodigo(codigoProducto);
                if (verdura != null) {
                    verdura.restarExistencia(Float.valueOf(cantidad));
                    verduraRepository.save(verdura);
                }
                break;
            case "CARNE":
                Carnes carne = carnesRepository.findByCodigo(codigoProducto);
                if (carne != null) {
                    carne.restarExistencia(Float.valueOf(cantidad));
                    carnesRepository.save(carne);
                }
                break;
        }
    }
}