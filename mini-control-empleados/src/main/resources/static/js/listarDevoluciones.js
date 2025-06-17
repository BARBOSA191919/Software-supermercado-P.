document.addEventListener('DOMContentLoaded', function() {
    const sidebar = document.getElementById('sidebar');
    const content = document.getElementById('content');
    const sidebarCollapse = document.getElementById('sidebarCollapse');

    // Aplicar estado inicial del sidebar
    function applySidebarState() {
        const savedState = localStorage.getItem('sidebarState') || 'open';
        if (savedState === 'closed') {
            sidebar.classList.remove('active');
            content.classList.remove('active');
            sidebarCollapse.style.left = '10px';
        } else {
            sidebar.classList.add('active');
            content.classList.add('active');
            sidebarCollapse.style.left = '265px';
        }
    }

    // Toggle del sidebar
    function toggleSidebar() {
        sidebar.classList.toggle('active');
        content.classList.toggle('active');
        sidebarCollapse.style.left = sidebar.classList.contains('active') ? '265px' : '10px';
        localStorage.setItem('sidebarState', sidebar.classList.contains('active') ? 'open' : 'closed');
    }

    applySidebarState();
    sidebarCollapse.addEventListener('click', toggleSidebar);

    // Función para actualizar la tabla de devoluciones
    function updateTable(search = '', motivo = '', fechaInicio = '', fechaFin = '', page = 0) {
        const url = `/devoluciones/listar?page=${page}` +
            (search ? `&search=${encodeURIComponent(search)}` : '') +
            (motivo ? `&motivo=${encodeURIComponent(motivo)}` : '') +
            (fechaInicio ? `&fechaInicio=${encodeURIComponent(fechaInicio)}` : '') +
            (fechaFin ? `&fechaFin=${encodeURIComponent(fechaFin)}` : '');

        fetch(url, {
            headers: { 'X-Requested-With': 'XMLHttpRequest' }
        })
            .then(response => response.text())
            .then(html => {
                const parser = new DOMParser();
                const doc = parser.parseFromString(html, 'text/html');
                const newTable = doc.querySelector('#table-container');
                const newPagination = doc.querySelector('nav');

                document.querySelector('#table-container').innerHTML = newTable.innerHTML;
                const paginationContainer = document.querySelector('nav');
                if (paginationContainer && newPagination) {
                    paginationContainer.innerHTML = newPagination.innerHTML;
                }

                // Reasignar eventos a los nuevos botones de editar y eliminar
                reassignModalEvents();
            })
            .catch(error => console.error('Error al actualizar tabla:', error));
    }

    // Filtros
    let searchTimeout = null;
    document.getElementById('buscadorDevoluciones').addEventListener('input', function() {
        clearTimeout(searchTimeout);
        searchTimeout = setTimeout(() => {
            const search = this.value.trim();
            const motivo = document.getElementById('filtroMotivo').value;
            const fechaInicio = document.getElementById('filtroFechaInicio').value;
            const fechaFin = document.getElementById('filtroFechaFin').value;
            updateTable(search, motivo, fechaInicio, fechaFin);
        }, 500);
    });

    document.getElementById('filtroMotivo').addEventListener('change', function() {
        const search = document.getElementById('buscadorDevoluciones').value.trim();
        const motivo = this.value;
        const fechaInicio = document.getElementById('filtroFechaInicio').value;
        const fechaFin = document.getElementById('filtroFechaFin').value;
        updateTable(search, motivo, fechaInicio, fechaFin);
    });

    document.getElementById('filtroFechaInicio').addEventListener('change', function() {
        const search = document.getElementById('buscadorDevoluciones').value.trim();
        const motivo = document.getElementById('filtroMotivo').value;
        const fechaInicio = this.value;
        const fechaFin = document.getElementById('filtroFechaFin').value;
        updateTable(search, motivo, fechaInicio, fechaFin);
    });

    document.getElementById('filtroFechaFin').addEventListener('change', function() {
        const search = document.getElementById('buscadorDevoluciones').value.trim();
        const motivo = document.getElementById('filtroMotivo').value;
        const fechaInicio = document.getElementById('filtroFechaInicio').value;
        const fechaFin = this.value;
        updateTable(search, motivo, fechaInicio, fechaFin);
    });

    // Resetear filtros
    document.getElementById('resetFiltros').addEventListener('click', function() {
        document.getElementById('buscadorDevoluciones').value = '';
        document.getElementById('filtroMotivo').value = '';
        document.getElementById('filtroFechaInicio').value = '';
        document.getElementById('filtroFechaFin').value = '';
        updateTable();
        Swal.fire({ toast: true, position: 'top-end', icon: 'info', title: 'Filtros reseteados', showConfirmButton: false, timer: 1500 });
    });

    // Agregar devolución
    document.querySelector('#agregarDevolucionModal form').addEventListener('submit', function(e) {
        e.preventDefault();
        fetch(this.action, {
            method: 'POST',
            body: new FormData(this),
            headers: { 'X-Requested-With': 'XMLHttpRequest' }
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    Swal.fire({ toast: true, position: 'top-end', icon: 'success', title: 'Devolución Agregada', text: data.message, timer: 2000, showConfirmButton: false });
                    bootstrap.Modal.getInstance(document.querySelector('#agregarDevolucionModal')).hide();
                    updateTable();
                } else {
                    Swal.fire({ toast: true, position: 'top-end', icon: 'error', title: 'Error', text: data.error, timer: 2000, showConfirmButton: false });
                }
            })
            .catch(error => Swal.fire({ toast: true, position: 'top-end', icon: 'error', title: 'Error', text: 'Error al agregar devolución', timer: 2000, showConfirmButton: false }));
    });

    // Reasignar eventos a los modales de edición y eliminación
    function reassignModalEvents() {
        document.querySelectorAll('[id^="editarDevolucionModal"] form').forEach(form => {
            form.removeEventListener('submit', handleEditSubmit); // Evitar duplicados
            form.addEventListener('submit', handleEditSubmit);
        });

        document.querySelectorAll('[id^="eliminarDevolucionModal"] .btn-danger').forEach(button => {
            button.removeEventListener('click', handleDeleteClick); // Evitar duplicados
            button.addEventListener('click', handleDeleteClick);
        });
    }

    function handleEditSubmit(e) {
        e.preventDefault();
        fetch(this.action, {
            method: 'POST',
            body: new FormData(this),
            headers: { 'X-Requested-With': 'XMLHttpRequest' }
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    Swal.fire({ toast: true, position: 'top-end', icon: 'success', title: 'Devolución Actualizada', text: data.message, timer: 2000, showConfirmButton: false });
                    bootstrap.Modal.getInstance(this.closest('.modal')).hide();
                    updateTable();
                } else {
                    Swal.fire({ toast: true, position: 'top-end', icon: 'error', title: 'Error', text: data.error, timer: 2000, showConfirmButton: false });
                }
            })
            .catch(error => Swal.fire({ toast: true, position: 'top-end', icon: 'error', title: 'Error', text: 'Error al actualizar devolución', timer: 2000, showConfirmButton: false }));
    }

    function handleDeleteClick(e) {
        e.preventDefault();
        const url = this.getAttribute('href');
        fetch(url, {
            headers: { 'X-Requested-With': 'XMLHttpRequest' }
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    Swal.fire({ toast: true, position: 'top-end', icon: 'success', title: 'Devolución Eliminada', text: data.message, timer: 2000, showConfirmButton: false });
                    bootstrap.Modal.getInstance(this.closest('.modal')).hide();
                    updateTable();
                } else {
                    Swal.fire({ toast: true, position: 'top-end', icon: 'error', title: 'Error', text: data.error, timer: 2000, showConfirmButton: false });
                }
            })
            .catch(error => Swal.fire({ toast: true, position: 'top-end', icon: 'error', title: 'Error', text: 'Error al eliminar devolución', timer: 2000, showConfirmButton: false }));
    }

    // Confirmar logout
    window.confirmLogout = function() {
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
    };

    // Funciones existentes de búsqueda de productos (sin cambios)
    function cambiarTipoBusqueda() {
        const tipoBusqueda = document.getElementById("tipoBusqueda").value;
        document.getElementById("campoBusquedaGeneral").style.display = tipoBusqueda === "venta" ? "none" : "block";
        document.getElementById("campoBusquedaVenta").style.display = tipoBusqueda === "venta" ? "block" : "none";
    }

    document.getElementById("idVenta").addEventListener('input', function() {
        const idVenta = this.value.trim();
        if (idVenta) {
            fetch(`/devoluciones/buscarProductosPorVenta?idVenta=${idVenta}`)
                .then(response => response.json())
                .then(data => {
                    const resultadosDiv = document.getElementById("resultadosBusquedaVenta");
                    resultadosDiv.innerHTML = '';
                    if (data.length === 0) {
                        Swal.fire({ toast: true, position: "top-end", icon: 'warning', title: 'Sin resultados', text: 'No se encontraron productos para esta venta.', timer: 1000, showConfirmButton: false });
                        return;
                    }
                    const listGroup = document.createElement("div");
                    listGroup.className = "list-group";
                    resultadosDiv.appendChild(listGroup);
                    data.forEach(producto => {
                        const item = document.createElement("div");
                        item.className = "list-group-item list-group-item-action";
                        item.innerHTML = `<strong>${producto.nombre}</strong> (Código: ${producto.codigo})<br>Precio: $${producto.precio.toFixed(2)} - Cantidad: ${producto.cantidad}`;
                        item.addEventListener('click', () => {
                            document.getElementById("codigoProductoAgregar").value = producto.codigo;
                            document.getElementById("nombreProductoAgregar").value = producto.nombre;
                            document.getElementById("precioUnitarioAgregar").value = producto.precio;
                            document.getElementById("cantidadDevolucionAdd").value = 1;
                            calcularPrecioFinal("cantidadDevolucionAdd", "precioUnitarioAgregar", "precioFinalAgregar");
                            Swal.fire({ toast: true, position: "top-end", icon: 'success', title: 'Éxito', text: 'Producto seleccionado.', timer: 1000, showConfirmButton: false });
                        });
                        listGroup.appendChild(item);
                    });
                })
                .catch(error => Swal.fire({ toast: true, position: "top-end", icon: 'error', title: 'Error', text: 'Error con el servidor.', timer: 1000, showConfirmButton: false }));
        } else {
            document.getElementById("resultadosBusquedaVenta").innerHTML = '';
        }
    });

    function buscarProductos(modal) {
        const cantidadSuffix = modal === 'agregar' ? 'Add' : 'Edit';
        const prefix = modal === 'agregar' ? 'Agregar' : 'Edit';
        const searchInput = document.getElementById(`buscarProducto${prefix}`);
        const resultadosDiv = document.getElementById(`resultadosBusqueda${prefix}`);
        const tipoProductoSelect = document.getElementById(`tipoProducto${prefix}`);

        if (!searchInput || !resultadosDiv || !tipoProductoSelect) return;

        searchInput.addEventListener('input', function() {
            const query = this.value.trim();
            const tipoProducto = tipoProductoSelect.value;
            if (query.length >= 2 && tipoProducto) {
                fetch(`/devoluciones/buscarProductos?q=${query}&tipo=${tipoProducto}`)
                    .then(response => response.json())
                    .then(data => {
                        resultadosDiv.innerHTML = '';
                        if (data.length === 0) {
                            Swal.fire({ toast: true, position: "top-end", icon: 'warning', title: 'Sin resultados', text: 'No se encontraron productos.', timer: 1000, showConfirmButton: false });
                            return;
                        }
                        const listGroup = document.createElement('div');
                        listGroup.className = 'list-group';
                        resultadosDiv.appendChild(listGroup);
                        data.forEach(producto => {
                            const item = document.createElement('div');
                            item.className = 'list-group-item list-group-item-action';
                            item.innerHTML = `<strong>${producto.nombre}</strong> (Código: ${producto.codigo}) - Precio: $${producto.precio}`;
                            item.addEventListener('click', () => {
                                document.getElementById(`codigoProducto${prefix}`).value = producto.codigo;
                                document.getElementById(`nombreProducto${prefix}`).value = producto.nombre;
                                document.getElementById(`precioUnitario${prefix}`).value = producto.precio;
                                const cantidadInput = document.getElementById(`cantidadDevolucion${cantidadSuffix}`);
                                if (cantidadInput && !cantidadInput.value) cantidadInput.value = 1;
                                calcularPrecioFinal(`cantidadDevolucion${cantidadSuffix}`, `precioUnitario${prefix}`, `precioFinal${prefix}`);
                                Swal.fire({ toast: true, position: "top-end", icon: 'success', title: 'Éxito', text: 'Producto seleccionado correctamente.', timer: 1000, showConfirmButton: false });
                            });
                            listGroup.appendChild(item);
                        });
                    })
                    .catch(error => Swal.fire({ toast: true, position: "top-end", icon: 'warning', title: 'Error', text: 'Conectando con el servidor.', timer: 1000, showConfirmButton: false }));
            } else if (query.length > 0 && query.length < 2) {
                Swal.fire({ toast: true, position: "top-end", icon: 'info', title: 'Información', text: 'Escribe 2 caracteres para buscar 🔍︎', timer: 3000, showConfirmButton: false });
            } else {
                resultadosDiv.innerHTML = '';
            }
        });

        tipoProductoSelect.addEventListener('change', () => {
            const query = searchInput.value.trim();
            if (query.length >= 2 && tipoProductoSelect.value) searchInput.dispatchEvent(new Event('input'));
        });
    }

    buscarProductos('agregar');
    document.querySelectorAll('[id^="editarDevolucionModal"]').forEach(() => buscarProductos('edit'));

    document.querySelectorAll('[id^="cantidadDevolucion"]').forEach(input => {
        let prefix = input.id.includes('Add') ? 'Agregar' : 'Edit';
        input.addEventListener('input', () => calcularPrecioFinal(input.id, `precioUnitario${prefix}`, `precioFinal${prefix}`));
    });

    function calcularPrecioFinal(cantidadInputId, precioUnitarioInputId, precioFinalInputId) {
        const cantidad = document.getElementById(cantidadInputId).value;
        const precioUnitario = document.getElementById(precioUnitarioInputId).value;
        document.getElementById(precioFinalInputId).value = (cantidad * precioUnitario).toFixed(2);
    }

    // Exportar a PDF (sin cambios, pero asegúrate de que funcione con la tabla actualizada)
