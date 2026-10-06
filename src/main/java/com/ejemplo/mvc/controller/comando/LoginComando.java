package com.ejemplo.mvc.controller.comando;

import com.ejemplo.mvc.model.Usuario;
import com.ejemplo.mvc.service.AutenticacionService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Comando para gestionar la autenticación de usuarios en el sistema.
 */
public class LoginComando implements Comando {

    private final AutenticacionService authService;

    public LoginComando(AutenticacionService authService) {
        this.authService = authService;
    }

    @Override
    public String ejecutar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");

        // Si no viene username (GET inicial del login), mostrar vista de login
        if (username == null) {
            return "/WEB-INF/views/login.jsp";
        }

        String clave = req.getParameter("clave");
        Usuario usuario = authService.autenticar(username, clave);

        if (usuario == null) {
            req.setAttribute("errorLogin", "Usuario o contraseña incorrectos.");
            req.setAttribute("username", username);
            return "/WEB-INF/views/login.jsp";
        }

        // Crear o renovar sesión con tiempo de inactividad de 30 minutos (1800 segundos)
        HttpSession session = req.getSession(true);
        session.setAttribute("usuarioActual", usuario);
        session.setMaxInactiveInterval(1800);

        resp.sendRedirect(req.getContextPath() + "/app?comando=listar");
        return null;
    }
}
