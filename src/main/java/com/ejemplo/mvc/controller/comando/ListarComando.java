package com.ejemplo.mvc.controller.comando;

import com.ejemplo.mvc.service.TareaService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Comando para listar todas las tareas registradas.
 */
public class ListarComando implements Comando {

    private final TareaService tareaService;

    public ListarComando(TareaService tareaService) {
        this.tareaService = tareaService;
    }

    @Override
    public String ejecutar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("tareas", tareaService.obtenerTodas());
        return "/WEB-INF/views/lista.jsp";
    }
}
