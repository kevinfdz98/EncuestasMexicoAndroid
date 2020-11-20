package com.example.encuestasmexicoandroid.Classes;

import java.io.Serializable;

public class FormList implements Serializable {
    private String formID;
    private String nombreFormulario;
    private String fechaInicio;
    private String fechaFin;
    private String estatus;

    public FormList(String formID, String nombreFormulario, String fechaInicio, String fechaFin, String estatus) {
        this.formID = formID;
        this.nombreFormulario = nombreFormulario;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estatus = estatus;

    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
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
}
