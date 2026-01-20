
let formulario = document.getElementById("formulario");
let email = document.getElementById("email");
let password = document.getElementById("password");
let btnRegistro = document.getElementById("btnRegistro");
var toast = document.getElementById("toast")


formulario.addEventListener('submit',(e)=> {
    e.preventDefault();

    var emailMetido = email.value.trim()
    var passwordMetido = password.value.trim()

    fetch('/login', {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            email: emailMetido,
            password: passwordMetido
        })
    })
        .then((response) => {
            if(response.ok){
                return response.json();
            }
            else if (response.status === 401){
                throw new Error("Credenciales Incorrectas")
            }
            else {
                throw new Error("Error en el servidor")
            }
        })
        .then((data)=>{
            console.log(data);
           // window.location.href = '/control'
        })
        .catch((error) => {
            // --- CASO ERROR (401, 500, o fallo de red) ---
            console.error("Fallo en login:", error);

            toast.textContent = error;
            // Mostramos el Toast
            toast.style.display = "flex";
            // Opcional: Podrías poner el mensaje del error en el toast
            // toast.innerText = "Usuario o contraseña incorrectos";

            setTimeout(() => {
                toast.style.display = "none";
            }, 3000);
        });
});

