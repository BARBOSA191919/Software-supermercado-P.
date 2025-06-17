document.addEventListener('DOMContentLoaded', function() {
    const sidebar = document.getElementById('sidebar');
    const content = document.getElementById('content');
    const sidebarCollapse = document.getElementById('sidebarCollapse');

    // Sidebar toggle
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

    function toggleSidebar() {
        sidebar.classList.toggle('active');
        content.classList.toggle('active');
        sidebarCollapse.style.left = sidebar.classList.contains('active') ? '265px' : '10px';
        localStorage.setItem('sidebarState', sidebar.classList.contains('active') ? 'open' : 'closed');
    }

    applySidebarState();
    sidebarCollapse.addEventListener('click', toggleSidebar);

    // Actualizar tabla y paginación
    function updateTable(search = '', page = 0) {
        const tableUrl = `/compras/listar?page=${page}` + (search ? `&search=${encodeURIComponent(search)}` : '');
        fetch(tableUrl, {
            headers: { 'X-Requested-With': 'XMLHttpRequest' }
        })
            .then(response => response.text())
            .then(html => {
                const parser = new DOMParser();
                const doc = parser.parseFromString(html, 'text/html');
                const newTable = doc.querySelector('#table-container');
                document.querySelector('#table-container').innerHTML = newTable.innerHTML;

                const paginationUrl = `/compras/listar?page=${page}` + (search ? `&search=${encodeURIComponent(search)}` : '') + '&fragment=pagination';
                fetch(paginationUrl, {
                    headers: { 'X-Requested-With': 'XMLHttpRequest' }
                })
                    .then(response => response.text())
                    .then(paginationHtml => {
                        const paginationDoc = parser.parseFromString(paginationHtml, 'text/html');
                        const newPagination = paginationDoc.querySelector('#pagination-container');
                        const paginationContainer = document.querySelector('#pagination-container');
                        if (paginationContainer && newPagination) {
                            paginationContainer.innerHTML = newPagination.innerHTML;
                        }
                        reassignModalEvents();
                    })
                    .catch(error => console.error('Error al actualizar paginación:', error));
            })
            .catch(error => console.error('Error al actualizar tabla:', error));
    }

    // Buscador
    let searchTimeout = null;
    document.getElementById('buscadorCompras').addEventListener('input', function() {
        clearTimeout(searchTimeout);
        searchTimeout = setTimeout(() => {
            const search = this.value.trim();
            updateTable(search);
        }, 500);
    });

    // Agregar compra
    document.getElementById('agregarCompraModal').querySelector('form').addEventListener('submit', function(e) {
        e.preventDefault();
        fetch(this.action, {
            method: 'POST',
            body: new FormData(this),
            headers: { 'X-Requested-With': 'XMLHttpRequest' }
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    Swal.fire({ toast: true, position: 'top-end', icon: 'success', title: 'Compra Agregada', text: data.message, timer: 2000, showConfirmButton: false });
                    bootstrap.Modal.getInstance(document.querySelector('#agregarCompraModal')).hide();
                    updateTable();
                } else {
                    Swal.fire({ toast: true, position: 'top-end', icon: 'error', title: 'Error', text: data.error, timer: 2000, showConfirmButton: false });
                }
            })
            .catch(error => Swal.fire({ toast: true, position: 'top-end', icon: 'error', title: 'Error', text: 'Error al agregar compra', timer: 2000, showConfirmButton: false }));
    });

    // Reasignar eventos a modales
    function reassignModalEvents() {
        document.querySelectorAll('[id^="editarModal"] form').forEach(form => {
            form.removeEventListener('submit', handleEditSubmit);
            form.addEventListener('submit', handleEditSubmit);
        });

        document.querySelectorAll('.eliminar-compra-btn').forEach(button => {
            button.removeEventListener('click', handleDeleteClick);
            button.addEventListener('click', function() {
                const modal = document.querySelector(this.getAttribute('data-bs-target'));
                modal.querySelector('.btn-danger').onclick = handleDeleteClick;
            });
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
                    Swal.fire({ toast: true, position: 'top-end', icon: 'success', title: 'Compra Actualizada', text: data.message, timer: 2000, showConfirmButton: false });
                    bootstrap.Modal.getInstance(this.closest('.modal')).hide();
                    updateTable();
                } else {
                    Swal.fire({ toast: true, position: 'top-end', icon: 'error', title: 'Error', text: data.error, timer: 2000, showConfirmButton: false });
                }
            })
            .catch(error => Swal.fire({ toast: true, position: 'top-end', icon: 'error', title: 'Error', text: 'Error al actualizar compra', timer: 2000, showConfirmButton: false }));
    }

    function handleDeleteClick(e) {
        e.preventDefault();
        const url = this.closest('.modal-footer').querySelector('.btn-danger').getAttribute('href');
        fetch(url.replace('/eliminar/', '/eliminar/'), {
            method: 'POST',
            headers: { 'X-Requested-With': 'XMLHttpRequest' }
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    Swal.fire({ toast: true, position: 'top-end', icon: 'success', title: 'Compra Eliminada', text: data.message, timer: 2000, showConfirmButton: false });
                    bootstrap.Modal.getInstance(this.closest('.modal')).hide();
                    updateTable();
                } else {
                    Swal.fire({ toast: true, position: 'top-end', icon: 'error', title: 'Error', text: data.error, timer: 2000, showConfirmButton: false });
                }
            })
            .catch(error => Swal.fire({ toast: true, position: 'top-end', icon: 'error', title: 'Error', text: 'Error al eliminar compra', timer: 2000, showConfirmButton: false }));
    }

    // Exportar a PDF
    window.comprasPDF = function() {
        const { jsPDF } = window.jspdf;
        const doc = new jsPDF();
        const filtroTexto = document.getElementById('buscadorCompras').value.trim();
        const url = `/compras/todos` + (filtroTexto ? `?search=${encodeURIComponent(filtroTexto)}` : '');

        fetch(url, {
            headers: { 'X-Requested-With': 'XMLHttpRequest' }
        })
            .then(response => response.json())
            .then(data => {
                if (data.length === 0) {
                    Swal.fire({ icon: 'info', title: 'Sin datos', text: 'No hay compras que coincidan con el criterio de búsqueda.' });
                    return;
                }

                doc.setFontSize(18);
                doc.text('Listado de Compras', 14, 20);
                let yPos = 25;
                doc.setFontSize(10);
                if (filtroTexto) {
                    doc.text(`Filtro de búsqueda: "${filtroTexto}"`, 14, yPos);
                    yPos += 5;
                }

                const columns = ['Código', 'Proveedor', 'Producto', 'Fecha', 'Cantidad', 'IVA', 'Descuento', 'Precio', 'Total'];
                const rows = data.map(compra => [
                    compra.codigo,
                    compra.proveedor.nombreProveedor,
                    compra.nombre,
                    new Date(compra.fecha).toLocaleDateString('es-ES'),
                    compra.cantidad,
                    compra.iva + '%',
                    compra.descuento + '%',
                    compra.precio + ' $',
                    compra.total + ' $'
                ]);

                doc.autoTable({
                    head: [columns],
                    body: rows,
                    startY: yPos + 5,
                    theme: 'striped',
                    headStyles: { fillColor: [41, 128, 185], textColor: 255 },
                    styles: { fontSize: 9, cellPadding: 2.2 }
                });

                const totalCompras = rows.length;
                doc.setFontSize(10);
                doc.text(`Total de compras: ${totalCompras}`, 14, doc.lastAutoTable.finalY + 10);
                const fecha = new Date().toLocaleDateString();
                doc.text(`Generado el: ${fecha}`, 14, doc.lastAutoTable.finalY + 15);

                const pageCount = doc.internal.getNumberOfPages();
                for (let i = 1; i <= pageCount; i++) {
                    doc.setPage(i);
                    doc.setFontSize(8);
                    doc.text('Página ' + i + ' de ' + pageCount, doc.internal.pageSize.width - 20, doc.internal.pageSize.height - 10);
                    doc.text('Fecha de generación: ' + fecha, 20, doc.internal.pageSize.height - 10);
                }

                doc.save('listado_compras.pdf');
                Swal.fire({
                    toast: true,
                    position: 'top-end',
                    icon: 'success',
                    title: 'PDF Generado',
                    text: `Se ha generado el PDF con ${totalCompras} compras.`,
                    timer: 2000,
                    showConfirmButton: false
                });
            })
            .catch(error => Swal.fire({
                icon: 'error',
                title: 'Error',
                text: 'Ocurrió un error al generar el PDF: ' + error.message
            }));
    };

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

    // Calcular total en modales
    function calcularTotal(modalId) {
        const cantidadInput = document.querySelector(`#${modalId} [name='cantidad']`);
        const precioInput = document.querySelector(`#${modalId} [name='precio']`);
        const ivaInput = document.querySelector(`#${modalId} [name='iva']`);
        const descuentoInput = document.querySelector(`#${modalId} [name='descuento']`);
        const totalInput = document.querySelector(`#${modalId} [name='total']`);

        function calcular() {
            const cantidad = parseFloat(cantidadInput.value) || 0;
            const precio = parseFloat(precioInput.value) || 0;
            const iva = parseFloat(ivaInput.value) || 0;
            const descuento = parseFloat(descuentoInput.value) || 0;

            const subtotal = cantidad * precio;
            const valorIva = subtotal * (iva / 100);
            const valorDescuento = subtotal * (descuento / 100);
            const total = subtotal + valorIva - valorDescuento;

            totalInput.value = total.toFixed(2);
        }

        cantidadInput.addEventListener('input', calcular);
        precioInput.addEventListener('input', calcular);
        ivaInput.addEventListener('input', calcular);
        descuentoInput.addEventListener('input', calcular);
        calcular(); // Calcular al cargar
    }

    calcularTotal('agregarCompraModal');
    document.querySelectorAll('[id^="editarModal"]').forEach(modal => calcularTotal(modal.id));

    // Inicializar eventos
    reassignModalEvents();
});