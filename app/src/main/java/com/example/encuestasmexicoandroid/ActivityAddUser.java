package com.example.encuestasmexicoandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class ActivityAddUser extends AppCompatActivity {
    private FirebaseAuth fAuth = FirebaseAuth.getInstance();
    // Variable of firestore to access data from firebase
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    // Reference to User data on firebase
    private CollectionReference userRef = db.collection("Usuarios");

    Button btnGuardar;
    Button btnCancelar;
    ToggleButton tgBtnEstatus;
    ToggleButton tgBtnTipo;
    EditText etNombreCompleto;
    EditText etDireccion;
    EditText etCorreo;
    EditText etcontraseña;
    Boolean editFlag;

    String nombre;
    String direccion;
    String correo;
    String contraseña;
    String tipo;
    String estatus;
    String uid;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        etNombreCompleto = findViewById(R.id.editTextNombreCompleto);
        etDireccion = findViewById(R.id.editTextDireccion);
        etCorreo = findViewById(R.id.editTextCorreoElectronico);
        etcontraseña = findViewById(R.id.editTextPassword);
        tgBtnTipo = findViewById(R.id.tgBtnType);
        tgBtnEstatus = findViewById(R.id.userStatus);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnCancelar = findViewById(R.id.btnCancelar);
        Intent intent = getIntent();
        editFlag =  intent.getBooleanExtra("editFlag",false);

        if (editFlag) {
            uid = intent.getStringExtra("uid");
            etNombreCompleto.setText(intent.getStringExtra("userName"));
            etDireccion.setText(intent.getStringExtra("userDireccion"));
            etCorreo.setText(intent.getStringExtra("userCorreo"));
            etcontraseña.setText("******");
            etcontraseña.setFocusable(false);
            if(intent.getStringExtra("userEstatus").equals("Activo")){tgBtnEstatus.setChecked(true);}
            else{tgBtnEstatus.setChecked(false);}
            if(intent.getStringExtra("userTipo").equals("Administrador")){tgBtnTipo.setChecked(true);}
            else{tgBtnTipo.setChecked(false);}
        }

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
               finish();
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nombre = etNombreCompleto.getText().toString();
                direccion = etDireccion.getText().toString();
                correo = etCorreo.getText().toString();
                contraseña = etcontraseña.getText().toString();
                if (tgBtnTipo.isChecked()){tipo = "Administrador";}else{tipo = "Encuestador";}
                if (tgBtnEstatus.isChecked()){estatus = "Activo";}else{estatus = "Inactivo";}
                if (editFlag){
                    editUser();
                }else{
                    createNewUser();
                }

            }
        });


    }

    void createNewUser(){
        if(nombre.equals("") || correo.equals("") || contraseña.equals("")){
            Toast.makeText(ActivityAddUser.this, "Todos los" +
                    "campos son obligatorios", Toast.LENGTH_SHORT).show();
        }else{

            fAuth.createUserWithEmailAndPassword(correo, contraseña)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String userCreated = fAuth.getUid();
                                Map<String, Object> data = new HashMap<>();
                                data.put("nombreUsuario",nombre);
                                data.put("correo", correo);
                                data.put("direccion", direccion);
                                data.put("tipo", tipo);
                                data.put("estatus", estatus);
                                data.put("uid",userCreated);
                                userRef.document(userCreated).set(data)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(ActivityAddUser.this, "Usuario" +
                                                        "creado correctamente", Toast.LENGTH_SHORT).show();
                                                setResult(RESULT_OK);
                                                finish();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                });
                            } else {
                                Toast.makeText(ActivityAddUser.this, "Algo ha salido mal" +
                                        " al conectarse con la base de datos", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    void editUser(){
        Map<String, Object> data = new HashMap<>();
        data.put("nombreUsuario",nombre);
        data.put("correo", correo);
        data.put("direccion", direccion);
        data.put("tipo", tipo);
        data.put("estatus", estatus);
        data.put("uid",uid);
        userRef.document(uid).set(data, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ActivityAddUser.this, "Usuario" +
                                "creado correctamente", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }


}