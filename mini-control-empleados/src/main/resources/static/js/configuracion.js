

document.addEventListener('DOMContentLoaded', () => {
    // Cargar configuración al iniciar
    const configuracion = cargarConfiguracion();
    document.getElementById('emailConfig').value = configuracion.emailDestinatario;
    document.getElementById('stockThreshold').value = configuracion.umbralStock;
    document.getElementById('enableEmailAlerts').checked = configuracion.alertasHabilitadas;

    // Cargar historial de alertas
    actualizarHistorialAlertas();
});

document.getElementById('btnEnviarEmail').addEventListener('click', function (event) {
    event.preventDefault(); // Evita que el formulario se envíe y la página se recargue

    const email = document.getElementById('emailConfig').value;

    // Validar el correo electrónico
    if (!validarEmail(email)) {
        Swal.fire({
            title: 'Error',
            text: 'Por favor, ingrese un correo electrónico válido.',
            icon: 'error',
            timer: 2000,
            timerProgressBar: true,
            showConfirmButton: false, // Opcional: quita el botón de confirmar
            toast: true,
            position: "top-end",
        });
        return; // Detener el proceso si el correo no es válido
    }

    // Obtener y loguear las alertas del localStorage
    const alertasRaw = localStorage.getItem('alertas');
    console.log('Datos raw del localStorage:', alertasRaw);

    let alertasPendientes = [];
    try {
        alertasPendientes = JSON.parse(alertasRaw || '[]');
        console.log('Alertas parseadas:', alertasPendientes);
    } catch (e) {
        console.error('Error al parsear alertas:', e);
        Swal.fire({
            title: 'Error',
            text: 'Error al leer las alertas almacenadas',
            icon: 'error',
            timer: 2000,
            timerProgressBar: true,
            showConfirmButton: false, // Opcional: quita el botón de confirmar
            toast: true,
            position: "top-end",
        });
        return;
    }

    if (alertasPendientes.length === 0) {
        Swal.fire({
            title: 'Info',
            text: 'No hay alertas pendientes para enviar.',
            icon: 'info',
            timer: 2000,
            timerProgressBar: true,
            showConfirmButton: false, // Opcional: quita el botón de confirmar
            toast: true,
            position: "top-end",

        });
        return;
    }

    // Construir el mensaje sin filtrar por estado inicialmente
    let mensaje = '🚨 Umbral de Alerta de Stock 20 unidades o menos \n\n';

    // Loguear cada alerta mientras construimos el mensaje
    alertasPendientes.forEach((alerta, index) => {
        console.log(`Procesando alerta ${index + 1}:`, alerta);

        mensaje += `Producto: ${alerta.nombre || 'No especificado'}\n`;
        mensaje += `Código: ${alerta.codigo || 'No especificado'}\n`;
        mensaje += `Stock: ${alerta.stock || '0'}\n`;
        mensaje += `Fecha: ${alerta.fecha || 'No especificada'}\n`;
        mensaje += `Estado: ${alerta.estado || 'Pendiente'}\n`;
        mensaje += '----------------------------------------\n';
    });

    console.log('Mensaje final:', mensaje);

    const data = {
        toUser: [email],
        subject: '⚠️ Alertas de Stock Pendientes',
        message: mensaje,
        isHtml: false
    };

    console.log('Datos a enviar:', data);

    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    fetch('/v1/sendMessage', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            [csrfHeader]: csrfToken
        },
        body: JSON.stringify(data)
    })
        .then(response => {
            console.log('Respuesta del servidor:', response);
            return response.json();
        })
        .then(data => {
            console.log('Datos de respuesta:', data);
            // Mostrar alerta de éxito
            Swal.fire({
                title: '¡Enviado!',
                text: 'La alerta ha sido enviada correctamente',
                icon: 'success',
                showConfirmButton: false,
                timer: 2000,
                timerProgressBar: true,
                toast: true,
                position: 'top-end'
            });

            // Actualizar la tabla de historial
            actualizarHistorialAlertas();
        })
        .catch(error => {
            console.error('Error completo:', error);
            // Mostrar alerta de error
            Swal.fire({
                title: '¡Error!',
                text: 'Hubo un problema al enviar la alerta',
                icon: 'error',
                confirmButtonText: 'Ok',
                toast: true,
                position: 'top-end'
            });
        });
});
// Función para mostrar las alertas actuales
function mostrarAlertasActuales() {
    const alertas = localStorage.getItem('alertas');
    console.log('Contenido actual de alertas en localStorage:', alertas);

    if (alertas) {
        try {
            const alertasParsed = JSON.parse(alertas);
            console.log('Alertas parseadas:', alertasParsed);

            // Mostrar en la consola cada alerta
            alertasParsed.forEach((alerta, index) => {
                console.log(`Alerta ${index + 1}:`, {
                    nombre: alerta.nombre,
                    codigo: alerta.codigo,
                    stock: alerta.stock,
                    fecha: alerta.fecha,
                    estado: alerta.estado
                });
            });
        } catch (e) {
            console.error('Error al parsear alertas:', e);
        }
    } else {
        console.log('No hay alertas en localStorage');
    }
}

// Ejecutar al cargar la página
document.addEventListener('DOMContentLoaded', () => {
    mostrarAlertasActuales();
});

function validarEmail(email) {
    const regex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/; // Expresión regular para validar correo
    return regex.test(email); // Retorna true si el correo es válido, false si no lo es
}

function cargarConfiguracion() {
    const config = JSON.parse(localStorage.getItem('alertConfig') || '{}');
    return {
        emailDestinatario: config.emailDestinatario || '',
        umbralStock: config.umbralStock || 20,
        alertasHabilitadas: config.alertasHabilitadas || false
    };
}

// Función para actualizar el historial de alertas
function actualizarHistorialAlertas() {
    const alertas = JSON.parse(localStorage.getItem('alertas') || '[]');
    const tablaBody = document.getElementById('alertHistoryBody');

    // Limpiar tabla existente
    tablaBody.innerHTML = '';

    // Poblar tabla con alertas
    alertas.forEach(alerta => {
        const fila = document.createElement('tr');
        fila.innerHTML = `
            <td>${alerta.nombre}</td>
            <td>${alerta.codigo}</td>
            <td>${alerta.stock}</td>
            <td>${alerta.fecha}</td>
            <td>${alerta.estado || 'Pendiente'}</td>
        `;
        tablaBody.appendChild(fila);
    });
}