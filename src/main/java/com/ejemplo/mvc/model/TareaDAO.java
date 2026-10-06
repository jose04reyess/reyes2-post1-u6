package com.ejemplo.mvc.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * DAO en memoria para la gestión de entidades Tarea.
 * Mantiene una colección estática con tareas iniciales predefinidas y soporte para auto-incremento de ID.
 */
public class TareaDAO {

    private static final List<Tarea> tareas = new ArrayList<>();
    private static final AtomicInteger nextId = new AtomicInteger(1);

    static {
        // Inicialización de 3 tareas iniciales predefinidas
        Calendar cal = Calendar.getInstance();

        cal.add(Calendar.DAY_OF_MONTH, 3);
        Date fecha1 = cal.getTime();

        cal.add(Calendar.DAY_OF_MONTH, 4);
        Date fecha2 = cal.getTime();

        cal.add(Calendar.DAY_OF_MONTH, 7);
        Date fecha3 = cal.getTime();

        tareas.add(new Tarea(nextId.getAndIncrement(), "Configurar servidor Tomcat 10 y Jakarta EE", "Infraestructura", "Alta", fecha1, false));
        tareas.add(new Tarea(nextId.getAndIncrement(), "Diseñar vistas JSP con etiquetas JSTL y CSS moderno", "Frontend", "Media", fecha2, false));
        tareas.add(new Tarea(nextId.getAndIncrement(), "Implementar autenticación y control de roles", "Seguridad", "Alta", fecha3, true));
    }

    /**
     * Retorna una lista inmutable con todas las tareas registradas.
     * @return List<Tarea> inmutable.
     */
    public synchronized List<Tarea> findAll() {
        return Collections.unmodifiableList(new ArrayList<>(tareas));
    }

    /**
     * Busca una tarea por su identificador único.
     * @param id Identificador de la tarea.
     * @return Tarea encontrada o null si no existe.
     */
    public synchronized Tarea findById(int id) {
        for (Tarea t : tareas) {
            if (t.getId() == id) {
                return t;
            }
        }
        return null;
    }

    /**
     * Guarda o actualiza una tarea en la colección.
     * Si el ID es 0 o menor, se le asigna un nuevo ID autoincremental.
     * Si ya posee ID, se actualiza el registro correspondiente.
     * @param tarea Tarea a guardar.
     */
    public synchronized void save(Tarea tarea) {
        if (tarea == null) {
            return;
        }
        if (tarea.getId() <= 0) {
            tarea.setId(nextId.getAndIncrement());
            tareas.add(tarea);
        } else {
            boolean encontrado = false;
            for (int i = 0; i < tareas.size(); i++) {
                if (tareas.get(i).getId() == tarea.getId()) {
                    tareas.set(i, tarea);
                    encontrado = true;
                    break;
                }
            }
            if (!encontrado) {
                tareas.add(tarea);
            }
        }
    }

    /**
     * Elimina una tarea por su identificador único.
     * @param id Identificador de la tarea a eliminar.
     * @return true si fue eliminada, false en caso contrario.
     */
    public synchronized boolean delete(int id) {
        return tareas.removeIf(t -> t.getId() == id);
    }
}
