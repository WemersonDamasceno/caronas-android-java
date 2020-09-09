package com.ufc.com.carona_ufc.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Carona implements Parcelable {
    private String id;
    private String enderecoSaida;
    private String enderecoChegada;
    private String data;
    private String hora;
    private int qtdVagas;
    private String valorCarona;
    private String idMotorista;
    private double latOrigem;
    private double lngOrigem;
    private double latDestino;
    private double lngDestino;
    private String horaChegadaprox;

    public static final Creator<Carona> CREATOR = new Creator<Carona>() {
        @Override
        public Carona createFromParcel(Parcel in) {
            return new Carona(in);
        }

        @Override
        public Carona[] newArray(int size) {
            return new Carona[size];
        }
    };

    public Carona() {
    }

    public Carona(String id, String enderecoSaida, String enderecoChegada, String data, String hora, int qtdVagas, String valorCarona, String idMotorista, double latOrigem, double lngOrigem, double latDestino, double lngDestino, String horaChegadaprox) {
        this.id = id;
        this.enderecoSaida = enderecoSaida;
        this.enderecoChegada = enderecoChegada;
        this.data = data;
        this.hora = hora;
        this.qtdVagas = qtdVagas;
        this.valorCarona = valorCarona;
        this.idMotorista = idMotorista;
        this.latOrigem = latOrigem;
        this.lngOrigem = lngOrigem;
        this.latDestino = latDestino;
        this.lngDestino = lngDestino;
        this.horaChegadaprox = horaChegadaprox;
    }

    protected Carona(Parcel in) {
        id = in.readString();
        enderecoSaida = in.readString();
        enderecoChegada = in.readString();
        data = in.readString();
        hora = in.readString();
        qtdVagas = in.readInt();
        valorCarona = in.readString();
        idMotorista = in.readString();
        latOrigem = in.readDouble();
        lngOrigem = in.readDouble();
        latDestino = in.readDouble();
        lngDestino = in.readDouble();
        horaChegadaprox = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getValorCarona() {
        return valorCarona;
    }

    public void setValorCarona(String valorCarona) {
        this.valorCarona = valorCarona;
    }

    public String getIdMotorista() {
        return idMotorista;
    }

    public void setIdMotorista(String idMotorista) {
        this.idMotorista = idMotorista;
    }

    public double getLatOrigem() {
        return latOrigem;
    }

    public void setLatOrigem(double latOrigem) {
        this.latOrigem = latOrigem;
    }

    public double getLngOrigem() {
        return lngOrigem;
    }

    public void setLngOrigem(double lngOrigem) {
        this.lngOrigem = lngOrigem;
    }

    public double getLatDestino() {
        return latDestino;
    }

    public void setLatDestino(double latDestino) {
        this.latDestino = latDestino;
    }

    public double getLngDestino() {
        return lngDestino;
    }

    public void setLngDestino(double lngDestino) {
        this.lngDestino = lngDestino;
    }

    public String getHoraChegadaprox() {
        return horaChegadaprox;
    }

    public void setHoraChegadaprox(String horaChegadaprox) {
        this.horaChegadaprox = horaChegadaprox;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(enderecoSaida);
        dest.writeString(enderecoChegada);
        dest.writeString(data);
        dest.writeString(hora);
        dest.writeInt(qtdVagas);
        dest.writeString(valorCarona);
        dest.writeString(idMotorista);
        dest.writeDouble(latOrigem);
        dest.writeDouble(lngOrigem);
        dest.writeDouble(latDestino);
        dest.writeDouble(lngDestino);
        dest.writeString(horaChegadaprox);
    }
}