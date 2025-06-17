document.addEventListener('DOMContentLoaded', function () {
    const form = document.querySelector('form[th\\:action="@{/vender/agregar}"]');
    if (form) {
        form.addEventListener('submit', function (e) {
            e.preventDefault();
            const formData = new FormData(form);
            const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
            const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

            fetch(form.getAttribute('action'), {
                method: 'POST',
                body: formData,
                headers: { [csrfHeader]: csrfToken }
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Error en la solicitud');
                    }
                    return response.json();
                })
                .then(data => {
                    if (data.success) {
                        actualizarCarrito(data);
                        form.reset();
                    } else {
                        Swal.fire('Error', data.message, 'error');
                    }
                })
                .catch(error => {
                    console.error('Error al agregar producto:', error);
                    Swal.fire('Error', 'Ocurrió un error al agregar el producto.', 'error');
                });
        });
    }
});

document.addEventListener('DOMContentLoaded', function () {
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
    console.log('CSRF Token:', csrfToken);
    console.log('CSRF Header:', csrfHeader);
});

function actualizarCarrito(data) {
    const tbody = document.querySelector('#carrito-table tbody');
    const subtotalContainer = document.querySelector('#subtotal-container h5');
    const totalContainer = document.querySelector('#total-container h5');

    tbody.innerHTML = '';
    data.carrito.forEach((producto, index) => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${producto.codigo}</td>
            <td>${producto.nombre}</td>
            <td>${producto.precio_final.toLocaleString('es-CO')} $</td>
            <td>${producto.iva} %</td>
            <td>${producto.descuento} %</td>
            <td>
                <div class="input-group input-group-sm">
                    <button class="btn btn-sm btn-secondary" onclick="disminuirCantidad(${index})">-</button>
                    <input type="number" value="${producto.cantidad}" class="form-control form-control-sm text-center cantidad-input" 
                           style="width: 45px;" min="1" step="1" data-indice="${index}" data-stock="${producto.existencia}">
                    <button class="btn btn-sm btn-secondary" onclick="aumentarCantidad(${index})">+</button>
                </div>
            </td>
            <td>${producto.total.toLocaleString('es-CO')} $</td>
            <td>
                <button class="btn btn-danger btn-sm" onclick="quitarProducto(${index})">
                    <i class="fa fa-trash mr-1"></i>
                </button>
            </td>
        `;
        tbody.appendChild(row);
    });
    subtotalContainer.textContent = `Subtotal: $${data.subtotal.toLocaleString('es-CO')}`;
    totalContainer.textContent = `Total: $${data.total.toLocaleString('es-CO')}`;
}

function aumentarCantidad(indice) {
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    if (typeof indice !== 'number' || indice < 0) {
        Swal.fire({
            toast: true,
            position: 'top-end',
            icon: 'error',
            title: 'Índice inválido: ' + indice,
            timer: 2000,
            showConfirmButton: false
        });
        return;
    }

    fetch(`/vender/aumentar/${indice}`, {
        method: 'POST',
        headers: { [csrfHeader]: csrfToken }
    })
        .then(response => {
            if (!response.ok) {
                return response.json().then(data => {
                    throw new Error(data.message); // Solo pasamos el mensaje del servidor
                });
            }
            return response.json();
        })
        .then(data => {
            if (data.success) {
                actualizarCarrito(data);
            } else {
                Swal.fire({
                    toast: true,
                    position: 'top-end',
                    icon: 'error',
                    title: 'Ocurrió un error al aumentar la cantidad',
                    text: data.message || 'No se pudo aumentar la cantidad.',
                    timer: 2000,
                    showConfirmButton: false
                });
            }
        })
        .catch(error => {
            console.error('Error al aumentar cantidad:', error);
            Swal.fire({
                toast: true,
                position: 'top-end',
                icon: 'error',
                title: 'Ocurrió un error al aumentar la cantidad',
                text: error.message, // Mostrará "La cantidad máxima es 2.0"
                timer: 2000,
                showConfirmButton: false
            });
        });
}
function disminuirCantidad(indice) {
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    if (typeof indice !== 'number' || indice < 0) {
        Swal.fire({
            toast: true,
            position: 'top-end',
            icon: 'error',
            title: 'Índice inválido: ' + indice,
            timer: 2000,
            showConfirmButton: false
        });
        return;
    }

    fetch(`/vender/disminuir/${indice}`, {
        method: 'POST',
        headers: { [csrfHeader]: csrfToken }
    })
        .then(response => {
            if (!response.ok) {
                return response.json().then(data => {
                    throw new Error(data.message); // Solo pasamos el mensaje del servidor
                });
            }
            return response.json();
        })
        .then(data => {
            if (data.success) {
                actualizarCarrito(data);
            } else {
                Swal.fire({
                    toast: true,
                    position: 'top-end',
                    icon: 'error',
                    title: 'Ocurrió un error al disminuir la cantidad',
                    text: data.message || 'No se pudo disminuir la cantidad.',
                    timer: 2000,
                    showConfirmButton: false
                });
            }
        })
        .catch(error => {
            console.error('Error al disminuir cantidad:', error);
            Swal.fire({
                toast: true,
                position: 'top-end',
                icon: 'error',
                title: 'Ocurrió un error al disminuir la cantidad',
                text: error.message, // Mostrará "La cantidad mínima es 1"
                timer: 2000,
                showConfirmButton: false
            });
        });
}

function cancelarVenta() {
    Swal.fire({
        title: '¿Cancelar venta?',
        text: 'Se eliminarán todos los productos del carrito.',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Sí, cancelar',
        cancelButtonText: 'No'
    }).then(result => {
        if (result.isConfirmed) {
            const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
            const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

            fetch('/vender/limpiar', {
                method: 'POST',
                headers: { [csrfHeader]: csrfToken }
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error(`Error HTTP: ${response.status}`);
                    }
                    return response.json();
                })
                .then(data => {
                    if (data.success) {
                        actualizarCarrito({ carrito: [], subtotal: 0, total: 0 });
                        Swal.fire({ toast: true, position: 'top-end', icon: 'info', title: 'Venta cancelada', timer: 2000, showConfirmButton: false });
                    } else {
                        Swal.fire('Error', data.message || 'No se pudo cancelar la venta.', 'error');
                    }
                })
                .catch(error => {
                    console.error('Error al cancelar venta:', error);
                    Swal.fire('Error', 'Ocurrió un error al cancelar la venta: ' + error.message, 'error');
                });
        }
    });
}





function quitarProducto(indice) {
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    fetch(`/vender/quitar/${indice}`, {
        method: 'POST',
        headers: { [csrfHeader]: csrfToken }
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                actualizarCarrito(data);
            } else {
                Swal.fire('Error', data.message, 'error');
            }
        })
        .catch(error => Swal.fire('Error', 'Ocurrió un error al quitar el producto.', 'error'));
}

function confirmarTerminarVenta(event) {
    event.preventDefault();

    const tabla = document.querySelector('#carrito-table tbody');
    if (!tabla || tabla.rows.length === 0) {
        Swal.fire({ toast: true, position: 'top-end', icon: 'warning', title: 'Carrito Vacío', text: 'Debe agregar productos al carrito.', timer: 2000, showConfirmButton: false });
        return false;
    }

    Swal.fire({
        title: '¿Confirmar Venta?',
        text: "¿Está seguro de que desea terminar la venta?",
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#28a745',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Sí, terminar venta',
        cancelButtonText: 'Cancelar'
    }).then(result => {
        if (result.isConfirmed) {
            Swal.fire({ title: 'Procesando...', text: 'Por favor espere...', allowOutsideClick: false, didOpen: () => Swal.showLoading() });

            const form = document.getElementById('terminarVentaForm');
            const formData = new FormData(form);
            const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
            const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

            fetch(form.action, {
                method: 'POST',
                body: formData,
                headers: { [csrfHeader]: csrfToken }
            })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        fetch(`/ventas/facturar/${data.ventaId}`)
                            .then(response => response.blob())
                            .then(blob => {
                                const url = URL.createObjectURL(blob);
                                const printIframe = document.createElement('iframe');
                                printIframe.style.position = 'absolute';
                                printIframe.style.top = '-9999px';
                                printIframe.src = url;
                                document.body.appendChild(printIframe);

                                printIframe.onload = () => {
                                    printIframe.contentWindow.print();
                                    Swal.fire({
                                        icon: 'success',
                                        title: 'Venta Exitosa',
                                        text: 'La venta se ha procesado correctamente.',
                                        confirmButtonText: 'Continuar vendiendo'
                                    }).then(() => {
                                        actualizarCarrito({ carrito: [], subtotal: 0, total: 0 });
                                        form.reset();
                                    });
                                    setTimeout(() => {
                                        document.body.removeChild(printIframe);
                                        URL.revokeObjectURL(url);
                                    }, 10000);
                                };
                            })
                            .catch(error => Swal.fire('Error', 'Error al generar la factura.', 'error'));
                    } else {
                        Swal.fire('Error', data.message, 'error');
                    }
                })
                .catch(error => Swal.fire('Error', 'Ocurrió un error al procesar la venta.', 'error'));
        }
    });
    return false;
}

document.getElementById('efectivo').addEventListener('input', function() {
    const total = parseFloat(document.getElementById('totalValue').value);
    const efectivo = parseFloat(this.value) || 0;
    if (efectivo < total) {
        document.getElementById('efectivoError').textContent = 'El efectivo es menor que el total.';
        document.getElementById('efectivoError').style.display = 'block';
    } else {
        document.getElementById('efectivoError').style.display = 'none';
    }
});



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
                confirmButtonText: 'Aceptar',
                timer: 3000,
                timerProgressBar: true
            });
        }
    }
});

<!-- Actualizar el script al final del archivo -->


document.addEventListener('DOMContentLoaded', function() {
    // Función para imprimir el estado del carrito
    const debugCarrito = () => {
        const carritoElement = document.getElementById('carrito-data');
        const debugElement = document.getElementById('debug-carrito');
        console.log('Elemento del carrito:', carritoElement);
        console.log('Valor del carrito:', carritoElement?.value);
        console.log('Debug div contenido:', debugElement?.textContent);

        // Intentar parsear el carrito
        if (carritoElement?.value) {
            try {
                const carrito = JSON.parse(carritoElement.value);
                console.log('Carrito parseado:', carrito);
                return carrito;
            } catch (e) {
                console.error('Error al parsear el carrito:', e);
                console.log('Valor raw del carrito:', carritoElement.value);
            }
        }
        return null;
    };

    // Función para obtener el carrito de la sesión
    const obtenerCarrito = () => {
        try {
            // Primero intentar obtener del input hidden
            const carritoElement = document.getElementById('carrito-data');
            if (carritoElement?.value) {
                return JSON.parse(carritoElement.value);
            }

            // Si no hay datos en el input, intentar obtener del debug div
            const debugElement = document.getElementById('debug-carrito');
            if (debugElement?.textContent) {
                return JSON.parse(debugElement.textContent);
            }

            return null;
        } catch (e) {
            console.error('Error al obtener el carrito:', e);
            return null;
        }
    };

    // Agregar evento de clic al botón "Actualizar"
    const actualizarBtn = document.getElementById('btn-actualizar');
    if (actualizarBtn) {
        // Debug inicial
        console.log('Estado inicial del carrito:');
        debugCarrito();

        actualizarBtn.addEventListener('click', () => {
            const ventaIdInput = document.getElementById('venta-id');
            const efectivoInput = document.getElementById('efectivo');
            const clienteSelect = document.getElementById('cliente');

            // Debug antes de validaciones
            console.log('Estado del carrito antes de validaciones:');
            const carrito = obtenerCarrito();
            console.log('Carrito obtenido:', carrito);

            // Validaciones
            if (!ventaIdInput?.value) {
                console.error('No se encontró el input de ventaId o está vacío');
                alert('No se encontró el ID de la venta');
                return;
            }

            if (!efectivoInput?.value) {
                alert('Por favor, ingrese el monto en efectivo');
                return;
            }

            if (!clienteSelect?.value) {
                alert('Por favor, seleccione un cliente');
                return;
            }

            if (!carrito || carrito.length === 0) {
                console.error('Carrito vacío o inválido:', carrito);
                alert('El carrito está vacío. Por favor, agregue productos.');
                return;
            }

            // Debug antes de enviar
            console.log('Datos a enviar:', {
                ventaId: parseInt(ventaIdInput.value),
                efectivo: parseFloat(efectivoInput.value),
                clienteId: parseInt(clienteSelect.value),
                carrito: carrito
            });

            // Enviar actualización al servidor
            fetch('/vender/actualizar', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    ventaId: parseInt(ventaIdInput.value),
                    efectivo: parseFloat(efectivoInput.value),
                    clienteId: parseInt(clienteSelect.value),
                    carrito: carrito
                })
            })
                .then(response => {
                    if (!response.ok) {
                        return response.text().then(text => {
                            console.error('Error response:', text);
                            throw new Error('Error en la respuesta del servidor');
                        });
                    }
                    return response.json();
                })
                .then(data => {
                    if (data.success) {
                        alert('Venta actualizada correctamente.');
                        window.location.href = '/vender';
                    } else {
                        alert(data.message || 'Error al actualizar la venta.');
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('Ocurrió un error al actualizar la venta: ' + error.message);
                });
        });
    }
});



//Buscar producto
function buscarProductos(query) {
    const resultadosDiv = document.getElementById("resultados");

    if (query.length === 0) {
        resultadosDiv.classList.remove('show');
        setTimeout(() => {
            resultadosDiv.style.display = "none";
        }, 300);
        return;
    }

    fetch(`/vender/buscar?query=${encodeURIComponent(query)}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Error en la respuesta de la búsqueda');
            }
            return response.json();
        })
        .then(data => {
            resultadosDiv.innerHTML = "";

            if (data.length > 0) {
                data.forEach(producto => {
                    const productoElement = document.createElement("a");
                    productoElement.className = "list-group-item list-group-item-action";

                    const precioFormateado = new Intl.NumberFormat('es-CO', {
                        useGrouping: true,
                        minimumFractionDigits: 0,
                        maximumFractionDigits: 0
                    }).format(producto.precio_final);

                    productoElement.innerHTML = `
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <div class="producto-nombre">${producto.nombre}</div>
                            <div class="producto-codigo">
                                <i class="fas fa-barcode mr-1"></i>
                                ${producto.codigo}
                            </div>
                        </div>
                        <div class="producto-precio">
                            $${precioFormateado}
                        </div>
                    </div>
                `;

                    productoElement.onclick = () => {
                        const inputCodigo = document.getElementById("codigoNombre");
                        inputCodigo.value = producto.codigo;
                        productoElement.classList.add('active');
                        resultadosDiv.classList.remove('show');
                        setTimeout(() => {
                            resultadosDiv.style.display = "none";
                        }, 300);

                        const form = inputCodigo.closest('form');
                        const formData = new FormData(form);
                        const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
                        const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

                        if (!csrfToken || !csrfHeader) {
                            console.error('CSRF Token o Header no encontrados');
                            Swal.fire('Error', 'No se pudo obtener la configuración CSRF.', 'error');
                            return;
                        }

                        fetch(form.getAttribute('action'), {
                            method: 'POST',
                            body: formData,
                            headers: { [csrfHeader]: csrfToken }
                        })
                            .then(response => {
                                if (!response.ok) {
                                    throw new Error(`Error HTTP: ${response.status}`);
                                }
                                return response.json();
                            })
                            .then(data => {
                                if (data.success) {
                                    actualizarCarrito(data);
                                    form.reset();
                                } else {
                                    Swal.fire('Error', data.message, 'error');
                                    inputCodigo.value = ''; // Limpiar el input si hay error (ej. producto agotado)
                                }
                            })
                            .catch(error => {
                                console.error('Error al agregar producto:', error);
                                Swal.fire({ toast: true, position: 'top-end', icon: 'warning', title: 'Producto agotado, revisa el stock ', text: data.message, timer: 2000, showConfirmButton: false });
                                inputCodigo.value = ''; // Limpiar el input en caso de error
                            });
                    };

                    resultadosDiv.appendChild(productoElement);
                });

                resultadosDiv.style.display = "block";
                setTimeout(() => {
                    resultadosDiv.classList.add('show');
                }, 10);
            } else {
                const noResultados = document.createElement("div");
                noResultados.className = "list-group-item text-center text-muted";
                noResultados.innerHTML = `
                <i class="fas fa-search mr-2"></i>
                No se encontraron productos
            `;
                resultadosDiv.appendChild(noResultados);
                resultadosDiv.style.display = "block";
                setTimeout(() => {
                    resultadosDiv.classList.add('show');
                }, 10);
            }
        })
        .catch(error => {
            console.error('Error al buscar productos:', error);
            resultadosDiv.innerHTML = `
            <div class="list-group-item text-center text-danger">
                <i class="fas fa-exclamation-circle mr-2"></i>
                Error al buscar productos
            </div>
        `;
            resultadosDiv.style.display = "block";
            resultadosDiv.classList.add('show');
        });
}


