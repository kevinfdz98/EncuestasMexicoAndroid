package com.example.encuestasmexicoandroid.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.encuestasmexicoandroid.Adapters.UsuarioAdapter;
import com.example.encuestasmexicoandroid.Classes.Usuario;
import com.example.encuestasmexicoandroid.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class HomeFragment extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference userRef = db.collection("Usuarios");
    private UsuarioAdapter usuarioAdapter;
    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
       /* homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);*/

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.recycler_view_usuarios);
        setUpRecyclerView(root);
        return root;
    }

    private void setUpRecyclerView(View view){
       /* Task<QuerySnapshot> query;
        query = userRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("debuggin", document.getId() + " => " + document.getData());
                    }
                } else {
                    Log.d("NO", "Error getting documents: ", task.getException());
                }
            }
        });*/

        Query query2;
        query2 = userRef;
        FirestoreRecyclerOptions<Usuario> options = new FirestoreRecyclerOptions.Builder<Usuario>()
                .setQuery(query2,Usuario.class)
                .build();

        usuarioAdapter = new UsuarioAdapter(options);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_usuarios);
        //recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(usuarioAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        usuarioAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        usuarioAdapter.stopListening();
    }
}