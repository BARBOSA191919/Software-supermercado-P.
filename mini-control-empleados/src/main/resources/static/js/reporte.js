$(document).ready(function () {
    $('#sidebarCollapse').on('click', function () {
        $('#sidebar').toggleClass('active');
        localStorage.setItem('sidebarState', $('#sidebar').hasClass('active') ? 'open' : 'closed');
    });

    const sidebarState = localStorage.getItem('sidebarState');
    if (sidebarState === 'open') {
        $('#sidebar').addClass('active');
    }

    let searchTimeout = null;
    $('#codigoVenta').on('input', function () {
        clearTimeout(searchTimeout);
        searchTimeout = setTimeout(() => filtrarReportes(0), 500);
    });
    $('#fechaInicio').on('change', () => filtrarReportes(0));
    $('#fechaFin').on('change', () => filtrarReportes(0));

    filtrarReportes(0);
});

function abrirVentana(url) {
    window.location.href = url;
}

function filtrarReportes(page = 0) {
    const codigoVenta = $('#codigoVenta').val() ? $('#codigoVenta').val().trim() : '';
    const fechaInicio = $('#fechaInicio').val() || '';
    const fechaFin = $('#fechaFin').val() || '';
    const size = 10;

    let url = `/reportes?page=${page}&size=${size}`;
    if (codigoVenta) url += `&search=${encodeURIComponent(codigoVenta)}`;
    if (fechaInicio) url += `&fechaInicio=${encodeURIComponent(fechaInicio)}`;
    if (fechaFin) url += `&fechaFin=${encodeURIComponent(fechaFin)}`;

    fetch(url, {
        method: 'GET',
        headers: {
            'X-Requested-With': 'XMLHttpRequest',
            'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]').getAttribute('content')
        }
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => {
                    throw new Error(`Error ${response.status}: ${text}`);
                });
            }
            return response.text();
        })
        .then(html => {
            const parser = new DOMParser();
            const doc = parser.parseFromString(html, 'text/html');
            const newTable = doc.querySelector('#tableContent');
            $('#tableContent').html(newTable.innerHTML);

            $('.btn-link[data-toggle="collapse"]').on('click', function () {
                const target = $(this).data('target');
                $(target).collapse('toggle');
            });
        })
        .catch(error => {
            Swal.fire({ icon: 'error', title: 'Error', text: 'No se pudo cargar la tabla: ' + error.message });
        });
}

