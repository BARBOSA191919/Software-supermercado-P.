// Definir variables
const themeToggle = document.getElementById('theme-toggle');
const darkModeClass = 'dark-mode';

// Gestión del tema oscuro/claro
if (localStorage.getItem('theme') === 'dark') {
    document.body.classList.add(darkModeClass);
    themeToggle.innerHTML = '<i class="fas fa-sun"></i> Modo Claro';
} else {
    document.body.classList.remove(darkModeClass);
    themeToggle.innerHTML = '<i class="fas fa-moon"></i> Modo Oscuro';
}

themeToggle.addEventListener('click', function () {
    document.body.classList.toggle(darkModeClass);

    if (document.body.classList.contains(darkModeClass)) {
        localStorage.setItem('theme', 'dark');
        themeToggle.innerHTML = '<i class="fas fa-sun"></i> Modo Claro';
    } else {
        localStorage.setItem('theme', 'light');
        themeToggle.innerHTML = '<i class="fas fa-moon"></i> Modo Oscuro';
    }
});

// Función para abrir ventanas
function abrirVentana(url) {
    window.location.href = url;
}



$(document).ready(function() {
    // Cargar categorías al iniciar
    cargarCategoriaReporte();

    // Evento change para el filtro de categorías
    $('#categoria-filter2').change(function() {
        const categoriaId = $(this).val();
        const ordenSelect = $('#orden-filter');
        const btnImprimir = $('#btn-imprimir-pdf');

        if (categoriaId) {
            // Habilitar el select de ordenamiento si se seleccionó una categoría
            ordenSelect.prop('disabled', false);
            // Habilitar el botón de imprimir
            btnImprimir.prop('disabled', false);
        } else {
            // Deshabilitar el select de ordenamiento si no hay categoría seleccionada
            ordenSelect.prop('disabled', true);
            ordenSelect.val('');
            // Deshabilitar el botón de imprimir
            btnImprimir.prop('disabled', true);
        }
    });

    // Evento click para imprimir PDF de una categoría específica
    $('#btn-imprimir-pdf').click(function() {
        const categoriaId = $('#categoria-filter2').val();
        const ordenamiento = $('#orden-filter').val();

        if (categoriaId && ordenamiento) {
            const nombreCategoria = $('#categoria-filter2 option:selected').text();
            obtenerProductosPorCategoria(categoriaId, ordenamiento, nombreCategoria);
        } else {
            mostrarNotificacion('Seleccione una categoría y un ordenamiento', 'warning');
        }
    });

    // Evento click para imprimir todo el inventario
    $('#btn-imprimir-todo').click(function() {
        obtenerTodosLosProductos();
    });
});

function cargarCategoriaReporte() {
    const token = document.querySelector("meta[name='_csrf']").getAttribute("content");
    const header = document.querySelector("meta[name='_csrf_header']").getAttribute("content");

    fetch('/api/reporteG/categorias', {
        headers: {
            [header]: token,
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Error en la respuesta del servidor');
            }
            return response.json();
        })
        .then(categorias => {
            const selectReporteG = document.getElementById('categoria-filter2');

            // Reiniciar select
            selectReporteG.innerHTML = '<option value="">-- Seleccione una categoría --</option>';

            // Ordenar categorías por nombre
            categorias.sort((a, b) => a.nombre.localeCompare(b.nombre));

            // Agregar cada categoría al select
            categorias.forEach(categoria => {
                const option = document.createElement('option');
                option.value = categoria.id;
                option.textContent = categoria.nombre;
                selectReporteG.appendChild(option);
            });
        })
        .catch(error => {
            console.error('Error al cargar categorías:', error);
            if (typeof mostrarNotificacion === 'function') {
                mostrarNotificacion('Error al cargar las categorías', 'error');
            }
        });
}

