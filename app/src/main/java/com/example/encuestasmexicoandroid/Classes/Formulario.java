package com.example.encuestasmexicoandroid.Classes;

import java.lang.reflect.Array;

public class Formulario {
    private String formID;
    private String nombreFormulario;
    private String descripcion;
    private String fechaInicio;
    private String fechaFin;
    private String estatus;
    private Pregunta[] preguntas;

    public Formulario() {
        //Empty constuctor needed for firebase
    }

    public Formulario(String formID, String nombreFormulario, String descripcion,
                      String fechaInicio, String fechaFin, String estatus, Pregunta[] preguntas) {
        this.formID = formID;
        this.nombreFormulario = nombreFormulario;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estatus = estatus;
        this.preguntas = preguntas;
    }

    public String getFormID() {
        return formID;
    }

    public void setFormID(String formID) {
        this.formID = formID;
    }

    public String getNombreFormulario() {
        return nombreFormulario;
    }

    public void setNombreFormulario(String nombreFormulario) {
        this.nombreFormulario = nombreFormulario;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public Pregunta[] getPreguntas() {
        return preguntas;
    }

    public void setPreguntas(Pregunta[] preguntas) {
        this.preguntas = preguntas;
    }
}
