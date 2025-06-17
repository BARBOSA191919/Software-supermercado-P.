package com.gestion.inventario.controlador;

import com.gestion.inventario.entidades.*;
import com.gestion.inventario.repositorios.*;
import com.gestion.inventario.servicio.ReporteVentaService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;


@Controller
@RequestMapping(path = "/vender")
public class VenderController {

    @Autowired
    private ProductosRepository productosRepository;

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ProductosVendidosRepository productosVendidosRepository;

    @Autowired
    private FruvertRepository fruvertRepository;

    @Autowired
    private VerduraRepository verduraRepository;

    @Autowired
    private CarnesRepository carnesRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private TurnoRepository turnoRepository;

    @Autowired
    private ReporteVentaService reporteVentaService;


    @PostMapping(value = "/quitar/{indice}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> quitarDelCarrito(@PathVariable int indice, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        ArrayList<ProductoParaVender> carrito = this.obtenerCarrito(request);

        if (carrito != null && carrito.size() > 0 && indice < carrito.size()) {
            carrito.remove(indice);
            this.guardarCarrito(carrito, request);
            response.put("success", true);
            response.put("carrito", carrito);
            response.put("subtotal", carrito.stream().mapToDouble(ProductoParaVender::getSubtotal).sum());
            response.put("total", carrito.stream().mapToDouble(ProductoParaVender::getTotal).sum());
        } else {
            response.put("success", false);
            response.put("message", "Índice no válido o carrito vacío.");
        }
        return ResponseEntity.ok(response);
    }

    private void limpiarCarrito(HttpServletRequest request) {
        this.guardarCarrito(new ArrayList<>(), request);
    }

