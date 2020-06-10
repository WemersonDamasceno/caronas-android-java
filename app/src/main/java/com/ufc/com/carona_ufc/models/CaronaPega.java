package com.ufc.com.carona_ufc.models;

public class CaronaPega {
    private String idMotorista;
    private String idCarona;
    private String idUser;

    public CaronaPega(String idMotorista, String idCarona, String idUser) {
        this.idMotorista = idMotorista;
        this.idCarona = idCarona;
        this.idUser = idUser;

    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdMotorista() {
        return idMotorista;
    }

    public void setIdMotorista(String idMotorista) {
        this.idMotorista = idMotorista;
    }

    public String getIdCarona() {
        return idCarona;
    }

    public void setIdCarona(String idCarona) {
        this.idCarona = idCarona;
    }
}
