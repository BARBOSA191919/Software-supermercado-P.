package com.gestion.inventario.controlador;

import java.util.*;
import com.gestion.inventario.entidades.*;
import com.gestion.inventario.repositorios.*;
import com.gestion.inventario.servicio.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.gestion.inventario.servicio.InventarioService;

@Controller
public class InventarioController {

	@Autowired
	private InventarioService inventarioService;
	@Autowired
	private CategoriaService categoriaService;
	@Autowired
	private InventarioRepository inventarioRepository;
	@Autowired
	private CategoriaRepository categoriaRepository;
	@Autowired
	private FruvertRepository fruvertRepository;
	@Autowired
	private VerduraRepository verduraRepository;
	@Autowired
	private CarnesRepository carnesRepository;
	@Autowired
	private ProductosRepository productosRepository;
	@Autowired
	private ProveedorRepository proveedorRepository;


	@GetMapping("/inventario")
	public String mostrarInventario(Model model) {
		List<Categoria> categorias = categoriaRepository.findAll();
		List<Proveedor> proveedores = proveedorRepository.findAll(); // Asegúrate de que esta línea funcione
		model.addAttribute("proveedores", proveedores);
		model.addAttribute("categorias", categorias);
		model.addAttribute("titulo", "Inventario");
		return "Inventario/Inventarios"; // Asegúrate de que esta vista exista
	}

