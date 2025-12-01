package com.dam.accesodatos.miprimeraapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CalculadoraController {

    @GetMapping("/api/calculadora/sumar/{num1}/{num2}")
    public String sumar(@PathVariable int num1, @PathVariable int num2){
        return "La suma es de " + (num1 + num2);
    }
    @GetMapping("/api/calculadora/restar/{num1}/{num2}")
    public String restar(@PathVariable int num1, @PathVariable int num2){
        return "La resta es de " + (num1 +- num2);
    }
    @GetMapping("/api/calculadora/multiplicar/{num1}/{num2}")
    public String multiplicar(@PathVariable int num1, @PathVariable int num2){
        return "La multiplicacion es de " + (num1 * num2);
    }
}
