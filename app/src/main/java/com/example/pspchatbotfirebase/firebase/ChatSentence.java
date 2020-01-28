package com.example.pspchatbotfirebase.firebase;

import java.util.HashMap;
import java.util.Map;

public class ChatSentence {

    private String fraseEn, fraseEs, usuario, hora;

    public ChatSentence() {
    }

    public ChatSentence(String fraseEn, String fraseEs, String usuario, String hora) {
        this.fraseEn = fraseEn;
        this.fraseEs = fraseEs;
        this.usuario = usuario;
        this.hora = hora;
    }


    public String getFraseEn() {
        return fraseEn;
    }

    public void setFraseEn(String fraseEn) {
        this.fraseEn = fraseEn;
    }

    public String getFraseEs() {
        return fraseEs;
    }

    public void setFraseEs(String fraseEs) {
        this.fraseEs = fraseEs;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    @Override
    public String toString() {
        return "ChatSentence{" +
                "fraseEn='" + fraseEn + '\'' +
                ", fraseEs='" + fraseEs + '\'' +
                ", usuario='" + usuario + '\'' +
                ", hora='" + hora + '\'' +
                '}';
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("fraseEn", fraseEn);
        result.put("fraseEs", fraseEs);
        result.put("usuario", usuario);
        result.put("hora", hora);
        return result;
    }
}
