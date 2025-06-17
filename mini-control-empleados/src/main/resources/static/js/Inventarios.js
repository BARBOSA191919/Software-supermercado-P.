
// Función para guardar movimientos en localStorage
function guardarMovimientosEnLocalStorage(movimientos) {
    let movimientosGuardados = JSON.parse(localStorage.getItem('movimientos') || '[]');

    movimientos.forEach(movimiento => {
        movimientosGuardados.unshift({
            ...movimiento,
            fecha: new Date().toLocaleString()
        });
    });

    movimientosGuardados = movimientosGuardados.slice(0, 50);

    localStorage.setItem('movimientos', JSON.stringify(movimientosGuardados));
}
function confirmLogout() {
    Swal.fire({
        title: '¿Estás seguro?',
        text: "¿Quieres cerrar sesión?",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#3085d6',
        confirmButtonText: 'Sí, cerrar sesión',
        cancelButtonText: 'Cancelar'
    }).then((result) => {
        if (result.isConfirmed) {
            document.getElementById("logoutForm").submit();
        }
    });
}
// Función para guardar alertas en localStorage
function guardarAlertasEnLocalStorage(alertas) {
    let alertasGuardadas = JSON.parse(localStorage.getItem('alertas') || '[]');
    let nuevasAlertas = [];

    // Filtrar alertas existentes
    alertasGuardadas = alertasGuardadas.filter(alertaGuardada => {
        // Buscar si el producto sigue teniendo stock bajo
        const productoActual = todosProductos.find(p =>
            p.codigo === alertaGuardada.codigo
        );

        if (productoActual) {
            const stockActual = productoActual.existencia !== undefined
                ? productoActual.existencia
                : (productoActual.stock || 0);

            // Solo mantener la alerta si el stock sigue siendo bajo
            return stockActual <= 20;
        }

        return false;
    });

    // Agregar nuevas alertas
    alertas.forEach(alerta => {
        const existeAlerta = alertasGuardadas.some(a =>
            a.codigo === alerta.codigo && a.nombre === alerta.nombre
        );

        if (!existeAlerta) {
            alertasGuardadas.unshift({
                ...alerta,
                fecha: new Date().toLocaleString()
            });
        }
    });

    // Limitar a las 50 alertas más recientes
    alertasGuardadas = alertasGuardadas.slice(0, 50);

    localStorage.setItem('alertas', JSON.stringify(alertasGuardadas));
}

// Función para cargar y mostrar movimientos desde localStorage
function cargarMovimientosDesdeLocalStorage() {
    const movimientos = JSON.parse(localStorage.getItem('movimientos') || '[]');
    const movimientosContainer = $('#movimientos-list');
    movimientosContainer.empty();

    movimientos.forEach(movimiento => {
        const movimientoHTML = `
            <div class="alert alert-info alert-dismissible fade show" role="alert">
                <strong>Código: ${movimiento.codigo}</strong>
                <br>Nombre: ${movimiento.nombre}
                <br>Stock Anterior: ${movimiento.stockAnterior} → Nuevo Stock: ${movimiento.nuevoStock}
                <br>Fecha: ${movimiento.fecha}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        `;
        movimientosContainer.append(movimientoHTML);
    });
}

// Función para cargar y mostrar alertas desde localStorage
function cargarAlertasDesdeLocalStorage() {
    const alertas = JSON.parse(localStorage.getItem('alertas') || '[]');
    const alertasContainer = $('#alertas-list');
    alertasContainer.empty();

    alertas.forEach(alerta => {
        const alertaHTML = `
            <div class="alert alert-warning alert-dismissible fade show" role="alert">
                <strong>${alerta.nombre}</strong>
                <br>Código: ${alerta.codigo}
                <br>Stock actual: ${alerta.stock} unidades
                <br>Fecha: ${alerta.fecha}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        `;
        alertasContainer.append(alertaHTML);
    });
}



// Modificar verificarAlertasStock para guardar en localStorage
function verificarAlertasStock() {
    const alertasList = $('#alertas-list');
    alertasList.empty();

    if (!todosProductos || todosProductos.length === 0) {
        return;
    }

    const productosBajoStock = todosProductos.filter(producto => {
        const stock = producto.existencia !== undefined ? producto.existencia : (producto.stock || 0);
        return stock <= 20;
    });

    const alertasParaGuardar = [];

    if (productosBajoStock.length > 0) {
        Swal.fire({
            title: '¡Atención!',
            text: `Hay ${productosBajoStock.length} productos con stock bajo`,
            icon: 'warning',
            toast: true,
            position: 'top-end',
            showConfirmButton: false,
            timer: 3000
        });

        productosBajoStock.forEach(producto => {
            const stock = producto.existencia !== undefined ? producto.existencia : (producto.stock || 0);

            const alertaParaGuardar = {
                nombre: producto.nombre,
                codigo: producto.codigo,
                stock: stock
            };

            alertasParaGuardar.push(alertaParaGuardar);

            const alertaHTML = `
                <div class="alert alert-warning alert-dismissible fade show" role="alert">
                    <strong>${producto.nombre}</strong>
                    <br>Código: ${producto.codigo}
                    <br>Stock actual: ${stock} unidades
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            `;
            alertasList.append(alertaHTML);
        });

        // Guardar alertas en localStorage
        guardarAlertasEnLocalStorage(alertasParaGuardar);
    }
}


