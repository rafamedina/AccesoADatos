btnsalir = document.getElementById("btnSalir")
btnVolver = document.getElementById("btnVolver")
btnVerUsuarios = document.getElementById("btnVerUsuarios")
datosUsuarios = document.getElementById("datosUsuarios")

fetch("/admin")


btnsalir.addEventListener("click",()=>{
    window.location.href = '/killSession'
})

btnVerUsuarios.addEventListener("click", ()=>{
    btnVerUsuarios.style.display="none";
    fetch("admin/cargarUsuarios",{

    }).then((response) => {
        if(response.ok){
            return response.json();
        } else {
            // CAMBIO AQUÍ: Añadimos el status para saber qué pasa
            throw new Error(`Error ${response.status}: ${response.statusText}`);
        }
    })
        .then((data) => {
            // 1. Verificamos si hay datos
            if (!data || data.length === 0) {
                datosUsuarios.innerHTML = '<div class="alert alert-info">No hay usuarios registrados.</div>';
                return;
            }

            // 2. Construimos el HTML de la tabla
            let htmlTabla = `
            <table class="table table-striped table-hover align-middle">
                <thead class="table-dark">
                    <tr>
                        <th>ID</th>
                        <th>Nombre Completo</th>
                        <th>Usuario</th>
                        <th>Email</th>
                        <th>Estado</th>
                        <th>Fecha Creación</th>
                        <th>Rol</th>
                        <th class="text-center">Acciones</th>
                    </tr>
                </thead>
                <tbody>
            `;

            // 3. Iteramos sobre el array de usuarios
            data.forEach(user => {
                // Formateo de fecha (manejo de null)
                const fecha = user.fechaCreacion
                    ? new Date(user.fechaCreacion).toLocaleDateString()
                    : '-';

                // Formateo de estado (badge verde o rojo)
                const estadoBadge = user.estado
                    ? '<span class="badge bg-success">Activo</span>'
                    : '<span class="badge bg-danger">Inactivo</span>';

                // Construcción de la fila
                htmlTabla += `
                    <tr>
                        <td>${user.id}</td>
                        <td>${user.nombre} ${user.apellidos}</td>
                        <td>${user.nombreusuario}</td> <td>${user.email}</td>
                        <td>${estadoBadge}</td>
                        <td>${fecha}</td>
                        <td>${user.rol}</td>
                        <td class="text-center">
                            <button onclick="actualizarUsuario(${user.id})" class="btn btn-warning btn-sm me-2">
                                <i class="bi bi-pencil-square"></i> Actualizar
                            </button>
                            
                            <button onclick="eliminarUsuario(${user.id})" class="btn btn-danger btn-sm">
                                <i class="bi bi-trash"></i> Eliminar
                            </button>
                        </td>
                    </tr>
                `;
            });
            console.log(data)
            htmlTabla += `</tbody></table>`;

            // 4. Insertamos el HTML en el div
            datosUsuarios.innerHTML = htmlTabla
            btnVolver.style.display ="flex"
        })
        .catch(error => {
            console.error("Error:", error);
        });
} )

btnVolver.addEventListener("click", ()=>{

datosUsuarios.innerHTML = null
    btnVerUsuarios.style.display="flex"
    btnVolver.style.display="none"
})







