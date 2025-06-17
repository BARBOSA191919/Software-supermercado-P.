// Variables globales
let categoryModal;
let categoriaActualId = null;
let isSearching = false;
let currentQuery = '';

document.addEventListener('DOMContentLoaded', function() {
    console.log('DOM cargado');
    const modalElement = document.getElementById('categoryModal');
    if (modalElement) {
        categoryModal = new bootstrap.Modal(modalElement);
        modalElement.addEventListener('show.bs.modal', function() {
            const form = document.getElementById('categoryForm');
            if (form) form.reset();
        });
    }
});

function mostrarProductos(categoriaId, page = 0) {
    isSearching = false;
    currentQuery = '';
    categoriaActualId = categoriaId;
    document.getElementById('bienvenida').classList.remove('active-section');
    document.getElementById('productos').classList.add('active-section');

    const lista = document.getElementById('productos-lista');
    lista.innerHTML = '<tr><td colspan="8" class="text-center"><div class="spinner-border" role="status"><span class="visually-hidden">Cargando...</span></div></td></tr>';

    const csrfToken = document.querySelector("meta[name='_csrf']")?.getAttribute("content");
    const csrfHeader = document.querySelector("meta[name='_csrf_header']")?.getAttribute("content");
    const headers = {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
    };
    if (csrfToken && csrfHeader) headers[csrfHeader] = csrfToken;

    fetch(`/api/categorias/${categoriaId}/productos?page=${page}&size=10`, {
        method: 'GET',
        headers: headers
    })
        .then(response => response.ok ? response.json() : Promise.reject(response.text()))
        .then(data => {
            lista.innerHTML = '';
            document.getElementById('categoria-titulo').textContent = data.nombreCategoria || 'Sin título';

            const allProducts = [
                ...(data.productos || []),
                ...(data.fruvert || []),
                ...(data.verdura || []),
                ...(data.carne || [])
            ];

            renderProductos(allProducts, lista);
            renderProductosPagination(data.totalPages, data.currentPage, categoriaId, false);
        })
        .catch(error => {
            console.error('Error:', error);
            lista.innerHTML = `<tr><td colspan="8" class="text-center text-danger">Error al cargar productos</td></tr>`;
        });
}

function buscarProductos(page = 0) {
    const query = document.getElementById('buscador-productos').value.trim();
    if (query === '' && !isSearching) return;

    isSearching = true;
    currentQuery = query;
    categoriaActualId = null;

    const lista = document.getElementById('productos-lista');
    lista.innerHTML = '<tr><td colspan="8" class="text-center"><div class="spinner-border" role="status"><span class="visually-hidden">Cargando...</span></div></td></tr>';

    document.getElementById('bienvenida').classList.remove('active-section');
    document.getElementById('productos').classList.add('active-section');
    document.getElementById('categoria-titulo').textContent = query ? `Resultados de búsqueda: "${query}"` : 'Buscar Productos';

    const csrfToken = document.querySelector("meta[name='_csrf']")?.getAttribute("content");
    const csrfHeader = document.querySelector("meta[name='_csrf_header']")?.getAttribute("content");
    const headers = {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
    };
    if (csrfToken && csrfHeader) headers[csrfHeader] = csrfToken;

    fetch(`/api/productos/buscar?query=${encodeURIComponent(query)}&page=${page}&size=10`, {
        method: 'GET',
        headers: headers
    })
        .then(response => response.ok ? response.json() : Promise.reject(response.text()))
        .then(data => {
            lista.innerHTML = '';
            renderProductos(data.productos || [], lista);
            renderProductosPagination(data.totalPages, data.currentPage, null, true);
        })
        .catch(error => {
            console.error('Error:', error);
            lista.innerHTML = `<tr><td colspan="8" class="text-center text-danger">Error al buscar productos</td></tr>`;
        });
}