$(document).ready(function() {
    // Cargar proveedores
    $.ajax({
        url: '/api/proveedores',
        method: 'GET',
        success: function(data) {
            data.forEach(function(proveedor) {
                $('#proveedor').append(
                    $('<option></option>').val(proveedor.id).text(proveedor.nombreProveedor)
                );
            });
        },
        error: function() {
            console.error('Error al cargar los proveedores');
        }
    });

    $('#alertas-link').on('click', function() {
        $('#alertas-container').toggle(); // Mostrar u ocultar el contenedor de alertas
    });

    mostrarProductosInventario();
});




function guardarTodosLosStock() {
    const stockInputs = $('#inventarioTableBody input.stock-control');
    const stockUpdates = [];
    let hayErrores = false;
    let cantidadActualizados = 0;

    stockInputs.each(function () {
        const productoId = $(this).data('producto-id');
        const nuevoStock = $(this).val();
        const originalStock = $(this).data('original-stock');

        if (nuevoStock !== originalStock) {
            // Solo agregar a updates si el stock ha cambiado
            const update = {
                id: productoId,
                codigo: $(this).data('codigo'),
                nombre: $(this).data('nombre-producto'),
                nuevoStock: nuevoStock,
                stockAnterior: originalStock,
                tipo: $(this).data('tipo'),
                categoria: $(this).closest('tr').find('td:eq(2)').text().trim(),
                proveedor: $('#proveedor option:selected').text() || 'Sin proveedor',
                precio: $(this).closest('tr').find('td:eq(4)').text().replace('$', '').trim()
            };
            stockUpdates.push(update);
            cantidadActualizados++;
        }
    });

    if (hayErrores) {
        return;
    }

    if (stockUpdates.length === 0) {
        mostrarNotificacion('No hay cambios para guardar', 'info');
        return;
    }

    // Mostrar modal de confirmación con la cantidad de productos actualizados
    Swal.fire({
        title: 'Confirmar actualización',
        text: `Se actualizarán ${cantidadActualizados} registros`,
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Sí, actualizar',
        cancelButtonText: 'Cancelar'
    }).then((result) => {
        if (result.isConfirmed) {
            // Actualizar los productos
            const token = $("meta[name='_csrf']").attr("content");
            const header = $("meta[name='_csrf_header']").attr("content");
            $.ajax({
                url: '/api/inventario/actualizarStockMultiple',
                method: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(stockUpdates),
                beforeSend: function(xhr) {
                    if (token && header) {
                        xhr.setRequestHeader(header, token);
                    }
                },
                success: function(response) {
                    if (response.success) {
                        mostrarNotificacion(`Se han actualizado ${cantidadActualizados} registros`, 'success');
                        // Actualizar los valores originales
                        stockInputs.each(function () {
                            $(this).data('original-stock', $(this).val());
                        });
                        // Recargar la tabla
                        mostrarProductosInventario();
                    } else {
                        mostrarNotificacion(response.error || 'Error al actualizar stocks', 'error');
                    }
                },
                error: function(xhr, status, error) {
                    console.error('Error al actualizar stocks:', error);
                    mostrarNotificacion('Error al actualizar los stocks', 'error');
                }
            });
        }
    });
}
// Asegúrate de que la función actualizarSeccionMovimientos esté correctamente configurada para mostrar los datos
function actualizarSeccionMovimientos(stockUpdates) {
    let movimientosContainer = $('#movimientos-list');

    if (movimientosContainer.length === 0) {
        $('#movimientos-container .card-body').append('<div id="movimientos-list"></div>');
        movimientosContainer = $('#movimientos-list');
    }

    stockUpdates.forEach(update => {
        const movimientoHTML = `
            <div class="alert alert-info alert-dismissible fade show" role="alert">
                <strong>Código: ${update.codigo}</strong>
                <br>Nombre: ${update.nombre}
                <br>Precio: $${parseFloat(update.precio || 0).toFixed(2)}
                <br>Stock Anterior: ${update.stockAnterior} → Nuevo Stock: ${update.nuevoStock}
                <br>Categoría: ${update.categoria}
                <br>Proveedor: ${update.proveedor}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        `;
        movimientosContainer.prepend(movimientoHTML);
    });
    // Guardar movimientos en localStorage
    guardarMovimientosEnLocalStorage(stockUpdates);
}
// Modificar la función cargarInventario para establecer datos originales
function actualizarTablaInventario(productos) {
    const tbody = $('#inventarioTableBody');
    tbody.empty();

    productos.forEach(function(producto) {
        let categoriaNombre = producto.categoria ? (producto.categoria.nombre || 'Sin categoría') : 'Sin categoría';

        tbody.append(`
      <tr>
        <td>${producto.codigo || ''}</td>
        <td>${producto.nombre || ''}</td>
        <td>${categoriaNombre}</td>
        <td>
  <input type="text"
       class="form-control form-control-sm stock-control"
       value="${stock}"
       data-producto-id="${producto.id}"
       data-tipo="${tipo}"
       data-original-stock="${stock}"
       onkeypress="return soloEnteros(event)"
       oninput="this.value = this.value.replace(/[^0-9]/g, '');">
    <button class="btn btn-primary btn-sm" onclick="actualizarStock(${producto.id}, this.previousElementSibling.value, '${tipo}')">
        Guardar
    </button>

        </td>
        <td>${producto.precio ? '$' + Number(producto.precio).toLocaleString('es-CO') : '$0'}</td>


        <td>
          <div class="btn-group">
            <button class="btn btn-sm btn-danger" onclick="eliminarProducto(${producto.id})">
              <i class="fas fa-trash"></i>
            </button>
          </div>
        </td>
      </tr>
    `);
    });
}

