
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

$(document).ready(function() {
    // Inicializar Select2 en el select de categorías
    $('#categoria').select2({
        placeholder: "Selecciona una categoría", // Texto de placeholder
        allowClear: true, // Permite limpiar la selección
        width: '100%' // Ajusta el ancho del select
    });

    // Otras inicializaciones que ya tengas
    $('#sidebarCollapse').on('click', function () {
        $('#sidebar').toggleClass('active');
    });
});

function validarFormulario() {
    let isValid = true;

    // Limpiar mensajes de error
    document.getElementById('categoriaError').style.display = 'none';

    // Validar categoría
    const categoriaSelect = document.getElementById('categoria');
    if (categoriaSelect.value === "") {
        document.getElementById('categoriaError').innerText = 'Por favor, selecciona una categoría';
        document.getElementById('categoriaError').style.display = 'block';
        categoriaSelect.focus();
        isValid = false;
    }

    // Validar nombre
    const nombreInput = document.getElementById('nombre');
    if (nombreInput.value.trim() === "") {
        showError(nombreInput, 'Por favor, ingresa el nombre del producto');
        isValid = false;
    }

    // Validar código
    const codigoInput = document.getElementById('codigo');
    if (codigoInput.value.trim() === "") {
        showError(codigoInput, 'Por favor, ingresa el código del producto');
        isValid = false;
    }

    // Validar IVA incluido
    const ivaIncluidoRadios = document.getElementsByName('ivaIncluido');
    const ivaErrorDiv = document.getElementById('ivaError');
    let ivaSeleccionado = false;

    for (let radio of ivaIncluidoRadios) {
        if (radio.checked) {
            ivaSeleccionado = true;
            break;
        }
    }

    if (!ivaSeleccionado) {
        ivaErrorDiv.textContent = 'Por favor, selecciona si el IVA está incluido';
        ivaErrorDiv.style.display = 'block';
        isValid = false;
    } else {
        ivaErrorDiv.textContent = '';
        ivaErrorDiv.style.display = 'none';
    }

    // Validar precio
    const precioInput = document.getElementById('precio');
    const precioValue = parseFloat(precioInput.value);
    if (precioInput.value.trim() === "" || precioValue <= 0) {
        showError(precioInput, 'Por favor, ingresa un precio válido (mayor que 0)');
        isValid = false;
    }

    // Validar descuento
    const descuentoInput = document.getElementById('descuento');
    const descuentoValue = parseFloat(descuentoInput.value);
    if (descuentoInput.value.trim() === "" || descuentoValue < 0 || descuentoValue > 100) {
        showError(descuentoInput, 'Por favor, ingresa un descuento válido (0 a 100)');
        isValid = false;
    }

    // Calcular el precio final basado en el descuento
    const precioFinalInput = document.getElementById('precio_final');

    if (isValid) {
        const precio = parseFloat(precioInput.value) || 0;
        const descuento = parseFloat(descuentoInput.value) || 0;
        const precioFinal = precio - (precio * (descuento / 100));
        precioFinalInput.value = precioFinal.toFixed(2);
    }

    return isValid;
}

function showError(input, message) {
    const errorDiv = document.createElement('div');
    errorDiv.className = 'error-message';
    errorDiv.style.color = 'red';
    errorDiv.innerText = message;

    // Eliminar mensajes de error anteriores
    const existingError = input.parentElement.querySelector('.error-message');
    if (existingError) {
        existingError.innerText = message;
    } else {
        input.parentElement.appendChild(errorDiv);
    }
    input.focus();
}

// otro script
document.addEventListener('DOMContentLoaded', function() {
    var existenciaInput = document.getElementById('existencia');
    existenciaInput.value = 0;
});

$(document).ready(function () {
    $('#sidebarCollapse').on('click', function () {
        $('#sidebar').toggleClass('active');
    });
});

function abrirVentana(url) {
    window.location.href = url;
}

