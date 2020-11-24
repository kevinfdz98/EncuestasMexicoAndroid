package com.example.encuestasmexicoandroid;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.encuestasmexicoandroid.Adapters.MostrarPreguntasAdapter;
import com.example.encuestasmexicoandroid.Classes.Pregunta;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ActivityMostrarPreguntas extends AppCompatActivity implements MostrarPreguntasAdapter.onRecyclerClickListener, dialog_response.dialogResponseListener{

    // Variable of firestore to access data from firebase
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    // Reference to User data on firebase
    private final CollectionReference formRef = db.collection("Formularios");
    private final CollectionReference listasRef = db.collection("Listas");

    private RecyclerView recyclerView;
    private MostrarPreguntasAdapter mostrarPreguntasAdapter;
    private List<Pregunta> preguntaList;
    private String TAG = "display";
    private String formID;
    private int NumberofQuestions = 0;
    private DocumentReference documentReference ;
    List<String> listaDeRespuestas;
    Button guardarRespuesta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_preguntas);
        recyclerView = findViewById(R.id.recycler_view_preguntas);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        preguntaList = new ArrayList<>();
        guardarRespuesta = findViewById(R.id.Button_GUARDAR_RESPUESTAS_ENCUESTADOR);
        Intent intent = getIntent();
        formID = intent.getStringExtra("formID");
        documentReference = formRef.document(formID);
        displayQuestions(documentReference);
        guardarRespuesta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prepareObject();
            }
        });

    }

    public void displayQuestions(DocumentReference documentReference){
        preguntaList.clear();
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String,Object> auxMap = (Map<String, Object>) document.get("preguntas");
                        NumberofQuestions =  auxMap.size();
                        Map<String,String> auxQuestion;
                        for (Map.Entry<String, Object> entry : auxMap.entrySet()){
                            auxQuestion = (Map<String, String>) entry.getValue();
                            String textoPregunta = auxQuestion.get("textoPregunta");
                            String tipoPregunta = auxQuestion.get("tipoPregunta");
                            if (tipoPregunta.equals("Opción Múltiple")){
                                String respuestas = auxQuestion.get("listaRespuesta");
                                Pregunta pregunta =  new Pregunta(entry.getKey(),textoPregunta,tipoPregunta);
                                pregunta.setListaRespuesta(respuestas);
                                preguntaList.add(pregunta);
                            }else{
                                preguntaList.add(new Pregunta(entry.getKey(),textoPregunta,tipoPregunta));
                            }
                        }
                        Collections.sort(preguntaList, new Comparator<Pregunta>() {
                            @Override
                            public int compare(Pregunta pregunta, Pregunta t1) {
                                return pregunta.getPreguntaID().compareTo(t1.getPreguntaID());
                            }
                        });
                        initAdapaterRecycler();
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void prepareObject(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyy hh:mm:ss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        Map<String, Object> question = new HashMap<>();
        question.put("fechaEncuesta", currentDateandTime);
        for(Pregunta pregunta : preguntaList){
            Map<String, String> response = new HashMap<>();
            response.put("pregunta", pregunta.getTextoPregunta());
            response.put("respuesta", pregunta.getRespuesta());
            question.put(pregunta.getPreguntaID(), response);
        }
        CollectionReference RespuestasRef = db.collection("Respuestas");
        RespuestasRef.document(formID).collection("Pablo").document().set(question);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        displayQuestions(documentReference);

    }

    public void  initAdapaterRecycler(){
        mostrarPreguntasAdapter = new MostrarPreguntasAdapter(this, preguntaList, this);
        recyclerView.setAdapter(mostrarPreguntasAdapter);
    }

    @Override
    public void onPreguntaClick(int position) {
        Pregunta pregunta = preguntaList.get(position);
        String idPregunta = pregunta.getPreguntaID();
        String tipo = pregunta.getTipoPregunta();
        String texto = pregunta.getTextoPregunta();
        if(tipo.equals("Opción Múltiple")){
            String lista = pregunta.getListaRespuesta();
            openDialog(idPregunta,texto,tipo,lista, position);
        }else{
            openDialog(idPregunta,texto, tipo, "", position);
        }

    }

    public void openDialog(String preguntaID, String texto, String tipo, String lista, int position){
        dialog_response response = new dialog_response();
        Bundle bundle = new Bundle();
        bundle.putString("texto", texto);
        bundle.putString("tipo", tipo);
        bundle.putString("lista", lista);
        bundle.putString("idPregunta", preguntaID);
        bundle.putInt("position", position);
        response.setArguments(bundle);
        response.show(getSupportFragmentManager(), "Get response");
    }

    @Override
    public void applyAnswer(String idPregunta ,String response, String texto, String tipo,int Position) {
        // Here you add the answer to the questions object
        Log.d(TAG, response + idPregunta + texto + tipo + Position);
        Pregunta respuestaPregunta = new Pregunta(idPregunta, texto, tipo);
        respuestaPregunta.setRespuesta(response);
        preguntaList.set(Position, respuestaPregunta);
        Log.d(TAG, preguntaList.get(Position).getRespuesta());
    }


}