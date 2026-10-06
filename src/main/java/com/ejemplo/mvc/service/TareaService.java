package com.ejemplo.mvc.service;

import com.ejemplo.mvc.model.Tarea;
import com.ejemplo.mvc.model.TareaDAO;

import java.util.List;

/**
 * Servicio de lógica de negocio para la gestión de Tareas.
 * Interactúa con la capa de acceso a datos a través de TareaDAO.
 */
public class TareaService {

    private final TareaDAO tareaDAO;

    public TareaService() {
        this.tareaDAO = new TareaDAO();
    }

    public TareaService(TareaDAO tareaDAO) {
        this.tareaDAO = tareaDAO;
    }

    /**
     * Obtiene la lista completa de tareas.
     * @return Lista inmutable de tareas.
     */
    public List<Tarea> obtenerTodas() {
        return tareaDAO.findAll();
    }

    /**
     * Obtiene una tarea específica por su ID.
     * @param id Identificador de la tarea.
     * @return Tarea encontrada o null.
     */
    public Tarea obtenerPorId(int id) {
        return tareaDAO.findById(id);
    }

    /**
     * Guarda una tarea validando que el título no sea nulo ni vacío.
     * @param tarea Objeto Tarea a registrar o actualizar.
     * @throws IllegalArgumentException si la tarea es nula o el título está vacío.
     */
    public void guardar(Tarea tarea) {
        if (tarea == null) {
            throw new IllegalArgumentException("La tarea no puede ser nula.");
        }
        if (tarea.getTitulo() == null || tarea.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("El título es obligatorio.");
        }
        tareaDAO.save(tarea);
    }

    /**
     * Marca una tarea existente como completada.
     * @param id Identificador de la tarea a completar.
     */
    public void completar(int id) {
        Tarea tarea = tareaDAO.findById(id);
        if (tarea != null) {
            tarea.setCompletada(true);
            tareaDAO.save(tarea);
        }
    }

    /**
     * Elimina una tarea por su identificador.
     * @param id Identificador de la tarea a eliminar.
     */
    public void eliminar(int id) {
        tareaDAO.delete(id);
    }
}
