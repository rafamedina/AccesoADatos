listaUsuarios = document.getElementById("listaUsuarios")



fetch("admin/cargarUsuarios",{

}).then((response) => {
    if(response.ok){
        return response.json();
    } else {
        // CAMBIO AQUÍ: Añadimos el status para saber qué pasa
        throw new Error(`Error ${response.status}: ${response.statusText}`);
    }
})