package com.example.encuestasmexicoandroid.Classes;

public class Usuario {
    private String id;
    private String nombreUsuario;
    private String tipoUsuario;
    private String correoUsuario;
    private String direccionUsuario;
    private String estatus;


    public Usuario() {
        //Empty constuctor needed for firebase
    }

    public Usuario(String id,String nombreUsuario, String tipoUsuario, String correoUsuario,
                   String direccionUsuario, String estatus) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.tipoUsuario = tipoUsuario;
        this.correoUsuario = correoUsuario;
        this.direccionUsuario = direccionUsuario;
        this.estatus = estatus;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public String getCorreoUsuario() {
        return correoUsuario;
    }

    public void setCorreoUsuario(String correoUsuario) {
        this.correoUsuario = correoUsuario;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDireccionUsuario() {
        return direccionUsuario;
    }

    public void setDireccionUsuario(String direccionUsuario) {
        this.direccionUsuario = direccionUsuario;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }
}
