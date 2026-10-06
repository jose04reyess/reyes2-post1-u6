package com.ejemplo.mvc.controller.comando;

import com.ejemplo.mvc.model.Tarea;
import com.ejemplo.mvc.service.TareaService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Comando para preparar y mostrar el formulario de creación o edición de tareas.
 */
public class FormularioComando implements Comando {

    private final TareaService tareaService;

    public FormularioComando(TareaService tareaService) {
        this.tareaService = tareaService;
    }

    @Override
    public String ejecutar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        if (idParam != null && !idParam.trim().isEmpty()) {
            try {
                int id = Integer.parseInt(idParam.trim());
                Tarea tarea = tareaService.obtenerPorId(id);
                if (tarea != null) {
                    req.setAttribute("tarea", tarea);
                }
            } catch (NumberFormatException ignored) {
                // Si el id no es un número válido, se procede con formulario en blanco
            }
        }
        return "/WEB-INF/views/formulario.jsp";
    }
}
