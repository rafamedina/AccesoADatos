tablaUser = document.getElementById("datosUser")


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
            // Ya no recibes 'data' (lista), recibes un 'user' (objeto único)
            console.log(user);

            // 1. Preparamos los datos visuales
            const fecha = user.fechaCreacion ? new Date(user.fechaCreacion).toLocaleDateString() : '-';

            const estadoBadge = user.activo
                ? '<span class="badge bg-success">Activo</span>'
                : '<span class="badge bg-danger">Inactivo</span>';

            // 2. Pintamos la tabla DIRECTAMENTE (Sin bucles forEach)
            tablaUser.innerHTML = `
            <table class="table table-striped table-hover table-bordered align-middle">
                <thead class="table-dark">
                    <tr>
                        <th>ID</th>
                        <th>Nombre</th>
                        <th>Apellidos</th>
                        <th>Usuario</th>
                        <th>Email</th>
                        <th>Estado</th>
                        <th>Fecha Creación</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>${user.id}</td>
                        <td>${user.nombre}</td>
                        <td>${user.apellidos}</td>
                        <td>${user.nombreusuario}</td>
                        <td>${user.email}</td>
                        <td class="text-center">${estadoBadge}</td>
                        <td>${fecha}</td>
                    </tr>
                </tbody>
            </table>
        `;
        })

        .catch((error) => {
            // --- CASO ERROR (401, 500, o fallo de red) ---
            console.error("Fallo en login:", error);
        });
