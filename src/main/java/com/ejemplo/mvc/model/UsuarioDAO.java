package com.ejemplo.mvc.model;

import java.util.HashMap;
import java.util.Map;

/**
 * DAO en memoria para el acceso a datos de Usuarios.
 * Mantiene un mapa estático con las cuentas precargadas del sistema.
 */
public class UsuarioDAO {

    private static final Map<String, Usuario> usuarios = new HashMap<>();

    static {
        // Cuentas precargadas
        usuarios.put("admin", new Usuario("admin", "Admin123!", "Administrador General", "ADMIN"));
        usuarios.put("maria", new Usuario("maria", "Maria2026!", "María Fernanda Rojas", "USER"));
    }

    /**
     * Busca un usuario por su nombre de usuario (username).
     * @param username Nombre de usuario a buscar.
     * @return Usuario correspondiente o null si no existe.
     */
    public Usuario buscarPorUsername(String username) {
        if (username == null) {
            return null;
        }
        return usuarios.get(username.trim().toLowerCase());
    }
}
