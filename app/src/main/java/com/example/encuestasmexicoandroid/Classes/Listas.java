package com.example.encuestasmexicoandroid.Classes;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Listas implements Serializable {

    private String documentID;
    private String nombre;
    List<String> respuestas;

    public Listas() {}

    public Listas(String nombre, List<String> respuestas) {
        this.nombre = nombre;
        this.respuestas = respuestas;
    }

    @Exclude
    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<String> getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(List<String> respuestas) {
        this.respuestas = respuestas;
    }
}