function obtenerProductosPorCategoria(categoriaId, ordenamiento, nombreCategoria) {
    const token = document.querySelector("meta[name='_csrf']").getAttribute("content");
    const header = document.querySelector("meta[name='_csrf_header']").getAttribute("content");

    console.log("Solicitando productos para categoría:", categoriaId);

    fetch(`/api/inventario/porCategoria?categoriaId=${categoriaId}`, {
        method: 'GET',
        headers: {
            [header]: token,
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            console.log("Datos recibidos:", data);

            if (!Array.isArray(data)) {
                console.error('Los datos recibidos no son un array:', data);
                mostrarNotificacion('Formato de datos incorrecto', 'error');
                return;
            }

            if (data.length === 0) {
                mostrarNotificacion('No se encontraron productos para esta categoría', 'warning');
                return;
            }

            // Ordenar productos con validación
            const productosOrdenados = ordenarProductos(data, ordenamiento);

            // Generar el PDF con los datos ordenados
            generarPDF(productosOrdenados, `Inventario - ${nombreCategoria}`);
        })
        .catch(error => {
            console.error('Error completo:', error);
            Swal.fire({
                toast: true,
                position: "top-end",
                title: 'Error al agregar',
                text: 'Error al agregar el producto',
                icon: 'success',
                timer: 2000,
                timerProgressBar: true,
                showConfirmButton: false
            })
        });
}

function ordenarProductos(productos, ordenamiento) {
    // Validar que productos sea un array
    if (!Array.isArray(productos)) return [];

    // Crear una copia del array
    const productosClone = [...productos];

    try {
        switch (ordenamiento) {
            case "asc_nombre":
                return productosClone.sort((a, b) => {
                    return (a?.nombre || '').localeCompare(b?.nombre || '');
                });
            case "desc_nombre":
                return productosClone.sort((a, b) => {
                    return (b?.nombre || '').localeCompare(a?.nombre || '');
                });
            case "mayor_stock":
                return productosClone.sort((a, b) => {
                    return (Number(b?.existencia) || 0) - (Number(a?.existencia) || 0);
                });
            case "menor_stock":
                return productosClone.sort((a, b) => {
                    return (Number(a?.existencia) || 0) - (Number(b?.existencia) || 0);
                });
            default:
                return productosClone;
        }
    } catch (error) {
        console.error('Error al ordenar productos:', error);
        return productosClone;
    }
}

function obtenerTodosLosProductos() {
    const token = document.querySelector("meta[name='_csrf']").getAttribute("content");
    const header = document.querySelector("meta[name='_csrf_header']").getAttribute("content");

    // Hacer la solicitud para obtener todos los productos sin paginación
    fetch('/api/inventario/buscar?size=1000', {  // Ajusta 'size' según lo permita tu API
        headers: {
            [header]: token,
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Error en la respuesta del servidor');
            }
            return response.json();
        })
        .then(data => {
            console.log("Datos recibidos:", data);

            // Si los datos vienen paginados, extraemos el array de productos
            const productos = data.productos || data; // Ajusta según la estructura de la respuesta

            // Validar y transformar los datos
            const productosFormateados = productos.map(producto => {
                return {
                    codigo: producto.codigo || '',
                    nombre: producto.nombre || '',
                    categoria: {
                        nombre: producto.categoria?.nombre || producto.categoria || 'Sin categoría'
                    },
                    stock: producto.stock || producto.existencia || 0,
                    precio: producto.precio || 0
                };
            });

            // Ordenar productos por categoría y luego por nombre
            productosFormateados.sort((a, b) => {
                const catA = a.categoria?.nombre || 'Sin categoría';
                const catB = b.categoria?.nombre || 'Sin categoría';
                if (catA === catB) {
                    return (a.nombre || '').localeCompare(b.nombre || '');
                }
                return catA.localeCompare(catB);
            });

            // Generar el PDF
            generarPDF(productosFormateados, 'Inventario Completo');
        })
        .catch(error => {
            console.error('Error al obtener productos:', error);
            Swal.fire({
                toast: true,
                position: "top-end",
                title: 'Ten cuidado',
                text: 'Error al obtener productos',
                icon: 'error',
                timer: 2000,
                timerProgressBar: true,
                showConfirmButton: false
            });
        });
}

function generarPDF(datos, titulo) {
    const { jsPDF } = window.jspdf;
    const doc = new jsPDF();

    // Configurar título
    doc.setFont('helvetica', 'bold');
    doc.setFontSize(16);
    doc.setTextColor(23, 77, 113);
    doc.text(titulo, 105, 15, { align: 'center' });

    // Agregar metadatos
    doc.setFont('helvetica', 'normal');
    doc.setFontSize(10);
    doc.setTextColor(100);
    const fechaHora = new Date().toLocaleString();
    doc.text(`Fecha generación: ${fechaHora}`, 200, 22, { align: 'right' });
    doc.text(`Total productos: ${datos.length}`, 200, 28, { align: 'right' });

    // Definir encabezados
    const headers = [
        'Código',
        'Nombre',
        'Categoría',
        'Stock',
        'Precio'
    ];

    // Preparar datos para la tabla
    const tableData = datos.map(producto => [
        producto.codigo || '',
        producto.nombre || '',
        typeof producto.categoria === 'object'
            ? (producto.categoria?.nombre || 'Sin categoría')
            : (producto.categoria || 'Sin categoría'),
        (producto.stock || producto.existencia || '0').toString(),
        `$${parseFloat(producto.precio || 0).toFixed(2)}`
    ]);

    // Crear tabla
    doc.autoTable({
        startY: 35,
        head: [headers],
        body: tableData,
        theme: 'striped',
        headStyles: {
            fillColor: [36, 119, 176],
            textColor: 255,
            fontStyle: 'bold'
        },
        alternateRowStyles: {
            fillColor: [240, 240, 240]
        },
        columnStyles: {
            0: { cellWidth: 25 },     // Código
            1: { cellWidth: 60 },     // Nombre
            2: { cellWidth: 40 },     // Categoría
            3: { cellWidth: 25 },     // Stock
            4: { cellWidth: 25 }      // Precio
        },
        styles: {
            overflow: 'ellipsize',
            cellPadding: 3,
        },
        margin: { left: 15, right: 15 }
    });

    // Agregar pie de página con número de página
    const pageCount = doc.internal.getNumberOfPages();
    for (let i = 1; i <= pageCount; i++) {
        doc.setPage(i);
        doc.setFontSize(10);
        doc.setTextColor(150);
        doc.text(`Página ${i} de ${pageCount}`, 105, doc.internal.pageSize.height - 10, { align: 'center' });
    }

    // Crear una ventana modal para mostrar opciones
    Swal.fire({
        title: 'PDF Generado',
        html: `
            <div class="mb-4">¿Qué deseas hacer con el PDF?</div>
            <div class="flex justify-center gap-4">
                <button id="btn-preview" class="btn btn-primary">Ver Preview</button>
                <button id="btn-download" class="btn btn-success">Descargar</button>
            </div>
        `,
        showConfirmButton: false,
        showCloseButton: true,
        didRender: () => {
            // Botón de preview
            document.getElementById('btn-preview').addEventListener('click', () => {
                const pdfDataUri = doc.output('datauristring');
                const newWindow = window.open();
                newWindow.document.write(`
                    <iframe 
                        width="100%" 
                        height="100%" 
                        src="${pdfDataUri}" 
                        style="border: none; position: absolute; top: 0; left: 0;">
                    </iframe>
                `);
                Swal.close();
            });

            // Botón de descarga
            document.getElementById('btn-download').addEventListener('click', () => {
                doc.save(`${titulo.replace(/ /g, '_')}_${new Date().toISOString().slice(0, 10)}.pdf`);
                Swal.close();
            });
        }
    });
}




// Función auxiliar para mostrar notificaciones
function mostrarNotificacion(mensaje, tipo) {
    if (window.Swal) {
        Swal.fire({
            text: mensaje,
            icon: tipo === 'success' ? 'success' :
                tipo === 'warning' ? 'warning' :
                    tipo === 'error' ? 'error' : 'info',
            showCloseButton: true,
            timer: 3000,
            toast: true,
            position: 'top-end'
        });
    } else {
        alert(mensaje);
    }
}