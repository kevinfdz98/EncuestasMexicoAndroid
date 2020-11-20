package com.example.encuestasmexicoandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.encuestasmexicoandroid.Classes.FormList;
import com.example.encuestasmexicoandroid.Classes.Pregunta;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.protobuf.Any;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionView extends AppCompatActivity {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference listasRef = db.collection("Listas");
    private final CollectionReference formRef = db.collection("Formularios");

    Spinner spinnerOptions;
    Spinner spinnerRespuestas;
    EditText textoPregunta;
    TextView outputSpinner;
    TextView tituloListas;
    TextView outputSpinnerListas;
    LinearLayout layoutSpinner;
    Button btnGuardar;
    Button btnCancelar;
    private String TAG = "display";
    private String formID;
    private int numberOfQuestions;
    private String version = "1";
    Boolean editFlag = false;


    String [] mOptions = {"Pregunta Abierta", "SI/NO", "Del 1 al 10",
        "Opción Múltiple", "Númerica", "Código Postal"};
    List<String> listaDeRespuestas;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_view);
        textoPregunta = findViewById(R.id.editTextTextoPregunta);
        spinnerOptions = findViewById(R.id.spinnerOption_pregunta);
        outputSpinner = findViewById(R.id.outPutSpinner);
        spinnerRespuestas = findViewById(R.id.spinnerOption_Listas);
        tituloListas = findViewById(R.id.titulolistas);
        layoutSpinner = findViewById(R.id.layoutSpinner);
        outputSpinnerListas = findViewById(R.id.outPutListasSpinner);
        btnGuardar = findViewById(R.id.btn_guardar_pregunta_nueva);
        btnCancelar = findViewById(R.id.btn_cancelar_pregunta_nueva);
        listaDeRespuestas = new ArrayList<>();
        Intent intent = getIntent();
        formID = intent.getStringExtra("formID");
        numberOfQuestions = intent.getIntExtra("numberQuestions", 0);
        editFlag = intent.getBooleanExtra("editFlag", false);
        getListas();
        setSpinners();
        setButtons();

        if (editFlag){
            fillInfo();
        }


    }

public void fillInfo(){
        Intent intent = getIntent();
        intent.getStringExtra("textoPregunta");
}
    public void setButtons(){
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createQuestion();
            }
        });
    }


    public void setSpinners(){
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, mOptions);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOptions.setAdapter(arrayAdapter);
        spinnerOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                outputSpinner.setText(adapterView.getItemAtPosition(i).toString());
                if (i == 3){
                    tituloListas.setVisibility(View.VISIBLE);
                    layoutSpinner.setVisibility(View.VISIBLE);
                    spinnerRespuestas.setVisibility(View.VISIBLE);
                }else{
                    tituloListas.setVisibility(View.GONE);
                    layoutSpinner.setVisibility(View.GONE);
                    spinnerRespuestas.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter arrayAdapter1 = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, listaDeRespuestas);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRespuestas.setAdapter(arrayAdapter1);
        spinnerRespuestas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    outputSpinnerListas.setText(adapterView.getItemAtPosition(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    public void createQuestion(){
        String preguntaTexto = textoPregunta.getText().toString();
        String tipoPregunta = outputSpinner.getText().toString();
        String listaRespuestas = outputSpinnerListas.getText().toString();
        numberOfQuestions = numberOfQuestions + 1;
        String idQuestion = "v" + version + "p" + numberOfQuestions;


        if (preguntaTexto.matches("")){
            Toast.makeText(QuestionView.this, "Todos los campos son " +
                            "obligatorios",
                    Toast.LENGTH_SHORT).show();
        }else{
            Map<String, String> question = new HashMap<>();
            question.put("textoPregunta",preguntaTexto);
            question.put("tipoPregunta",tipoPregunta);

            if (tipoPregunta.matches("Opción Múltiple"))
            {question.put("listaRespuesta",listaRespuestas);}
            Map<String, Map> fullQuestion = new HashMap<>();
            Map<String, Map> preguntas= new HashMap<>();
            fullQuestion.put(idQuestion,question);
            preguntas.put("preguntas",fullQuestion);
           final DocumentReference documentReference = formRef.document(formID);
           documentReference.set(preguntas, SetOptions.merge())
                   .addOnSuccessListener(new OnSuccessListener<Void>() {
               @Override
               public void onSuccess(Void aVoid) {
                   Toast.makeText(QuestionView.this, "Pregunta" +
                           "creada correctamente", Toast.LENGTH_SHORT).show();
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

    public void getListas(){
        Task<QuerySnapshot> query;
        query = listasRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    listaDeRespuestas.clear();
                    for(QueryDocumentSnapshot document : task.getResult()){
                       String nombreLista = (String) document.get("nombre");
                       listaDeRespuestas.add(nombreLista);
                    }
                }else{
                    Log.d("NO", "Error getting documents: ", task.getException());
                }
            }
        });
    }
}