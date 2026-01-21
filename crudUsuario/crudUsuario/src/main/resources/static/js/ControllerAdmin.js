// 1. SELECTORES DE ELEMENTOS DEL DOM
const btnsalir = document.getElementById("btnSalir");
const btnVolver = document.getElementById("btnVolver");
const btnVerUsuarios = document.getElementById("btnVerUsuarios");
const datosUsuarios = document.getElementById("datosUsuarios");
const modal = document.getElementById('miModal');
const btnCerrar = document.getElementById('btnCerrar');
const contenidoModal = document.getElementById('contenido-modal');

// 2. VARIABLE GLOBAL PARA CACHÉ (Evita tener que volver a pedir datos al abrir el modal)
let usuariosCache = [];

// 3. EVENT LISTENERS

// Botón Salir
btnsalir.addEventListener("click", () => {
    window.location.href = '/killSession';
});

// Botón Cerrar Modal
btnCerrar.addEventListener('click', () => {
    modal.close();
});

// Botón Volver (Limpia la tabla y muestra el menú principal)
btnVolver.addEventListener("click", () => {
    datosUsuarios.innerHTML = "";
    btnVerUsuarios.style.display = "flex";
    btnVolver.style.display = "none";
});

// Botón Ver Usuarios (Carga principal)
btnVerUsuarios.addEventListener("click", () => {
    // Ocultamos el botón de ver y mostramos carga (opcional)
    btnVerUsuarios.style.display = "none";

    fetch("admin/cargarUsuarios") // Asegúrate que esta ruta coincide con tu Controller
        .then((response) => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error(`Error ${response.status}: ${response.statusText}`);
            }
        })
        .then((data) => {
            // A) Guardamos los datos en la variable global para usarlos al editar
            usuariosCache = data;

            // B) Verificamos si hay datos
            if (!data || data.length === 0) {
                datosUsuarios.innerHTML = '<div class="alert alert-info">No hay usuarios registrados.</div>';
                btnVolver.style.display = "flex"; // Mostramos botón volver aunque no haya datos
                return;
            }

            // C) Construimos la cabecera de la tabla
            let htmlTabla = `
            <table class="table table-striped table-hover align-middle table-bordered">
                <thead class="table-dark">
                    <tr>
                        <th>ID</th>
                        <th>Nombre Completo</th>
                        <th>Usuario</th>
                        <th>Email</th>
                        <th>Rol</th>
                        <th>Estado</th>
                        <th>Fecha Creación</th>
                        <th class="text-center">Acciones</th>
                    </tr>
                </thead>
                <tbody>
            `;

            // D) Iteramos sobre los datos
            data.forEach(user => {
                // Formateo de fecha
                const fecha = user.fechaCreacion
                    ? new Date(user.fechaCreacion).toLocaleDateString()
                    : '-';

                // Badge de estado (Usando user.estado del DTO)
                const estadoBadge = user.estado
                    ? '<span class="badge bg-success">Activo</span>'
                    : '<span class="badge bg-danger">Inactivo</span>';

                // Filas de la tabla
                htmlTabla += `
                    <tr>
                        <td>${user.id}</td>
                        <td>${user.nombre} ${user.apellidos}</td>
                        <td>${user.username}</td> 
                        <td>${user.email}</td>
                        <td>${user.rol}</td>
                        <td class="text-center">${estadoBadge}</td>
                        <td>${fecha}</td>
                        <td class="text-center">
                            <button onclick="prepararEdicion(${user.id})" class="btn btn-warning btn-sm me-2">
                                <i class="bi bi-pencil-square"></i> Actualizar
                            </button>
                            
                            <button onclick="eliminarUsuario(${user.id})" class="btn btn-danger btn-sm">
                                <i class="bi bi-trash"></i> Eliminar
                            </button>
                        </td>
                    </tr>
                `;
            });

            htmlTabla += `</tbody></table>`;

            // E) Insertamos el HTML y mostramos el botón de volver
            datosUsuarios.innerHTML = htmlTabla;
            btnVolver.style.display = "flex";
        })
        .catch(error => {
            console.error("Error cargando usuarios:", error);
            datosUsuarios.innerHTML = `<div class="alert alert-danger">Error al cargar usuarios: ${error.message}</div>`;
            btnVolver.style.display = "flex";
        });
});

// 4. FUNCIONES AUXILIARES (Globales para que el HTML onclick las vea)

