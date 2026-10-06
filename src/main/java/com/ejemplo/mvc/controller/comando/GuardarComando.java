package com.ejemplo.mvc.controller.comando;

import com.ejemplo.mvc.model.Tarea;
import com.ejemplo.mvc.service.TareaService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Comando para validar y persistir los datos de una tarea.
 * Implementa el patrón PRG (Post-Redirect-Get) tras un guardado exitoso
 * y validación granular campo por campo en caso de inconsistencias.
 */
public class GuardarComando implements Comando {

    private final TareaService tareaService;

    public GuardarComando(TareaService tareaService) {
        this.tareaService = tareaService;
    }

    @Override
    public String ejecutar(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        String titulo = req.getParameter("titulo");
        String categoria = req.getParameter("categoria");
        String prioridad = req.getParameter("prioridad");
        String fechaLimiteParam = req.getParameter("fechaLimite");

        // 1. Obtener longitud máxima permitida desde el ServletContext
        int maxLongitudTitulo = 80;
        Object maxAttr = req.getServletContext().getAttribute("maxLongitudTitulo");
        if (maxAttr instanceof Integer) {
            maxLongitudTitulo = (Integer) maxAttr;
        } else if (maxAttr instanceof String) {
            try {
                maxLongitudTitulo = Integer.parseInt((String) maxAttr);
            } catch (NumberFormatException ignored) {
            }
        }

        // 2. Validación campo por campo en un Map ordenado
        Map<String, String> errores = new LinkedHashMap<>();

        // Validación de Título
        if (titulo == null || titulo.trim().isEmpty()) {
            errores.put("titulo", "El título es obligatorio.");
        } else if (titulo.trim().length() > maxLongitudTitulo) {
            errores.put("titulo", "El título no puede superar los " + maxLongitudTitulo + " caracteres.");
        }

        // Validación de Categoría
        if (categoria == null || categoria.trim().isEmpty()) {
            errores.put("categoria", "La categoría es obligatoria.");
        }

        // Validación de Prioridad
        if (prioridad == null || prioridad.trim().isEmpty() ||
                (!prioridad.equalsIgnoreCase("Alta") && !prioridad.equalsIgnoreCase("Media") && !prioridad.equalsIgnoreCase("Baja"))) {
            errores.put("prioridad", "La prioridad debe ser 'Alta', 'Media' o 'Baja'.");
        }

        // Validación de Fecha Límite
        Date fechaLimiteParsed = null;
        if (fechaLimiteParam == null || fechaLimiteParam.trim().isEmpty()) {
            errores.put("fechaLimite", "La fecha límite es obligatoria.");
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            try {
                fechaLimiteParsed = sdf.parse(fechaLimiteParam.trim());

                Calendar calHoy = Calendar.getInstance();
                calHoy.set(Calendar.HOUR_OF_DAY, 0);
                calHoy.set(Calendar.MINUTE, 0);
                calHoy.set(Calendar.SECOND, 0);
                calHoy.set(Calendar.MILLISECOND, 0);

                Calendar calLimite = Calendar.getInstance();
                calLimite.setTime(fechaLimiteParsed);
                calLimite.set(Calendar.HOUR_OF_DAY, 0);
                calLimite.set(Calendar.MINUTE, 0);
                calLimite.set(Calendar.SECOND, 0);
                calLimite.set(Calendar.MILLISECOND, 0);

                if (calLimite.before(calHoy)) {
                    errores.put("fechaLimite", "La fecha límite debe ser igual o posterior al día de hoy.");
                }
            } catch (ParseException e) {
                errores.put("fechaLimite", "El formato de fecha debe ser yyyy-MM-dd.");
            }
        }

        // 3. Manejo de errores de validación
        if (!errores.isEmpty()) {
            req.setAttribute("errores", errores);
            req.setAttribute("id", idParam);
            req.setAttribute("titulo", titulo);
            req.setAttribute("categoria", categoria);
            req.setAttribute("prioridad", prioridad);
            req.setAttribute("fechaLimite", fechaLimiteParam);
            return "/WEB-INF/views/formulario.jsp";
        }

        // 4. Si es válido, construir entidad y persistir
        int id = 0;
        if (idParam != null && !idParam.trim().isEmpty()) {
            try {
                id = Integer.parseInt(idParam.trim());
            } catch (NumberFormatException ignored) {
            }
        }

        boolean completada = false;
        if (id > 0) {
            Tarea existente = tareaService.obtenerPorId(id);
            if (existente != null) {
                completada = existente.isCompletada();
            }
        }

        // Normalizar prioridad (primera mayúscula)
        String prioridadNormalizada = normalizarPrioridad(prioridad.trim());

        Tarea tarea = new Tarea(id, titulo.trim(), categoria.trim(), prioridadNormalizada, fechaLimiteParsed, completada);
        tareaService.guardar(tarea);

        // 5. Patrón PRG (Post-Redirect-Get): Redirección HTTP y retorno de null
        resp.sendRedirect(req.getContextPath() + "/app?comando=listar");
        return null;
    }

    private String normalizarPrioridad(String p) {
        if ("alta".equalsIgnoreCase(p)) return "Alta";
        if ("media".equalsIgnoreCase(p)) return "Media";
        if ("baja".equalsIgnoreCase(p)) return "Baja";
        return p;
    }
}
