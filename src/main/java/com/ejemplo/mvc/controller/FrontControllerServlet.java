package com.ejemplo.mvc.controller;

import com.ejemplo.mvc.controller.comando.*;
import com.ejemplo.mvc.model.Usuario;
import com.ejemplo.mvc.service.AutenticacionService;
import com.ejemplo.mvc.service.TareaService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Front Controller centralizado de la aplicación.
 * Actúa como punto de entrada único para todas las peticiones a la ruta /app.
 * Gestiona el ciclo de vida de los comandos, la inicialización de servicios y parámetros globales,
 * y la verificación centralizada de sesiones y autenticación.
 */
@WebServlet(name = "FrontControllerServlet", urlPatterns = {"/app"})
public class FrontControllerServlet extends HttpServlet {

    private final Map<String, Comando> comandos = new HashMap<>();
    private final Set<String> comandosPublicos = new HashSet<>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext ctx = config.getServletContext();

        // 1. Lectura de context-params y publicación en ServletContext
        String appNombre = ctx.getInitParameter("app.nombre");
        String maxLongitudTitulo = ctx.getInitParameter("app.maxLongitudTitulo");

        if (appNombre != null) {
            ctx.setAttribute("app.nombre", appNombre);
            ctx.setAttribute("nombreApp", appNombre);
        }
        if (maxLongitudTitulo != null) {
            ctx.setAttribute("app.maxLongitudTitulo", maxLongitudTitulo);
            ctx.setAttribute("maxLongitudTitulo", maxLongitudTitulo);
        }

        // 2. Instanciación de servicios de negocio
        TareaService tareaService = new TareaService();
        AutenticacionService authService = new AutenticacionService();

        // 3. Registro de comandos en el mapa
        comandos.put("listar", new ListarComando(tareaService));
        comandos.put("formulario", new FormularioComando(tareaService));
        comandos.put("guardar", new GuardarComando(tareaService));
        comandos.put("eliminar", new EliminarComando(tareaService));
        comandos.put("completar", new CompletarComando(tareaService));
        comandos.put("login", new LoginComando(authService));
        comandos.put("logout", new LogoutComando());
        comandos.put("idioma", new IdiomaComando());

        // 4. Registro de comandos públicos (accesibles sin sesión)
        comandosPublicos.add("login");
        comandosPublicos.add("idioma");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        procesar(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        procesar(req, resp);
    }

    /**
     * Procesa de manera unificada las peticiones GET y POST.
     */
    protected void procesar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        // 1. Resolver nombre del comando (por defecto "listar")
        String nombreComando = req.getParameter("comando");
        if (nombreComando == null || nombreComando.trim().isEmpty()) {
            nombreComando = "listar";
        } else {
            nombreComando = nombreComando.trim().toLowerCase();
        }

        // 2. VERIFICACIÓN CENTRALIZADA DE SESIÓN:
        // Si el comando no es público, se requiere usuario autenticado en sesión
        if (!comandosPublicos.contains(nombreComando)) {
            HttpSession session = req.getSession(false);
            Usuario usuarioActual = (session != null) ? (Usuario) session.getAttribute("usuarioActual") : null;

            if (usuarioActual == null) {
                resp.sendRedirect(req.getContextPath() + "/app?comando=login");
                return;
            }
        }

        // 3. Obtener el comando del mapa
        Comando comando = comandos.get(nombreComando);
        if (comando == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "El comando solicitado no existe: " + nombreComando);
            return;
        }

        // 4. Ejecutar el comando
        String vista = comando.ejecutar(req, resp);

        // 5. Despachar a la vista lógica si no se realizó una redirección
        if (vista != null) {
            req.getRequestDispatcher(vista).forward(req, resp);
        }
    }
}
