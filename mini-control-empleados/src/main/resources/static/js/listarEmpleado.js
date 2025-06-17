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

    // Función para actualizar la tabla y la paginación
    function updateTable(search = '', page = 0) {
        const tableUrl = `/empleados/listar?page=${page}` + (search ? `&search=${encodeURIComponent(search)}` : '');
        fetch(tableUrl, {
            headers: { 'X-Requested-With': 'XMLHttpRequest' }
        })
            .then(response => response.text())
            .then(html => {
                const parser = new DOMParser();
                const doc = parser.parseFromString(html, 'text/html');
                const newTable = doc.querySelector('#table-container');
                document.querySelector('#table-container').innerHTML = newTable.innerHTML;

                // Actualizar la paginación por separado
                const paginationUrl = `/empleados/listar?page=${page}` + (search ? `&search=${encodeURIComponent(search)}` : '') + '&fragment=pagination';
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

                        // Reasignar eventos a los nuevos botones de editar y eliminar
                        reassignModalEvents();
                    })
                    .catch(error => console.error('Error al actualizar paginación:', error));
            })
            .catch(error => console.error('Error al actualizar tabla:', error));
    }

    // Buscador
    let searchTimeout = null;
    document.getElementById('buscadorEmpleados').addEventListener('input', function() {
        clearTimeout(searchTimeout);
        searchTimeout = setTimeout(() => {
            const search = this.value.trim();
            updateTable(search);
        }, 500);
    });

    // Agregar empleado
    document.getElementById('agregarEmpleadoForm').addEventListener('submit', function(e) {
        e.preventDefault();
        fetch(this.action, {
            method: 'POST',
            body: new FormData(this),
            headers: { 'X-Requested-With': 'XMLHttpRequest' }
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    Swal.fire({ toast: true, position: 'top-end', icon: 'success', title: 'Empleado Agregado', text: data.message, timer: 2000, showConfirmButton: false });
                    bootstrap.Modal.getInstance(document.querySelector('#agregarEmpleadoModal')).hide();
                    updateTable();
                } else {
                    Swal.fire({ toast: true, position: 'top-end', icon: 'error', title: 'Error', text: data.error, timer: 2000, showConfirmButton: false });
                }
            })
            .catch(error => Swal.fire({ toast: true, position: 'top-end', icon: 'error', title: 'Error', text: 'Error al agregar empleado', timer: 2000, showConfirmButton: false }));
    });

    // Reasignar eventos a los modales de edición y eliminación
    function reassignModalEvents() {
        document.querySelectorAll('[id^="editarEmpleadoModal"] form').forEach(form => {
            form.removeEventListener('submit', handleEditSubmit);
            form.addEventListener('submit', handleEditSubmit);
        });

        document.querySelectorAll('[id^="eliminarEmpleadoModal"] .btn-danger').forEach(button => {
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
                    Swal.fire({ toast: true, position: 'top-end', icon: 'success', title: 'Empleado Actualizado', text: data.message, timer: 2000, showConfirmButton: false });
                    bootstrap.Modal.getInstance(this.closest('.modal')).hide();
                    updateTable();
                } else {
                    Swal.fire({ toast: true, position: 'top-end', icon: 'error', title: 'Error', text: data.error, timer: 2000, showConfirmButton: false });
                }
            })
            .catch(error => Swal.fire({ toast: true, position: 'top-end', icon: 'error', title: 'Error', text: 'Error al actualizar empleado', timer: 2000, showConfirmButton: false }));
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
                    Swal.fire({ toast: true, position: 'top-end', icon: 'success', title: 'Empleado Eliminado', text: data.message, timer: 2000, showConfirmButton: false });
                    bootstrap.Modal.getInstance(this.closest('.modal')).hide();
                    updateTable();
                } else {
                    Swal.fire({ toast: true, position: 'top-end', icon: 'error', title: 'Error', text: data.error, timer: 2000, showConfirmButton: false });
                }
            })
            .catch(error => Swal.fire({ toast: true, position: 'top-end', icon: 'error', title: 'Error', text: 'Error al eliminar empleado', timer: 2000, showConfirmButton: false }));
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
    window.empleadosPDF = function() {
        const { jsPDF } = window.jspdf;
        const doc = new jsPDF();
        const filtroTexto = document.getElementById('buscadorEmpleados').value.trim();

        const url = `/empleados/todos` + (filtroTexto ? `?search=${encodeURIComponent(filtroTexto)}` : '');
        fetch(url, {
            headers: { 'X-Requested-With': 'XMLHttpRequest' }
        })
            .then(response => response.json())
            .then(data => {
                if (data.length === 0) {
                    Swal.fire({ icon: 'info', title: 'Sin datos', text: 'No hay empleados que coincidan con el criterio de búsqueda.' });
                    return;
                }

                doc.setFontSize(18);
                doc.text('Listado de Empleados', 14, 20);
                let yPos = 25;
                doc.setFontSize(10);
                if (filtroTexto) {
                    doc.text(`Filtro de búsqueda: "${filtroTexto}"`, 14, yPos);
                    yPos += 5;
                }

                const columns = ['ID', 'Nombre', 'Apellido', 'Cédula', 'Teléfono'];
                const rows = data.map(empleado => [
                    empleado.id,
                    empleado.nombre,
                    empleado.apellido,
                    empleado.cedula,
                    empleado.telefono
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
                        2: { cellWidth: 40 },
                        3: { cellWidth: 40 },
                        4: { cellWidth: 40 }
                    }
                });

                const totalEmpleados = rows.length;
                doc.setFontSize(10);
                doc.text(`Total de empleados: ${totalEmpleados}`, 14, doc.lastAutoTable.finalY + 10);
                const fecha = new Date().toLocaleDateString();
                doc.text(`Generado el: ${fecha}`, 14, doc.lastAutoTable.finalY + 15);

                const pageCount = doc.internal.getNumberOfPages();
                for (let i = 1; i <= pageCount; i++) {
                    doc.setPage(i);
                    doc.setFontSize(8);
                    doc.text('Página ' + i + ' de ' + pageCount, doc.internal.pageSize.width - 20, doc.internal.pageSize.height - 10);
                    doc.text('Fecha de generación: ' + fecha, 20, doc.internal.pageSize.height - 10);
                }

                doc.save('listado-Empleados.pdf');
                Swal.fire({
                    toast: true,
                    position: 'top-end',
                    icon: 'success',
                    title: 'PDF Generado',
                    text: `Se ha generado el PDF con ${totalEmpleados} empleados.`,
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

    // Inicializar eventos
    reassignModalEvents();
});