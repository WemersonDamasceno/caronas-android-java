package com.ufc.com.carona_ufc.dao;

import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.ufc.com.carona_ufc.models.Usuario;

import java.util.List;

public class UsuarioFirebaseDAO {
    private Usuario usuarioRetornado;

    public UsuarioFirebaseDAO() {
        usuarioRetornado = new Usuario();
    }

    public Usuario getUser(final String uid) {
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
                                    usuarioRetornado = user;
                                    return;
                                }
                            }
                        }
                    }
                });

        Log.i("teste", "nomeDAO: " + usuarioRetornado.getNomeUser());
        return usuarioRetornado;
    }

}
