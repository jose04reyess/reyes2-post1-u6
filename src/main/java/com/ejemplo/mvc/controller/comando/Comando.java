package com.ejemplo.mvc.controller.comando;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Interfaz base para el patrón Comando (Command Pattern).
 * Desacopla las acciones de la aplicación del FrontControllerServlet.
 */
public interface Comando {

    /**
     * Ejecuta la acción correspondiente a la petición HTTP.
     * @param req HttpServletRequest de la petición entrante.
     * @param resp HttpServletResponse de la respuesta.
     * @return Ruta de la vista JSP a despachar (forward), o null si se realizó una redirección HTTP (redirect).
     * @throws ServletException si ocurre un error de servlet.
     * @throws IOException si ocurre un error de entrada/salida.
     */
    String ejecutar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException;
}
