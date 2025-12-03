package com.pizzamdp.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controlador para gestionar la información del usuario autenticado.
 */
@RestController
public class UserController {

    /**
     * Proporciona información sobre el usuario actualmente autenticado, incluyendo
     * su nombre de usuario y roles. Este endpoint está protegido y requiere un token JWT válido.
     *
     * @param authentication El objeto de autenticación inyectado por Spring Security,
     *                       que contiene los detalles del principal (usuario).
     * @return Un mapa que contiene el nombre de usuario y una lista de sus roles.
     * @example
     * {
     *   "username": "admin",
     *   "roles": ["ADMINISTRADOR"]
     * }
     */
    @GetMapping("/api/me")
    public Map<String, Object> getUserInfo(Authentication authentication) {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("username", authentication.getName());
        userInfo.put("roles", authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(role -> role.replace("ROLE_", ""))
                .collect(Collectors.toList()));
        return userInfo;
    }
}
