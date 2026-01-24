// ==========================================
// 1. SELECTORES DE ELEMENTOS DEL DOM
// ==========================================
let btnsalir = document.getElementById("btnSalir");
const btnVolver = document.getElementById("btnVolver");
const btnVerUsuarios = document.getElementById("btnVerUsuarios");
const btnCrearUsuario = document.getElementById("btnCrearUsuario"); // Botón NUEVO
const btnjson = document.getElementById("btnjson"); // Botón NUEVO
const datosUsuarios = document.getElementById("datosUsuarios");
const modal = document.getElementById('miModal');
const btnCerrar = document.getElementById('btnCerrar');
const contenidoModal = document.getElementById('contenido-modal');

// ==========================================
// 2. VARIABLE GLOBAL PARA CACHÉ
// ==========================================
let usuariosCache = [];

// ==========================================
// 3. EVENT LISTENERS (BOTONES PRINCIPALES)
// ==========================================

btnjson.addEventListener("click",()=>{
    window.location.href = "admin/exportarJSON";
})


// --- Botón Salir ---
btnsalir.addEventListener("click", () => {
    window.location.href = '/killSession';
});

// --- Botón Cerrar Modal ---
btnCerrar.addEventListener('click', () => {
    modal.close();
});

// --- Botón Volver (Limpia la tabla) ---
btnVolver.addEventListener("click", () => {
    datosUsuarios.innerHTML = "";
    btnVerUsuarios.style.display = "inline-block"; // Reaparece el botón ver
    btnCrearUsuario.style.display = "inline-block"; // Reaparece el botón crear
    btnVolver.style.display = "none";
});

// --- Botón Crear Usuario (Abre Modal Vacío) ---
if (btnCrearUsuario) {
    btnCrearUsuario.addEventListener("click", () => {
        mostrarModalCreacion();
        modal.showModal();
    });
}

