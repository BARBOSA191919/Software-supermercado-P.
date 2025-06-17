
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
    // Definir variables
    const themeToggle = document.getElementById('theme-toggle');
    const darkModeClass = 'dark-mode';

    // Comprobar si hay un tema guardado en localStorage
    if (localStorage.getItem('theme') === 'dark') {
    document.body.classList.add(darkModeClass);
    themeToggle.innerHTML = '<i class="fas fa-sun"></i> Modo Claro';
} else {
    document.body.classList.remove(darkModeClass);
    themeToggle.innerHTML = '<i class="fas fa-moon"></i> Modo Oscuro';
}

    // Escuchar el evento click
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

<!-- Your head content -->

    function abrirVentana(url) {
    window.location.href = url;
}