function soloEnteros(event) {
    const key = event.key;
    // Permitir solo números y teclas de control
    return /^[0-9]$/.test(key) || event.ctrlKey || event.altKey || event.key === 'Backspace' || event.key === 'Tab';
}

// Add a button to save all stocks in the card header
$('.card-header .btn-danger').after(`
    <button type="button" class="btn btn-primary ms-2" onclick="guardarTodosLosStock()">
        <i class="fas fa-save me-2"></i> Guardar stock
    </button>
    `);

// Add confirmation modal to the HTML (you would need to add this to your existing HTML)
$('body').append(`
    <div class="modal fade" id="confirmacionStockModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Confirmar Actualización de Stocks</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    ¿Está seguro que desea guardar todos los cambios de stock?
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                    <button type="button" class="btn btn-primary" id="btnConfirmarStock">Confirmar</button>
                </div>
            </div>
        </div>
    </div>
    `);

// Variable global para almacenar todos los productos
let todosProductos = [];

function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

function configurarAutocompletado() {
    // Preparamos los datos para autocompletado
    const sugerencias = todosProductos.map(producto => ({
        label: `${producto.codigo} - ${producto.nombre}`,
        value: producto.nombre,
        codigo: producto.codigo,
        categoria: producto.categoria,
        barcode: producto.codigoBarras || ''
    }));

    // Función auxiliar para normalizar textos en búsqueda
    const normalizeSearch = (text) => {
        return text.toString().toLowerCase().trim();
    };

    // Función de filtrado mejorada
    const filtrarSugerencias = (term) => {
        const searchTerm = normalizeSearch(term);
        return sugerencias.filter(item => {
            const normalizedLabel = normalizeSearch(item.label);
            const normalizedCodigo = normalizeSearch(item.codigo);
            return normalizedLabel.includes(searchTerm) ||
                normalizedCodigo.includes(searchTerm) ||
                searchTerm.includes(normalizedCodigo);
        }).slice(0, 6);
    };

    // Configuramos autocompletado usando jQuery UI
    $("#searchInput").autocomplete({
        source: function(request, response) {
            response(filtrarSugerencias(request.term));
        },
        minLength: 1, // Reducimos a 1 para códigos cortos
        select: function(event, ui) {
            event.preventDefault();
            $("#searchInput").val(ui.item.value);
            const categoriaId = $('#categoria-filter').val();
            mostrarProductosInventario(categoriaId, ui.item.value);
        },
        focus: function(event, ui) {
            event.preventDefault();
            $(this).val(ui.item.label);
        }
    }).autocomplete("instance")._renderItem = function(ul, item) {
        return $("<li>")
            .append(`
        <div class="d-flex justify-content-between align-items-center">
          <span>${item.label}</span>
          ${item.barcode ? `<img src="data:image/svg+xml;utf8,${generarCodigoBarrasSVG(item.barcode)}" alt="Código de Barras" style="height: 20px; margin-left: 10px;">` : ''}
        </div>
      `)
            .appendTo(ul);
    };

    // Crear versión debounced de mostrarProductosInventario
    const debouncedSearch = debounce((categoriaId, busqueda, page) => {
        const tbody = document.getElementById('inventarioTableBody');

        // Mostrar indicador de carga solo si hay término de búsqueda
        if (busqueda && busqueda.length > 0) {
            tbody.innerHTML = `
        <tr>
          <td colspan="6" class="text-center">
            <div class="d-flex align-items-center justify-content-center">
              <div class="spinner-border spinner-border-sm text-primary me-2" role="status">
                <span class="visually-hidden">Buscando...</span>
              </div>
              <span>Buscando "${busqueda}"...</span>
            </div>
          </td>
        </tr>
      `;
        }

        mostrarProductosInventario(categoriaId, busqueda, page);
    }, 600);



    $("#searchInput").on('input', function() {
        const categoriaId = $('#categoria-filter').val();
        const busqueda = $(this).val();
        debouncedSearch(categoriaId, busqueda, 0);
    });

    $("#searchInput").on('keypress', function(e) {
        if (e.which === 13) {
            const categoriaId = $('#categoria-filter').val();
            const busqueda = $(this).val();
            mostrarProductosInventario(categoriaId, busqueda, 0);
        }
    });
}

function mostrarNotificacion(mensaje, tipo) {
    Swal.fire({
        icon: tipo,
        title: mensaje,
        showConfirmButton: false,
        timer: 1500
    });
}