function limpiarBusqueda() {
    const buscador = document.getElementById('buscador-productos');
    buscador.value = '';
    isSearching = false;
    currentQuery = '';
    categoriaActualId = null;
    document.getElementById('categoria-titulo').textContent = 'Buscar Productos';
    document.getElementById('productos-lista').innerHTML = '<tr><td colspan="8" class="text-center">Ingresa un término de búsqueda.</td></tr>';
    document.getElementById('paginacion-productos').innerHTML = '';
}

function renderProductos(productos, lista) {
    if (productos.length === 0) {
        lista.innerHTML = '<tr><td colspan="8" class="text-center">No se encontraron productos.</td></tr>';
    } else {
        productos.forEach(item => {
            lista.innerHTML += `
                <tr>
                    <td>${item.codigo || ''}</td>
                    <td>${item.nombre || ''}</td>
                    <td>${item.precio?.toFixed(2) || '0.00'}</td>
                    <td>${item.existencia || '0'}</td>
                    <td>${item.iva?.toFixed(2) || '0.00'}</td>
                    <td>${item.descuento?.toFixed(2) || '0.00'}</td>
                    <td>${item.precio_final?.toFixed(2) || '0.00'}</td>
                    <td>${item.categoria?.nombre || ''}</td>
                </tr>
            `;
        });
    }
}

function renderProductosPagination(totalPages, currentPage, categoriaId, isSearchMode) {
    const paginationDiv = document.getElementById('paginacion-productos');
    if (!paginationDiv) {
        console.error('No se encontró #paginacion-productos');
        return;
    }
    paginationDiv.innerHTML = '';

    if (totalPages <= 1) return;

    const ul = document.createElement('ul');
    ul.className = 'pagination';
    ul.innerHTML += `
        <li class="page-item ${currentPage === 0 ? 'disabled' : ''}">
            <a class="page-link" href="#" onclick="${isSearchMode ? `buscarProductos(${currentPage - 1})` : `mostrarProductos(${categoriaId}, ${currentPage - 1})`}; return false;">Anterior</a>
        </li>
    `;
    for (let i = 0; i < totalPages; i++) {
        ul.innerHTML += `
            <li class="page-item ${i === currentPage ? 'active' : ''}">
                <a class="page-link" href="#" onclick="${isSearchMode ? `buscarProductos(${i})` : `mostrarProductos(${categoriaId}, ${i})`}; return false;">${i + 1}</a>
            </li>
        `;
    }
    ul.innerHTML += `
        <li class="page-item ${currentPage === totalPages - 1 ? 'disabled' : ''}">
            <a class="page-link" href="#" onclick="${isSearchMode ? `buscarProductos(${currentPage + 1})` : `mostrarProductos(${categoriaId}, ${currentPage + 1})`}; return false;">Siguiente</a>
        </li>
    `;
    paginationDiv.appendChild(ul);
}

function guardarCategoria() {
    const form = document.getElementById('categoryForm');
    const formData = new FormData(form);
    const categoria = {
        codigo: parseInt(formData.get('codigo').trim()),
        nombre: formData.get('nombre').trim()
    };

    if (!categoria.codigo || !categoria.nombre) {
        Swal.fire({ title: 'Error', text: 'Complete todos los campos', icon: 'error' });
        return;
    }
    if (categoria.codigo < 1 || categoria.codigo > 99999) {
        Swal.fire({ toast: 'true',position: 'top-end',  title: 'Error', text: 'El código debe estar entre 1 y 99999', icon: 'error', timer:1000, showConfirmButton: false });
        return;
    }

    const csrfToken = document.querySelector("meta[name='_csrf']")?.getAttribute("content");
    const csrfHeader = document.querySelector("meta[name='_csrf_header']")?.getAttribute("content");
    const headers = {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
    };
    if (csrfToken && csrfHeader) headers[csrfHeader] = csrfToken;

    fetch('/api/categorias', {
        method: 'POST',
        headers: headers,
        body: JSON.stringify(categoria)
    })
        .then(response => response.ok ? response.json() : Promise.reject(response.text()))
        .then(nuevaCategoria => {
            if (categoryModal) categoryModal.hide();
            Swal.fire({toast:'true', position:'top-end', title: '¡Éxito!', text: 'Categoría guardada', icon: 'success', timer:1000, showConfirmButton: false});

            const lista = document.getElementById('lista-categorias');
            lista.insertAdjacentHTML('beforeend', `
            <li class="nav-item">
                <div class="d-flex justify-content-between align-items-center nav-link">
                    <a href="#" onclick="mostrarProductos(${nuevaCategoria.id}); return false;">
                        <span class="categoria-codigo">${nuevaCategoria.codigo}.</span>
                        <span class="nombre-categoria">${nuevaCategoria.nombre}</span>
                    </a>
                    <button class="btn btn-sm btn-danger" onclick="confirmarEliminarCategoria(${nuevaCategoria.id});">
                        <i class="fas fa-trash"></i>
                    </button>
                </div>
            </li>
        `);
            mostrarProductos(nuevaCategoria.id);
        })
        .catch(error => {
            console.error('Error:', error);
            Swal.fire({ title: 'Error', text: 'Error al guardar', icon: 'error' });
        });
}