// --- Botón Ver Usuarios (Carga datos del servidor) ---
btnVerUsuarios.addEventListener("click", () => {
    // 1. Ocultamos botones principales
    btnVerUsuarios.style.display = "none";
    btnCrearUsuario.style.display = "none"; // También ocultamos este para limpiar la vista

    fetch("admin/cargarUsuarios")
        .then((response) => {
            if (response.ok) return response.json();
            throw new Error(`Error ${response.status}: ${response.statusText}`);
        })
        .then((data) => {
            usuariosCache = data; // Guardamos en caché

            // 2. Cabecera y Título
            let htmlContent = `
                <div class="container fade-in">
                    <h2 class="text-center mb-4" style="color: #3c096c; font-weight: 400;">Todos los Usuarios</h2>
            `;

            if (!data || data.length === 0) {
                htmlContent += '<div class="alert alert-info text-center">No hay usuarios registrados.</div>';
            } else {
                // 3. Tabla
                htmlContent += `
                <table class="table table-striped table-hover align-middle shadow-sm">
                    <thead>
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

                // 4. Filas
                data.forEach(user => {
                    const fecha = user.fechaCreacion
                        ? new Date(user.fechaCreacion).toLocaleDateString()
                        : '-';

                    const estadoBadge = user.estado
                        ? '<span class="badge bg-success rounded-pill">Activo</span>'
                        : '<span class="badge bg-danger rounded-pill">Inactivo</span>';

                    htmlContent += `
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
                                    Actualizar
                                </button>
                                <button onclick="eliminarUsuario(${user.id})" class="btn btn-danger btn-sm">
                                    Eliminar
                                </button>
                            </td>
                        </tr>
                    `;
                });

                htmlContent += `</tbody></table>`;
            }

            htmlContent += `</div>`;
            datosUsuarios.innerHTML = htmlContent;

            // 5. Mostrar botón Volver y Salir
            btnVolver.style.display = "inline-block";
            if (btnsalir) btnsalir.style.display = "inline-block";

        })
        .catch(error => {
            console.error("Error:", error);
            datosUsuarios.innerHTML = `<div class="alert alert-danger">Error cargando datos.</div>`;
            // Si falla, mostramos volver para no quedar atrapados
            btnVolver.style.display = "inline-block";
        });
});

// ==========================================
// 4. FUNCIONES DE LÓGICA (MODALES Y FETCH)
// ==========================================

/**
 * MODO EDICIÓN: Busca el usuario y rellena el modal
 */
function prepararEdicion(id) {
    const usuarioEncontrado = usuariosCache.find(u => u.id === id);

    if (usuarioEncontrado) {
        mostrarModalConDatos(usuarioEncontrado);
        modal.showModal();
    } else {
        alert("Error: No se pudo cargar la información del usuario.");
    }
}

/**
 * HTML DEL MODAL PARA EDITAR (Sin campo contraseña)
 */
function mostrarModalConDatos(user) {
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
                <label for="editUsername" class="form-label">Nombre de Usuario</label>
                <input type="text" class="form-control" id="editUsername" value="${user.username}"> 
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
                    Guardar Cambios
                </button>
            </div>
        </form>
    `;
}

/**
 * HTML DEL MODAL PARA CREAR (Con campo contraseña)
 */
function mostrarModalCreacion() {
    contenidoModal.innerHTML = `
        <h3 class="mb-4 text-primary">Crear Nuevo Usuario</h3>
        
        <form id="formCrearUsuario">
            <div class="row">
                <div class="col-md-6 mb-3">
                    <label for="newNombre" class="form-label">Nombre</label>
                    <input type="text" class="form-control" id="newNombre" placeholder="Nombre">
                </div>
                <div class="col-md-6 mb-3">
                    <label for="newApellidos" class="form-label">Apellidos</label>
                    <input type="text" class="form-control" id="newApellidos" placeholder="Apellidos">
                </div>
            </div>

            <div class="mb-3">
                <label for="newUsername" class="form-label">Nombre de Usuario</label>
                <input type="text" class="form-control" id="newUsername" placeholder="Usuario único"> 
            </div>

            <div class="mb-3">
                <label for="newPassword" class="form-label">Contraseña</label>
                <input type="password" class="form-control" id="newPassword" placeholder="******"> 
            </div>

            <div class="mb-3">
                <label for="newEmail" class="form-label">Email</label>
                <input type="email" class="form-control" id="newEmail" placeholder="correo@ejemplo.com">
            </div>

            <div class="d-flex justify-content-end gap-2 mt-3">
                <button type="button" class="btn btn-secondary" onclick="modal.close()">Cancelar</button>
                <button type="button" class="btn btn-success" onclick="crearUsuario()">
                    Crear Usuario
                </button>
            </div>
        </form>
    `;
}

// ==========================================
// 5. PETICIONES AL SERVIDOR (PUT, POST, DELETE)
// ==========================================

/**
 * PUT: Actualizar Usuario Existente
 */
function guardarCambiosUsuario() {
    const id = document.getElementById('editId').value;

    const usuarioActualizado = {
        id: id,
        nombre: document.getElementById('editNombre').value,
        apellidos: document.getElementById('editApellidos').value,
        username: document.getElementById('editUsername').value,
        email: document.getElementById('editEmail').value,
        rol: document.getElementById('editRol').value,
        estado: document.getElementById('editEstado').value === 'true'
    };

    fetch("/admin/actualizarUsuario", {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(usuarioActualizado)
    })
        .then(response => {
            if (response.ok) {
                modal.close();
                alert("Usuario actualizado correctamente.");
                // Recargar tabla
                btnVolver.click();
                setTimeout(() => btnVerUsuarios.click(), 100);
            } else {
                return response.text().then(text => { throw new Error(text) });
            }
        })
        .catch(error => {
            console.error("Error:", error);
            alert("Error al actualizar: " + error.message);
        });
}

/**
 * POST: Crear Nuevo Usuario
 */
function crearUsuario() {
    const nuevoUsuario = {
        nombre: document.getElementById('newNombre').value,
        apellidos: document.getElementById('newApellidos').value,
        // Asegúrate de que este nombre coincida con tu entidad Java (nombreusuario o username)
        nombreusuario: document.getElementById('newUsername').value,
        password: document.getElementById('newPassword').value,
        email: document.getElementById('newEmail').value,
        activo: true
    };

    // Validación 1: Campos vacíos
    if(!nuevoUsuario.nombreusuario || !nuevoUsuario.password || !nuevoUsuario.email) {
        alert("Usuario, Contraseña y Email son obligatorios.");
        return;
    }

    // Validación 2: EL EMAIL (NUEVO)
    if (!nuevoUsuario.email.includes("@")) {
        alert("Por favor, introduce un correo electrónico válido (falta el '@').");
        return; // Detenemos la función aquí
    }

    fetch("/admin/crearUsuario", {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(nuevoUsuario)
    })
        .then(response => {
            if (response.ok) {
                modal.close();
                alert("Usuario creado con éxito.");
                if (datosUsuarios.innerHTML !== "") {
                    btnVolver.click();
                    setTimeout(() => btnVerUsuarios.click(), 100);
                }
            } else {
                return response.text().then(text => { throw new Error(text) });
            }
        })
        .catch(error => {
            console.error("Error:", error);
            alert("Error al crear: " + error.message);
        });
}

/**
 * DELETE: Eliminar Usuario
 */
function eliminarUsuario(id) {
    if(confirm("¿Estás seguro de que quieres eliminar este usuario? Esta acción no se puede deshacer.")) {
        fetch(`/admin/eliminarUsuario/${id}`, {
            method: 'DELETE'
        })
            .then(response => {
                if (response.ok) {
                    // Recargar tabla
                    btnVolver.click();
                    setTimeout(() => btnVerUsuarios.click(), 100);
                } else {
                    alert("No se pudo eliminar el usuario.");
                }
            })
            .catch(error => console.error("Error eliminando:", error));
    }
}