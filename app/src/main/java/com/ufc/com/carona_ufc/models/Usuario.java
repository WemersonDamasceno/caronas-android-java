package com.ufc.com.carona_ufc.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Usuario implements Parcelable {
    private String idUser;
    private String nomeUser;
    private String emailUser;
    private String senhaUser;
    private String enderecoUser;
    private String telefoneUser;
    private String sexoUser;
    private String miniBiografiaUser;
    private String urlFotoUser;
    public static final Creator<Usuario> CREATOR = new Creator<Usuario>() {
        @Override
        public Usuario createFromParcel(Parcel in) {
            return new Usuario(in);
        }

        @Override
        public Usuario[] newArray(int size) {
            return new Usuario[size];
        }
    };
    private int avaliacao;
    private int qtdCaronasOferecidas;

    public Usuario() {
    }

    public Usuario(String idUser, String nomeUser, String emailUser, String senhaUser, String enderecoUser, String telefoneUser, String sexoUser, String miniBiografiaUser, String urlFotoUser, int avaliacao, int qtdCaronasOferecidas) {
        this.idUser = idUser;
        this.nomeUser = nomeUser;
        this.emailUser = emailUser;
        this.senhaUser = senhaUser;
        this.enderecoUser = enderecoUser;
        this.telefoneUser = telefoneUser;
        this.sexoUser = sexoUser;
        this.miniBiografiaUser = miniBiografiaUser;
        this.urlFotoUser = urlFotoUser;
        this.avaliacao = avaliacao;
        this.qtdCaronasOferecidas = qtdCaronasOferecidas;
    }

    protected Usuario(Parcel in) {
        idUser = in.readString();
        nomeUser = in.readString();
        emailUser = in.readString();
        senhaUser = in.readString();
        enderecoUser = in.readString();
        telefoneUser = in.readString();
        sexoUser = in.readString();
        miniBiografiaUser = in.readString();
        urlFotoUser = in.readString();
        avaliacao = in.readInt();
        qtdCaronasOferecidas = in.readInt();
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getNomeUser() {
        return nomeUser;
    }

    public void setNomeUser(String nomeUser) {
        this.nomeUser = nomeUser;
    }

    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    public String getSenhaUser() {
        return senhaUser;
    }

    public void setSenhaUser(String senhaUser) {
        this.senhaUser = senhaUser;
    }

    public String getEnderecoUser() {
        return enderecoUser;
    }

    public void setEnderecoUser(String enderecoUser) {
        this.enderecoUser = enderecoUser;
    }

    public String getTelefoneUser() {
        return telefoneUser;
    }

    public void setTelefoneUser(String telefoneUser) {
        this.telefoneUser = telefoneUser;
    }

    public String getSexoUser() {
        return sexoUser;
    }

    public void setSexoUser(String sexoUser) {
        this.sexoUser = sexoUser;
    }

    public String getMiniBiografiaUser() {
        return miniBiografiaUser;
    }

    public void setMiniBiografiaUser(String miniBiografiaUser) {
        this.miniBiografiaUser = miniBiografiaUser;
    }

    public String getUrlFotoUser() {
        return urlFotoUser;
    }

    public void setUrlFotoUser(String urlFotoUser) {
        this.urlFotoUser = urlFotoUser;
    }

    public int getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(int avaliacao) {
        this.avaliacao = avaliacao;
    }

    public int getQtdCaronasOferecidas() {
        return qtdCaronasOferecidas;
    }

    public void setQtdCaronasOferecidas(int qtdCaronasOferecidas) {
        this.qtdCaronasOferecidas = qtdCaronasOferecidas;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idUser);
        dest.writeString(nomeUser);
        dest.writeString(emailUser);
        dest.writeString(senhaUser);
        dest.writeString(enderecoUser);
        dest.writeString(telefoneUser);
        dest.writeString(sexoUser);
        dest.writeString(miniBiografiaUser);
        dest.writeString(urlFotoUser);
        dest.writeInt(avaliacao);
        dest.writeInt(qtdCaronasOferecidas);
    }
}
