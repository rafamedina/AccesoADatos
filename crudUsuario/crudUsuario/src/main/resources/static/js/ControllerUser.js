tablaUser = document.getElementById("datosUser")
btnsalir = document.getElementById("btnSalir")
    saludo = document.getElementById("saludo")

btnsalir.addEventListener("click",()=>{
    window.location.href = '/killSession'
})

    fetch('/user/list', {
    })
        .then((response) => {
            if(response.ok){
                return response.json();
            } else {
                // CAMBIO AQUÍ: Añadimos el status para saber qué pasa
                throw new Error(`Error ${response.status}: ${response.statusText}`);
            }
        })
        .then((user) => {
            // Debug: Verás que las claves ahora coinciden con tu DTO (id, username, rol, estado...)
            console.log("DTO recibido:", user);

            // 1. Formatear la fecha
            const fecha = user.fechaCreacion ? new Date(user.fechaCreacion).toLocaleDateString() : '-';

            // 2. Lógica del estado (Usamos 'user.estado' del DTO)
            const estadoBadge = user.estado
                ? '<span class="badge bg-success">Activo</span>'
                : '<span class="badge bg-danger">Inactivo</span>';

            // 3. Pintar la tabla
            tablaUser.innerHTML = `
    <table class="table table-striped table-hover table-bordered align-middle">
        <thead class="table-dark">
            <tr>
                <th>ID</th>
                <th>Nombre</th>
                <th>Apellidos</th>
                <th>Usuario</th>
                <th>Email</th>
                <th>Rol</th>
                <th>Estado</th>
                <th>Fecha Creación</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td>${user.id}</td>
                <td>${user.nombre}</td>
                <td>${user.apellidos}</td>
                
                <td>${user.username}</td>
                
                <td>${user.email}</td>
                
                <td>${user.rol}</td>
                
                <td class="text-center">${estadoBadge}</td>
                <td>${fecha}</td>
            </tr>
        </tbody>
    </table>
`;
        saludo.innerText = `Bienvenido ${user.nombre}, Aqui tienes tus datos`;
        })
        .catch(error => {
            console.error("Error:", error);
        });

