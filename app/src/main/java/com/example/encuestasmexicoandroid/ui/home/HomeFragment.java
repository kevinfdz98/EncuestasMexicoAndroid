package com.example.encuestasmexicoandroid.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.encuestasmexicoandroid.Adapters.UserAdapter;
import com.example.encuestasmexicoandroid.Classes.Usuario;
import com.example.encuestasmexicoandroid.ActivityAddUser;
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

public class HomeFragment extends Fragment {
    // Variable of firestore to access data from firebase
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Reference to User data on firebase
    private CollectionReference userRef = db.collection("Usuarios");

    //Variables used to implement the recycler view
    private RecyclerView recyclerViewUser;
    private UserAdapter userAdapter;
    private List<Usuario> mUserList;
    private FloatingActionButton addUser;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerViewUser = root.findViewById(R.id.recycler_view_usuarios);
        recyclerViewUser.setHasFixedSize(true);
        recyclerViewUser.setLayoutManager(new LinearLayoutManager(getContext()));
        mUserList = new ArrayList<>();
        displayUsers();
        FloatingActionButton newUserBtn = root.findViewById(R.id.button_add_user);
        newUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Start new Activity here ", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent intent = new Intent(getActivity(), ActivityAddUser.class);
                startActivity(intent);
            }
        });
        return root;
    }

    private void displayUsers(){
        Task<QuerySnapshot> query;
        query = userRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String id = document.getId();
                        String nombre = (String) document.get("nombre");
                        String tipo = (String)document.get("tipo");
                        String correo = (String)document.get("correo");
                        mUserList.add(new Usuario(id, nombre, tipo, correo));
                    }
                    userAdapter = new UserAdapter(getContext(), mUserList);
                    recyclerViewUser.setAdapter(userAdapter);
                } else {
                    Log.d("NO", "Error getting documents: ", task.getException());
                }
            }
        });
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

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}