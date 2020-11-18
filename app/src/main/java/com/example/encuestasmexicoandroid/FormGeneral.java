package com.example.encuestasmexicoandroid;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.ColorStateListDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class FormGeneral extends AppCompatActivity {
    // Variable of firestore to access data from firebase
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    // Reference to User data on firebase
    private final CollectionReference formulariosRef= db.collection("Formularios");

    private static final String TAG = "FormGeneral";

    private TextView mFechaInicio;
    private TextView mFechaFin;
    private DatePickerDialog.OnDateSetListener mOnDateInicioSetListener;
    private DatePickerDialog.OnDateSetListener mOnDateFinSetListener;
    private Boolean editFlag;
    private String editableForm;
    private Map<String,Object> existingQuestions = new HashMap<>();
    EditText mNombreForm;
    EditText mDescripcionForm;
    Button btnNextStep;
    Button btnCancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_general);
        Intent intent = getIntent();
        editFlag = intent.getBooleanExtra("editFlag", false);
        mNombreForm = (EditText) findViewById(R.id.editTextFormNombre);
        mDescripcionForm = (EditText) findViewById(R.id.editTextFormDescripcion);
        mFechaInicio = (TextView) findViewById(R.id.editTextFormInicio);
        mFechaFin = (TextView) findViewById(R.id.editTextFormFin);
        btnNextStep = (Button) findViewById(R.id.btnGuardar);
        btnCancelar = (Button) findViewById(R.id.btnCancelar);

        setPickerInicio();
        setPickerFin();

        if (editFlag){
            editableForm = intent.getStringExtra("formID");
            fillFields(editableForm);
        }

        btnNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(editFlag){
                    //Edit existing form
                    editForm();
                }else{
                    //Create new form
                    createForm();
                }
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    public void createForm(){
        String nombre = mNombreForm.getText().toString();
        String descripcion = mDescripcionForm.getText().toString();
        String fechaInicio = mFechaInicio.getText().toString();
        String fechaFin = mFechaFin.getText().toString();

        if (nombre.matches("") || descripcion.matches("")||
                fechaFin.matches("")||fechaInicio.matches("")){
            //All fields must be filled
            Toast.makeText(FormGeneral.this, "Todos los campos son " +
                            "obligatorios",
                    Toast.LENGTH_SHORT).show();
        }else{
            //Everything is fine create form
            final DocumentReference newFormRef = formulariosRef.document();
            Map<String,Object> preguntas = new HashMap<>();
            Map<String, Object> data = new HashMap<>();
            data.put("nombre", nombre);
            data.put("descripcion", descripcion);
            data.put("fin", fechaFin);
            data.put("inicio", fechaInicio);
            data.put("estatus", "Activo");
            data.put("version","1");
            data.put("preguntas", preguntas);
            data.put("formID", newFormRef.getId());
            newFormRef.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(FormGeneral.this, "Formulario" +
                            "creado correctamente", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    Intent secondIntent = new Intent(getApplicationContext(), FormPreguntas.class);
                    secondIntent.putExtra("formID",newFormRef.getId());
                    startActivity(secondIntent);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(FormGeneral.this, "Database ERROR",
                            Toast.LENGTH_SHORT).show();
                }
            });


        }
    }

    public void editForm(){
        String nombre = mNombreForm.getText().toString();
        String descripcion = mDescripcionForm.getText().toString();
        String fechaInicio = mFechaInicio.getText().toString();
        String fechaFin = mFechaFin.getText().toString();

        if (nombre.matches("") || descripcion.matches("")||
                fechaFin.matches("")||fechaInicio.matches("")){
            //All fields must be filled
            Toast.makeText(FormGeneral.this, "Todos los campos son " +
                            "obligatorios",
                    Toast.LENGTH_SHORT).show();
        }else{
            //Everything is fine edit form
            final DocumentReference editFormRef = formulariosRef.document(editableForm);

            Map<String, Object> data = new HashMap<>();
            data.put("nombre", nombre);
            data.put("descripcion", descripcion);
            data.put("fin", fechaFin);
            data.put("inicio", fechaInicio);
            data.put("estatus", "Activo");
            data.put("version","1");
            data.put("preguntas",existingQuestions);
            data.put("formID", editableForm);
            editFormRef.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(FormGeneral.this, "Formulario" +
                            "actualizado correctamente", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    Intent secondIntent = new Intent(getApplicationContext(), FormPreguntas.class);
                    secondIntent.putExtra("formID",editableForm);
                    startActivity(secondIntent);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(FormGeneral.this, "Database ERROR",
                            Toast.LENGTH_SHORT).show();
                }
            });


        }
    }

    public void fillFields(String formID){
        formulariosRef.document(formID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        String nombre = (String)document.get("nombre");
                        String descripcion = (String)document.get("descripcion");
                        String inicio = (String)document.get("inicio");
                        String fin = (String)document.get("fin");
                        existingQuestions = (Map<String, Object>) document.get("preguntas");

                        mNombreForm.setText(nombre);
                        mDescripcionForm.setText(descripcion);
                        mFechaInicio.setText(inicio);
                        mFechaFin.setText(fin);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void setPickerInicio() {
        mFechaInicio.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(FormGeneral.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mOnDateInicioSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mOnDateInicioSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = day + "-" + month + "-" + year;
                mFechaInicio.setText(date);
            }
        };
    }//end of setPickerInicio

    public void setPickerFin() {
        mFechaFin.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(FormGeneral.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mOnDateFinSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mOnDateFinSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = day + "-" + month + "-" + year;
                mFechaFin.setText(date);
            }
        };

    }
}