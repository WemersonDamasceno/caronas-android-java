package com.ufc.com.carona_ufc.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;
import com.ufc.com.carona_ufc.R;
import com.ufc.com.carona_ufc.interfaces.ItemClickListener;
import com.ufc.com.carona_ufc.models.Carona;
import com.ufc.com.carona_ufc.models.Usuario;

import java.util.ArrayList;
import java.util.List;

public class CaronaAdapter extends RecyclerView.Adapter<CaronaAdapter.ViewHolderCaronas> {
    private ArrayList<Carona> listCaronas;
    private static ItemClickListener itemClickListener;

    public CaronaAdapter() {
        this.listCaronas = new ArrayList<>();
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener) {
        CaronaAdapter.itemClickListener = itemClickListener;
    }

    public ArrayList<Carona> getListCaronas() {
        return listCaronas;
    }

    public void setListCaronas(ArrayList<Carona> listCaronas) {
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

    public void add(Carona carona) {
        listCaronas.add(carona);
        notifyDataSetChanged();
    }

    public void delete(final Carona carona) {
        listCaronas.remove(carona);
        notifyDataSetChanged();
    }

    public void filterList(ArrayList<Carona> filteredList) {
        listCaronas = filteredList;
        notifyDataSetChanged();
    }


    public class ViewHolderCaronas extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvNomeMotorista;
        TextView tvEndSaida;
        TextView tvEndChegada;
        TextView tvData;
        TextView tvHora;
        TextView tvQtdVagas;
        ImageView imgPerfil;
        ImageView btnNotify;
        TextView tvHorarioChegadaLista;

        ViewHolderCaronas(@NonNull View itemView) {
            super(itemView);
            tvNomeMotorista = itemView.findViewById(R.id.tvNomeMotoristaLista);
            tvEndSaida = itemView.findViewById(R.id.tvEndSaidaLista);
            tvEndChegada = itemView.findViewById(R.id.tvEndChegadaLista);
            tvData = itemView.findViewById(R.id.tvDataSaidaLista);
            tvQtdVagas = itemView.findViewById(R.id.tvQtdVagasLista);
            tvHora = itemView.findViewById(R.id.tvHorarioSaidaLista);
            imgPerfil = itemView.findViewById(R.id.imgPerfilLista);
            btnNotify = itemView.findViewById(R.id.btnNotify);
            tvHorarioChegadaLista = itemView.findViewById(R.id.tvHorarioChegadaLista);

            itemView.setOnClickListener(this);

            //notificar o usuario quando faltar 5 min.
            btnNotify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Notificar", Toast.LENGTH_SHORT).show();
                }
            });


        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(getAdapterPosition());
            }
        }

        void setDados(final Carona carona) {
            FirebaseFirestore.getInstance().collection("/users")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot doc : docs) {
                                Usuario user = doc.toObject(Usuario.class);
                                if (carona.getIdMotorista().equals(user.getIdUser())) {
                                    String[] firstName = user.getNomeUser().split(" ");
                                    tvNomeMotorista.setText(firstName[0]);

                                    Picasso.get().load(user.getUrlFotoUser()).into(imgPerfil);

                                    tvEndSaida.setText(carona.getEnderecoSaida());
                                    tvEndChegada.setText(carona.getEnderecoChegada());
                                    tvData.setText(carona.getData());
                                    tvHora.setText(carona.getHora());
                                    tvQtdVagas.setText(String.valueOf(carona.getQtdVagas()));
                                    tvHorarioChegadaLista.setText(carona.getHoraChegadaprox());


                                }
                            }
                        }
                    });
        }


    }


}
