package com.ufc.com.carona_ufc.controller.dao;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.ufc.com.carona_ufc.model.Usuario;

import java.util.ArrayList;
import java.util.List;

public class UsuarioFirebaseDAO {

    public Usuario getUser(final String uid, final Usuario userRetornado) {
        FirebaseFirestore.getInstance().collection("/users")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.i("teste", "erro em buscar no bd: " + e.getMessage());
                        } else {
                            List<DocumentSnapshot> docs = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot doc : docs) {
                                Usuario user = doc.toObject(Usuario.class);
                                Log.i("teste", "Usuarios do banco: " + user.getNomeUser());
                                if (user.getIdUser().equals(uid)) {
                                    userRetornado.setNomeUser(user.getNomeUser());
                                    return;
                                }
                            }
                        }
                    }
                });
        return userRetornado;
    }

    public void salvarUsuarioBanco(final Usuario usuario) {
        FirebaseFirestore.getInstance().collection("/users")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        List<Usuario> users = new ArrayList<>();
                        Usuario user = new Usuario();
                        for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                            Usuario usuario = doc.toObject(Usuario.class);
                            users.add(usuario);
                            if (usuario.getIdUser().equals(FirebaseAuth.getInstance().getUid())) {
                                user = usuario;
                            }
                        }

                        if (!users.contains(user)) {
                            FirebaseFirestore.getInstance().collection("users")
                                    .add(usuario)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            Log.i("teste", "sucesso ao add o novo usuario" + documentReference.getId());
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.i("teste", "falha ao add o novo usuario: " + e.getMessage());
                                }
                            });
                        }
                    }
                });


    }

}