// Reemplaza la función existente en listarDevoluciones.js
    window.devolucionesPDF = function() {
        const { jsPDF } = window.jspdf;
        const doc = new jsPDF();

        // Obtener los valores de los filtros actuales
        const filtroTexto = document.getElementById('buscadorDevoluciones').value.toLowerCase();
        const filtroMotivo = document.getElementById('filtroMotivo').value;
        const filtroFechaInicio = document.getElementById('filtroFechaInicio').value;
        const filtroFechaFin = document.getElementById('filtroFechaFin').value;

        // Construir la URL con los filtros
        const url = `/devoluciones/todas` +
            (filtroTexto ? `?search=${encodeURIComponent(filtroTexto)}` : '') +
            (filtroMotivo ? `${filtroTexto ? '&' : '?'}motivo=${encodeURIComponent(filtroMotivo)}` : '') +
            (filtroFechaInicio ? `${filtroTexto || filtroMotivo ? '&' : '?'}fechaInicio=${encodeURIComponent(filtroFechaInicio)}` : '') +
            (filtroFechaFin ? `${filtroTexto || filtroMotivo || filtroFechaInicio ? '&' : '?'}fechaFin=${encodeURIComponent(filtroFechaFin)}` : '');

        fetch(url, {
            headers: { 'X-Requested-With': 'XMLHttpRequest' }
        })
            .then(response => response.json())
            .then(data => {
                if (data.length === 0) {
                    Swal.fire({
                        icon: 'info',
                        title: 'Sin datos',
                        text: 'No hay devoluciones que coincidan con los filtros aplicados.'
                    });
                    return;
                }

                // Título del PDF
                doc.setFontSize(18);
                doc.text('Listado de Devoluciones', 14, 20);

                // Información de filtros
                let yPos = 25;
                doc.setFontSize(10);
                if (filtroTexto) { doc.text(`Filtro de búsqueda: "${filtroTexto}"`, 14, yPos); yPos += 5; }
                if (filtroMotivo) { doc.text(`Motivo: ${filtroMotivo}`, 14, yPos); yPos += 5; }
                if (filtroFechaInicio) { doc.text(`Desde: ${new Date(filtroFechaInicio).toLocaleDateString()}`, 14, yPos); yPos += 5; }
                if (filtroFechaFin) { doc.text(`Hasta: ${new Date(filtroFechaFin).toLocaleDateString()}`, 14, yPos); yPos += 5; }

                // Preparar datos para la tabla
                const columns = ['Ítem', 'Fecha', 'Cód product', 'Nombre', 'Cant', 'Precio', 'Total', 'Motivo'];
                const rows = data.map(devolucion => [
                    devolucion.id,
                    new Date(devolucion.fecha).toLocaleDateString('es-ES', { day: '2-digit', month: '2-digit', year: 'numeric' }),
                    devolucion.codigoProducto,
                    devolucion.nombreProducto,
                    devolucion.cantidad,
                    devolucion.precio.toFixed(2) + ' $',
                    devolucion.precio_final.toFixed(2) + ' $',
                    devolucion.motivo
                ]);

                // Generar la tabla en el PDF
                doc.autoTable({
                    head: [columns],
                    body: rows,
                    startY: yPos + 5,
                    theme: 'striped',
                    headStyles: { fillColor: [41, 128, 185], textColor: 255 },
                    styles: { fontSize: 9, cellPadding: 2.2 },
                    columnStyles: {
                        0: { cellWidth: 12 }, // Ítem
                        1: { cellWidth: 20 }, // Fecha
                        2: { cellWidth: 20 }, // Cód product
                        3: { cellWidth: 35 }, // Nombre
                        4: { cellWidth: 20 }, // Cant
                        5: { cellWidth: 28 }, // Precio
                        6: { cellWidth: 28 }, // Total
                        7: { cellWidth: 25 }  // Motivo
                    }
                });

                // Información adicional
                const totalDevoluciones = rows.length;
                doc.setFontSize(10);
                doc.text(`Total de devoluciones: ${totalDevoluciones}`, 14, doc.lastAutoTable.finalY + 10);
                const fecha = new Date().toLocaleDateString();
                doc.text(`Generado el: ${fecha}`, 14, doc.lastAutoTable.finalY + 15);

                // Pie de página
                const pageCount = doc.internal.getNumberOfPages();
                for (let i = 1; i <= pageCount; i++) {
                    doc.setPage(i);
                    doc.setFontSize(8);
                    doc.text('Página ' + i + ' de ' + pageCount, doc.internal.pageSize.width - 20, doc.internal.pageSize.height - 10);
                    doc.text('Fecha de generación: ' + fecha, 20, doc.internal.pageSize.height - 10);
                }

                // Descargar el PDF
                doc.save('listado-Devoluciones.pdf');

                // Notificación de éxito
                Swal.fire({
                    toast: true,
                    position: "top-end",
                    icon: 'success',
                    title: 'PDF Generado',
                    text: `Se ha generado el PDF con ${totalDevoluciones} devoluciones.`,
                    timer: 2000,
                    timerProgressBar: true,
                    showConfirmButton: false
                });
            })
            .catch(error => {
                console.error('Error al generar PDF:', error);
                Swal.fire({
                    icon: 'error',
                    title: 'Error',
                    text: 'Ocurrió un error al generar el PDF: ' + error.message,
                    confirmButtonColor: '#3085d6'
                });
            });
    };
    // Inicializar eventos
    reassignModalEvents();
});