// Agregar evento para cerrar resultados al hacer clic fuera
document.addEventListener('click', function(event) {
    const resultadosDiv = document.getElementById("resultados");
    const searchInput = document.getElementById("codigoNombre");

    if (!resultadosDiv.contains(event.target) && event.target !== searchInput) {
        resultadosDiv.classList.remove('show');
        setTimeout(() => {
            resultadosDiv.style.display = "none";
        }, 300);
    }
});

function confirmLogout() {
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
}



function calcularCambio() {
    const totalElement = document.getElementById('totalValue');
    const efectivoElement = document.getElementById('efectivo');
    const cambioElement = document.getElementById('cambio');
    const errorElement = document.getElementById('efectivoError');

    if (!totalElement || !efectivoElement || !cambioElement || !errorElement) {
        console.error('No se encontraron los elementos total, efectivo, cambio o error');
        return;
    }

    const totalText = document.querySelector('#total-container h5').textContent; // "Total: $123,456"
    const total = parseFloat(totalText.replace(/[^0-9,-]+/g, '').replace(',', '.')) || 0;
    const efectivo = parseFloat(efectivoElement.value) || 0;

    if (efectivoElement.value === '' || efectivo === 0) {
        // Si el campo está vacío o es 0, no mostrar error ni calcular cambio
        errorElement.style.display = 'none';
        cambioElement.value = '';
    } else if (efectivo < total) {
        errorElement.textContent = 'El efectivo es menor al total. Ingrese un monto suficiente.';
        errorElement.style.display = 'block';
        cambioElement.value = '0'; // Sin decimales
    } else {
        errorElement.style.display = 'none';
        const cambio = Math.floor(efectivo - total); // Redondear hacia abajo a entero
        cambioElement.value = cambio; // Mostrar solo el entero
    }
}

