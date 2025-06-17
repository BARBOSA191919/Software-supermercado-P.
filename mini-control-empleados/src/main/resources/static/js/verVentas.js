
function confirmarEliminarVenta(url) {
    Swal.fire({
        title: '¿Estás seguro?',
        text: "Esta acción no se puede deshacer. Los productos se devolverán al inventario.",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#3085d6',
        confirmButtonText: 'Sí, eliminar',
        cancelButtonText: 'Cancelar',
        timer: 5000,
        timerProgressBar: true,


    }).then((result) => {
        if (result.isConfirmed) {
            window.location.href = url; // Redirige para eliminar la venta
            // Mostrar mensaje de éxito después de redirigir
            Swal.fire({
                title: '¡Eliminado!',
                text: 'La venta se ha eliminado correctamente.',
                icon: 'success',
                confirmButtonColor: '#28a745',
                timer: 3000, // Mostrar el mensaje por 3 segundos
                timerProgressBar: true,
            });

        }
    });
}

function printPreview(id) {
    if (!id) {
        console.error('El ID es indefinido');
        return;
    }

    fetch(`/ventas/facturar/${id}`)
        .then(response => response.blob())
        .then(blob => {
            // Crear URL del blob
            const pdfUrl = URL.createObjectURL(blob);
            // Abrir en nueva ventana
            const printWindow = window.open(pdfUrl, '_blank');
            if (printWindow) {
                printWindow.addEventListener('load', function () {
                    printWindow.focus();
                    printWindow.print();
                    // Liberar el URL del blob después de un tiempo
                    setTimeout(() => URL.revokeObjectURL(pdfUrl), 1000);
                });
            } else {
                alert('Por favor, permita las ventanas emergentes para esta función.');
            }
        })
        .catch(error => {
            console.error('Error al obtener el PDF:', error);
            alert('Error al generar la factura');
        });
}

$(document).ready(function () {
    $('#sidebarCollapse').on('click', function () {
        $('#sidebar').toggleClass('active');
    });
});

    function abrirVentana(url) {
    window.location.href = url;
}
