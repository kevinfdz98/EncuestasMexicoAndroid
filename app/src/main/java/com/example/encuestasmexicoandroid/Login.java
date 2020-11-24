package com.example.encuestasmexicoandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {
    EditText mEmail, mPassword;
    Button mLoginButton;
    Button mRegisterButton;
    private String nombre;
    // Variable of firestore to access data from firebase
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth fAuth = FirebaseAuth.getInstance();
    private final CollectionReference userRef = db.collection("Usuarios");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmail = findViewById(R.id.editTextUsuario);
        mPassword = findViewById(R.id.editTextTextPassword);
        mLoginButton = findViewById(R.id.botonIngresar);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();


                fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = fAuth.getCurrentUser();
                            String idUser = user.getUid();
                            DocumentReference docRef = userRef.document(idUser);
                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {
                                            String tipoUsuario = (String)document.get("tipo");
                                            String estatusUsuario = (String)document.get("estatus");
                                            nombre = (String)document.get("nombreUsuario");
                                            if(tipoUsuario.matches("Administrador") &&
                                                    estatusUsuario.matches("Activo")){
                                                Toast.makeText(Login.this, "Admin Logged in",
                                                        Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(getApplicationContext(),
                                                        MainActivity.class));
                                            }else if (tipoUsuario.matches("Encuestador") &&
                                                    estatusUsuario.matches("Activo")){
                                                Toast.makeText(Login.this, "Encuestador Logged in",
                                                        Toast.LENGTH_SHORT).show();

                                                Intent intent = new Intent(getApplicationContext(), EncuestadorMain.class);
                                                intent.putExtra("NOMBRE", nombre);
                                                startActivity(intent);
                                            }

                                        } else {
                                            Toast.makeText(Login.this, "No existe " +
                                                    "usuario", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(Login.this, "Fallo acceso a " +
                                                "base de datos", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(Login.this, "Usuario o contraseña incorrectos",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }
}