// Evento para recalcular al ingresar efectivo
document.addEventListener('DOMContentLoaded', function () {
    const efectivoInput = document.getElementById('efectivo');
    if (efectivoInput) {
        efectivoInput.addEventListener('input', calcularCambio);
    }
});

// Actualizar el evento para que se dispare con cada cambio
document.addEventListener('DOMContentLoaded', function () {
    const efectivoInput = document.getElementById('efectivo');
    if (efectivoInput) {
        efectivoInput.addEventListener('input', calcularCambio);
    }
});
// Agregar evento al input de efectivo
document.addEventListener('DOMContentLoaded', function () {
    const efectivoInput = document.getElementById('efectivo');
    if (efectivoInput) {
        efectivoInput.addEventListener('input', calcularCambio);
    }
});

function validarEfectivo() {
    // Leer el valor del input oculto
    var total = parseFloat(document.getElementById('totalValue').value);
    var efectivo = parseFloat(document.getElementById('efectivo').value) || 0;
    var errorDiv = document.getElementById('efectivoError');

    // Limpiar mensaje de error
    errorDiv.style.display = 'none';
    errorDiv.innerHTML = '';

    if (efectivo < total) {
        errorDiv.innerHTML = 'El efectivo ingresado es menor que el total de la venta. Por favor, ingrese un monto válido.';
        errorDiv.style.display = 'block'; // Mostrar el mensaje de error
        return false; // No se envía el formulario
    }

    return true; // Se envía el formulario
}

