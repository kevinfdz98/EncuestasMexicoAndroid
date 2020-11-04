package com.example.encuestasmexicoandroid.Classes;

public class Usuario {
    private String id;
    private String nombreUsuario;
    private String tipoUsuario;
    private String correoUsuario;

    public Usuario() {
        //Empty constuctor needed for firebase
    }

    public Usuario(String id,String nombreUsuario, String tipoUsuario, String correoUsuario) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.tipoUsuario = tipoUsuario;
        this.correoUsuario = correoUsuario;
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
}
