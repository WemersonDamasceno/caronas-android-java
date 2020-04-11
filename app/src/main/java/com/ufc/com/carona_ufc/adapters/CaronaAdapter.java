package com.ufc.com.carona_ufc.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.ufc.com.carona_ufc.R;
import com.ufc.com.carona_ufc.models.Carona;

import java.util.ArrayList;

public class CaronaAdapter extends RecyclerView.Adapter<CaronaAdapter.ViewHolderCaronas> {
    private ArrayList<Carona> listCaronas;

    public CaronaAdapter(ArrayList<Carona> listCaronas) {
        this.listCaronas = listCaronas;
    }

    @NonNull
    @Override
    public ViewHolderCaronas onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.carona_list, null, false);
        return new ViewHolderCaronas(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderCaronas holder, int position) {
        holder.setDados(listCaronas.get(position));
    }

    @Override
    public int getItemCount() {
        return listCaronas.size();
    }

    public class ViewHolderCaronas extends RecyclerView.ViewHolder {
        TextView tvNomeMotorista;
        TextView tvEndSaida;
        TextView tvEndChegada;
        TextView tvData;
        TextView tvHora;
        TextView tvQtdVagas;
        ImageView imgPerfil;


        public ViewHolderCaronas(@NonNull View itemView) {
            super(itemView);
            tvNomeMotorista = itemView.findViewById(R.id.tvNomeMotoristaLista);
            tvEndSaida = itemView.findViewById(R.id.tvEndSaidaLista);
            tvEndChegada = itemView.findViewById(R.id.tvEndChegadaLista);
            tvData = itemView.findViewById(R.id.tvDataSaidaLista);
            tvQtdVagas = itemView.findViewById(R.id.tvQtdVagasLista);
            tvHora = itemView.findViewById(R.id.tvHorarioSaidaLista);
            imgPerfil = itemView.findViewById(R.id.imgPerfilLista);
        }

        public void setDados(Carona carona) {
            tvNomeMotorista.setText(carona.getNomeMotorista());
            tvEndSaida.setText(carona.getEnderecoSaida());
            tvEndChegada.setText(carona.getEnderecoChegada());
            tvData.setText(carona.getData());
            tvHora.setText(carona.getHora());
            tvQtdVagas.setText("" + carona.getQtdVagas());

            if (carona.getNomeMotorista().equals("Jose Maria")) {
                Picasso.get().load(R.drawable.img_pessoa_01).into(imgPerfil);
            } else if (carona.getNomeMotorista().equals("Lucia Cruz")) {
                Picasso.get().load(R.drawable.img_pessoa_02).into(imgPerfil);
            } else if (carona.getNomeMotorista().equals("Manuela Bu.")) {
                Picasso.get().load(R.drawable.img_pessoa_03).into(imgPerfil);
            }

        }
    }
}
