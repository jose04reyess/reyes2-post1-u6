package com.ejemplo.mvc.service;

import com.ejemplo.mvc.model.Usuario;
import com.ejemplo.mvc.model.UsuarioDAO;

/**
 * Servicio de lógica de negocio para la autenticación y validación de credenciales de usuarios.
 */
public class AutenticacionService {

    private final UsuarioDAO usuarioDAO;

    public AutenticacionService() {
        this.usuarioDAO = new UsuarioDAO();
    }

    public AutenticacionService(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    /**
     * Valida las credenciales ingresadas contra los usuarios registrados.
     * @param username Nombre de usuario.
     * @param clave Contraseña en texto claro.
     * @return Objeto Usuario si las credenciales son válidas; null en caso contrario.
     */
    public Usuario autenticar(String username, String clave) {
        if (username == null || clave == null) {
            return null;
        }
        Usuario usuario = usuarioDAO.buscarPorUsername(username.trim());
        if (usuario != null && usuario.getClave().equals(clave)) {
            return usuario;
        }
        return null;
    }
}
