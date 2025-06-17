package com.gestion.inventario.controlador;

import com.gestion.inventario.entidades.Compras;
import com.gestion.inventario.servicio.ComprasService;
import com.gestion.inventario.servicio.ProveedorService;
import com.gestion.inventario.util.paginacion.PageRender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/compras")
public class CompraController {

    private static final Logger log = LoggerFactory.getLogger(CompraController.class);

    @Autowired
    private ComprasService compraService;

    @Autowired
    private ProveedorService proveedorService;

    @Autowired
    private HttpServletRequest request;

    @GetMapping("/listar")
    public String listarCompras(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "search", required = false) String search,
            Model model) {

        page = Math.max(0, page);
        Pageable pageRequest = PageRequest.of(page, 5);
        Page<Compras> compras = compraService.findAllBySearch(search, pageRequest);

        if (compras.getTotalElements() == 0 && page > 0) {
            return "redirect:/compras/listar?search=" + (search != null ? search : "") + "&page=0";
        }

        PageRender<Compras> pageRender = new PageRender<>("/compras/listar", compras);

        model.addAttribute("compras", compras.getContent());
        model.addAttribute("compra", new Compras());
        model.addAttribute("page", pageRender);
        model.addAttribute("search", search);
        model.addAttribute("titulo", "Lista de Compras");
        model.addAttribute("proveedores", proveedorService.findAll());

        String acceptHeader = request.getHeader("X-Requested-With");
        if ("XMLHttpRequest".equals(acceptHeader)) {
            String fragment = request.getParameter("fragment");
            if ("pagination".equals(fragment)) {
                return "Compras/listarCompras :: #pagination-container";
            }
            return "Compras/listarCompras :: #table-container";
        }

        return "Compras/listarCompras";
    }

    @GetMapping("/contabilidad")
    public String mostrarContabilidad(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaFin,
            @RequestParam(required = false) Long proveedor,
            @RequestParam(defaultValue = "diario") String reportType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String section,
            Model model,
            Principal principal,
            HttpServletResponse response) {

        try {
            String username = principal != null ? principal.getName() : "Anonymous";
            model.addAttribute("username", username);

            section = (section != null && !section.isEmpty()) ? section : "proveedores";
            model.addAttribute("section", section);

            page = Math.max(0, page);
            Pageable pageable = PageRequest.of(page, size);

            // Convert LocalDate to Date, ensure correct date range
            Date startDate = fechaInicio != null ?
                    Date.from(fechaInicio.atStartOfDay(ZoneId.systemDefault()).toInstant()) : null;
            Date endDate = fechaFin != null ?
                    Date.from(fechaFin.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant()) : null;

            log.info("Processing request: search={}, fechaInicio={}, fechaFin={}, proveedor={}, reportType={}",
                    search, fechaInicio, fechaFin, proveedor, reportType);

            if ("proveedores".equals(section)) {
                Page<Compras> compras = compraService.findAllByFilters(search, startDate, endDate, proveedor, pageable);
                log.info("Found {} compras for filters", compras.getTotalElements());

                if (compras.getTotalElements() == 0 && page > 0) {
                    String redirectUrl = String.format(
                            "/compras/contabilidad?search=%s&page=0&section=proveedores&fechaInicio=%s&fechaFin=%s&reportType=%s",
                            search != null ? URLEncoder.encode(search, StandardCharsets.UTF_8) : "",
                            fechaInicio != null ? fechaInicio : "",
                            fechaFin != null ? fechaFin : "",
                            reportType);
                    return "redirect:" + redirectUrl;
                }

                PageRender<Compras> pageRender = new PageRender<>("/compras/contabilidad", compras);
                model.addAttribute("compras", compras.getContent());
                model.addAttribute("page", pageRender);
                model.addAttribute("titulo", "Gestión de Compras");
                model.addAttribute("totalCompras", compraService.calcularTotalCompras(compras.getContent()));
                model.addAttribute("totalPages", compras.getTotalPages());
                model.addAttribute("startPage", Math.max(0, page - 2));
                model.addAttribute("endPage", Math.min(page + 2, compras.getTotalPages() - 1));
            } else if ("compras".equals(section)) {
                Page<Compras> compras = compraService.findAllByFilters(search, startDate, endDate, proveedor, pageable);
                PageRender<Compras> pageRender = new PageRender<>("/compras/contabilidad", compras);
                List<Compras> allCompras = compras.getContent();

                LocalDate today = LocalDate.now();
                Double totalComprasDiarias = compraService.calculateTotalByPeriod(today, today);
                Double totalComprasSemanales = compraService.calculateTotalByPeriod(today.minusDays(6), today);
                Double totalComprasMensuales = compraService.calculateTotalByPeriod(today.withDayOfMonth(1), today);
                Double totalCompras = compraService.calcularTotalCompras(allCompras);
                Double totalGastoCompras = compraService.calcularTotalCompras(compraService.findAll());

                model.addAttribute("compras", allCompras);
                model.addAttribute("page", pageRender);
                model.addAttribute("titulo", "Gestión de Compras");
                model.addAttribute("fechaInicio", fechaInicio);
                model.addAttribute("fechaFin", fechaFin);
                model.addAttribute("proveedor", proveedor);
                model.addAttribute("reportType", reportType);
                model.addAttribute("totalCompras", totalCompras);
                model.addAttribute("totalComprasDiarias", totalComprasDiarias);
                model.addAttribute("totalComprasSemanales", totalComprasSemanales);
                model.addAttribute("totalComprasMensuales", totalComprasMensuales);
                model.addAttribute("totalGastoCompras", totalGastoCompras);
                model.addAttribute("proveedores", proveedorService.findAll());
                model.addAttribute("totalPages", compras.getTotalPages());
                model.addAttribute("startPage", Math.max(0, page - 2));
                model.addAttribute("endPage", Math.min(page + 2, compras.getTotalPages() - 1));
            }

            model.addAttribute("search", search);
            model.addAttribute("fechaInicio", fechaInicio);
            model.addAttribute("fechaFin", fechaFin);
            model.addAttribute("reportType", reportType);
            model.addAttribute("currentPage", page);
            model.addAttribute("size", size);

            String acceptHeader = request.getHeader("X-Requested-With");
            if ("XMLHttpRequest".equals(acceptHeader)) {
                response.setContentType("text/html; charset=UTF-8");
                response.setHeader("Transfer-Encoding", "chunked");
                response.setBufferSize(8192);
                String fragment = request.getParameter("fragment");
                if ("pagination".equals(fragment)) {
                    return "Contabilidad :: #proveedores-pagination-container";
                }
                if ("table".equals(fragment)) {
                    return "Contabilidad :: #proveedores-table-container";
                }
            }

            return "Contabilidad";
        } catch (Exception e) {
            log.error("Error processing /compras/contabilidad", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return "error/500";
        }
    }

    @GetMapping("/contabilidad/grafico-proveedores")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> datosGraficoProveedores(
            @RequestParam(name = "search", defaultValue = "") String search,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaFin,
            @RequestParam(defaultValue = "diario") String reportType) {

        try {
            Map<String, Object> response = new HashMap<>();
            LocalDate startDate = fechaInicio != null ? fechaInicio : LocalDate.of(2000, 1, 1);
            LocalDate endDate = fechaFin != null ? fechaFin : LocalDate.now();

            if (endDate.isAfter(LocalDate.now())) {
                endDate = LocalDate.now();
            }

            Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date end = Date.from(endDate.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());

            List<Compras> compras = compraService.findAllByFilters(search, start, end, null, PageRequest.of(0, Integer.MAX_VALUE)).getContent();

            List<String> labels = new ArrayList<>();
            List<Double> data = new ArrayList<>();
            List<Float> ivaData = new ArrayList<>();
            List<Float> descuentoData = new ArrayList<>();

            switch (reportType) {
                case "semanal":
                    Map<String, Map<String, Object>> comprasPorSemana = compras.stream()
                            .collect(Collectors.groupingBy(
                                    c -> {
                                        LocalDate localDate = c.getFecha().toLocalDate();
                                        return localDate.minusDays(localDate.getDayOfWeek().getValue() - 1)
                                                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                                    },
                                    Collectors.reducing(
                                            new HashMap<String, Object>() {{
                                                put("total", 0.0);
                                                put("iva", 0.0f);
                                                put("descuento", 0.0f);
                                            }},
                                            c -> new HashMap<String, Object>() {{
                                                put("total", c.getTotal());
                                                put("iva", c.getIva());
                                                put("descuento", c.getDescuento());
                                            }},
                                            (a, b) -> new HashMap<String, Object>() {{
                                                put("total", (Double) a.get("total") + (Double) b.get("total"));
                                                put("iva", (Float) a.get("iva") + (Float) b.get("iva"));
                                                put("descuento", (Float) a.get("descuento") + (Float) b.get("descuento"));
                                            }}
                                    )
                            ));
                    labels = comprasPorSemana.keySet().stream().sorted().collect(Collectors.toList());
                    data = labels.stream().map(k -> (Double) comprasPorSemana.get(k).get("total")).collect(Collectors.toList());
                    ivaData = labels.stream().map(k -> (Float) comprasPorSemana.get(k).get("iva")).collect(Collectors.toList());
                    descuentoData = labels.stream().map(k -> (Float) comprasPorSemana.get(k).get("descuento")).collect(Collectors.toList());
                    break;
                case "mensual":
                    Map<String, Map<String, Object>> comprasPorMes = compras.stream()
                            .collect(Collectors.groupingBy(
                                    c -> c.getFecha().toLocalDate()
                                            .withDayOfMonth(1)
                                            .format(DateTimeFormatter.ofPattern("MM/yyyy")),
                                    Collectors.reducing(
                                            new HashMap<String, Object>() {{
                                                put("total", 0.0);
                                                put("iva", 0.0f);
                                                put("descuento", 0.0f);
                                            }},
                                            c -> new HashMap<String, Object>() {{
                                                put("total", c.getTotal());
                                                put("iva", c.getIva());
                                                put("descuento", c.getDescuento());
                                            }},
                                            (a, b) -> new HashMap<String, Object>() {{
                                                put("total", (Double) a.get("total") + (Double) b.get("total"));
                                                put("iva", (Float) a.get("iva") + (Float) b.get("iva"));
                                                put("descuento", (Float) a.get("descuento") + (Float) b.get("descuento"));
                                            }}
                                    )
                            ));
                    labels = comprasPorMes.keySet().stream().sorted().collect(Collectors.toList());
                    data = labels.stream().map(k -> (Double) comprasPorMes.get(k).get("total")).collect(Collectors.toList());
                    ivaData = labels.stream().map(k -> (Float) comprasPorMes.get(k).get("iva")).collect(Collectors.toList());
                    descuentoData = labels.stream().map(k -> (Float) comprasPorMes.get(k).get("descuento")).collect(Collectors.toList());
                    break;
                default:
                    Map<String, Map<String, Object>> comprasPorDia = compras.stream()
                            .collect(Collectors.groupingBy(
                                    c -> c.getFecha().toLocalDate()
                                            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                                    Collectors.reducing(
                                            new HashMap<String, Object>() {{
                                                put("total", 0.0);
                                                put("iva", 0.0f);
                                                put("descuento", 0.0f);
                                            }},
                                            c -> new HashMap<String, Object>() {{
                                                put("total", c.getTotal());
                                                put("iva", c.getIva());
                                                put("descuento", c.getDescuento());
                                            }},
                                            (a, b) -> new HashMap<String, Object>() {{
                                                put("total", (Double) a.get("total") + (Double) b.get("total"));
                                                put("iva", (Float) a.get("iva") + (Float) b.get("iva"));
                                                put("descuento", (Float) a.get("descuento") + (Float) b.get("descuento"));
                                            }}
                                    )
                            ));
                    labels = comprasPorDia.keySet().stream().sorted().collect(Collectors.toList());
                    data = labels.stream().map(k -> (Double) comprasPorDia.get(k).get("total")).collect(Collectors.toList());
                    ivaData = labels.stream().map(k -> (Float) comprasPorDia.get(k).get("iva")).collect(Collectors.toList());
                    descuentoData = labels.stream().map(k -> (Float) comprasPorDia.get(k).get("descuento")).collect(Collectors.toList());
                    break;
            }

            response.put("labels", labels);
            response.put("data", data);
            response.put("ivaData", ivaData);
            response.put("descuentoData", descuentoData);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error generating proveedores chart data", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("labels", new ArrayList<>());
            errorResponse.put("data", new ArrayList<>());
            errorResponse.put("ivaData", new ArrayList<>());
            errorResponse.put("descuentoData", new ArrayList<>());
            errorResponse.put("error", "Error al generar datos del gráfico: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/contabilidad/grafico")
    @ResponseBody
    public Map<String, Object> datosGraficoCompras(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaFin,
            @RequestParam(required = false) Long proveedor,
            @RequestParam(defaultValue = "diario") String reportType) {

        Map<String, Object> response = new HashMap<>();
        Date startDate = fechaInicio != null ? Date.from(fechaInicio.atStartOfDay(ZoneId.systemDefault()).toInstant()) : null;
        Date endDate = fechaFin != null ? Date.from(fechaFin.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant()) : null;
        List<Compras> compras = compraService.findAllByFilters(search, startDate, endDate, proveedor, PageRequest.of(0, Integer.MAX_VALUE)).getContent();

        List<String> labels;
        List<Double> data;

        switch (reportType) {
            case "semanal":
                Map<String, Double> comprasPorSemana = compras.stream()
                        .collect(Collectors.groupingBy(
                                c -> {
                                    LocalDate localDate = c.getFecha().toInstant()
                                            .atZone(ZoneId.systemDefault())
                                            .toLocalDate();
                                    return localDate.minusDays(localDate.getDayOfWeek().getValue() - 1)
                                            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                                },
                                Collectors.summingDouble(Compras::getTotal)
                        ));
                labels = comprasPorSemana.keySet().stream().sorted().collect(Collectors.toList());
                data = comprasPorSemana.values().stream().collect(Collectors.toList());
                break;
            case "mensual":
                Map<String, Double> comprasPorMes = compras.stream()
                        .collect(Collectors.groupingBy(
                                c -> {
                                    LocalDate localDate = c.getFecha().toInstant()
                                            .atZone(ZoneId.systemDefault())
                                            .toLocalDate();
                                    return localDate.withDayOfMonth(1)
                                            .format(DateTimeFormatter.ofPattern("MM/yyyy"));
                                },
                                Collectors.summingDouble(Compras::getTotal)
                        ));
                labels = comprasPorMes.keySet().stream().sorted().collect(Collectors.toList());
                data = comprasPorMes.values().stream().collect(Collectors.toList());
                break;
            default:
                Map<String, Double> comprasPorDia = compras.stream()
                        .collect(Collectors.groupingBy(
                                c -> {
                                    LocalDate localDate = c.getFecha().toInstant()
                                            .atZone(ZoneId.systemDefault())
                                            .toLocalDate();
                                    return localDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                                },
                                Collectors.summingDouble(Compras::getTotal)
                        ));
                labels = comprasPorDia.keySet().stream().sorted().collect(Collectors.toList());
                data = comprasPorDia.values().stream().collect(Collectors.toList());
        }

        response.put("labels", labels);
        response.put("data", data);

        return response;
    }

    @GetMapping("/todos")
    @ResponseBody
    public List<Compras> obtenerTodasCompras(@RequestParam(name = "search", required = false) String search) {
        return compraService.findAllBySearch(search, null).getContent();
    }

    @PostMapping("/agregar")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> agregarCompra(@ModelAttribute Compras compra) {
        Map<String, Object> response = new HashMap<>();
        try {
            compraService.save(compra);
            response.put("success", true);
            response.put("message", "Compra creada con éxito");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "Error al crear la compra: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/editar/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> editarCompra(
            @PathVariable Long id,
            @ModelAttribute Compras compra) {
        Map<String, Object> response = new HashMap<>();
        try {
            Compras compraExistente = compraService.findOne(id);
            if (compraExistente != null) {
                compraExistente.setCodigo(compra.getCodigo());
                compraExistente.setProveedor(compra.getProveedor());
                compraExistente.setNombre(compra.getNombre());
                compraExistente.setFecha(compra.getFecha());
                compraExistente.setCantidad(compra.getCantidad());
                compraExistente.setIva(compra.getIva());
                compraExistente.setDescuento(compra.getDescuento());
                compraExistente.setPrecio(compra.getPrecio());
                compraExistente.setTotal(compra.getTotal());
                compraService.save(compraExistente);
                response.put("success", true);
                response.put("message", "Compra actualizada con éxito");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("error", "Compra no encontrada");
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "Error al actualizar la compra: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/eliminar/{id}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> eliminarCompra(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            compraService.delete(id);
            response.put("success", true);
            response.put("message", "Compra eliminada con éxito");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "Error al eliminar la compra: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}