/**
 * Busca el usuario en la caché y abre el modal
 */
function prepararEdicion(id) {
    // Buscamos el objeto usuario completo en el array que guardamos antes
    const usuarioEncontrado = usuariosCache.find(u => u.id === id);

    if (usuarioEncontrado) {
        mostrarModalConDatos(usuarioEncontrado);
        modal.showModal();
    } else {
        alert("Error: No se pudo cargar la información del usuario para editar.");
    }
}

/**
 * Rellena el HTML del modal con los datos del usuario
 */
function mostrarModalConDatos(user) {
    // Preseleccionar opciones del select
    const selectedActivo = user.estado ? 'selected' : '';
    const selectedInactivo = !user.estado ? 'selected' : '';

    contenidoModal.innerHTML = `
        <h3 class="mb-4">Editar Usuario: ${user.username}</h3>
        
        <form id="formEditarUsuario">
            <input type="hidden" id="editId" value="${user.id}">

            <div class="row">
                <div class="col-md-6 mb-3">
                    <label for="editNombre" class="form-label">Nombre</label>
                    <input type="text" class="form-control" id="editNombre" value="${user.nombre}">
                </div>
                <div class="col-md-6 mb-3">
                    <label for="editApellidos" class="form-label">Apellidos</label>
                    <input type="text" class="form-control" id="editApellidos" value="${user.apellidos}">
                </div>
            </div>

            <div class="mb-3">
                <label for="editEmail" class="form-label">Email</label>
                <input type="email" class="form-control" id="editEmail" value="${user.email}">
            </div>

            <div class="row">
                <div class="col-md-6 mb-3">
                    <label for="editRol" class="form-label">Rol</label>
                    <input type="text" class="form-control" id="editRol" value="${user.rol}">
                </div>
                <div class="col-md-6 mb-3">
                    <label for="editEstado" class="form-label">Estado</label>
                    <select class="form-select" id="editEstado">
                        <option value="true" ${selectedActivo}>Activo</option>
                        <option value="false" ${selectedInactivo}>Inactivo</option>
                    </select>
                </div>
            </div>

            <div class="d-flex justify-content-end gap-2 mt-3">
                <button type="button" class="btn btn-secondary" onclick="modal.close()">Cancelar</button>
                <button type="button" class="btn btn-success" onclick="guardarCambiosUsuario()">
                    <i class="bi bi-save"></i> Guardar Cambios
                </button>
            </div>
        </form>
    `;
}

/**
 * Envía los datos modificados al servidor
 */
function guardarCambiosUsuario() {
    // 1. Recoger datos del formulario
    const id = document.getElementById('editId').value;

    const usuarioActualizado = {
        id: id,
        nombre: document.getElementById('editNombre').value,
        apellidos: document.getElementById('editApellidos').value,
        email: document.getElementById('editEmail').value,
        rol: document.getElementById('editRol').value,
        estado: document.getElementById('editEstado').value === 'true' // Convertir string a boolean
    };

    // 2. Enviar al backend (AJUSTA ESTA URL A TU CONTROLLER)
    fetch("/admin/actualizarUsuario", {
        method: 'PUT', // O 'POST' si tu backend usa POST
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(usuarioActualizado)
    })
        .then(response => {
            if (response.ok) {
                // Éxito: Cerramos modal y recargamos tabla
                modal.close();
                // Truco para recargar: Simular click en 'Volver' y luego en 'Ver Usuarios'
                btnVolver.click();
                setTimeout(() => btnVerUsuarios.click(), 100);
                alert("Usuario actualizado correctamente.");
            } else {
                alert("Error al actualizar el usuario.");
            }
        })
        .catch(error => {
            console.error("Error:", error);
            alert("Error de conexión al guardar.");
        });
}

/**
 * Elimina un usuario
 */
function eliminarUsuario(id) {
    if(confirm("¿Estás seguro de que quieres eliminar este usuario? Esta acción no se puede deshacer.")) {

        // AJUSTA ESTA URL A TU CONTROLLER
        fetch(`/admin/eliminarUsuario/${id}`, {
            method: 'DELETE'
        })
            .then(response => {
                if (response.ok) {
                    // Éxito: Recargamos tabla
                    btnVolver.click();
                    setTimeout(() => btnVerUsuarios.click(), 100);
                } else {
                    alert("No se pudo eliminar el usuario.");
                }
            })
            .catch(error => console.error("Error eliminando:", error));
    }
}