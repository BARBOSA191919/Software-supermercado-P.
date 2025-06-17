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
    if (precioInput.value.trim() === "" || parseFloat(precioInput.value) <= 0) {
        showError(precioInput, 'Por favor, ingresa un precio válido');
        isValid = false;
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

// Obtener referencias a los elementos del formulario
const ivaIncluidoRadio = document.getElementById('ivaIncluido');
const ivaNoIncluidoRadio = document.getElementById('ivaNoIncluido');
const selectIva = document.getElementById('iva');
const inputPrecio = document.getElementById('precio');
const inputPrecioFinal = document.getElementById('precio_final');

// Función para calcular el precio final
function calcularPrecioFinal() {
    const precio = parseFloat(inputPrecio.value) || 0;
    const iva = parseFloat(selectIva.value) / 100;
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

// Event listener para el precio final
inputPrecioFinal.addEventListener('input', function () {
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

//otroo

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
    const iva = parseFloat(selectIva.value) / 100;
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

