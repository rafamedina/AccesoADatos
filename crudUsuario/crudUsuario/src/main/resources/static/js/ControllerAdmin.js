btnsalir = document.getElementById("btnSalir")
btnVerUsuarios = document.getElementById("btnVerUsuarios")

fetch("/admin")


btnsalir.addEventListener("click",()=>{
    window.location.href = '/killSession'
})

btnVerUsuarios.addEventListener("click", ()=>{
    window.location.href = '/admin/listarUsuarios'
} )







