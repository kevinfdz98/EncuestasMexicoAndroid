package com.example.encuestasmexicoandroid.ui.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.encuestasmexicoandroid.Adapters.FormAdapter;
import com.example.encuestasmexicoandroid.Adapters.UserAdapter;
import com.example.encuestasmexicoandroid.AddForm;
import com.example.encuestasmexicoandroid.Classes.FormList;
import com.example.encuestasmexicoandroid.Classes.Formulario;
import com.example.encuestasmexicoandroid.Classes.Usuario;
import com.example.encuestasmexicoandroid.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment implements FormAdapter.onRecyclerClickListener {
    // Variable of firestore to access data from firebase
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    // Reference to User data on firebase
    private final CollectionReference formRef = db.collection("Formularios");

    //Variables used to implement the recycler view
    private RecyclerView recyclerViewForm;
    private FormAdapter formAdapter;
    private List<FormList> formularioList;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        recyclerViewForm = root.findViewById(R.id.recycler_view_forms);
        recyclerViewForm.setHasFixedSize(true);
        recyclerViewForm.setLayoutManager(new LinearLayoutManager(getContext()));
        formularioList = new ArrayList<>();
        displayForms();
        FloatingActionButton newFormButton = root.findViewById(R.id.button_add_form);
        newFormButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AddForm.class);
                intent.putExtra("editFlag", false);
                startActivityForResult(intent, 3);
            }
        });
        return root;
    }

    private void displayForms(){
        Task<QuerySnapshot> query;
        query = formRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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
        formAdapter = new FormAdapter(getContext(), formularioList, this);
        recyclerViewForm.setAdapter(formAdapter);
    }

    @Override
    public void onFormClick(int position) {
        FormList formList = formularioList.get(position);
        Intent intent = new Intent(getActivity(), AddForm.class);
        intent.putExtra("editFlag", true);
        intent.putExtra("formID", formList.getFormID());
        startActivityForResult(intent, 4);

    }
}