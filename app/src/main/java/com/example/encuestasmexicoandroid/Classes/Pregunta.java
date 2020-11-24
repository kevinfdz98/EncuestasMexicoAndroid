package com.example.encuestasmexicoandroid.Classes;

public class Pregunta {
    private String preguntaID;
    private String textoPregunta;
    private String tipoPregunta;
    private String listaRespuesta;
    private String respuesta;
    private Boolean contesto = false;

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

    public String getListaRespuesta() {
        return listaRespuesta;
    }

    public void setListaRespuesta(String listaRespuesta) {
        this.listaRespuesta = listaRespuesta;
    }

    public int compareTo(Pregunta other) {
        return preguntaID.compareTo(other.preguntaID);
    }

    public Boolean getContesto() {
        return contesto;
    }

    public void setContesto(Boolean contesto) {
        this.contesto = contesto;
    }

}