// Obtener referencias a los elementos del formulario
const ivaIncluidoRadio = document.getElementById('ivaIncluido');
const ivaNoIncluidoRadio = document.getElementById('ivaNoIncluido');
const selectIva = document.getElementById('iva');
const inputPrecio = document.getElementById('precio');
const inputPrecioFinal = document.getElementById('precio_final');

// Función para calcular el precio final
function calcularPrecioFinal() {
    const precio = parseFloat(inputPrecio.value) || 0;
    const iva = parseFloat(selectIva.value ) / 100;
    const ivaIncluido = ivaIncluidoRadio.checked;

    if (ivaIncluido) {
        // Si el IVA está incluido, el precio final es igual al precio ingresado
        inputPrecioFinal.value = precio.toFixed(2);
    } else {
        // Si el IVA no está incluido, calculamos el precio final sumando el IVA
        const precioFinal = precio * (1 + iva);
        inputPrecioFinal.value = precioFinal.toFixed(2);
    }
}

// Agregar event listeners para los radio buttons
ivaIncluidoRadio.addEventListener('change', calcularPrecioFinal);
ivaNoIncluidoRadio.addEventListener('change', calcularPrecioFinal);

// Event listener para el select del IVA
selectIva.addEventListener('change', calcularPrecioFinal);

// Event listener para el input de precio
inputPrecio.addEventListener('input', calcularPrecioFinal);

// Event listener para el input de descuento
const descuentoInput = document.getElementById('descuento');
descuentoInput.addEventListener('input', function() {
    const descuento = parseFloat(descuentoInput.value) || 0;
    const precio = parseFloat(inputPrecio.value) || 0;

    // Asegurarse de que el descuento esté entre 0 y 100
    if (descuento < 0) {
        descuentoInput.value = 0;
    } else if (descuento > 100) {
        descuentoInput.value = 100;
    }

    const precioFinal = precio - (precio * (descuento / 100));
    inputPrecioFinal.value = precioFinal.toFixed(2);
});

// Event listener para el precio final
inputPrecioFinal.addEventListener('input', function() {
    const precioFinal = parseFloat(inputPrecioFinal.value) || 0;
    const iva = parseFloat(selectIva.value) / 100;

    if (!ivaIncluidoRadio.checked) {
        // Si el IVA no está incluido, calculamos el precio base
        inputPrecio.value = (precioFinal / (1 + iva)).toFixed(2);
    } else {
        // Si el IVA está incluido, el precio base es igual al precio final
        inputPrecio.value = precioFinal.toFixed(2);
    }
});

// Validar que el precio base no sea negativo
inputPrecio.addEventListener('input', function() {
    const precio = parseFloat(inputPrecio.value) || 0;
    if (precio < 0) {
        inputPrecio.value = 0;
    }
});

descuentoInput.addEventListener('input', function() {
    const descuento = parseFloat(descuentoInput.value) || 0;
    if (descuento < 0) {
        descuentoInput.value = 0;
    }
});


document.addEventListener('DOMContentLoaded', function() {
    const select = document.getElementById('categoria');
    const options = Array.from(select.options);

    // Ordenar las opciones alfabéticamente
    options.sort((a, b) => a.text.localeCompare(b.text));

    // Limpiar y rellenar el select
    select.innerHTML = '';
    const defaultOption = document.createElement('option');
    defaultOption.value = '';
    defaultOption.disabled = true;
    defaultOption.selected = false; // No seleccionar esta opción
    defaultOption.text = 'Selecciona una categoría';
    select.add(defaultOption);

    // Agregar las opciones ordenadas al select
    options.forEach((option, index) => {
        if (option.value !== "") { // Ignorar la opción por defecto
            select.add(option);

            // Seleccionar la primera opción alfabética
            if (index === 0) {
                option.selected = true;
            }
        }
    });

    // Inicializar Select2
    $('#categoria').select2({
        placeholder: "Selecciona una categoría",
        allowClear: true,
        width: '100%'
    });
});