function actualizarCantidad(input) {
    const cantidad = parseFloat(input.value);
    const indice = input.getAttribute('data-indice');
    const cantidadOriginal = input.defaultValue;

    // Validar que la cantidad sea un número válido y mayor que 0
    if (isNaN(cantidad) || cantidad <= 0) {
        Swal.fire({
            icon: 'warning',
            title: 'Cantidad inválida',
            text: 'Por favor, ingrese una cantidad mayor a 0.',
        });
        input.value = cantidadOriginal;
        return;
    }

    // Hacer la petición AJAX para actualizar la cantidad
    fetch(`/vender/actualizar/${indice}/${cantidad}`, {
        method: 'GET',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        },
    })
        .then((response) => {
            if (response.ok) {
                return response.json(); // Leer la respuesta como JSON
            } else if (response.status === 400) {
                return response.text().then((message) => {
                    throw new Error(message); // Lanza el error con el mensaje recibido
                });
            } else {
                throw new Error('Error al actualizar la cantidad');
            }
        })
        .then((data) => {
            Swal.fire({
                icon: 'success',
                title: '¡Cantidad actualizada!',
                text: data.mensaje,
                timer: 1500, // Mostrar el mensaje por 1.5 segundos
                timerProgressBar: true,
            }).then(() => {
                // Recargar la página después de cerrar la alerta
                window.location.reload();
            });
        })
        .catch((error) => {
            console.error('Error:', error.message);
            Swal.fire({
                icon: 'error',
                title: 'Stock insuficiente',
                text: error.message,
            });

            // Revertir a la cantidad original si hay un error
            input.value = cantidadOriginal;
        });
}

