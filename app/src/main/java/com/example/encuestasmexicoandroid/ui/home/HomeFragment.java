package com.example.encuestasmexicoandroid.ui.home;

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

public class HomeFragment extends Fragment implements UserAdapter.onRecyclerClickListener{
    // Variable of firestore to access data from firebase
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Reference to User data on firebase
    private final CollectionReference userRef = db.collection("Usuarios");

    //Variables used to implement the recycler view
    private RecyclerView recyclerViewUser;
    private UserAdapter userAdapter;
    private List<Usuario> mUserList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("                   Usuarios");
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
                intent.putExtra("editFlag", false);
                startActivityForResult(intent, 1);
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
                    mUserList.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String id = document.getId();
                        String nombre = (String) document.get("nombreUsuario");
                        String tipo = (String)document.get("tipo");
                        String correo = (String)document.get("correo");
                        String direccion =(String)document.get("direccion");
                        String estatus = (String)document.get("estatus");
                        mUserList.add(new Usuario(id,nombre,tipo, correo,
                                direccion, estatus));
                    }
                    initAdapterRecycler();
                } else {
                    Log.d("NO", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    private void initAdapterRecycler(){
        userAdapter = new UserAdapter(getContext(), mUserList, this);
        recyclerViewUser.setAdapter(userAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            displayUsers();
        }
    }

    @Override
    public void onUserClick(int position) {
        Intent intent = new Intent(getActivity(), ActivityAddUser.class);
        Usuario user = mUserList.get(position);
        intent.putExtra("editFlag", true);
        intent.putExtra("uid", user.getId());
        intent.putExtra("userName", user.getNombreUsuario());
        intent.putExtra("userCorreo", user.getCorreoUsuario());
        intent.putExtra("userTipo", user.getTipoUsuario());
        intent.putExtra("userEstatus", user.getEstatus());
        intent.putExtra("userDireccion", user.getDireccionUsuario());
        startActivityForResult(intent, 2);
    }
}