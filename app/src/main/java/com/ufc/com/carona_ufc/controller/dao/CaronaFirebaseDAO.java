package com.ufc.com.carona_ufc.controller.dao;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ufc.com.carona_ufc.model.Carona;

public class CaronaFirebaseDAO {

    public void salvarCarona(Carona carona) {
        FirebaseFirestore.getInstance().collection("caronas")
                .add(carona)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.i("teste", "Sucesso ao add a carona no banco");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("teste", "Falha ao add a carona no banco");
            }
        });
    }
}
