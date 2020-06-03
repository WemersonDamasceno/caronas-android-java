package com.ufc.com.carona_ufc.adapters;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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
import com.ufc.com.carona_ufc.views.OferecerCaronaActivity;
import com.ufc.com.carona_ufc.views.ProcurarCaronaActivity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CaronaAdapter extends RecyclerView.Adapter<CaronaAdapter.ViewHolderCaronas> implements Filterable {
    private static Context getContext;
    private ArrayList<Carona> listCaronas;
    private ArrayList<Carona> listCaronasAll;
    private static ItemClickListener itemClickListener;
    Filter filter = new Filter() {
        //run on background thread
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Carona> filteredList = new ArrayList<>();
            if (constraint.toString().isEmpty()) {
                filteredList.addAll(listCaronasAll);
            } else {
                for (Carona carona : listCaronasAll) {
                    if (carona.getEnderecoChegada().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filteredList.add(carona);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;

            return filterResults;
        }

        //run on a ui thread
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            listCaronas.clear();
            listCaronas.addAll((Collection<? extends Carona>) results.values);
            notifyDataSetChanged();
        }
    };

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

    public CaronaAdapter(Context context) {
        this.listCaronas = new ArrayList<>();
        this.listCaronasAll = new ArrayList<>(listCaronas);
        getContext = context;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }


    public class ViewHolderCaronas extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvNomeMotorista;
        TextView tvEndSaida;
        TextView tvEndChegada;
        TextView tvData;
        TextView tvHora;
        TextView tvQtdVagas;
        ImageView imgPerfil;
        ImageView ic_notify;
        TextView tvHorarioChegadaLista;
        ImageView ic_editar;
        ImageView ic_excluir;

        ViewHolderCaronas(@NonNull View itemView) {
            super(itemView);
            tvNomeMotorista = itemView.findViewById(R.id.tvNomeMotoristaLista);
            tvEndSaida = itemView.findViewById(R.id.tvEndSaidaLista);
            tvEndChegada = itemView.findViewById(R.id.tvEndChegadaLista);
            tvData = itemView.findViewById(R.id.tvDataSaidaLista);
            tvQtdVagas = itemView.findViewById(R.id.tvQtdVagasLista);
            tvHora = itemView.findViewById(R.id.tvHorarioSaidaLista);
            imgPerfil = itemView.findViewById(R.id.imgPerfilLista);
            ic_notify = itemView.findViewById(R.id.ic_notify);
            tvHorarioChegadaLista = itemView.findViewById(R.id.tvHorarioChegadaLista);
            ic_editar = itemView.findViewById(R.id.ic_editar);
            ic_excluir = itemView.findViewById(R.id.ic_excluir);

            itemView.setOnClickListener(this);

            //notificar o usuario quando faltar 5 min.
            ic_notify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Notificar", Toast.LENGTH_SHORT).show();
                }
            });

            //arrumar os botoes aqui
            //tirar do pegar carona
            ic_editar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Carona carona = listCaronas.get(getAdapterPosition());
                    //pegar a carona e voltar para a tela de oferecer carona
                    FirebaseFirestore.getInstance().collection("/caronas")
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                    List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
                                    for (DocumentSnapshot doc : docs) {
                                        Carona car = doc.toObject(Carona.class);
                                        if (car.getId().equals(carona.getId())) {
                                            Intent intent = new Intent(getContext, OferecerCaronaActivity.class);
                                            Carona carona = doc.toObject(Carona.class);
                                            intent.putExtra("editar", carona);
                                            getContext.startActivity(intent);
                                        }
                                    }
                                }
                            });
                }
            });


            //excluir carona do banco de dados
            ic_excluir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Carona carona = listCaronas.get(getAdapterPosition());

                    FirebaseFirestore.getInstance().collection("/caronas")
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                    List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
                                    for (DocumentSnapshot doc : docs) {
                                        Carona car = doc.toObject(Carona.class);
                                        if (car.getId().equals(carona.getId())) {
                                            FirebaseFirestore.getInstance().collection("/caronas")
                                                    .document(doc.getId())
                                                    .delete()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            Toast.makeText(CaronaAdapter.getContext, "Carona removida!", Toast.LENGTH_SHORT).show();
                                                            Log.i("teste", "Carona Deleted");
                                                            Intent intent = new Intent(getContext, ProcurarCaronaActivity.class);
                                                            getContext.startActivity(intent);
                                                        }
                                                    });
                                        }
                                    }
                                }
                            });
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
            if (carona.getIdMotorista().equals(FirebaseAuth.getInstance().getUid())) {
                ic_notify.setVisibility(View.GONE);
                ic_editar.setVisibility(View.VISIBLE);
                ic_excluir.setVisibility(View.VISIBLE);
            }

        }


    }


}
