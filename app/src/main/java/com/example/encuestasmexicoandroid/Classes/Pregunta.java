package com.example.encuestasmexicoandroid.Classes;

public class Pregunta {
    private String preguntaID;
    private String textoPregunta;
    private String tipoPregunta;
    private String respuesta;

    public Pregunta() {
        //Empty constuctor needed for firebase
    }

    public Pregunta(String preguntaID, String textoPregunta, String tipoPregunta) {
        this.preguntaID = preguntaID;
        this.textoPregunta = textoPregunta;
        this.tipoPregunta = tipoPregunta;
    }

    public String getPreguntaID() {
        return preguntaID;
    }

    public void setPreguntaID(String preguntaID) {
        this.preguntaID = preguntaID;
    }

    public String getTextoPregunta() {
        return textoPregunta;
    }

    public void setTextoPregunta(String textoPregunta) {
        this.textoPregunta = textoPregunta;
    }

    public String getTipoPregunta() {
        return tipoPregunta;
    }

    public void setTipoPregunta(String tipoPregunta) {
        this.tipoPregunta = tipoPregunta;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }
}