// Función para eliminar categoría
function eliminarCategoria(id) {
    // Obtener token CSRF
    const csrfToken = document.querySelector("meta[name='_csrf']").getAttribute("content");
    const csrfHeader = document.querySelector("meta[name='_csrf_header']").getAttribute("content");

    fetch(`/api/categorias/${id}`, {
        method: 'DELETE',
        headers: {
            [csrfHeader]: csrfToken
        }
    })
        .then(response => {
            if (!response.ok) {
                return response.text().then(text => {
                    throw new Error(text || 'Error al eliminar la categoría');
                });
            }
            return response.text();
        })
        .then(() => {
            Swal.fire({
                toast: true,
                position: 'top-end',
                title: '¡Eliminado!',
                text: 'La categoría ha sido eliminada.',
                icon: 'error',
                timer: 1000,
                showConfirmButton: false,
            }).then(() => {
                window.location.reload();
            });
        })
        .catch(error => {
            Swal.fire({
                title: 'Error',
                text: error.message,
                icon: 'error'
            });
        });
}

// Funciones para productos
function confirmarEliminacion(id) {
    Swal.fire({
        title: '¿Estás seguro?',
        text: "¡No podrás revertir esto!",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Sí, eliminarlo',
        cancelButtonText: 'Cancelar'
    }).then((result) => {
        if (result.isConfirmed) {
            eliminarProducto(id);
        }
    });
}

// Función para confirmar eliminación de categoría
function confirmarEliminarCategoria(id) {
    Swal.fire({
        title: '¿Estás seguro?',
        text: "¡No podrás revertir esto! Se eliminarán todos los productos asociados.",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Sí, eliminar',
        cancelButtonText: 'Cancelar'
    }).then((result) => {
        if (result.isConfirmed) {
            eliminarCategoria(id);
        }
    });
}


function eliminarProducto(id) {
    // Obtener token CSRF
    const csrfToken = document.querySelector("meta[name='_csrf']").getAttribute("content");
    const csrfHeader = document.querySelector("meta[name='_csrf_header']").getAttribute("content");

    fetch(`/api/productos/${id}`, {
        method: 'DELETE',
        headers: {
            [csrfHeader]: csrfToken
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Error al eliminar el producto');
            }
            Swal.fire(
                '¡Eliminado!',
                'El producto ha sido eliminado.',
                'success'
            ).then(() => {
                // Recargar la lista de productos de la categoría actual
                if (categoriaActualId) {
                    mostrarProductos(categoriaActualId);
                }
            });
        })
        .catch(error => {
            Swal.fire({
                title: 'Error',
                text: error.message,
                icon: 'error'
            });
        });
}



// Confirmar logout
window.confirmLogout = function() {
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
};

function abrirVentana(url) {
    window.open(url);
}