// Función para validar entrada de teclado
function validarEntradaNumerica(event) {
    // Permitir solo números y teclas de control
    if (!/[\d\.]/.test(event.key) &&
        event.key !== 'Backspace' &&
        event.key !== 'Delete' &&
        event.key !== 'ArrowLeft' &&
        event.key !== 'ArrowRight' &&
        event.key !== 'Tab' &&
        event.key !== 'Enter') {
        event.preventDefault();
    }

    // Si ya hay un punto decimal y se intenta agregar otro, prevenir
    if (event.key === '.' && event.target.value.includes('.')) {
        event.preventDefault();
    }
}

// Función para manejar el evento blur (cuando el input pierde el foco)
function validarCantidadOnBlur(input) {
    var cantidad = parseFloat(input.value);

    if (isNaN(cantidad) || cantidad <= 0 || !Number.isInteger(cantidad)) {
        input.value = input.defaultValue;
        Swal.fire({
            icon: 'warning',
            title: 'Cantidad inválida',
            text: 'La cantidad debe ser un número entero mayor a 0'
        });
    }
}

// Inicializar los eventos cuando el documento esté listo
document.addEventListener('DOMContentLoaded', function () {
    const cantidadInputs = document.querySelectorAll('.cantidad-input');

    cantidadInputs.forEach((input) => {
        // Validar y actualizar al presionar Enter
        input.addEventListener('keypress', function (event) {
            if (event.key === 'Enter') {
                event.preventDefault();
                actualizarCantidad(this);
            }
        });

        // Validar entrada de teclado
        input.addEventListener('keydown', validarEntradaNumerica);

        // Validar cuando el input pierde el foco
        input.addEventListener('blur', function() {
            validarCantidadOnBlur(this);
        });

        // Manejar el evento Enter
        input.addEventListener('keypress', function(event) {
            if (event.key === 'Enter') {
                event.preventDefault();
                actualizarCantidad(this);
            }
        });
    });
});


function toggleSidebar() {
    const sidebar = $('#sidebar');
    sidebar.toggleClass('active'); // Cambia el estado del sidebar
    const isActive = sidebar.hasClass('active');

    // Guardar el estado en localStorage
    localStorage.setItem('sidebarState', isActive ? 'open' : 'closed');

    // Evitar cualquier acción de recarga o navegación
    return false; // Asegura que no se propague ningún evento
}
$(document).ready(function () {
    // Evento para el botón de colapsar sidebar
    $('#sidebarCollapse').on('click', toggleSidebar);

    // Verificar el estado guardado al cargar la página
    const sidebarState = localStorage.getItem('sidebarState');
    if (sidebarState === 'open') {
        $('#sidebar').addClass('active'); // Mantener el sidebar abierto
    } else {
        $('#sidebar').removeClass('active'); // Sidebar cerrado
    }
});