	@GetMapping("/listar")
	@ResponseBody
	public ResponseEntity<?> listarInventario(
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size,
			@RequestParam(name = "tipo", defaultValue = "todos") String tipo) {


		try {
			Pageable pageable = PageRequest.of(page, size);
			List<Map<String, Object>> allProducts = new ArrayList<>();
			long totalElements = 0;
			int maxPages = 0;

			// Si tipo es "todos", combinamos los resultados de todas las tablas
			if (tipo.equalsIgnoreCase("todos")) {
				Page<Inventario> inventarioPage = inventarioRepository.findAll(pageable);
				Page<Producto> productosPage = productosRepository.findAll(pageable);
				Page<Fruvert> fruvertPage = fruvertRepository.findAll(pageable);
				Page<Verdura> verduraPage = verduraRepository.findAll(pageable);
				Page<Carnes> carnesPage = carnesRepository.findAll(pageable);

				// Mapear a DTOs
				allProducts.addAll(mapToDTO(inventarioPage.getContent(), Inventario.class));
				allProducts.addAll(mapToDTO(productosPage.getContent(), Producto.class));
				allProducts.addAll(mapToDTO(fruvertPage.getContent(), Fruvert.class));
				allProducts.addAll(mapToDTO(verduraPage.getContent(), Verdura.class));
				allProducts.addAll(mapToDTO(carnesPage.getContent(), Carnes.class));

				// Calcular total de elementos y páginas
				totalElements = inventarioPage.getTotalElements() + productosPage.getTotalElements() +
						fruvertPage.getTotalElements() + verduraPage.getTotalElements() + carnesPage.getTotalElements();
				maxPages = Math.max(inventarioPage.getTotalPages(), Math.max(productosPage.getTotalPages(),
						Math.max(fruvertPage.getTotalPages(), Math.max(verduraPage.getTotalPages(), carnesPage.getTotalPages()))));
			} else {
				// Si se especifica un tipo, solo paginamos esa tabla
				Page<?> datosPaginados;
				switch (tipo.toLowerCase()) {
					case "fruvert":
						datosPaginados = fruvertRepository.findAll(pageable);
						allProducts.addAll(mapToDTO(datosPaginados.getContent(), Fruvert.class));
						break;
					case "verdura":
						datosPaginados = verduraRepository.findAll(pageable);
						allProducts.addAll(mapToDTO(datosPaginados.getContent(), Verdura.class));
						break;
					case "carnes":
						datosPaginados = carnesRepository.findAll(pageable);
						allProducts.addAll(mapToDTO(datosPaginados.getContent(), Carnes.class));
						break;
					case "productos":
						datosPaginados = productosRepository.findAll(pageable);
						allProducts.addAll(mapToDTO(datosPaginados.getContent(), Producto.class));
						break;
					default:
						datosPaginados = inventarioRepository.findAll(pageable);
						allProducts.addAll(mapToDTO(datosPaginados.getContent(), Inventario.class));
						break;
				}
				totalElements = datosPaginados.getTotalElements();
				maxPages = datosPaginados.getTotalPages();
			}

			// Crear respuesta
			Map<String, Object> response = new HashMap<>();
			response.put("productos", allProducts);
			response.put("total", totalElements);
			response.put("totalPages", maxPages);
			response.put("currentPage", page);

			return ResponseEntity.ok(response);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "Error al listar inventario: " + e.getMessage()));
		}
	}

	// Metodo auxiliar para mapear entidades a DTOs
	private List<Map<String, Object>> mapToDTO(List<?> entities, Class<?> entityType) {
		List<Map<String, Object>> dtos = new ArrayList<>();
		for (Object entity : entities) {
			Map<String, Object> dto = new HashMap<>();
			if (entity instanceof Inventario) {
				Inventario inv = (Inventario) entity;
				dto.put("id", inv.getId());
				dto.put("codigo", inv.getCodigo());
				dto.put("nombre", inv.getNombre());
				dto.put("precio", inv.getPrecio());
				dto.put("stock", inv.getStock());
				dto.put("categoria", inv.getCategoria() != null ? inv.getCategoria().getNombre() : "Sin categoría");
				dto.put("tipo", "Inventario");
			} else if (entity instanceof Producto) {
				Producto p = (Producto) entity;
				dto.put("id", p.getId());
				dto.put("codigo", p.getCodigo());
				dto.put("nombre", p.getNombre());
				dto.put("precio", p.getPrecio());
				dto.put("existencia", p.getExistencia());
				dto.put("categoria", p.getCategoria() != null ? p.getCategoria().getNombre() : "Sin categoría");
				dto.put("tipo", "Productos");
			} else if (entity instanceof Fruvert) {
				Fruvert f = (Fruvert) entity;
				dto.put("id", f.getId());
				dto.put("codigo", f.getCodigo());
				dto.put("nombre", f.getNombre());
				dto.put("precio", f.getPrecio());
				dto.put("existencia", f.getExistencia());
				dto.put("categoria", f.getCategoria() != null ? f.getCategoria().getNombre() : "Sin categoría");
				dto.put("tipo", "Fruvert");
			} else if (entity instanceof Verdura) {
				Verdura v = (Verdura) entity;
				dto.put("id", v.getId());
				dto.put("codigo", v.getCodigo());
				dto.put("nombre", v.getNombre());
				dto.put("precio", v.getPrecio());
				dto.put("existencia", v.getExistencia());
				dto.put("categoria", v.getCategoria() != null ? v.getCategoria().getNombre() : "Sin categoría");
				dto.put("tipo", "Verdura");
			} else if (entity instanceof Carnes) {
				Carnes c = (Carnes) entity;
				dto.put("id", c.getId());
				dto.put("codigo", c.getCodigo());
				dto.put("nombre", c.getNombre());
				dto.put("precio", c.getPrecio());
				dto.put("existencia", c.getExistencia());
				dto.put("categoria", c.getCategoria() != null ? c.getCategoria().getNombre() : "Sin categoría");
				dto.put("tipo", "Carnes");
			}
			dtos.add(dto);
		}
		return dtos;
	}


	@GetMapping("/api/proveedores")
	public ResponseEntity<List<Proveedor>> obtenerProveedores() {
		List<Proveedor> proveedores = proveedorRepository.findAll();
		return ResponseEntity.ok(proveedores);
	}

	@PostMapping("/api/productos")
	@ResponseBody
	public ResponseEntity<?> guardarProducto(@RequestBody Inventario producto) {
		try {
			// Validar que la categoría existe
			if (producto.getCategoria() != null && producto.getCategoria().getId() != null) {
				Optional<Categoria> categoria = categoriaRepository.findById(producto.getCategoria().getId());
				producto.setCategoria(categoria.orElse(null));
			}

			// Asegurarse de que los campos están en minúsculas
			if (producto.getCodigo() != null) {
				producto.setCodigo(producto.getCodigo().trim());
			}
			if (producto.getNombre() != null) {
				producto.setNombre(producto.getNombre().trim());
			}

			Inventario saved = inventarioRepository.save(producto);
			return ResponseEntity.ok(saved);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(Map.of(
					"error", e.getMessage(),
					"mensaje", "Error al guardar el producto"
			));
		}
	}

	@PutMapping("/api/productos/{id}")
	@ResponseBody
	public ResponseEntity<?> actualizarProducto(@PathVariable Long id, @RequestBody Inventario producto) {
		try {
			// Validar que el producto existe
			Optional<Inventario> productoExistente = inventarioRepository.findById(id);
			if (!productoExistente.isPresent()) {
				return ResponseEntity.notFound().build();
			}

			// Actualizar el producto
			Inventario prod = productoExistente.get();
			prod.setCodigo(producto.getCodigo());
			prod.setNombre(producto.getNombre());
			prod.setPrecio(producto.getPrecio());
			prod.setStock(producto.getStock());

			// Si la categoría está presente, actualizarla
			if (producto.getCategoria() != null) {
				Optional<Categoria> categoria = categoriaRepository.findById(producto.getCategoria().getId());
				prod.setCategoria(categoria.orElse(null));
			}

			inventarioRepository.save(prod);
			return ResponseEntity.ok(prod);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(Map.of("error", e.getMessage(), "mensaje", "Error al actualizar el producto"));
		}
	}

	//metodo eliminar para agregar
	@DeleteMapping("/api/productos/{id}")
	@ResponseBody
	public ResponseEntity<?> eliminarProducto(
			@PathVariable Integer id,
			@RequestParam(required = false, defaultValue = "inventario") String tipo
	) {
		try {
			switch (tipo.toLowerCase()) {
				case "inventario":
					if (!inventarioRepository.existsById(Long.valueOf(id))) {
						return ResponseEntity.notFound().build();
					}
					inventarioRepository.deleteById(Long.valueOf(id));
					break;
				case "productos":
					Producto producto = productosRepository.findById(id)
							.orElseThrow(() -> new RuntimeException("producto no encontrado"));
					productosRepository.delete(producto);
					break;
				case "verdura":
					Verdura verdura = verduraRepository.findById(id)
							.orElseThrow(() -> new RuntimeException("Verdura no encontrada"));
					verduraRepository.delete(verdura);
					break;
				case "fruvert":
					Fruvert fruvert = fruvertRepository.findById(id)
							.orElseThrow(() -> new RuntimeException("Fruvert no encontrada"));
					fruvertRepository.delete(fruvert);
					break;
				case "carnes":
					Carnes carne = carnesRepository.findById(id)
							.orElseThrow(() -> new RuntimeException("Carne no encontrada"));
					carnesRepository.delete(carne);
					break;
				default:
					return ResponseEntity.badRequest().body(Map.of(
							"success", false,
							"message", "Tipo de producto no válido"
					));
			}

			return ResponseEntity.ok().body(Map.of(
					"success", true,
					"message", "Producto eliminado correctamente"
			));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(Map.of(
					"success", false,
					"message", "Error al eliminar el producto: " + e.getMessage()
			));
		}
	}

	@GetMapping("/api/inventario/buscar")
	@ResponseBody
	public ResponseEntity<?> buscarTodosLosProductos(
			@RequestParam(required = false, defaultValue = "") String query,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		try {
			Pageable pageable = PageRequest.of(page, size);
			List<Map<String, Object>> productosEncontrados = new ArrayList<>();
			long totalElements = 0;
			int maxPages = 0;

			// Si no hay query, traer todos los productos con paginación
			if (query == null || query.trim().isEmpty()) {
				Page<Inventario> inventarioPage = inventarioRepository.findAll(pageable);
				Page<Producto> productosPage = productosRepository.findAll(pageable);
				Page<Fruvert> fruvertPage = fruvertRepository.findAll(pageable);
				Page<Verdura> verduraPage = verduraRepository.findAll(pageable);
				Page<Carnes> carnesPage = carnesRepository.findAll(pageable);

				productosEncontrados.addAll(mapToDTO(inventarioPage.getContent(), Inventario.class));
				productosEncontrados.addAll(mapToDTO(productosPage.getContent(), Producto.class));
				productosEncontrados.addAll(mapToDTO(fruvertPage.getContent(), Fruvert.class));
				productosEncontrados.addAll(mapToDTO(verduraPage.getContent(), Verdura.class));
				productosEncontrados.addAll(mapToDTO(carnesPage.getContent(), Carnes.class));

				totalElements = inventarioPage.getTotalElements() + productosPage.getTotalElements() +
						fruvertPage.getTotalElements() + verduraPage.getTotalElements() + carnesPage.getTotalElements();
				maxPages = Math.max(inventarioPage.getTotalPages(), Math.max(productosPage.getTotalPages(),
						Math.max(fruvertPage.getTotalPages(), Math.max(verduraPage.getTotalPages(), carnesPage.getTotalPages()))));
			} else {
				// Búsqueda con paginación
				Page<Inventario> inventarioPage = inventarioRepository.findByNombreContainingIgnoreCaseOrCodigoContainingIgnoreCase(query, query, pageable);
				Page<Producto> productosPage = productosRepository.findByNombreContainingIgnoreCaseOrCodigoContainingIgnoreCase(query, query, pageable);
				Page<Fruvert> fruvertPage = fruvertRepository.findByNombreContainingIgnoreCaseOrCodigoContainingIgnoreCase(query, query, pageable);
				Page<Verdura> verduraPage = verduraRepository.findByNombreContainingIgnoreCaseOrCodigoContainingIgnoreCase(query, query, pageable);
				Page<Carnes> carnesPage = carnesRepository.findByNombreContainingIgnoreCaseOrCodigoContainingIgnoreCase(query, query, pageable);

				productosEncontrados.addAll(mapToDTO(inventarioPage.getContent(), Inventario.class));
				productosEncontrados.addAll(mapToDTO(productosPage.getContent(), Producto.class));
				productosEncontrados.addAll(mapToDTO(fruvertPage.getContent(), Fruvert.class));
				productosEncontrados.addAll(mapToDTO(verduraPage.getContent(), Verdura.class));
				productosEncontrados.addAll(mapToDTO(carnesPage.getContent(), Carnes.class));

				totalElements = inventarioPage.getTotalElements() + productosPage.getTotalElements() +
						fruvertPage.getTotalElements() + verduraPage.getTotalElements() + carnesPage.getTotalElements();
				maxPages = Math.max(inventarioPage.getTotalPages(), Math.max(productosPage.getTotalPages(),
						Math.max(fruvertPage.getTotalPages(), Math.max(verduraPage.getTotalPages(), carnesPage.getTotalPages()))));
			}

			// Crear respuesta
			Map<String, Object> response = new HashMap<>();
			response.put("productos", productosEncontrados);
			response.put("total", totalElements);
			response.put("totalPages", maxPages);
			response.put("currentPage", page);

			return ResponseEntity.ok(response);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "Error al buscar productos: " + e.getMessage()));
		}
	}

	@GetMapping("/api/inventario/categorias")
	public ResponseEntity<?> getCategorias() {
		try {
			List<Categoria> categorias = categoriaService.findAll();
			return ResponseEntity.ok(categorias);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error al obtener las categorías: " + e.getMessage());
		}
	}


	@GetMapping("/categoria/{categoriaId}")
	public String listarPorCategoria(@PathVariable Long categoriaId,
									 @RequestParam(defaultValue = "0") int page,
									 Model model) {
		Categoria categoria = categoriaRepository.findById(categoriaId)
				.orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

		Pageable pageable = PageRequest.of(page, 40);
		Page<Inventario> productos = inventarioService.findByCategoria(categoria, pageable);

		model.addAttribute("inventario", productos);
		model.addAttribute("categoriaActual", categoria);
		return "Inventario/Inventarios";
	}



	@PostMapping("/actualizarStock/{id}")
	@ResponseBody
	public ResponseEntity<?> actualizarStock(@PathVariable Long id, @RequestParam Float nuevoStock) {
		try {
			Inventario producto = inventarioService.findOne(id);
			if (producto == null) {
				return ResponseEntity.notFound().build();
			}

			producto.setStock(nuevoStock);  // Ahora usando Float en lugar de Integer
			inventarioService.save(producto);

			return ResponseEntity.ok().body(Map.of(
					"success", true,
					"message", "Stock actualizado correctamente",
					"nuevoStock", nuevoStock
			));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(Map.of(
					"success", false,
					"message", "Error al actualizar el stock: " + e.getMessage()
			));
		}
	}

	//metodo para actualizar stock de cada una
	@PostMapping("/api/verduras/actualizarStock/{id}")
	@ResponseBody
	public ResponseEntity<?> actualizarStockVerdura(
			@PathVariable Integer id,
			@RequestBody Map<String, Object> payload
	) {
		try {
			Float nuevoStock = Float.parseFloat(payload.get("nuevoStock").toString());
			Verdura verdura = verduraRepository.findById(id)
					.orElseThrow(() -> new RuntimeException("Verdura no encontrada"));

			verdura.setExistencia(nuevoStock);
			verduraRepository.save(verdura);

			return ResponseEntity.ok().body(Map.of(
					"success", true,
					"message", "Stock de Verdura actualizado correctamente",
					"nuevoStock", nuevoStock
			));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(Map.of(
					"success", false,
					"message", "Error al actualizar la Verdura en el stock: " + e.getMessage()
			));
		}
	}

	@PostMapping("/api/fruvert/actualizarStock/{id}")
	@ResponseBody
	public ResponseEntity<?> actualizarStockFruvert(
			@PathVariable Integer id,
			@RequestBody Map<String, Object> payload
	) {
		try {
			Float nuevoStock = Float.parseFloat(payload.get("nuevoStock").toString());
			Fruvert fruvert = fruvertRepository.findById(id)
					.orElseThrow(() -> new RuntimeException("Fruta no encontrada"));

			fruvert.setExistencia(nuevoStock);
			fruvertRepository.save(fruvert);

			return ResponseEntity.ok().body(Map.of(
					"success", true,
					"message", "Stock de Fruta actualizado correctamente",
					"nuevoStock", nuevoStock
			));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(Map.of(
					"success", false,
					"message", "Error al actualizar la Fruta en el stock: " + e.getMessage()
			));
		}
	}

	@PostMapping("/api/carnes/actualizarStock/{id}")
	@ResponseBody
	public ResponseEntity<?> actualizarStockCarnes(
			@PathVariable Integer id,
			@RequestBody Map<String, Object> payload
	) {
		try {
			Float nuevoStock = Float.parseFloat(payload.get("nuevoStock").toString());
			Carnes carnes = carnesRepository.findById(id)
					.orElseThrow(() -> new RuntimeException("Carne no encontrada"));

			carnes.setExistencia(nuevoStock);
			carnesRepository.save(carnes);

			return ResponseEntity.ok().body(Map.of(
					"success", true,
					"message", "Stock de Carne actualizado correctamente",
					"nuevoStock", nuevoStock
			));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(Map.of(
					"success", false,
					"message", "Error al actualizar la Carne en el stock: " + e.getMessage()
			));
		}
	}


	@PostMapping("/api/productos/actualizarStock/{id}")
	@ResponseBody
	public ResponseEntity<?> actualizarStockProductos(
			@PathVariable Integer id,
			@RequestBody Map<String, Object> payload
	) {
		try {
			Float nuevoStock = Float.parseFloat(payload.get("nuevoStock").toString());
			Producto productos = productosRepository.findById(id)
					.orElseThrow(() -> new RuntimeException("Producto no encontrada"));

			productos.setExistencia(nuevoStock);
			productosRepository.save(productos);

			return ResponseEntity.ok().body(Map.of(
					"success", true,
					"message", "Stock de Producto actualizado correctamente",
					"nuevoStock", nuevoStock
			));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(Map.of(
					"success", false,
					"message", "Error al actualizar el Producto en el stock: " + e.getMessage()
			));
		}
	}

	@PostMapping("/api/inventario/actualizarStockMultiple")
	@ResponseBody
	public ResponseEntity<?> actualizarStockMultiple(@RequestBody List<Map<String, Object>> stockUpdates) {
		try {
			for (Map<String, Object> update : stockUpdates) {
				Integer id = Integer.parseInt(update.get("id").toString());
				Float nuevoStock = Float.parseFloat(update.get("nuevoStock").toString());
				String tipo = update.get("tipo").toString().toLowerCase();

				switch (tipo) {
					case "productos":    // Cambiado de "producto" a "inventario"
						Producto producto = productosRepository.findById(id)
								.orElseThrow(() -> new RuntimeException("Producto no encontrado"));
						producto.setExistencia(nuevoStock);
						productosRepository.save(producto);
						break;

					case "fruvert":
						Fruvert fruvert = fruvertRepository.findById(id)
								.orElseThrow(() -> new RuntimeException("Fruta no encontrada"));
						fruvert.setExistencia(nuevoStock);
						fruvertRepository.save(fruvert);
						break;
					case "verdura":
						Verdura verdura = verduraRepository.findById(id)
								.orElseThrow(() -> new RuntimeException("Verdura no encontrada"));
						verdura.setExistencia(nuevoStock);
						verduraRepository.save(verdura);
						break;
					case "carnes":
						Carnes carnes = carnesRepository.findById(id)
								.orElseThrow(() -> new RuntimeException("Carne no encontrada"));
						carnes.setExistencia(nuevoStock);
						carnesRepository.save(carnes);
						break;
					default:
						throw new RuntimeException("Tipo de producto no reconocido: " + tipo);
				}
			}

			return ResponseEntity.ok().body(Map.of(
					"success", true,
					"message", "Stocks actualizados correctamente"
			));
		} catch (Exception e) {
			e.printStackTrace(); // Agregar log para debugging
			return ResponseEntity.badRequest().body(Map.of(
					"success", false,
					"error", "Error al actualizar stocks: " + e.getMessage()
			));
		}
	}

	@GetMapping("/Movimientos")
	public String movimiento(Model modelo) {
		// Puedes añadir atributos al modelo si necesitas pasarlos a la vista
		modelo.addAttribute("titulo", "Página Principal");
		return "Inventario/Movimientos";
	}

	@GetMapping("/Alertas")
	public String alertas(Model modelo) {
		// Puedes añadir atributos al modelo si necesitas pasarlos a la vista
		modelo.addAttribute("titulo", "Página Principal");
		return "Inventario/Alertas";
	}

	@GetMapping("/Configuracion")
	public String configuracion(Model modelo) {
		// Puedes añadir atributos al modelo si necesitas pasarlos a la vista
		modelo.addAttribute("titulo", "Página Principal");
		return "Inventario/Configuracion";
	}

	@GetMapping("/Inventarios")
	public String mostrarFormularioDeRegistrarCarnes(Model model) {
		Carnes carnes = new Carnes();
		model.addAttribute("Carne", carnes);

		// Obtener las categorías y agregarlas al modelo
		List<Categoria> categorias = categoriaRepository.findAll();


		model.addAttribute("categorias", categorias);
		model.addAttribute("titulo", "Inventario");
		model.addAttribute("titulo", "Registro de Carnes");
		return "Inventario/Inventarios";
	}

	@GetMapping("/Fruvert")
	public String mostrarPaginovena(Model modelo) {
		// Puedes añadir atributos al modelo si necesitas pasarlos a la vista
		modelo.addAttribute("titulo", "Seccion de Fruvert");
		return "Fruvert";
	}

	@GetMapping("/Contabilidad")
	public String mostrarPaginasexta(Model modelo) {
		// Puedes añadir atributos al modelo si necesitas pasarlos a la vista
		modelo.addAttribute("titulo", "Página Principal");
		return "Contabilidad";
	}

	@GetMapping("/Reportes")
	public String mostrarPaginaquinta(Model modelo) {
		// Puedes añadir atributos al modelo si necesitas pasarlos a la vista
		modelo.addAttribute("titulo", "Página Principal");
		return "Reportes";
	}

	@GetMapping("/Inventario")
	public String mostrarPaginacuarta(Model modelo) {
		// Puedes añadir atributos al modelo si necesitas pasarlos a la vista
		modelo.addAttribute("titulo", "Página Principal");

		return "Inventario/Inventarios";

	}

	@GetMapping("/principal")
	public String mostrarPaginaPrincipal(Model modelo) {
		// Puedes añadir atributos al modelo si necesitas pasarlos a la vista
		modelo.addAttribute("titulo", "Página Principal");
		return "principal";
	}

}