document.getElementById('btnGuardarProducto').addEventListener('click', function() {
    const producto = {
        codigo: document.getElementById('codigo').value,
        nombre: document.getElementById('nombre').value,
        precio: parseFloat(document.getElementById('precio').value),
        stock: parseInt(document.getElementById('stock').value),
        categoriaId: document.getElementById('categoria').value
    };

    const token = document.querySelector("meta[name='_csrf']").getAttribute("content");
    const header = document.querySelector("meta[name='_csrf_header']").getAttribute("content");

    fetch('/api/productos', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            [header]: token
        },
        body: JSON.stringify(producto)
    })
        .then(response => {
            if (!response.ok) throw new Error('Error al guardar el producto');
            return response.json();
        })
        .then(data => {
            mostrarNotificacion('Producto guardado correctamente', 'success');
            $('#agregarProductoModal').modal('hide');
            mostrarProductosInventario();
        })
        .catch(error => {
            console.error('Error:', error);
            mostrarNotificacion('No se pudo guardar el producto', 'error');
        });
});


function actualizarProducto(id) {
    const producto = {
        codigo: document.getElementById('codigo').value,
        nombre: document.getElementById('nombre').value,
        precio: parseFloat(document.getElementById('precio').value),
        stock: parseInt(document.getElementById('stock').value),
        categoriaId: document.getElementById('categoria').value
    };

    const token = document.querySelector("meta[name='_csrf']").getAttribute("content");
    const header = document.querySelector("meta[name='_csrf_header']").getAttribute("content");

    fetch(`/api/productos/${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            [header]: token
        },
        body: JSON.stringify(producto)
    })
        .then(response => {
            if (!response.ok) throw new Error('Error al actualizar el producto');
            return response.json();
        })
        .then(data => {
            mostrarNotificacion('Producto actualizado correctamente', 'success');
            $('#agregarProductoModal').modal('hide');
            mostrarProductosInventario();
        })
        .catch(error => {
            console.error('Error:', error);
            mostrarNotificacion('No se pudo actualizar el producto', 'error');
        });
}

//agregar funcion para eliminar producto
function eliminarProducto(id, tipo = 'inventario') {
    const token = document.querySelector("meta[name='_csrf']").getAttribute("content");
    const header = document.querySelector("meta[name='_csrf_header']").getAttribute("content");
    const categoriaId = $('#categoria-filter').val(); // Get current category


    Swal.fire({
        title: '¿Está seguro?',
        text: "Esta acción no se puede deshacer",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Sí, eliminar',
        cancelButtonText: 'Cancelar'
    }).then((result) => {
        if (result.isConfirmed) {
            fetch(`/api/productos/${id}?tipo=${tipo}`, {
                method: 'DELETE',
                headers: {
                    [header]: token
                }
            })
                .then(response => {
                    if (!response.ok) throw new Error('Error al eliminar el producto');
                    mostrarNotificacion('Producto eliminado correctamente', 'success');
                    mostrarProductosInventario(categoriaId); // Pass current category
                })
                .catch(error => {
                    console.error('Error:', error);
                    mostrarNotificacion('No se pudo eliminar el producto', 'error');
                });
        }
    });
}

$(document).ready(function() {
    cargarCategorias();
    cargarMovimientosDesdeLocalStorage();
    cargarAlertasDesdeLocalStorage();

    $('#sidebarCollapse').on('click', function() {
        $('#sidebar').toggleClass('active');
        $('#content').toggleClass('sidebar-visible');
    });

    $('#categoria-filter').change(function() {
        const categoriaId = $(this).val();
        const busqueda = $('#searchInput').val();
        mostrarProductosInventario(categoriaId, busqueda, 0); // Reiniciar a la página 0
    });

    mostrarProductosInventario();
});

function cargarCategorias() {
    const token = document.querySelector("meta[name='_csrf']").getAttribute("content");
    const header = document.querySelector("meta[name='_csrf_header']").getAttribute("content");

    fetch('/api/inventario/categorias', {
        headers: {
            [header]: token
        }
    })
        .then(response => response.json())
        .then(categorias => {
            const selectInventario = document.getElementById('categoria-filter');
            const selectModal = document.getElementById('categoria');

            // Reiniciar selects
            selectInventario.innerHTML = '<option value="">-- Seleccione una categoría --</option>';
            selectModal.innerHTML = '<option value="">Seleccione una categoría</option>';

            // Contar productos por categoría (necesitamos los productos para esto)
            fetch('/api/inventario/buscar', {
                headers: {
                    [header]: token
                }
            })
                .then(response => response.json())
                .then(data => {
                    const productos = data.productos || [];

                    // Contar productos por categoría
                    const contadorCategorias = productos.reduce((contador, producto) => {
                        if (producto.categoria && producto.categoria.trim() !== '') {
                            contador[producto.categoria] = (contador[producto.categoria] || 0) + 1;
                        }
                        return contador;
                    }, {});

                    // Agregar categorías al select con su contador
                    categorias.forEach(categoria => {
                        const cantidadProductos = contadorCategorias[categoria.nombre] || 0;
                        const option = `<option value="${categoria.nombre}">${categoria.nombre} (${cantidadProductos})</option>`;
                        selectInventario.insertAdjacentHTML('beforeend', option);
                        selectModal.insertAdjacentHTML('beforeend', `<option value="${categoria.id}">${categoria.nombre}</option>`);
                    });
                })
                .catch(error => {
                    console.error('Error al contar productos por categoría:', error);
                    // Si falla el conteo, al menos mostramos las categorías sin contador
                    categorias.forEach(categoria => {
                        const option = `<option value="${categoria.nombre}">${categoria.nombre}</option>`;
                        selectInventario.insertAdjacentHTML('beforeend', option);
                        selectModal.insertAdjacentHTML('beforeend', `<option value="${categoria.id}">${categoria.nombre}</option>`);
                    });
                });
        })
        .catch(error => {
            console.error('Error al cargar categorías:', error);
            mostrarNotificacion('Error al cargar las categorías', 'error');
        });
}

let currentPage = 0;
let totalPages = 0;
let isSearching = false;
let currentQuery = '';
let currentCategoriaId = '';
let pageSize = 3; // Tamaño por defecto
let isLoading = false; // Nueva variable para evitar múltiples cargas simultáneas
let todosProductos2 = [];


function mostrarProductosInventario(categoriaId = '', busqueda = '', page = 0) {
    // Evitar múltiples solicitudes simultáneas
    if (isLoading) return;
    isLoading = true;

    currentPage = page;
    currentCategoriaId = categoriaId;
    currentQuery = busqueda;
    isSearching = busqueda.trim() !== '';

    pageSize = currentCategoriaId ? 10 : 3;

    const tbody = document.getElementById('inventarioTableBody');
    tbody.innerHTML = `
        <tr>
            <td colspan="6" class="text-center">
                <div class="spinner-border" role="status">
                    <span class="visually-hidden">Cargando...</span>
                </div>
            </td>
        </tr>
    `;
    tbody.style.opacity = '1'; // Asegurar que el spinner sea visible

    const token = document.querySelector("meta[name='_csrf']").getAttribute("content");
    const header = document.querySelector("meta[name='_csrf_header']").getAttribute("content");

    let url = isSearching
        ? `/api/inventario/buscar?query=${encodeURIComponent(busqueda)}&page=${page}&size=${pageSize}`
        : `/listar?page=${page}&size=${pageSize}&tipo=todos`;

    const minLoadingTime = 500; // Aumentamos a 500ms para un mejor efecto visual
    const startTime = Date.now();

    fetch(url, {
        headers: {
            [header]: token,
            'Accept': 'application/json'
        }
    })
        .then(response => response.ok ? response.json() : Promise.reject(response.text()))
        .then(data => {
            const elapsedTime = Date.now() - startTime;
            const remainingTime = Math.max(0, minLoadingTime - elapsedTime);

            setTimeout(() => {
                todosProductos = data.productos || [];
                totalPages = data.totalPages || 0;
                currentPage = data.currentPage || 0;

                let productosFiltrados = todosProductos;
                if (categoriaId) {
                    productosFiltrados = todosProductos.filter(producto => producto.categoria === categoriaId);
                }

                // Preparar la tabla para la transición
                tbody.style.transition = 'opacity 0.3s ease';
                tbody.style.opacity = '0';

                setTimeout(() => {
                    tbody.innerHTML = '';
                    if (productosFiltrados.length === 0) {
                        tbody.innerHTML = `
                        <tr>
                            <td colspan="6" class="text-center">No se encontraron productos.</td>
                        </tr>
                    `;
                        totalPages = 0;
                        currentPage = 0;
                    } else {
                        const fragment = document.createDocumentFragment();
                        productosFiltrados.forEach(producto => {
                            const stock = producto.existencia !== undefined ? producto.existencia : (producto.stock || 0);
                            const tipo = producto.tipo || 'Inventario';
                            const badgeClass = getBadgeClassForType(tipo);

                            const row = document.createElement('tr');
                            row.innerHTML = `
                            <td>${producto.codigo || ''}</td>
                            <td>${producto.nombre || ''}</td>
                            <td>
                                <span class="badge ${badgeClass}">${producto.categoria || 'Sin categoría'}</span>
                            </td>
                            <td>
                                <input type="text"
                                    class="form-control form-control-sm stock-control"
                                    value="${stock}"
                                    data-producto-id="${producto.id}"
                                    data-codigo="${producto.codigo || ''}"
                                    data-nombre-producto="${producto.nombre || ''}"
                                    data-tipo="${tipo}"
                                    data-original-stock="${stock}"
                                    onkeypress="return soloEnteros(event)"
                                    oninput="this.value = this.value.replace(/[^0-9]/g, '');">
                            </td>
                            <td>${producto.precio ? '$' + parseFloat(producto.precio).toFixed(2) : '$0.00'}</td>
                            <td>
                                <div class="btn-group">
                                    <button class="btn btn-sm btn-danger" onclick="eliminarProducto(${producto.id}, '${tipo}')">
                                        <i class="fas fa-trash"></i>
                                    </button>
                                </div>
                            </td>
                        `;
                            fragment.appendChild(row);
                        });
                        tbody.appendChild(fragment);
                    }

                    // Aplicar transición suave
                    tbody.style.opacity = '1';

                    renderPagination();
                    configurarAutocompletado();
                    verificarAlertasStock();
                    formatearPrecios();
                    isLoading = false; // Permitir nuevas solicitudes
                }, 50); // Pequeño retraso para asegurar que la opacidad se aplique correctamente
            }, remainingTime);
        })
        .catch(error => {
            const elapsedTime = Date.now() - startTime;
            const remainingTime = Math.max(0, minLoadingTime - elapsedTime);

            setTimeout(() => {
                console.error('Error al cargar inventario:', error);
                tbody.style.transition = 'opacity 0.3s ease';
                tbody.style.opacity = '0';

                setTimeout(() => {
                    tbody.innerHTML = `
                    <tr>
                        <td colspan="6" class="text-center text-danger">
                            <i class="fas fa-exclamation-triangle"></i>
                            Error al cargar los productos: ${error}
                        </td>
                    </tr>
                `;
                    tbody.style.opacity = '1';
                    totalPages = 0;
                    currentPage = 0;
                    renderPagination();
                    mostrarNotificacion('Error al cargar los productos', 'error');
                    isLoading = false; // Permitir nuevas solicitudes
                }, 50);
            }, remainingTime);
        });
}

function renderPagination() {
    const paginationUl = document.getElementById('pagination');
    const pageInfoSpan = document.getElementById('pageInfo');
    paginationUl.innerHTML = '';

    if (totalPages === 0) {
        pageInfoSpan.textContent = 'No hay resultados';
        paginationUl.innerHTML = '';
        return;
    }

    const maxPagesToShow = 5;
    let startPage = Math.max(0, currentPage - Math.floor(maxPagesToShow / 2));
    let endPage = Math.min(totalPages - 1, startPage + maxPagesToShow - 1);

    if (endPage - startPage + 1 < maxPagesToShow) {
        startPage = Math.max(0, endPage - maxPagesToShow + 1);
    }

    // Calcular el total de ítems correctamente
    const totalItems = totalPages * pageSize;
    const startItem = currentPage * pageSize + 1;
    const endItem = Math.min((currentPage + 1) * pageSize, totalItems);
    pageInfoSpan.textContent = `Mostrando ${startItem}-${endItem} de ${totalItems}`;

    const prevLi = document.createElement('li');
    prevLi.className = `page-item ${currentPage === 0 ? 'disabled' : ''}`;
    prevLi.innerHTML = `<a class="page-link" href="#" aria-label="Previous"><span aria-hidden="true">«</span></a>`;
    if (currentPage > 0) {
        prevLi.onclick = () => mostrarProductosInventario(currentCategoriaId, currentQuery, currentPage - 1);
    }
    paginationUl.appendChild(prevLi);

    for (let i = startPage; i <= endPage; i++) {
        const pageLi = document.createElement('li');
        pageLi.className = `page-item ${i === currentPage ? 'active' : ''}`;
        pageLi.innerHTML = `<a class="page-link" href="#">${i + 1}</a>`;
        if (i !== currentPage) {
            pageLi.onclick = () => mostrarProductosInventario(currentCategoriaId, currentQuery, i);
        }
        paginationUl.appendChild(pageLi);
    }

    const nextLi = document.createElement('li');
    nextLi.className = `page-item ${currentPage >= totalPages - 1 ? 'disabled' : ''}`;
    nextLi.innerHTML = `<a class="page-link" href="#" aria-label="Next"><span aria-hidden="true">»</span></a>`;
    if (currentPage < totalPages - 1) {
        nextLi.onclick = () => mostrarProductosInventario(currentCategoriaId, currentQuery, currentPage + 1);
    }
    paginationUl.appendChild(nextLi);
}

function getBadgeClassForType(tipo) {
    const tipos = {
        'inventario': 'bg-primary',
        'fruvert': 'bg-success',
        'verdura': 'bg-info',
        'carnes': 'bg-danger',
        'default': 'bg-secondary'
    };
    return tipos[tipo.toLowerCase()] || tipos.default;
}

// Función para actualizar el stock
function actualizarStock(id, nuevoValor, tipo) {
    const token = document.querySelector("meta[name='_csrf']").getAttribute("content");
    const header = document.querySelector("meta[name='_csrf_header']").getAttribute("content");

    const endpoints = {
        'Inventario': '/api/productos/actualizarStock',
        'Fruvert': '/api/fruvert/actualizarStock',
        'Verdura': '/api/verduras/actualizarStock',
        'Carnes': '/api/carnes/actualizarStock',
        'Productos': '/api/productos/actualizarStock'
    };

    const url = `${endpoints[tipo] || '/api/productos/actualizarStock'}/${id}`;

    fetch(url, {
        method: 'POST',
        headers: {
            [header]: token,
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            nuevoStock: nuevoValor
        })
    })
        .then(response => {
            if (!response.ok) throw new Error('Error al actualizar stock');
            mostrarNotificacion('Stock actualizado correctamente', 'success');
            mostrarProductosInventario(currentCategoriaId, currentQuery, currentPage); // Mantener la página actual
        })
        .catch(error => {
            console.error('Error al actualizar stock:', error);
            mostrarNotificacion('No se pudo actualizar el stock', 'error');
        });
}
// Modificar el evento de teclado existente
document.getElementById('inventarioTableBody').addEventListener('keydown', function (event) {
    // Verificar si la tecla presionada es "Enter"
    if (event.code === 'Enter') {
        // Obtener el input que disparó el evento
        const input = event.target;

        // Verificar que el evento provenga de un input de stock
        if (input.classList.contains('stock-control')) {
            const id = input.getAttribute('data-producto-id');
            const nuevoValor = input.value;
            const tipo = input.getAttribute('data-tipo');
            const codigoProducto = input.getAttribute('data-codigo');
            const nombreProducto = input.getAttribute('data-nombre-producto');

            // Prevenir el comportamiento por defecto
            event.preventDefault();

            // Confirmar la acción
            Swal.fire({
                title: '¿Estás seguro?',
                text: "¿Deseas actualizar el stock?",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Sí, actualizar!',
                cancelButtonText: 'Cancelar'
            }).then((result) => {
                if (result.isConfirmed) {
                    // Llamar a la función de actualización de stock
                    actualizarStock(id, nuevoValor, tipo);

                    // Crear un objeto de actualización para mostrar en movimientos
                    const stockUpdate = [{
                        id: id,
                        codigo: codigoProducto,
                        nombre: nombreProducto,
                        nuevoStock: nuevoValor,
                        stockAnterior: input.getAttribute('data-original-stock'),
                        tipo: tipo,
                        categoria: input.closest('tr').querySelector('td:nth-child(3)').textContent.trim(),
                        proveedor: $('#proveedor option:selected').text() || 'Sin proveedor',
                        precio: input.closest('tr').querySelector('td:nth-child(5)').textContent.replace('$', '').trim()
                    }];

                    // Actualizar la sección de movimientos
                    actualizarSeccionMovimientos(stockUpdate);

                    // Verificar alertas de stock
                    verificarAlertasStock();
                }
            });
        }
    }
});

// Función para obtener todos los productos
async function obtenerTodosLosProductos(categoriaId = '', busqueda = '') {
    const token = document.querySelector("meta[name='_csrf']").getAttribute("content");
    const header = document.querySelector("meta[name='_csrf_header']").getAttribute("content");

    let url = busqueda.trim() !== ''
        ? `/api/inventario/buscar?query=${encodeURIComponent(busqueda)}&page=0&size=10000` // Usamos un tamaño grande para obtener todos los datos
        : `/listar?page=0&size=10000&tipo=todos`; // Usamos un tamaño grande para obtener todos los datos

    try {
        const response = await fetch(url, {
            headers: {
                [header]: token,
                'Accept': 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error('Error al obtener los productos');
        }

        const data = await response.json();
        let productos = data.productos || [];

        // Filtrar por categoría si es necesario
        if (categoriaId) {
            productos = productos.filter(producto => producto.categoria === categoriaId);
        }

        return productos;
    } catch (error) {
        console.error('Error al obtener todos los productos:', error);
        mostrarNotificacion('Error al obtener los productos para exportar', 'error');
        return [];
    }
}

async function exportarPDF() {
// un spinner de carga cuando son muchos productos
    Swal.fire({
        toast: true,
        position: 'top-end',
        title: 'Cargando...',
        text: 'Obteniendo productos para exportar',
        showConfirmButton: false,
        allowOutsideClick: false,
        didOpen: () => {
            Swal.showLoading();
        }
    });

    const { jsPDF } = window.jspdf;
    const doc = new jsPDF();

    // Configurar el título
    doc.setFontSize(18);
    doc.text('Listado Completo de Productos', 14, 20);

    // Obtener todos los productos
    const categoriaId = $('#categoria-filter').val();
    const busqueda = $('#searchInput').val();
    const productos = await obtenerTodosLosProductos(categoriaId, busqueda);

    if (productos.length === 0) {
        mostrarNotificacion('No hay productos para exportar', 'info');
        return;
    }

    // Preparar los datos para la tabla
    const rows = [];
    productos.forEach(producto => {
        const stock = producto.existencia !== undefined ? producto.existencia : (producto.stock || 0);
        const categoria = producto.categoria || 'Sin categoría';
        const precio = producto.precio ? '$' + parseFloat(producto.precio).toFixed(2) : '$0.00';

        const rowData = [
            producto.codigo || '',
            producto.nombre || '',
            categoria,
            stock.toString(),
            precio
        ];
        rows.push(rowData);
    });

    // Configurar las columnas
    const columns = [
        'Código',
        'Nombre',
        'Categoría',
        'Stock',
        'Precio'
    ];

    // Generar la tabla en el PDF
    doc.autoTable({
        head: [columns],
        body: rows,
        startY: 30,
        theme: 'striped',
        headStyles: {
            fillColor: [41, 128, 185],
            textColor: 255
        },
        styles: {
            fontSize: 10,
            cellPadding: 5
        }
    });

    // Agregar fecha de generación
    const fecha = new Date().toLocaleDateString();
    doc.setFontSize(10);
    doc.text(`Generado el: ${fecha}`, 14, doc.lastAutoTable.finalY + 10);

    // Descargar el PDF
    doc.save('listado-productos-completo.pdf');

    // Mostrar notificación con la cantidad de productos exportados
    Swal.fire({
        toast: true,
        position: 'top-end',
        icon: 'success',
        title: 'PDF Generado',
        text: `Se ha generado el PDF con ${productos.length} productos.`,
        timer: 2000,
        showConfirmButton: false
    });
}

// Función para exportar a Excel
async function exportarExcel() {
    // Mostrar spinner de carga cuando son muchos productos
    Swal.fire({
        toast: true,
        position: 'top-end',
        title: 'Cargando...',
        text: 'Obteniendo productos para exportar',
        showConfirmButton: false,
        allowOutsideClick: false,
        didOpen: () => {
            Swal.showLoading();
        }
    });
    // Obtener todos los productos
    const categoriaId = $('#categoria-filter').val();
    const busqueda = $('#searchInput').val();
    const productos = await obtenerTodosLosProductos(categoriaId, busqueda);

    if (productos.length === 0) {
        mostrarNotificacion('No hay productos para exportar', 'info');
        return;
    }

    // Crear array de datos con los encabezados
    const data = [
        ['Código', 'Nombre', 'Categoría', 'Stock', 'Precio'] // Encabezados
    ];

    // Usar los productos obtenidos
    productos.forEach(producto => {
        const stock = producto.existencia !== undefined ? producto.existencia : (producto.stock || 0);
        const categoria = producto.categoria || 'Sin categoría';
        const precio = producto.precio ? '$' + parseFloat(producto.precio).toFixed(2) : '$0.00';

        const rowData = [
            producto.codigo || '',
            producto.nombre || '',
            categoria,
            stock,
            precio
        ];
        data.push(rowData);
    });

    // Crear libro de trabajo
    const ws = XLSX.utils.aoa_to_sheet(data);
    const wb = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(wb, ws, "Inventario");

    // Generar el archivo
    XLSX.writeFile(wb, "inventario_completo.xlsx");

    // Mostrar notificación con la cantidad de productos exportados
    Swal.fire({
        toast: true,
        position: 'top-end',
        icon: 'success',
        title: 'Excel Generado',
        text: `Se ha generado el Excel con ${productos.length} productos.`,
        timer: 2000,
        showConfirmButton: false
    });
}
// Función para importar desde Excel
function importarExcel(input) {
    const file = input.files[0];
    const reader = new FileReader();

    reader.onload = function(e) {
        try {
            const data = new Uint8Array(e.target.result);
            const workbook = XLSX.read(data, { type: 'array' });
            const firstSheet = workbook.Sheets[workbook.SheetNames[0]];
            const jsonData = XLSX.utils.sheet_to_json(firstSheet, { header: 1 });

            // Log detallado de los datos
            console.log('Datos a importar:', JSON.stringify(jsonData, null, 2));

            // Confirmar la importación
            Swal.fire({
                title: '¿Confirmar actualización?',
                text: `Se actualizarán ${jsonData.length} productos`,
                icon: 'warning',
                showCancelButton: true,
                confirmButtonText: 'Sí, actualizar',
                cancelButtonText: 'Cancelar'
            }).then((result) => {
                if (result.isConfirmed) {
                    actualizarInventarioDesdeExcel(jsonData);
                }
            });
        } catch (error) {
            console.error('Error al procesar Excel:', error);
            Swal.fire({
                title: 'Error',
                text: 'No se pudo procesar el archivo Excel',
                icon: 'error'
            });
        }
    };

    reader.readAsArrayBuffer(file);
    input.value = ''; // Limpiar input para permitir cargar el mismo archivo
}

function actualizarInventarioDesdeExcel(data) {
    const token = document.querySelector("meta[name='_csrf']").getAttribute("content");
    const header = document.querySelector("meta[name='_csrf_header']").getAttribute("content");

    // Obtener los datos actuales de la tabla
    const tbody = document.getElementById('inventarioTableBody');
    const rows = tbody.getElementsByTagName('tr');
    const productosActuales = [];
    for (let i = 0; i < rows.length; i++) {
        const row = rows[i];
        const codigoProducto = row.cells[0].textContent;
        const stockActual = row.cells[3].querySelector('input').value;
        productosActuales.push({ codigo: codigoProducto, stock: stockActual });
    }

    // Comparar los datos del archivo Excel con los datos actuales de la tabla
    const actualizaciones = data.slice(1).map(row => {
        const codigoProducto = row[0];
        const stockNuevo = row[3];
        const productoActualizado = productosActuales.find(producto => producto.codigo === codigoProducto);
        if (productoActualizado && productoActualizado.stock !== stockNuevo) {
            return { codigo: codigoProducto, nuevoStock: stockNuevo };
        }
        return null;
    }).filter(actualizacion => actualizacion !== null);

    // Actualizar el inventario
    fetch('/api/inventario/actualizarLote', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            [header]: token
        },
        body: JSON.stringify(actualizaciones)
    })
        .then(response => response.json())
        .then(data => {
            Swal.fire({
                title: '¡Éxito!',
                text: data.mensaje || 'Inventario actualizado correctamente',
                icon: 'success'
            });

            // Actualizar la tabla
            for (let i = 0; i < rows.length; i++) {
                const row = rows[i];
                const codigoProducto = row.cells[0].textContent;
                const actualizacion = actualizaciones.find(a => a.codigo === codigoProducto);
                if (actualizacion) {
                    row.cells[3].querySelector('input').value = actualizacion.nuevoStock;
                }
            }
        })
        .catch(error => {
            console.error('Error detallado:', error);
            Swal.fire({
                title: 'Error',
                text: error.message || 'No se pudo actualizar el inventario',
                icon:'error'
            });
        });
}
function formatearPrecios() {
    document.querySelectorAll('#inventarioTableBody td:nth-child(5)').forEach(td => {
        let precio = td.textContent.replace('$', '').replace(/\./g, '').trim();
        if (!isNaN(precio) && precio !== "") {
            td.textContent = `$${Number(precio).toLocaleString('es-CO')}`;
        }
    });
}