function abrirVentana(url) {
    // Carga el contenido dinámicamente sin recargar la página
    fetch(url)
        .then((response) => response.text())
        .then((html) => {
            const parser = new DOMParser();
            const doc = parser.parseFromString(html, 'text/html');

            // Solo reemplaza el contenido del contenedor principal
            const newContent = doc.querySelector('#content').innerHTML;
            document.querySelector('#content').innerHTML = newContent;
        })
        .catch((error) => console.error('Error al cargar la página:', error));
}

const formAgregar = document.querySelector('#agregarDevolucionModal form');
// Alerta para agregar devolución
if (formAgregar) {
    formAgregar.addEventListener('submit', function(e) {
        e.preventDefault();

        fetch(this.action, {
            method: 'POST',
            body: new FormData(this)
        })
            .then(response => {
                if (response.ok) {
                    Swal.fire({
                        toast: true,
                        position: "top-end",
                        title: 'Devolución Agregada',
                        text: 'La devolución se ha agregado correctamente.',
                        icon: 'success',
                        timer: 2000,
                        timerProgressBar: true,
                        showConfirmButton: false
                    });

                    const modal = bootstrap.Modal.getInstance(document.querySelector('#agregarDevolucionModal'));
                    modal.hide();
                    setTimeout(() => {
                        window.location.reload();
                        applySidebarState(); // Mantener el estado del sidebar después de recargar
                    }, 2000);
                }
            })
            .catch(error => {
                Swal.fire({
                    toast: true,
                    position: "top-end",
                    title: 'Error',
                    text: 'Hubo un error al agregar la devolución.',
                    icon: 'error',
                    timer: 2000,
                    timerProgressBar: true,
                    showConfirmButton: false
                });
            });
    });
}



// Función para buscar productos por venta
function buscarProductosPorVenta(idVenta) {
    fetch(`/devoluciones/buscarProductosPorVenta?idVenta=${idVenta}`)
        .then(response => response.json())
        .then(data => {
            const resultadosDiv = document.getElementById("resultadosBusquedaVenta");
            resultadosDiv.innerHTML = '';

            if (data.length === 0) {
                // Usar SweetAlert en lugar de alertas HTML
                Swal.fire({
                    toast: true,
                    position: "top-end",
                    icon: 'warning',
                    title: 'Sin resultados',
                    text: 'No se encontraron productos para esta venta.',
                    timer: 1000,
                    showConfirmButton: false,
                    timerProgressBar: true
                });
                return;
            }

            // Crear contenedor de resultados
            const listGroup = document.createElement("div");
            listGroup.className = "list-group";
            resultadosDiv.appendChild(listGroup);

            // Mostrar los productos
            data.forEach(producto => {
                const item = document.createElement("div");
                item.className = "list-group-item list-group-item-action";
                item.innerHTML = `<strong>${producto.nombre}</strong> (Código: ${producto.codigo})<br>
                                     Precio: $${producto.precio.toFixed(2)} - Cantidad: ${producto.cantidad}`;
                item.addEventListener('click', () => {
                    // Autocompletar campos
                    document.getElementById("codigoProductoAgregar").value = producto.codigo;
                    document.getElementById("nombreProductoAgregar").value = producto.nombre;
                    document.getElementById("precioUnitarioAgregar").value = producto.precio;
                    document.getElementById("cantidadDevolucionAdd").value = 1; // Default a 1

                    // Calcular precio final
                    calcularPrecioFinal(
                        "cantidadDevolucionAdd",
                        "precioUnitarioAgregar",
                        "precioFinalAgregar"
                    );

                    // Usar SweetAlert en lugar de alertas HTML
                    Swal.fire({
                        toast: true,
                        position: "top-end",
                        icon: 'success',
                        title: 'Seleccionado',
                        text: 'Producto seleccionado correctamente.',
                        timer: 1000,
                        showConfirmButton: false,
                        timerProgressBar: true
                    });

                    // Limpiar resultados después de seleccionar
                    resultadosDiv.innerHTML = '';
                });
                listGroup.appendChild(item);
            });
        })
        .catch(error => {
            console.error("Error al buscar productos por venta:", error);
            // Usar SweetAlert para mostrar errores
            Swal.fire({
                toast: true,
                position: "top-end",
                icon: 'error',
                title: 'error',
                text: 'Error con el servidor.',
                timer: 1000,
                showConfirmButton: false,
                timerProgressBar: true
            });
        });
}

