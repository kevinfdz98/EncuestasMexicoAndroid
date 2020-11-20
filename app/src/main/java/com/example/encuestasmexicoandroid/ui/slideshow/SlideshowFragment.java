package com.example.encuestasmexicoandroid.ui.slideshow;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.encuestasmexicoandroid.Adapters.ListasAdapter;
import com.example.encuestasmexicoandroid.AddNuevaLista;
import com.example.encuestasmexicoandroid.Classes.Listas;
import com.example.encuestasmexicoandroid.EditLista;
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

public class SlideshowFragment extends Fragment implements ListasAdapter.onRecyclerClickListener{

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference listasRef = db.collection("Listas");

    private RecyclerView recyclerView;
    private ListasAdapter listasAdapter;
    private List<Listas> mListasList;

    FloatingActionButton mButtonAgregarListas;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        //((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("                     Listas");
        //((AppCompatActivity)getActivity()).getSupportActionBar().setIcon(R.drawable.logo);

        recyclerView = root.findViewById(R.id.recycler_view_listas);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mListasList = new ArrayList<>();

        displayListas();

        mButtonAgregarListas = root.findViewById(R.id.button_add_listas);

        mButtonAgregarListas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNuevaLista();
            }
        });
        return root;
    }

    void AddNuevaLista() {
        Intent intent = new Intent(getActivity(), AddNuevaLista.class);
        startActivityForResult(intent, 1);
    }

    void displayListas() {
        Task<QuerySnapshot> query;
        query = listasRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    mListasList.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Listas listas = document.toObject(Listas.class);
                        listas.setDocumentID(document.getId());

                        mListasList.add(listas);
                    }
                    initAdapterRecycler();
                } else {
                    Log.d("NO", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    private void initAdapterRecycler(){
        listasAdapter = new ListasAdapter(getContext(), mListasList, this);
        recyclerView.setAdapter(listasAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            displayListas();
        }
    }

    @Override
    public void onUserClick(int position) {
        Listas listas1 = mListasList.get(position);

        Intent intent = new Intent(getActivity(), EditLista.class);
        intent.putExtra("Listas", listas1);
        startActivityForResult(intent, 1);


       /* Intent intent = new Intent(getActivity(), ActivityAddUser.class);
        Usuario user = mUserList.get(position);
        intent.putExtra("editFlag", true);
        intent.putExtra("uid", user.getId());
        intent.putExtra("userName", user.getNombreUsuario());
        intent.putExtra("userCorreo", user.getCorreoUsuario());
        intent.putExtra("userTipo", user.getTipoUsuario());
        intent.putExtra("userEstatus", user.getEstatus());
        intent.putExtra("userDireccion", user.getDireccionUsuario());
        startActivityForResult(intent, 2);*/
    }
}