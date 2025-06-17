document.addEventListener('DOMContentLoaded', function() {
    const searchInput = document.getElementById('searchInput');
    const products = document.getElementsByClassName('product-item');
    const productContainer = document.getElementById('productContainer');

    function filterProducts() {
        const searchTerm = searchInput.value.toLowerCase();
        const productList = Array.from(products);

        let visibleProducts = 0;

        // Aplicar filtro con animaciones
        productList.forEach(product => {
            const productId = product.getAttribute('data-id').toLowerCase();
            const productName = product.getAttribute('data-name').toLowerCase();

            if (productId.includes(searchTerm) || productName.includes(searchTerm)) {
                // Remover clase hidden si existe
                product.classList.remove('hidden');
                // Agregar clase visible para la animación de entrada
                product.classList.add('visible');
                product.style.display = 'block';
                visibleProducts++;
            } else {
                // Agregar clase hidden para la animación de salida
                product.classList.add('hidden');
                product.classList.remove('visible');
                product.style.display = 'none';
            }
        });

        // Mostrar mensaje cuando no hay resultados
        let noResultsMessage = productContainer.querySelector('.no-results');
        if (visibleProducts === 0) {
            if (!noResultsMessage) {
                noResultsMessage = document.createElement('div');
                noResultsMessage.className = 'no-results';
                noResultsMessage.textContent = 'No se encontraron productos que coincidan con la búsqueda';
                productContainer.appendChild(noResultsMessage);
            }
        } else if (noResultsMessage) {
            noResultsMessage.remove();
        }
    }

    // Agregar un pequeño retraso para evitar demasiadas actualizaciones
    let timeout = null;
    searchInput.addEventListener('input', function() {
        clearTimeout(timeout);
        timeout = setTimeout(filterProducts, 150);
    });

    // Inicializar con una clase visible en todos los productos
    Array.from(products).forEach(product => {
        product.classList.add('visible');
    });
});

function toggleSidebar() {
    const sidebar = document.getElementById('sidebar');
    const content = document.querySelector('.content');

    sidebar.classList.toggle('active');

    // Ajustar el margen del contenido cuando el sidebar está activo
    if (sidebar.classList.contains('active')) {
        content.style.marginLeft = '250px';
    } else {
        content.style.marginLeft = '0';
    }
}

// Inicializar correctamente el sidebar al cargar la página
document.addEventListener('DOMContentLoaded', function() {
    const sidebar = document.getElementById('sidebar');
    const content = document.querySelector('.content');

    // Establecer estado inicial
    if (sidebar.classList.contains('active')) {
        content.style.marginLeft = '250px';
    } else {
        content.style.marginLeft = '0';
    }
});