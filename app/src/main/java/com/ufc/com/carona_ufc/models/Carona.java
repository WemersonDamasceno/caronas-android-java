package com.ufc.com.carona_ufc.models;

public class Carona {
    private String id;
    private String enderecoSaida;
    private String enderecoChegada;
    private String data;
    private String hora;
    private int qtdVagas;
    private boolean checkBoxHelp;
    private String idMotorista;


    public Carona(String id, String enderecoSaida, String enderecoChegada, String data,
                  String hora, int qtdVagas, boolean checkBoxHelp, String idMotorista) {
        this.id = id;
        this.enderecoSaida = enderecoSaida;
        this.enderecoChegada = enderecoChegada;
        this.data = data;
        this.hora = hora;
        this.qtdVagas = qtdVagas;
        this.checkBoxHelp = checkBoxHelp;
        this.idMotorista = idMotorista;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdMotorista() {
        return idMotorista;
    }

    public void setIdMotorista(String idMotorista) {
        this.idMotorista = idMotorista;
    }
    public String getEnderecoSaida() {
        return enderecoSaida;
    }

    public void setEnderecoSaida(String enderecoSaida) {
        this.enderecoSaida = enderecoSaida;
    }

    public String getEnderecoChegada() {
        return enderecoChegada;
    }

    public void setEnderecoChegada(String enderecoChegada) {
        this.enderecoChegada = enderecoChegada;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public int getQtdVagas() {
        return qtdVagas;
    }

    public void setQtdVagas(int qtdVagas) {
        this.qtdVagas = qtdVagas;
    }

    public boolean isCheckBoxHelp() {
        return checkBoxHelp;
    }

    public void setCheckBoxHelp(boolean checkBoxHelp) {
        this.checkBoxHelp = checkBoxHelp;
    }
}
