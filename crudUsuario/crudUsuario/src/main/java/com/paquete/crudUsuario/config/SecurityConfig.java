package com.paquete.crudUsuario.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // 1) Rutas públicas
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/").permitAll()  // Acceso sin login a la ruta principal
                        .anyRequest().authenticated()    // Resto de rutas requieren autenticación
                );

//                // 2) Configuración de login en "/"
//                .formLogin(form -> form
//                        .loginPage("/")  // Login en la raíz
//                        .loginProcessingUrl("/")  // Procesa el login en la raíz
//                        .defaultSuccessUrl("/home", true)  // Redirige a home después del login
//                        .permitAll()
//                )
//
//                // 3) Logout
//                .logout(logout -> logout
//                        .logoutUrl("/logout")  // Ruta para logout
//                        .logoutSuccessUrl("/")  // Redirige a la raíz después de logout
//                        .permitAll()
//                )
//
//                // 4) Gestión de sesiones
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)  // Crea sesión solo si es necesario
//                        .maximumSessions(1)  // Limita a una sesión por usuario
//                        .maxSessionsPreventsLogin(true)  // Previene que otro usuario inicie sesión si ya hay una activa
//                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
