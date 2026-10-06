package com.ejemplo.mvc.controller.comando;

import com.ejemplo.mvc.model.Usuario;
import com.ejemplo.mvc.service.TareaService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Comando para eliminar una tarea.
 * Aplica autorización basada en roles (RBAC): Únicamente los usuarios con rol ADMIN
 * tienen permiso para ejecutar la eliminación.
 */
public class EliminarComando implements Comando {

    private final TareaService tareaService;

    public EliminarComando(TareaService tareaService) {
        this.tareaService = tareaService;
    }

    @Override
    public String ejecutar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        Usuario usuarioActual = (session != null) ? (Usuario) session.getAttribute("usuarioActual") : null;

        // Control de acceso por Rol
        if (usuarioActual == null || !"ADMIN".equalsIgnoreCase(usuarioActual.getRol())) {
            req.setAttribute("error", "Solo un administrador puede eliminar tareas.");
            req.setAttribute("tareas", tareaService.obtenerTodas());
            return "/WEB-INF/views/lista.jsp";
        }

        // Si es ADMIN, procesar la eliminación
        String idParam = req.getParameter("id");
        if (idParam != null && !idParam.trim().isEmpty()) {
            try {
                int id = Integer.parseInt(idParam.trim());
                tareaService.eliminar(id);
            } catch (NumberFormatException ignored) {
            }
        }

        resp.sendRedirect(req.getContextPath() + "/app?comando=listar");
        return null;
    }
}