// Event Listener para el campo de ID de venta
document.getElementById("idVenta").addEventListener('input', function() {
    const idVenta = this.value.trim();
    if (idVenta) {
        buscarProductosPorVenta(idVenta);
    } else {
        // Limpiar resultados si se borra el ID
        document.getElementById("resultadosBusquedaVenta").innerHTML = '';
    }
});

function buscarProductosDevolucion(modal) {
    const cantidadSuffix = modal === 'agregar' ? 'Add' : 'Edit';
    const prefix = modal === 'agregar' ? 'Agregar' : 'Edit';

    const idVentaInput = document.getElementById('idVenta');
    const resultadosDiv = document.getElementById('resultadosBusquedaVenta');

    if (!idVentaInput || !resultadosDiv) {
        console.error('No se encontraron elementos para la búsqueda por venta.');
        return;
    }

    idVentaInput.addEventListener('input', function() {
        const idVenta = this.value.trim();

        if (idVenta.length > 0) {
            fetch(`/devoluciones/buscarPorVenta?idVenta=${idVenta}`)
                .then(response => response.json())
                .then(data => {
                    resultadosDiv.innerHTML = '';

                    if (data.length === 0) {
                        // Usar SweetAlert
                        Swal.fire({
                            toast: true,
                            position: "top-end",
                            icon: 'warning',
                            title: 'Sin resultados',
                            text: 'No se encontraron productos para esta venta.',
                            timer: 1000,
                            showConfirmButton: false,
                            timerProgressBar: true
                        });
                        return;
                    }

                    const listGroup = document.createElement('div');
                    listGroup.className = 'list-group';
                    resultadosDiv.appendChild(listGroup);

                    data.forEach(producto => {
                        const item = document.createElement('div');
                        item.className = 'list-group-item list-group-item-action';
                        item.innerHTML = `<strong>${producto.nombre}</strong> (Código: ${producto.codigo}) - Precio: $${producto.precio}`;

                        item.addEventListener('click', () => {
                            document.getElementById(`codigoProducto${prefix}`).value = producto.codigo;
                            document.getElementById(`nombreProducto${prefix}`).value = producto.nombre;
                            document.getElementById(`precioUnitario${prefix}`).value = producto.precio;

                            const cantidadInput = document.getElementById(`cantidadDevolucion${cantidadSuffix}`);
                            if (cantidadInput && !cantidadInput.value) {
                                cantidadInput.value = 1;
                            }

                            // Calcular precio final después de seleccionar el producto
                            calcularPrecioFinal(
                                `cantidadDevolucion${cantidadSuffix}`,
                                `precioUnitario${prefix}`,
                                `precioFinal${prefix}`
                            );

                            // Usar SweetAlert
                            Swal.fire({
                                toast: true,
                                position: "top-end",
                                icon: 'success',
                                title: 'Seleccionado',
                                text: 'Producto seleccionado correctamente.',
                                timer: 1000,
                                showConfirmButton: false,
                                timerProgressBar: true
                            });

                            // Limpiar resultados
                            resultadosDiv.innerHTML = '';
                        });

                        listGroup.appendChild(item);
                    });
                })
                .catch(error => {
                    console.error('Error al buscar productos por venta:', error);
                    // Usar SweetAlert para errores
                    Swal.fire({
                        toast: true,
                        position: "top-end",
                        icon: 'warning',
                        title: 'warning',
                        text: 'Conectando con el servidor.',
                        timer: 1000,
                        showConfirmButton: false,
                        timerProgressBar: true
                    });
                });
        } else {
            resultadosDiv.innerHTML = '';
        }
    });
}

// Función para calcular el precio final en tiempo real
function calcularPrecioFinal(cantidadInputId, precioUnitarioInputId, precioFinalInputId) {
    const cantidad = parseFloat(document.getElementById(cantidadInputId).value) || 0;
    const precioUnitario = parseFloat(document.getElementById(precioUnitarioInputId).value) || 0;
    const precioFinal = cantidad * precioUnitario;
    document.getElementById(precioFinalInputId).value = precioFinal.toFixed(2);
}

// Agregar event listeners para calcular precio en tiempo real cuando cambie la cantidad
document.addEventListener('DOMContentLoaded', function() {
    console.log('Inicializando funciones de devolución...');

    // Inicializar búsqueda
    buscarProductosDevolucion('agregar');

    // Agregar event listener al campo de cantidad para cálculos en tiempo real
    const cantidadInput = document.getElementById('cantidadDevolucionAdd');
    if (cantidadInput) {
        cantidadInput.addEventListener('input', function() {
            calcularPrecioFinal(
                "cantidadDevolucionAdd",
                "precioUnitarioAgregar",
                "precioFinalAgregar"
            );
        });
    }

    // Establecer fecha actual por defecto
    const fechaInput = document.getElementById('fechaDevolucionAdd');
    if (fechaInput) {
        const hoy = new Date();
        const fechaFormateada = hoy.toISOString().split('T')[0];
        fechaInput.value = fechaFormateada;
    }

    // Añadir cálculo inicial al cargar el modal
    const agregarModal = document.getElementById('agregarDevolucionModal');
    if (agregarModal) {
        agregarModal.addEventListener('shown.bs.modal', function() {
            calcularPrecioFinal(
                "cantidadDevolucionAdd",
                "precioUnitarioAgregar",
                "precioFinalAgregar"
            );
        });
    }
});