function generarPDF(codigoVenta) {
    if (!codigoVenta || codigoVenta === '') {
        Swal.fire({ icon: 'error', title: 'Error', text: 'Código de venta inválido.' });
        return;
    }

    // Expandir el colapsable en la interfaz actual para asegurarnos de que los productos estén cargados
    const collapseButton = document.querySelector(`button[data-target='#productos-${codigoVenta}']`);
    if (collapseButton) {
        collapseButton.click(); // Simula un clic para expandir
    }

    const { jsPDF } = window.jspdf;
    const doc = new jsPDF();
    const token = document.querySelector("meta[name='_csrf']").getAttribute("content");

    const url = `/reportes?page=0&size=1&search=${encodeURIComponent(codigoVenta)}`;

    fetch(url, {
        method: 'GET',
        headers: {
            'X-Requested-With': 'XMLHttpRequest',
            'X-CSRF-TOKEN': token
        },
        credentials: 'same-origin'
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => {
                    throw new Error(`Error ${response.status}: ${text}`);
                });
            }
            return response.text();
        })
        .then(html => {
            const parser = new DOMParser();
            const docHtml = parser.parseFromString(html, 'text/html');
            const rows = docHtml.querySelectorAll('#reportesBody tr:not(.collapse)');
            if (!rows.length) {
                Swal.fire({ icon: 'info', title: 'Sin datos', text: 'No se encontró la venta especificada.' });
                return;
            }

            const row = rows[0];
            const ventaData = {
                codigo: row.cells[1].textContent.trim(),
                fecha: row.cells[2].textContent.trim(),
                cliente: row.cells[3].textContent.trim(),
                vendedor: row.cells[4].textContent.trim(),
                turno: row.cells[5].textContent.trim(),
                total: row.cells[6].textContent.trim()
            };

            const collapseId = `productos-${ventaData.codigo}`;
            const productosRows = docHtml.querySelectorAll(`#${collapseId} tbody tr`);
            const productos = Array.from(productosRows).map(productoRow => [
                productoRow.cells[0].textContent.trim(),
                productoRow.cells[1].textContent.trim(),
                productoRow.cells[2].textContent.trim(),
                productoRow.cells[3].textContent.trim(),
                productoRow.cells[4].textContent.trim()
            ]);

            // Título
            doc.setFontSize(18);
            doc.text(`Factura de Venta #${ventaData.codigo}`, 14, 20);
            let yPos = 25;

            // Metadatos
            doc.setFontSize(10);
            doc.text(`Filtro de búsqueda: "${codigoVenta}"`, 14, yPos);
            yPos += 5;
            doc.text(`Fecha: ${ventaData.fecha}`, 14, yPos);
            yPos += 5;
            doc.text(`Cliente: ${ventaData.cliente}`, 14, yPos);
            yPos += 5;
            doc.text(`Vendedor: ${ventaData.vendedor}`, 14, yPos);
            yPos += 5;
            doc.text(`Turno: ${ventaData.turno}`, 14, yPos);
            yPos += 5;

            // Tabla de productos
            const columns = ['Código', 'Nombre', 'Cantidad', 'Precio Unitario', 'Total'];
            const rowsData = productos.length > 0 ? productos : [['N/A', 'No hay productos', '0', 'N/A', 'N/A']];

            doc.autoTable({
                head: [columns],
                body: rowsData,
                startY: yPos + 5,
                theme: 'striped',
                headStyles: { fillColor: [41, 128, 185], textColor: 255 },
                styles: { fontSize: 9, cellPadding: 2.2 },
                columnStyles: {
                    0: { cellWidth: 25 },
                    1: { cellWidth: 60 },
                    2: { cellWidth: 20 },
                    3: { cellWidth: 35 },
                    4: { cellWidth: 35 }
                }
            });

            // Total y fecha
            const totalY = doc.lastAutoTable.finalY + 10;
            doc.setFontSize(10);
            doc.text(`Total de la venta: ${ventaData.total}`, 14, totalY);
            const fecha = new Date().toLocaleDateString('es-CO', { year: 'numeric', month: 'long', day: 'numeric' });
            doc.text(`Generado el: ${fecha}`, 14, totalY + 5);

            // Paginación
            const pageCount = doc.internal.getNumberOfPages();
            for (let i = 1; i <= pageCount; i++) {
                doc.setPage(i);
                doc.setFontSize(8);
                doc.text('Página ' + i + ' de ' + pageCount, doc.internal.pageSize.width - 20, doc.internal.pageSize.height - 10);
                doc.text('Fecha de generación: ' + fecha, 14, doc.internal.pageSize.height - 10);
            }

            // Guardar
            doc.save(`factura_${ventaData.codigo}.pdf`);
            Swal.fire({
                toast: true,
                position: 'top-end',
                icon: 'success',
                title: 'PDF Generado',
                text: `Se ha generado el PDF para la venta ${ventaData.codigo}.`,
                timer: 2000,
                showConfirmButton: false
            });
        })
        .catch(error => {
            console.error('Error detallado:', error);
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: `No se pudo generar el PDF: ${error.message}`
            });
        });
}

// Función auxiliar para comprobar si un valor es un número válido
function esNumeroValido(valor) {
    if (valor === undefined || valor === null) return false;
    if (typeof valor === 'number') return !isNaN(valor);
    if (typeof valor === 'string') {
        // Limpiar el string de caracteres no numéricos excepto punto y signo
        const numeroLimpio = valor.replace(/[^\d.-]/g, '');
        return !isNaN(parseFloat(numeroLimpio));
    }
    return false;
}


function eliminarVenta(codigoVenta) {
    if (!codigoVenta || codigoVenta === 'N/A') {
        Swal.fire({ icon: 'error', title: 'Error', text: 'Código de venta inválido.' });
        return;
    }

    Swal.fire({
        title: '¿Estás seguro?',
        text: "No podrás revertir esta acción",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Sí, eliminar',
        cancelButtonText: 'Cancelar'
    }).then((result) => {
        if (result.isConfirmed) {
            fetch(`/reportes/eliminar/${encodeURIComponent(codigoVenta)}`, {
                method: 'POST',
                headers: {
                    'X-Requested-With': 'XMLHttpRequest',
                    'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]').getAttribute('content')
                }
            })
                .then(response => {
                    if (!response.ok) throw new Error(`Error ${response.status}: ${response.statusText}`);
                    return response.text();
                })
                .then(data => {
                    Swal.fire({ toast: true, position: 'top-end', icon: 'success', title: 'Eliminado', text: data, timer: 2000, showConfirmButton: false });
                    filtrarReportes(0);
                })
                .catch(error => {
                    Swal.fire({ icon: 'error', title: 'Error', text: 'No se pudo eliminar la venta: ' + error.message });
                });
        }
    });
}

function limpiarFiltros() {
    $('#codigoVenta').val('');
    $('#fechaInicio').val('');
    $('#fechaFin').val('');
    filtrarReportes(0);
}

function openInvoiceWindow(url) {
    // Dimensiones de la ventana emergente
    const width = 600;
    const height = 800;

    // Calcular la posición para centrar la ventana
    const left = (screen.width / 2) - (width / 2);
    const top = (screen.height / 2) - (height / 2);

    // Abrir la ventana emergente centrada
    window.open(url, 'invoiceWindow', `width=${width},height=${height},scrollbars=yes,resizable=yes,left=${left},top=${top}`);
}