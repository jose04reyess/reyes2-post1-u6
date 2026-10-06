package com.ejemplo.mvc.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Representa un Usuario del sistema con su rol de acceso (ADMIN o USER).
 * Implementa Serializable para su almacenamiento seguro en la sesión HTTP (HttpSession).
 */
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    private String username;
    private String clave;
    private String nombreCompleto;
    private String rol; // "ADMIN" o "USER"

    public Usuario() {
    }

    public Usuario(String username, String clave, String nombreCompleto, String rol) {
        this.username = username;
        this.clave = clave;
        this.nombreCompleto = nombreCompleto;
        this.rol = rol;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public boolean isAdmin() {
        return "ADMIN".equalsIgnoreCase(this.rol);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(username, usuario.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "username='" + username + '\'' +
                ", nombreCompleto='" + nombreCompleto + '\'' +
                ", rol='" + rol + '\'' +
                '}';
    }
}
