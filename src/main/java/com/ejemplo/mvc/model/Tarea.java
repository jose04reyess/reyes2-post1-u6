package com.ejemplo.mvc.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * Representa una Tarea en el sistema de gestión de tareas MVC.
 * Implementa Serializable para persistencia en sesión o transferencia de datos.
 */
public class Tarea implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String titulo;
    private String categoria;
    private String prioridad;
    private Date fechaLimite;
    private boolean completada;

    public Tarea() {
    }

    public Tarea(int id, String titulo, String categoria, String prioridad, Date fechaLimite, boolean completada) {
        this.id = id;
        this.titulo = titulo;
        this.categoria = categoria;
        this.prioridad = prioridad;
        this.fechaLimite = fechaLimite;
        this.completada = completada;
    }

    public Tarea(String titulo, String categoria, String prioridad, Date fechaLimite, boolean completada) {
        this(0, titulo, categoria, prioridad, fechaLimite, completada);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public Date getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(Date fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public boolean isCompletada() {
        return completada;
    }

    public void setCompletada(boolean completada) {
        this.completada = completada;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tarea tarea = (Tarea) o;
        return id == tarea.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Tarea{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", categoria='" + categoria + '\'' +
                ", prioridad='" + prioridad + '\'' +
                ", fechaLimite=" + fechaLimite +
                ", completada=" + completada +
                '}';
    }
}
