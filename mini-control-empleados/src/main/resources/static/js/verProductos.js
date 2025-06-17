// Función para calcular el precio final
function calcularPrecioFinal(productoId) {
    // Obtener los valores en tiempo real
    const precio = document.getElementById('precioEdit' + productoId);
    const descuento = document.getElementById('descuentoEdit' + productoId);
    const iva = document.getElementById('ivaEdit' + productoId);
    const precioFinalInput = document.getElementById('precioFinalEdit' + productoId);

    // Agregar event listener para cálculo en tiempo real
    precio.addEventListener('input', actualizarPrecio);
    descuento.addEventListener('input', actualizarPrecio);
    iva.addEventListener('input', actualizarPrecio);
    document.querySelectorAll('input[name="ivaIncluido"]').forEach(radio => {
        radio.addEventListener('change', actualizarPrecio);
    });

    function actualizarPrecio() {
        const precioValor = parseFloat(precio.value) || 0;
        const descuentoValor = parseFloat(descuento.value) || 0;
        const ivaValor = parseFloat(iva.value) || 0;
        const ivaIncluido = document.querySelector('input[name="ivaIncluido"]:checked')?.value === 'true';

        let precioFinal;

        // Aplicar descuento
        const precioConDescuento = precioValor * (1 - descuentoValor / 100);

        if (ivaIncluido) {
            // Si el IVA está incluido, el precio final es el precio con descuento
            precioFinal = precioConDescuento;
        } else {
            // Si el IVA no está incluido, añadir el IVA después del descuento
            precioFinal = precioConDescuento * (1 + ivaValor / 100);
        }

        // Redondear a 2 decimales y mostrar
        precioFinal = Math.round(precioFinal * 100) / 100;
        precioFinalInput.value = precioFinal;
    }

    // Calcular precio inicial
    actualizarPrecio();
}

// Inicializar calculadora para cada modal de producto
document.addEventListener('DOMContentLoaded', function() {
    const productos = document.querySelectorAll('[id^="editarProductoModal"]');
    productos.forEach(modal => {
        const productoId = modal.id.replace('editarProductoModal', '');

        // Inicializar cuando se muestre el modal
        modal.addEventListener('shown.bs.modal', function() {
            calcularPrecioFinal(productoId);
        });
    });
});

// Mostrar alertas automáticas al cargar la página
document.addEventListener('DOMContentLoaded', function() {
    const mensajeDiv = document.getElementById('mensajeAlert');
    if (mensajeDiv) {
        const mensaje = mensajeDiv.getAttribute('data-mensaje');
        const clase = mensajeDiv.getAttribute('data-clase');

        if (mensaje) {
            let icon = 'info';
            if (clase === 'success') icon = 'success';
            if (clase === 'warning') icon = 'warning';
            if (clase === 'error') icon = 'error';

            Swal.fire({
                title: clase === 'success' ? '¡Éxito!' : '¡Atención!',
                text: mensaje,
                icon: icon,
                timer: 2000,
                timerProgressBar: true,
                showConfirmButton: false, // Opcional: quita el botón de confirmar
                toast: true,
                position: "top-end",
            });
        }
    }
});

// Función para confirmar eliminación
function confirmarEliminacion(id) {
    Swal.fire({
        title: '¿Estás seguro?',
        text: "¡No podrás revertir esta acción!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#3085d6',
        confirmButtonText: 'Sí, eliminar',
        cancelButtonText: 'Cancelar',
        timerProgressBar: true,
        toast: true,
        position: "top-end",
    }).then((result) => {
        if (result.isConfirmed) {
            document.getElementById('formEliminar_' + id).submit();
        }
    });
}


// Función para mostrar mensaje después de editar
function mostrarMensajeEdicion(success) {
    if (success) {
        Swal.fire({
            title: '¡Éxito!',
            text: 'Producto actualizado correctamente',
            icon: 'success',
            timer: 2000,
            showConfirmButton: false
        });
    }
}
$(document).ready(function () {
    $('#sidebarCollapse').on('click', function () {
        $('#sidebar').toggleClass('active');
    });
});


function abrirVentana(url) {
    window.location.href = url;
}