    @PostMapping(value = "/limpiar")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> cancelarVenta(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        this.limpiarCarrito(request);
        response.put("success", true);
        response.put("message", "Venta cancelada");
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/actualizar")
    public ResponseEntity<Map<String, Serializable>> actualizarVenta(@RequestBody Map<String, Object> requestData, Model model, Integer ventaId) {
        try {

            if (ventaId != null) {
                // Imprimir para debug
                System.out.println("VentaId recibido: " + ventaId);

                // Agregar el ventaId al modelo
                model.addAttribute("ventaId", ventaId);

                // Si tienes una venta existente, cárgala
                Venta venta = ventaRepository.findById(ventaId).orElse(null);
                if (venta != null) {
                    model.addAttribute("venta", venta);
                }
            }

            // Obtener el ID de la venta, efectivo y cliente ID correctamente
            ventaId = Integer.parseInt(requestData.get("ventaId").toString());
            float efectivo = Float.parseFloat(requestData.get("efectivo").toString());
            Long clienteId = Long.parseLong(requestData.get("clienteId").toString());

            // Declarar la lista fuera del if
            List<ProductoParaVender> productosList;

            Object carritoObject = requestData.get("carrito");
            if (carritoObject instanceof List<?>) {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> carrito = (List<Map<String, Object>>) carritoObject;
                productosList = carrito.stream()
                        .map(item -> new ProductoParaVender(
                                item.get("nombre").toString(),
                                item.get("codigo").toString(),
                                Float.parseFloat(item.get("precio").toString()),
                                Float.parseFloat(item.get("precio_final").toString()),
                                Float.parseFloat(item.get("cantidad").toString()),
                                new BigDecimal(item.get("iva").toString()),
                                Float.parseFloat(item.get("Descuento").toString()),
                                Integer.parseInt(item.get("id").toString()),
                                Float.parseFloat(item.get("cantidad").toString())
                        ))
                        .collect(Collectors.toList());
            } else {
                // Handle the case where the "carrito" object is not a List
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("success", false, "message", "Invalid data format for 'carrito'"));
            }

            // Verificar que la venta existe
            Venta venta = ventaRepository.findById(ventaId).orElse(null);
            if (venta == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("success", false, "message", "Venta no encontrada"));
            }

            // Actualizar los datos de la venta
            venta.setEfectivo(efectivo);
            venta.setCambio(efectivo - venta.getTotal());
            venta.setCliente(clienteRepository.findById(clienteId).orElse(null));

            // Actualizar los productos en la venta
            venta.getProductos().clear();
            for (ProductoParaVender productoParaVender : productosList) { // Usar productosList en lugar de carrito
                ProductoVendido productoVendido = new ProductoVendido(
                        productoParaVender.getCantidad(),
                        productoParaVender.getPrecio(),
                        productoParaVender.getPrecio_final(),
                        productoParaVender.getIva(),
                        productoParaVender.getDescuento(),
                        productoParaVender.getNombre(),
                        productoParaVender.getCodigo(),
                        venta
                );
                venta.getProductos().add(productoVendido);
            }

            model.addAttribute("ventaId", ventaId);

            // Guardar los cambios en la venta
            ventaRepository.save(venta);
            return ResponseEntity.ok(Map.of("success", true));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Error en los datos enviados: " + e.getMessage()
            ));
        }
    }


    @PostMapping(value = "/terminar")
    @ResponseBody // Asegúrate de que este método devuelva JSON
    public ResponseEntity<Map<String, Object>> terminarVenta(
            @RequestParam float efectivo,
            @RequestParam Long clienteId,
            @RequestParam Long turnoId,
            HttpServletRequest request,
            RedirectAttributes redirectAttrs) {

        ArrayList<ProductoParaVender> carrito = this.obtenerCarrito(request);
        if (carrito == null || carrito.size() <= 0) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "El carrito está vacío."));
        }

        float total = 0;
        for (ProductoParaVender productoParaVender : carrito) {
            total += productoParaVender.getTotal();
            // Verificar disponibilidad de la cantidad solicitada
            Producto p = productosRepository.findByCodigo(productoParaVender.getCodigo());
            Fruvert fruta = fruvertRepository.findByCodigo(productoParaVender.getCodigo());
            Verdura verdura = verduraRepository.findByCodigo(productoParaVender.getCodigo());
            Carnes carnes = carnesRepository.findByCodigo(productoParaVender.getCodigo());

            float cantidadSolicitada = productoParaVender.getCantidad();
            if ((p != null && p.getExistencia() < cantidadSolicitada) ||
                    (fruta != null && fruta.getExistencia() < cantidadSolicitada) ||
                    (carnes != null && carnes.getExistencia() < cantidadSolicitada) ||
                    (verdura != null && verdura.getExistencia() < cantidadSolicitada)) {

                redirectAttrs
                        .addFlashAttribute("mensaje", "La cantidad solicitada de " + productoParaVender.getNombre() + " excede el stock disponible. Por favor, agregue más cantidad al inventario.")
                        .addFlashAttribute("clase", "warning");
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Stock insuficiente."));

            }
        }

        // Continuar con el proceso de venta si hay suficiente stock
        if (efectivo < total) {
            redirectAttrs
                    .addFlashAttribute("mensaje", "El efectivo proporcionado es menor que el total de la venta")
                    .addFlashAttribute("clase", "warning");
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Efectivo insuficiente."));
        }

        Cliente cliente = clienteRepository.findById(clienteId).orElse(null);
        if (cliente == null) {
            redirectAttrs
                    .addFlashAttribute("mensaje", "El cliente seleccionado no existe")
                    .addFlashAttribute("clase", "warning");
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Cliente no encontrado."));
        }

        Turno turno = turnoRepository.findById(turnoId).orElse(null);
        if (turno == null) {
            redirectAttrs
                    .addFlashAttribute("mensaje", "El turno seleccionado no existe")
                    .addFlashAttribute("clase", "warning");
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Turno no encontrado."));
        }

        // Crear la venta y relacionarla con el cliente
        Venta v = new Venta();
        v.setEfectivo(efectivo);
        v.setCambio(efectivo - total);
        v.setCliente(cliente);  // Asocia el cliente a la venta
        v.setTurno(turno);  // Asocia el turno a la venta

        ventaRepository.save(v);
        reporteVentaService.registrarReporteVenta(v);

        // Resto del código para actualizar existencias y guardar venta
        for (ProductoParaVender productoParaVender : carrito) {
            Producto p = productosRepository.findByCodigo(productoParaVender.getCodigo());
            Fruvert fruta = fruvertRepository.findByCodigo(productoParaVender.getCodigo());
            Verdura verdura = verduraRepository.findByCodigo(productoParaVender.getCodigo());
            Carnes carnes = carnesRepository.findByCodigo(productoParaVender.getCodigo());

            if (p != null) {
                p.restarExistencia(productoParaVender.getCantidad());
                productosRepository.save(p);
            } else if (fruta != null) {
                fruta.restarExistencia(productoParaVender.getCantidad());
                fruvertRepository.save(fruta);
            } else if (verdura != null) {
                verdura.restarExistencia(productoParaVender.getCantidad());
                verduraRepository.save(verdura);
            } else if (carnes != null) {
                carnes.restarExistencia(productoParaVender.getCantidad());
                carnesRepository.save(carnes);
            }
            ProductoVendido productoVendido = new ProductoVendido(
                    productoParaVender.getCantidad(),
                    productoParaVender.getPrecio(),
                    productoParaVender.getPrecio_final(),
                    productoParaVender.getIva(),
                    productoParaVender.getDescuento(),
                    productoParaVender.getNombre(),
                    productoParaVender.getCodigo(),
                    v
            );
            productosVendidosRepository.save(productoVendido);
        }

        this.limpiarCarrito(request);

        redirectAttrs
                .addFlashAttribute("mensaje", "Venta realizada correctamente. Cambio: " + (efectivo - total))
                .addFlashAttribute("clase", "success");

        // Devolver la respuesta con el ID de la venta
        return ResponseEntity.ok(Map.of("success", true, "ventaId", v.getId()));
    }


    @GetMapping(value = "/")
    public String interfazVender(Model model, HttpServletRequest request, Principal principal) {
        String username = principal.getName(); // Obtén el nombre del usuario autenticado
        model.addAttribute("username", username);

        model.addAttribute("producto", new Producto());
        model.addAttribute("venta", new Venta()); // Inicializa una nueva venta

        // Obtener y agregar la lista de clientes
        List<Cliente> listaClientes = clienteRepository.findAll();
        model.addAttribute("clientes", listaClientes);

        // Obtener turnos del usuario
        List<Turno> listaTurnos = turnoRepository.findByUsuario_Username(username);
        model.addAttribute("turnos", listaTurnos);

        // Seleccionar turno del usuario
        Turno turnoSeleccionado = listaTurnos.stream()
                .findFirst()
                .orElse(null);
        model.addAttribute("turnoSeleccionado", turnoSeleccionado); // Agregar el turno seleccionado al modelo

        float total = 0;
        float subtotal = 0; // Variable para la suma del subtotal
        ArrayList<ProductoParaVender> carrito = this.obtenerCarrito(request);

        // Calcula el subtotal y el total de los productos en el carrito
        for (ProductoParaVender p : carrito) {
            subtotal += p.getSubtotal(); // Sumar cada subtotal
            total += p.getTotal();
        }

        // Agrega el total y el subtotal al modelo
        model.addAttribute("total", total);
        model.addAttribute("subtotal", subtotal);

        // Devuelve la vista correspondiente
        return "vender/vender";
    }

    private ArrayList<ProductoParaVender> obtenerCarrito(HttpServletRequest request) {
        ArrayList<ProductoParaVender> carrito = (ArrayList<ProductoParaVender>) request.getSession().getAttribute("carrito");
        if (carrito == null) {
            carrito = new ArrayList<>();
        }
        return carrito;
    }

    private void guardarCarrito(ArrayList<ProductoParaVender> carrito, HttpServletRequest request) {
        request.getSession().setAttribute("carrito", carrito);
    }

    // Modificación del controlador (Java)
    @PostMapping(value = "/agregar")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> agregarAlCarrito(@ModelAttribute Producto producto, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        ArrayList<ProductoParaVender> carrito = this.obtenerCarrito(request);

        // Buscar producto (mantén tu lógica actual)
        Producto productoBuscadoPorCodigo = productosRepository.findByCodigo(producto.getCodigo());
        Fruvert frutaBuscadaPorCodigo = fruvertRepository.findByCodigo(producto.getCodigo());
        Verdura verduraBuscadaPorCodigo = verduraRepository.findByCodigo(producto.getCodigo());
        Carnes carnesBuscadaPorCodigo = carnesRepository.findByCodigo(producto.getCodigo());

        if (productoBuscadoPorCodigo == null) productoBuscadoPorCodigo = productosRepository.findByNombre(producto.getCodigo());
        if (frutaBuscadaPorCodigo == null) frutaBuscadaPorCodigo = fruvertRepository.findByNombre(producto.getCodigo());
        if (verduraBuscadaPorCodigo == null) verduraBuscadaPorCodigo = verduraRepository.findByNombre(producto.getCodigo());
        if (carnesBuscadaPorCodigo == null) carnesBuscadaPorCodigo = carnesRepository.findByNombre(producto.getCodigo());

        if (productoBuscadoPorCodigo == null && frutaBuscadaPorCodigo == null &&
                verduraBuscadaPorCodigo == null && carnesBuscadaPorCodigo == null) {
            response.put("success", false);
            response.put("message", "El producto con el código o nombre " + producto.getCodigo() + " no existe");
            return ResponseEntity.badRequest().body(response);
        }

        // Verificar existencia (mantén tu lógica)
        if ((productoBuscadoPorCodigo != null && productoBuscadoPorCodigo.sinExistencia()) ||
                (frutaBuscadaPorCodigo != null && frutaBuscadaPorCodigo.sinExistencia()) ||
                (carnesBuscadaPorCodigo != null && carnesBuscadaPorCodigo.sinExistencia()) ||
                (verduraBuscadaPorCodigo != null && verduraBuscadaPorCodigo.sinExistencia())) {
            response.put("success", false);
            response.put("message", "El producto está agotado");
            return ResponseEntity.badRequest().body(response);
        }

        // Agregar al carrito (mantén tu lógica)
        String codigoAComparar = productoBuscadoPorCodigo != null ? productoBuscadoPorCodigo.getCodigo() :
                (frutaBuscadaPorCodigo != null ? frutaBuscadaPorCodigo.getCodigo() :
                        carnesBuscadaPorCodigo != null ? carnesBuscadaPorCodigo.getCodigo() :
                                verduraBuscadaPorCodigo.getCodigo());

        boolean encontrado = false;
        for (ProductoParaVender productoParaVenderActual : carrito) {
            if (productoParaVenderActual.getCodigo().equals(codigoAComparar)) {
                productoParaVenderActual.aumentarCantidad();
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            if (productoBuscadoPorCodigo != null) {
                carrito.add(new ProductoParaVender(productoBuscadoPorCodigo.getNombre(), productoBuscadoPorCodigo.getCodigo(),
                        productoBuscadoPorCodigo.getPrecio(), productoBuscadoPorCodigo.getPrecio_final(),
                        productoBuscadoPorCodigo.getExistencia(), productoBuscadoPorCodigo.getIva(),
                        productoBuscadoPorCodigo.getDescuento(), productoBuscadoPorCodigo.getId(), 1f));
            } else if (frutaBuscadaPorCodigo != null) {
                carrito.add(new FrutaParaVender(frutaBuscadaPorCodigo.getNombre(), frutaBuscadaPorCodigo.getCodigo(),
                        frutaBuscadaPorCodigo.getPrecio(), frutaBuscadaPorCodigo.getPrecio_final(),
                        frutaBuscadaPorCodigo.getIva(), frutaBuscadaPorCodigo.getDescuento(), 1f,
                        frutaBuscadaPorCodigo.getId(), frutaBuscadaPorCodigo.getExistencia()));
            } else if (carnesBuscadaPorCodigo != null) {
                carrito.add(new CarnesParaVender(carnesBuscadaPorCodigo.getNombre(), carnesBuscadaPorCodigo.getCodigo(),
                        carnesBuscadaPorCodigo.getPrecio(), carnesBuscadaPorCodigo.getPrecio_final(),
                        carnesBuscadaPorCodigo.getIva(), carnesBuscadaPorCodigo.getDescuento(), 1f,
                        carnesBuscadaPorCodigo.getId(), carnesBuscadaPorCodigo.getExistencia()));
            } else {
                carrito.add(new VerduraParaVender(verduraBuscadaPorCodigo.getNombre(), verduraBuscadaPorCodigo.getCodigo(),
                        verduraBuscadaPorCodigo.getPrecio(), verduraBuscadaPorCodigo.getPrecio_final(),
                        verduraBuscadaPorCodigo.getIva(), verduraBuscadaPorCodigo.getDescuento(), 1f,
                        verduraBuscadaPorCodigo.getId(), verduraBuscadaPorCodigo.getExistencia()));
            }
        }

        this.guardarCarrito(carrito, request);
        response.put("success", true);
        response.put("carrito", carrito);
        response.put("subtotal", carrito.stream().mapToDouble(ProductoParaVender::getSubtotal).sum());
        response.put("total", carrito.stream().mapToDouble(ProductoParaVender::getTotal).sum());
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "/aumentar/{indice}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> aumentarCantidad(
            @PathVariable int indice,
            @RequestParam(required = false) Float cantidad,
            HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        ArrayList<ProductoParaVender> carrito = this.obtenerCarrito(request);

        if (carrito != null && indice < carrito.size()) {
            ProductoParaVender producto = carrito.get(indice);
            Producto p = productosRepository.findByCodigo(producto.getCodigo());
            Fruvert fruta = fruvertRepository.findByCodigo(producto.getCodigo());
            Verdura verdura = verduraRepository.findByCodigo(producto.getCodigo());
            Carnes carnes = carnesRepository.findByCodigo(producto.getCodigo());

            float stockActual = p != null ? p.getExistencia() :
                    fruta != null ? fruta.getExistencia() :
                            verdura != null ? verdura.getExistencia() :
                                    carnes != null ? carnes.getExistencia() : 0;

            if (cantidad != null) {
                // Si se proporciona una cantidad específica
                if (cantidad <= stockActual) {
                    producto.setCantidad(cantidad);
                } else {
                    response.put("success", false);
                    response.put("message", "La cantidad máxima es " + stockActual);
                    return ResponseEntity.badRequest().body(response);
                }
            } else {
                // Si no se proporciona cantidad, aumentar en 1
                if (producto.getCantidad() < stockActual) {
                    producto.aumentarCantidad();
                } else {
                    response.put("success", false);
                    response.put("message", "La cantidad máxima es " + stockActual);
                    return ResponseEntity.badRequest().body(response);
                }
            }

            this.guardarCarrito(carrito, request);
            response.put("success", true);
            response.put("carrito", carrito);
            response.put("subtotal", carrito.stream().mapToDouble(ProductoParaVender::getSubtotal).sum());
            response.put("total", carrito.stream().mapToDouble(ProductoParaVender::getTotal).sum());
        } else {
            response.put("success", false);
            response.put("message", "Índice no válido");
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }


    @PostMapping(value = "/disminuir/{indice}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> disminuirCantidad(@PathVariable int indice, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        ArrayList<ProductoParaVender> carrito = this.obtenerCarrito(request);

        if (carrito != null && indice < carrito.size()) {
            ProductoParaVender producto = carrito.get(indice);
            if (producto.getCantidad() > 1) {
                producto.disminuirCantidad();
                this.guardarCarrito(carrito, request);
                response.put("success", true);
                response.put("carrito", carrito);
                response.put("subtotal", carrito.stream().mapToDouble(ProductoParaVender::getSubtotal).sum());
                response.put("total", carrito.stream().mapToDouble(ProductoParaVender::getTotal).sum());
            } else {
                response.put("success", false);
                response.put("message", "La cantidad mínima es 1");
            }
        } else {
            response.put("success", false);
            response.put("message", "Índice no válido");
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/actualizar/{indice}/{cantidad}")
    @ResponseBody
    public ResponseEntity<?> actualizarCantidad(
            @PathVariable int indice,
            @PathVariable float cantidad,
            HttpServletRequest request) {

        ArrayList<ProductoParaVender> carrito = this.obtenerCarrito(request);

        if (carrito != null && indice < carrito.size()) {
            ProductoParaVender producto = carrito.get(indice);

            // Verificar disponibilidad de stock
            Producto p = productosRepository.findByCodigo(producto.getCodigo());
            Fruvert fruta = fruvertRepository.findByCodigo(producto.getCodigo());
            Verdura verdura = verduraRepository.findByCodigo(producto.getCodigo());
            Carnes carnes = carnesRepository.findByCodigo(producto.getCodigo());

            float stockActual = 0;
            if (p != null) stockActual = p.getExistencia();
            else if (fruta != null) stockActual = fruta.getExistencia();
            else if (verdura != null) stockActual = verdura.getExistencia();
            else if (carnes != null) stockActual = carnes.getExistencia();


            // Verificar si la cantidad solicitada no excede el stock
            if (cantidad <= stockActual) {
                producto.setCantidad(cantidad);
                this.guardarCarrito(carrito, request);

                // Calcular subtotal y total
                double subtotal = carrito.stream()
                        .mapToDouble(prod -> prod.getPrecio() * prod.getCantidad())
                        .sum();
                double total = carrito.stream()
                        .mapToDouble(ProductoParaVender::getTotal)
                        .sum();

                // Respuesta JSON con subtotal y total
                Map<String, Object> response = new HashMap<>();
                response.put("mensaje", "Cantidad actualizada correctamente");
                response.put("subtotal", subtotal);
                response.put("total", total);

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("La cantidad solicitada excede el stock disponible (" + stockActual + ")");
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado en el carrito");
    }


    @GetMapping("/buscar")
    public ResponseEntity<?> buscarProductos(@RequestParam String query) {
        try {
            // Usar un Set para evitar duplicados
            Set<Producto> productosEncontrados = new HashSet<>();

            // Buscar y convertir productos propios
            List<Producto> productosNombre = productosRepository.findByNombreContainingIgnoreCase(query);
            List<Producto> productosCodigo = productosRepository.findByCodigoContainingIgnoreCase(query);

            // Convertir productos propios directamente (similar a frutas y verduras)
            if (productosNombre != null) {
                for (Producto producto : productosNombre) {
                    productosEncontrados.add(new Producto(
                            producto.getNombre(),
                            producto.getCodigo(),
                            producto.getPrecio() // Asegúrate de que el método se llame getPrecio()
                    ));
                }
            }
            if (productosCodigo != null) {
                for (Producto producto : productosCodigo) {
                    productosEncontrados.add(new Producto(
                            producto.getNombre(),
                            producto.getCodigo(),
                            producto.getPrecio()
                    ));
                }
            }

            // Buscar frutas por nombre y código
            List<Fruvert> frutasNombre = fruvertRepository.findByNombreContainingIgnoreCase(query);
            List<Fruvert> frutasCodigo = fruvertRepository.findByCodigoContainingIgnoreCase(query);

            // Convertir frutas a Producto
            if (frutasNombre != null) {
                for (Fruvert fruta : frutasNombre) {
                    productosEncontrados.add(new Producto(
                            fruta.getNombre(),
                            fruta.getCodigo(),
                            fruta.getPrecio_final()
                    ));
                }
            }
            if (frutasCodigo != null) {
                for (Fruvert fruta : frutasCodigo) {
                    productosEncontrados.add(new Producto(
                            fruta.getNombre(),
                            fruta.getCodigo(),
                            fruta.getPrecio_final()
                    ));
                }
            }

            // Buscar verduras por nombre y código
            List<Verdura> verdurasNombre = verduraRepository.findByNombreContainingIgnoreCase(query);
            List<Verdura> verdurasCodigo = verduraRepository.findByCodigoContainingIgnoreCase(query);

            // Convertir verduras a Producto
            if (verdurasNombre != null) {
                for (Verdura verdura : verdurasNombre) {
                    productosEncontrados.add(new Producto(
                            verdura.getNombre(),
                            verdura.getCodigo(),
                            verdura.getPrecio_final()
                    ));
                }
            }
            if (verdurasCodigo != null) {
                for (Verdura verdura : verdurasCodigo) {
                    productosEncontrados.add(new Producto(
                            verdura.getNombre(),
                            verdura.getCodigo(),
                            verdura.getPrecio_final()
                    ));
                }
            }

            // Buscar Carnes por nombre y código
            List<Carnes> carnesNombre = carnesRepository.findByNombreContainingIgnoreCase(query);
            List<Carnes> carnesCodigo = carnesRepository.findByCodigoContainingIgnoreCase(query);

            // Convertir verduras a Producto
            if (carnesNombre != null) {
                for (Carnes carnes : carnesNombre) {
                    productosEncontrados.add(new Producto(
                            carnes.getNombre(),
                            carnes.getCodigo(),
                            carnes.getPrecio_final()
                    ));
                }
            }
            if (carnesCodigo != null) {
                for (Carnes carnes : carnesCodigo) {
                    productosEncontrados.add(new Producto(
                            carnes.getNombre(),
                            carnes.getCodigo(),
                            carnes.getPrecio_final()
                    ));
                }
            }


            // Convertir a lista
            List<Producto> resultado = new ArrayList<>(productosEncontrados);

            // Devolver ResponseEntity
            return ResponseEntity.ok(resultado);

        } catch (Exception e) {
            // Loguear el error completo
            e.printStackTrace();

            // Devolver error interno del servidor
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al buscar productos: " + e.getMessage());
        }

    }
}