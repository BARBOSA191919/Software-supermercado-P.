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

    // Función para actualizar la tabla de proveedores
    function updateTable(search = '', page = 0) {
        const url = `/proveedores/listar?page=${page}` + (search ? `&search=${encodeURIComponent(search)}` : '');
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

    // Buscador
    let searchTimeout = null;
    document.getElementById('buscadorProveedores').addEventListener('input', function() {
        clearTimeout(searchTimeout);
        searchTimeout = setTimeout(() => {
            const search = this.value.trim();
            updateTable(search);
        }, 500);
    });

    // Agregar proveedor
    document.getElementById('agregarProveedorForm').addEventListener('submit', function(e) {
        e.preventDefault();
        fetch(this.action, {
            method: 'POST',
            body: new FormData(this),
            headers: { 'X-Requested-With': 'XMLHttpRequest' }
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    Swal.fire({ toast: true, position: 'top-end', icon: 'success', title: 'Proveedor Agregado', text: data.message, timer: 2000, showConfirmButton: false });
                    bootstrap.Modal.getInstance(document.querySelector('#agregarProveedorModal')).hide();
                    updateTable();
                } else {
                    Swal.fire({ toast: true, position: 'top-end', icon: 'error', title: 'Error', text: data.error, timer: 2000, showConfirmButton: false });
                }
            })
            .catch(error => Swal.fire({ toast: true, position: 'top-end', icon: 'error', title: 'Error', text: 'Error al agregar proveedor', timer: 2000, showConfirmButton: false }));
    });

    // Reasignar eventos a los modales de edición y eliminación
    function reassignModalEvents() {
        document.querySelectorAll('[id^="editarModal"] form').forEach(form => {
            form.removeEventListener('submit', handleEditSubmit);
            form.addEventListener('submit', handleEditSubmit);
        });

        document.querySelectorAll('[id^="eliminarModal"] .btn-danger').forEach(button => {
            button.removeEventListener('click', handleDeleteClick);
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
                    Swal.fire({ toast: true, position: 'top-end', icon: 'success', title: 'Proveedor Actualizado', text: data.message, timer: 2000, showConfirmButton: false });
                    bootstrap.Modal.getInstance(this.closest('.modal')).hide();
                    updateTable();
                } else {
                    Swal.fire({ toast: true, position: 'top-end', icon: 'error', title: 'Error', text: data.error, timer: 2000, showConfirmButton: false });
                }
            })
            .catch(error => Swal.fire({ toast: true, position: 'top-end', icon: 'error', title: 'Error', text: 'Error al actualizar proveedor', timer: 2000, showConfirmButton: false }));
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
                    Swal.fire({ toast: true, position: 'top-end', icon: 'success', title: 'Proveedor Eliminado', text: data.message, timer: 2000, showConfirmButton: false });
                    bootstrap.Modal.getInstance(this.closest('.modal')).hide();
                    updateTable();
                } else {
                    Swal.fire({ toast: true, position: 'top-end', icon: 'error', title: 'Error', text: data.error, timer: 2000, showConfirmButton: false });
                }
            })
            .catch(error => Swal.fire({ toast: true, position: 'top-end', icon: 'error', title: 'Error', text: 'Error al eliminar proveedor', timer: 2000, showConfirmButton: false }));
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

    // Exportar a PDF
    window.proveedoresPDF = function() {
        const { jsPDF } = window.jspdf;
        const doc = new jsPDF();
        const filtroTexto = document.getElementById('buscadorProveedores').value.trim();

        const url = `/proveedores/todos` + (filtroTexto ? `?search=${encodeURIComponent(filtroTexto)}` : '');
        fetch(url, {
            headers: { 'X-Requested-With': 'XMLHttpRequest' }
        })
            .then(response => response.json())
            .then(data => {
                if (data.length === 0) {
                    Swal.fire({ icon: 'info', title: 'Sin datos', text: 'No hay proveedores que coincidan con el criterio de búsqueda.' });
                    return;
                }

                doc.setFontSize(18);
                doc.text('Listado de Proveedores', 14, 20);
                let yPos = 25;
                doc.setFontSize(10);
                if (filtroTexto) { doc.text(`Filtro de búsqueda: "${filtroTexto}"`, 14, yPos); yPos += 5; }

                const columns = ['ID', 'Nombre', 'Dirección', 'Teléfono', 'Email'];
                const rows = data.map(proveedor => [
                    proveedor.id,
                    proveedor.nombreProveedor,
                    proveedor.direccion,
                    proveedor.telefono,
                    proveedor.email
                ]);

                doc.autoTable({
                    head: [columns],
                    body: rows,
                    startY: yPos + 5,
                    theme: 'striped',
                    headStyles: { fillColor: [41, 128, 185], textColor: 255 },
                    styles: { fontSize: 9, cellPadding: 2.2 },
                    columnStyles: {
                        0: { cellWidth: 15 },
                        1: { cellWidth: 40 },
                        2: { cellWidth: 50 },
                        3: { cellWidth: 30 },
                        4: { cellWidth: 55 }
                    }
                });

                const totalProveedores = rows.length;
                doc.setFontSize(10);
                doc.text(`Total de proveedores: ${totalProveedores}`, 14, doc.lastAutoTable.finalY + 10);
                const fecha = new Date().toLocaleDateString();
                doc.text(`Generado el: ${fecha}`, 14, doc.lastAutoTable.finalY + 15);

                const pageCount = doc.internal.getNumberOfPages();
                for (let i = 1; i <= pageCount; i++) {
                    doc.setPage(i);
                    doc.setFontSize(8);
                    doc.text('Página ' + i + ' de ' + pageCount, doc.internal.pageSize.width - 20, doc.internal.pageSize.height - 10);
                    doc.text('Fecha de generación: ' + fecha, 20, doc.internal.pageSize.height - 10);
                }

                doc.save('listado-Proveedores.pdf');
                Swal.fire({ toast: true, position: 'top-end', icon: 'success', title: 'PDF Generado', text: `Se ha generado el PDF con ${totalProveedores} proveedores.`, timer: 2000, showConfirmButton: false });
            })
            .catch(error => Swal.fire({ icon: 'error', title: 'Error', text: 'Ocurrió un error al generar el PDF: ' + error.message }));
    };

    // Inicializar eventos
    reassignModalEvents();
});