
function cargarMovimientosDesdeLocalStorage() {
    const movimientos = JSON.parse(localStorage.getItem('movimientos') || '[]');
    const itemsPorPagina = 10;
    let paginaActual = 1;

    function mostrarPagina(pagina) {
        const movimientosContainer = $('#movimientos-list');
        movimientosContainer.empty();

        const inicio = (pagina - 1) * itemsPorPagina;
        const fin = inicio + itemsPorPagina;
        const movimientosPagina = movimientos.slice(inicio, fin);

        movimientosPagina.forEach((movimiento, index) => {
            const movimientoHTML = `
            <tr>
                <td>${movimiento.codigo}</td>
                <td>${movimiento.nombre}</td>
                <td>${movimiento.categoria}</td>
                <td>${movimiento.proveedor}</td>
                <td>${movimiento.stockAnterior}</td>
                <td>${movimiento.nuevoStock}</td>
                <td>${movimiento.fecha}</td>
                <td>
                <button class="btn btn-sm btn-danger" onclick="eliminarMovimiento(${inicio + index})">
                    <i class="fas fa-trash"></i>
                </button>
            </td>
            </tr>
        `;
            movimientosContainer.append(movimientoHTML);
        });
    }


    $(document).ready(function() {
        // Cargar los movimientos
        const movimientos = JSON.parse(localStorage.getItem('movimientos') || '[]');

        // Agregar evento al botón de exportar PDF
        $('#exportPDFBtn').on('click', function() {
            // Si solo quieres exportar el último movimiento
            if (movimientos.length > 0) {
                window.exportarMovimientoPDF(movimientos[movimientos.length - 1]);
            } else {
                alert('No hay movimientos para exportar');
            }
        });
    });

//FUNCION PARA EXPORTAR PDF DE MOVIMIENTOS
    function exportarPDFMovimientos() {
        const { jsPDF } = window.jspdf;
        const doc = new jsPDF;// Usar landscape para más espacio

        // Configurar el título
        doc.setFontSize(18);
        doc.text('Listado de Movimientos de Stock', 14, 20);

        // Obtener los movimientos desde localStorage
        const movimientos = JSON.parse(localStorage.getItem('movimientos') || '[]');

        // Configurar las columnas
        const columns = [
            'Código',
            'Nombre',
            'Categoría',
            'Proveedor',
            'Stock Anterior',
            'Nuevo Stock',
            'Fecha'
        ];

        // Preparar los datos de los movimientos
        const rows = movimientos.map(movimiento => [
            movimiento.codigo,
            movimiento.nombre,
            movimiento.categoria,
            movimiento.proveedor,
            movimiento.stockAnterior,
            movimiento.nuevoStock,
            movimiento.fecha
        ]);

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
            },
        });

        // Agregar fecha de generación
        const fecha = new Date().toLocaleDateString();
        doc.setFontSize(10);
        doc.text(`Generado el: ${fecha}`, 14, doc.lastAutoTable.finalY + 10);

        // Descargar el PDF
        doc.save('listado-movimientos.pdf');
    }

// Modificar el botón para usar esta función
    window.exportarPDFMovimientos = exportarPDFMovimientos;


    window.eliminarMovimiento = function (index) {
        let movimientos = JSON.parse(localStorage.getItem('movimientos') || '[]');
        movimientos.splice(index, 1);
        localStorage.setItem('movimientos', JSON.stringify(movimientos));
        cargarMovimientosDesdeLocalStorage();
    }



    function generarPaginacion() {
        const totalPaginas = Math.ceil(movimientos.length / itemsPorPagina);
        const paginationContainer = $('#pagination');
        paginationContainer.empty();

        // Botón Anterior
        const anteriorLi = $('<li>').addClass('page-item').addClass(paginaActual === 1 ? 'disabled' : '');
        const anteriorLink = $('<a>').addClass('page-link').attr('href', '#').text('Anterior');
        anteriorLi.append(anteriorLink);
        paginationContainer.append(anteriorLi);

        // Números de página
        for (let i = 1; i <= totalPaginas; i++) {
            const li = $('<li>').addClass('page-item').addClass(i === paginaActual ? 'active' : '');
            const link = $('<a>').addClass('page-link').attr('href', '#').text(i);
            li.append(link);
            paginationContainer.append(li);
        }

        // Botón Siguiente
        const siguienteLi = $('<li>').addClass('page-item').addClass(paginaActual === totalPaginas ? 'disabled' : '');
        const siguienteLink = $('<a>').addClass('page-link').attr('href', '#').text('Siguiente');
        siguienteLi.append(siguienteLink);
        paginationContainer.append(siguienteLi);

        // Eventos de paginación
        paginationContainer.find('.page-link').on('click', function(e) {
            e.preventDefault();
            const texto = $(this).text();

            if (texto === 'Anterior' && paginaActual > 1) {
                paginaActual--;
            } else if (texto === 'Siguiente' && paginaActual < totalPaginas) {
                paginaActual++;
            } else if (!isNaN(texto)) {
                paginaActual = parseInt(texto);
            }

            mostrarPagina(paginaActual);
            generarPaginacion();
        });
    }

    // Mostrar primera página e inicializar paginación
    mostrarPagina(paginaActual);
    generarPaginacion();
}

// Cargar movimientos al iniciar la página
$(document).ready(function() {
    cargarMovimientosDesdeLocalStorage();
});
