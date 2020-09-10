package com.ufc.com.carona_ufc.controller.adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
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
import com.ufc.com.carona_ufc.controller.interfaces.ItemClickListener;
import com.ufc.com.carona_ufc.model.Carona;
import com.ufc.com.carona_ufc.model.Usuario;
import com.ufc.com.carona_ufc.views.OferecerCaronaActivity;
import com.ufc.com.carona_ufc.views.PegarCaronaActivity;
import com.ufc.com.carona_ufc.views.ProcurarCaronaActivity;

import java.util.ArrayList;
import java.util.List;

public class CaronaAdapter extends RecyclerView.Adapter<CaronaAdapter.ViewHolderCaronas> {
    @SuppressLint("StaticFieldLeak")
    private static Context getContext;
    private ArrayList<Carona> listCaronas;
    private static ItemClickListener itemClickListener;

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
        @SuppressLint("InflateParams") View view = LayoutInflater.from(parent.getContext())
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
        getContext = context;
    }


    public class ViewHolderCaronas extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvNomeMotorista;
        TextView tvEndSaida;
        TextView tvEndChegada;
        TextView tvData;
        TextView tvHora;
        TextView tvQtdVagas;
        ImageView imgPerfil;
        TextView tvHorarioChegadaLista;
        ImageView ic_editar;
        ImageView ic_excluir;
        TextView tvAvaliacao;


        ViewHolderCaronas(@NonNull final View itemView) {
            super(itemView);
            tvNomeMotorista = itemView.findViewById(R.id.tvNomeMotoristaLista);
            tvEndSaida = itemView.findViewById(R.id.tvEndSaidaLista);
            tvEndChegada = itemView.findViewById(R.id.tvEndChegadaLista);
            tvData = itemView.findViewById(R.id.tvDataSaidaLista);
            tvQtdVagas = itemView.findViewById(R.id.tvQtdVagasLista);
            tvHora = itemView.findViewById(R.id.tvHorarioSaidaLista);
            imgPerfil = itemView.findViewById(R.id.imgPerfilLista);
            tvHorarioChegadaLista = itemView.findViewById(R.id.tvHorarioChegadaLista);
            ic_editar = itemView.findViewById(R.id.ic_editar);
            ic_excluir = itemView.findViewById(R.id.ic_excluir);
            tvAvaliacao = itemView.findViewById(R.id.avaliacaolista);

            //itemView.setOnClickListener(this);
            //teste
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Carona carona = getListCaronas().get(getAdapterPosition());
                    Intent intent1 = new Intent(getContext, PegarCaronaActivity.class);
                    intent1.putExtra("carona", carona);
                    getContext.startActivity(intent1);
                }
            });


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
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    FirebaseFirestore.getInstance().collection("/caronas")
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                    List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
                                    for (DocumentSnapshot doc : docs) {
                                        Carona car = doc.toObject(Carona.class);
                                        if (car.getId().equals(carona.getId())) {
                                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                                            db.collection("/caronas")
                                                    .document(doc.getId())
                                                    .delete()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            Toast.makeText(CaronaAdapter.getContext, "Carona removida!", Toast.LENGTH_SHORT).show();
                                                            Log.i("teste", "Carona Deleted");
                                                        }
                                                    });
                                            findUserForUpdate();
                                            Intent intent = new Intent(getContext, ProcurarCaronaActivity.class);
                                            //Utilizando animação
                                            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeCustomAnimation(getContext, R.anim.fade_in, R.anim.fade_out);
                                            ActivityCompat.startActivity(getContext, intent, activityOptionsCompat.toBundle());
                                        }
                                    }
                                }
                            });
                }
            });


        }

        void findUserForUpdate() {
            FirebaseFirestore.getInstance().collection("users")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                                Usuario usuario = doc.toObject(Usuario.class);
                                if (usuario.getIdUser().equals(FirebaseAuth.getInstance().getUid())) {
                                    //Achei o user para att os dados
                                    updateCaronas(doc);
                                }
                            }
                        }
                    });
        }

        private void updateCaronas(final DocumentSnapshot doc) {
            final Usuario user = doc.toObject(Usuario.class);
            final FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("caronas")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            int qtd = 0;
                            for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                                Carona carona = doc.toObject(Carona.class);
                                if (carona.getIdMotorista().equals(user.getIdUser())) {
                                    qtd++;
                                }
                            }
                            final int finalQtd = qtd;
                            db.collection("users")
                                    .document(doc.getId())
                                    .update("qtdCaronasOferecidas", qtd)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Log.i("teste", "Incremento ok, qtd: " + finalQtd);
                                        }
                                    });
                        }
                    });
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
                                    float d = user.getAvaliacao();
                                    tvAvaliacao.setText(String.valueOf(d));
                                    Log.i("teste", "valor: " + user.getAvaliacao());
                                }
                            }
                        }
                    });
            if (carona.getIdMotorista().equals(FirebaseAuth.getInstance().getUid())) {
                ic_editar.setVisibility(View.VISIBLE);

                ic_excluir.setVisibility(View.VISIBLE);
            }

        }


        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(getAdapterPosition());
            }
        }
    }


}