// Funciones para manejar las selecciones
function guardarSelecciones() {
    const clienteId = document.getElementById('cliente').value;
    const turnoId = document.getElementById('turno').value;
    localStorage.setItem('selectedClienteId', clienteId);
    localStorage.setItem('selectedTurnoId', turnoId);
}

function restaurarSelecciones() {
    const clienteId = localStorage.getItem('selectedClienteId');
    const turnoId = localStorage.getItem('selectedTurnoId');

    if (clienteId) {
        document.getElementById('cliente').value = clienteId;
    }
    if (turnoId) {
        document.getElementById('turno').value = turnoId;
    }
}

// Modificar la función de registro de cliente
$(document).ready(function() {
    // Restaurar selecciones al cargar la página
    restaurarSelecciones();

    // Guardar selecciones cuando cambien
    $('#cliente, #turno').on('change', guardarSelecciones);

    // Manejar el formulario de cliente
    $('#clienteForm').on('submit', function(e) {
        e.preventDefault();

        $.ajax({
            type: 'POST',
            url: $(this).attr('action'),
            data: $(this).serialize(),
            success: function(response) {
                $('#clienteModal').modal('hide');

                // Actualizar la lista de clientes sin recargar
                $.get('/clientes/lista', function(data) {
                    const clienteSelect = $('#cliente');
                    const currentValue = clienteSelect.val();

                    clienteSelect.empty();
                    clienteSelect.append('<option value="">Seleccione Cliente</option>');

                    data.forEach(function(cliente) {
                        const option = new Option(
                            cliente.nombreCliente + ' ' + cliente.apellido,
                            cliente.id,
                            false,
                            cliente.id == currentValue
                        );
                        clienteSelect.append(option);
                    });
                });

                Swal.fire({
                    toast: true,
                    position: "top-end",
                    icon: 'success',
                    title: '¡Cliente registrado!',
                    text: 'El cliente ha sido registrado exitosamente',
                    showConfirmButton: false,
                    timer: 1000,
                    timerProgressBar: true
                }).then(() => {
                    $('#clienteForm')[0].reset();
                });
            },
            error: function(error) {
                Swal.fire({
                    toast: true,
                    position: "top-end",
                    icon: 'error',
                    title: 'Error',
                    text: 'No se pudo registrar el cliente. Por favor, intente nuevamente.',
                    showConfirmButton: false,
                    timer: 1000,
                    timerProgressBar: true
                });
            }
        });
    });
});


// Eliminar los eventos duplicados y unificar en uno solo
document.addEventListener('DOMContentLoaded', function () {
    // Función para agregar eventos a los inputs de cantidad
    function agregarEventosCantidad() {
        const cantidadInputs = document.querySelectorAll('.cantidad-input');
        cantidadInputs.forEach(input => {
            input.addEventListener('keypress', function (e) {
                if (e.key === 'Enter') {
                    e.preventDefault();
                    const indice = this.getAttribute('data-indice');
                    const nuevaCantidad = parseFloat(this.value);
                    const stock = parseFloat(this.getAttribute('data-stock'));

                    if (isNaN(nuevaCantidad) || nuevaCantidad < 1) {
                        Swal.fire('Error', 'La cantidad debe ser mayor a 0.', 'error');
                        this.value = 1; // Restaurar a valor mínimo
                        return;
                    }

                    if (nuevaCantidad > stock) {
                        Swal.fire('Error', `No hay suficiente stock. Máximo disponible: ${stock}`, 'error');
                        this.value = stock; // Limitar al stock disponible
                        return;
                    }

                    actualizarCantidadManual(indice, nuevaCantidad);
                }
            });
        });
    }

    // Llamar inicialmente por si hay inputs al cargar la página
    agregarEventosCantidad();

    // Volver a agregar eventos después de actualizar el carrito
    const originalActualizarCarrito = actualizarCarrito;
    actualizarCarrito = function(data) {
        originalActualizarCarrito(data);
        agregarEventosCantidad(); // Reaplicar eventos a los nuevos inputs
    };
});

function actualizarCantidadManual(indice, nuevaCantidad) {
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

    fetch(`/vender/aumentar/${indice}?cantidad=${nuevaCantidad}`, {
        method: 'POST',
        headers: { [csrfHeader]: csrfToken }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`Error HTTP: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            if (data.success) {
                actualizarCarrito(data);
            } else {
                Swal.fire('Error', data.message || 'No hay suficiente stock o cantidad inválida.', 'error');
            }
        })
        .catch(error => {
            console.error('Error al actualizar cantidad:', error);
            Swal.fire('Error', 'Ocurrió un error al actualizar la cantidad: ' + error.message, 'error');
        });
}