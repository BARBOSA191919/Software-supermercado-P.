
    // Función para cargar y mostrar alertas desde localStorage
    function cargarAlertasDesdeLocalStorage() {
    const alertas = JSON.parse(localStorage.getItem('alertas') || '[]');
    const alertasContainer = $('#alertas-list');
    alertasContainer.empty();

    alertas.forEach((alerta, index) => {
    const alertaHTML = `
                    <tr>
                        <td>${alerta.nombre}</td>
                        <td>${alerta.codigo}</td>
                        <td>${alerta.stock} unidades </td>
                        <td>${alerta.fecha}</td>
                        <td>
                            <button class="btn btn-sm btn-danger" onclick="eliminarAlerta('${index}')">
                                <i class="fas fa-trash"></i> Eliminar
                            </button>
                        </td>
                    </tr>
                `;
    alertasContainer.append(alertaHTML);
});
}

    // Función para eliminar una alerta
    function eliminarAlerta(index) {
    const alertas = JSON.parse(localStorage.getItem('alertas') || '[]');
    alertas.splice(index, 1);
    localStorage.setItem('alertas', JSON.stringify(alertas));
    cargarAlertasDesdeLocalStorage();
}

    function exportarPDFAlertas() {
    const { jsPDF } = window.jspdf;
    const doc = new jsPDF(); // Instanciar correctamente

    // Configurar el título
    doc.setFontSize(18);
    doc.text('Listado de Alertas de Stock', 14, 20);

    // Obtener los movimientos desde localStorage
    const alertas = JSON.parse(localStorage.getItem('alertas') || '[]');

    // Configurar las columnas
    const columns = [
    'Código',
    'Nombre',
    'Stock anterior',
    'Fecha'
    ];

    // Preparar los datos de los movimientos
    const rows = alertas.map(alerta => [ // Cambiar 'alertas' a 'alerta'
    alerta.codigo,
    alerta.nombre,
    alerta.stock + ' unidades',
    alerta.fecha
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

    // Añadir pie de página con fecha
    const pageCount = doc.internal.getNumberOfPages();
    for (let i = 1; i <= pageCount; i++) {
    doc.setPage(i);
    doc.setFontSize(10);
    doc.text('Fecha de generación: ' + new Date().toLocaleDateString(),
    doc.internal.pageSize.width / 2,
    doc.internal.pageSize.height - 10,
{ align: 'center' });
}

    // Descargar el PDF
    doc.save('listado-alertas.pdf');
}

    // Modificar el botón para usar esta función
    window.exportarPDFAlertas = exportarPDFAlertas;

    // Cargar alertas al iniciar la página
    $(document).ready(function() {
    cargarAlertasDesdeLocalStorage();
});
