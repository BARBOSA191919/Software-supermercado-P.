
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

    document.addEventListener('DOMContentLoaded', function() {
    const sidebar = document.getElementById('sidebar');
    const content = document.getElementById('content');
    const sidebarCollapse = document.getElementById('sidebarCollapse');

    function saveSidebarState(isOpen) {
    localStorage.setItem('sidebarState', isOpen ? 'open' : 'closed');
}

    const savedState = localStorage.getItem('sidebarState');
    if (savedState === 'open') {
    sidebar.classList.add('active');
    content.classList.add('active');
    sidebarCollapse.style.left = '250px';
}

    sidebarCollapse.addEventListener('click', function() {
    sidebar.classList.toggle('active');
    content.classList.toggle('active');

    if (sidebar.classList.contains('active')) {
    sidebarCollapse.style.left = '250px';
    saveSidebarState(true);
} else {
    sidebarCollapse.style.left = '0';
    saveSidebarState(false);
}
});
});

