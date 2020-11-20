package com.example.encuestasmexicoandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.encuestasmexicoandroid.Adapters.FormAdapter;
import com.example.encuestasmexicoandroid.Classes.FormList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class EncuestadorMain extends AppCompatActivity implements FormAdapter.onRecyclerClickListener {
    // Variable of firestore to access data from firebase
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    // Reference to User data on firebase
    private final CollectionReference formRef = db.collection("Formularios");

    //Variables used to implement the recycler view
    private RecyclerView recyclerViewForm;
    private FormAdapter formAdapter;
    private List<FormList> formularioList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encuestador_main);
        recyclerViewForm = findViewById(R.id.recycler_view_forms_encuestador);
        recyclerViewForm.setHasFixedSize(true);
        recyclerViewForm.setLayoutManager(new LinearLayoutManager(this));
        formularioList = new ArrayList<>();
        displayForms();
    }

    private void displayForms() {
        Task<QuerySnapshot> query;
        query = formRef.whereEqualTo("estatus","Activo").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            formularioList.clear();
                            for(QueryDocumentSnapshot document : task.getResult()){
                                String id = document.getId();
                                String nombre = (String)document.get("nombre");
                                String inicio = (String)document.get("inicio");
                                String fin = (String)document.get("fin");
                                String estatus = (String)document.get("estatus");
                                formularioList.add(new FormList(id,nombre,inicio,fin,estatus));
                            }
                            initAdapterRecycler();
                        }else{
                            Log.d("NO", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
    private void initAdapterRecycler(){
        formAdapter = new FormAdapter(this, formularioList, this);
        recyclerViewForm.setAdapter(formAdapter);
    }

    @Override
    public void onFormClick(int position) {


    }
}