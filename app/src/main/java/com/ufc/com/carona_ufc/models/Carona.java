package com.ufc.com.carona_ufc.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

public class Carona implements Parcelable {
    private String id;
    private String enderecoSaida;
    private String enderecoChegada;
    private String data;
    private String hora;
    private int qtdVagas;
    private boolean checkBoxHelp;
    private String idMotorista;
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
    private LatLng latLngOrigem;
    private LatLng latLngDestino;

    public Carona(String id, String enderecoSaida, String enderecoChegada, String data, String hora, int qtdVagas,
                  boolean checkBoxHelp, String idMotorista, LatLng latLngOrigem, LatLng latLngDestino) {
        this.id = id;
        this.enderecoSaida = enderecoSaida;
        this.enderecoChegada = enderecoChegada;
        this.data = data;
        this.hora = hora;
        this.qtdVagas = qtdVagas;
        this.checkBoxHelp = checkBoxHelp;
        this.idMotorista = idMotorista;
        this.latLngOrigem = latLngOrigem;
        this.latLngDestino = latLngDestino;
    }

    protected Carona(Parcel in) {
        id = in.readString();
        enderecoSaida = in.readString();
        enderecoChegada = in.readString();
        data = in.readString();
        hora = in.readString();
        qtdVagas = in.readInt();
        checkBoxHelp = in.readByte() != 0;
        idMotorista = in.readString();
        latLngOrigem = in.readParcelable(LatLng.class.getClassLoader());
        latLngDestino = in.readParcelable(LatLng.class.getClassLoader());
    }

    public Carona() {

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

    public boolean isCheckBoxHelp() {
        return checkBoxHelp;
    }

    public void setCheckBoxHelp(boolean checkBoxHelp) {
        this.checkBoxHelp = checkBoxHelp;
    }

    public String getIdMotorista() {
        return idMotorista;
    }

    public void setIdMotorista(String idMotorista) {
        this.idMotorista = idMotorista;
    }

    public LatLng getLatLngOrigem() {
        return latLngOrigem;
    }

    public void setLatLngOrigem(LatLng latLngOrigem) {
        this.latLngOrigem = latLngOrigem;
    }

    public LatLng getLatLngDestino() {
        return latLngDestino;
    }

    public void setLatLngDestino(LatLng latLngDestino) {
        this.latLngDestino = latLngDestino;
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
        dest.writeByte((byte) (checkBoxHelp ? 1 : 0));
        dest.writeString(idMotorista);
        dest.writeParcelable(latLngOrigem, flags);
        dest.writeParcelable(latLngDestino, flags);
    }
}
