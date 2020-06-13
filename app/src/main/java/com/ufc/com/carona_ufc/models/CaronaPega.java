package com.ufc.com.carona_ufc.models;

import android.os.Parcel;
import android.os.Parcelable;

public class CaronaPega implements Parcelable {
    private String idMotorista;
    private String idCarona;
    private String idUser;

    public CaronaPega(String idMotorista, String idCarona, String idUser) {
        this.idMotorista = idMotorista;
        this.idCarona = idCarona;
        this.idUser = idUser;

    }

    public static final Creator<CaronaPega> CREATOR = new Creator<CaronaPega>() {
        @Override
        public CaronaPega createFromParcel(Parcel in) {
            return new CaronaPega(in);
        }

        @Override
        public CaronaPega[] newArray(int size) {
            return new CaronaPega[size];
        }
    };

    public CaronaPega() {
    }

    protected CaronaPega(Parcel in) {
        idMotorista = in.readString();
        idCarona = in.readString();
        idUser = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idMotorista);
        dest.writeString(idCarona);
        dest.writeString(idUser);
    }
}
