package com.example.encuestasmexicoandroid;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.encuestasmexicoandroid.Adapters.PreguntaAdapter;
import com.example.encuestasmexicoandroid.Classes.Pregunta;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class FormPreguntas extends AppCompatActivity implements PreguntaAdapter.onRecyclerClickListener{
    // Variable of firestore to access data from firebase
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    // Reference to User data on firebase
    private final CollectionReference formRef = db.collection("Formularios");

    private RecyclerView recyclerView;
    private PreguntaAdapter preguntaAdapter;
    private List<Pregunta> preguntaList;
    private String TAG = "display";
    private String formID;
    private int NumberofQuestions = 0;
    private DocumentReference documentReference ;
    FloatingActionButton questionEdit;
    FloatingActionButton finish;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_preguntas);
        recyclerView = findViewById(R.id.recycler_view_preguntas);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        preguntaList = new ArrayList<>();
        Intent intent = getIntent();
        formID = intent.getStringExtra("formID");
        documentReference = formRef.document(formID);
        displayQuestions(documentReference);
        questionEdit = findViewById(R.id.button_add_new_pregunta);
        finish = findViewById(R.id.button_finish);
        questionEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplication(), QuestionView.class);
                intent1.putExtra("formID", formID);
                intent1.putExtra("numberQuestions", NumberofQuestions);
                startActivityForResult(intent1, 101);
            }
        });
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FormPreguntas.this, MainActivity.class);        // Specify any activity here e.g. home or splash or login etc
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("EXIT", true);
                startActivity(i);
                finish();
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
                            if (tipoPregunta.equals("Opcion Múltiple")){
                                String respuestas = auxQuestion.get("respuestas");
                                Pregunta pregunta =  new Pregunta(entry.getKey(),textoPregunta,tipoPregunta);
                                pregunta.setRespuesta(respuestas);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            displayQuestions(documentReference);

    }

    public void  initAdapaterRecycler(){
        preguntaAdapter = new PreguntaAdapter(this,preguntaList, this);
        recyclerView.setAdapter(preguntaAdapter);
    }

    @Override
    public void onPreguntaClick(int position) {
        String textoPregunta;
        Intent intent1 = new Intent(getApplication(), QuestionView.class);
        Pregunta preguntaEditar = preguntaList.get(position);
        intent1.putExtra("editFlag", true);
        intent1.putExtra("textoPregunta", preguntaEditar.getTextoPregunta());
        intent1.putExtra("tipoPregunta", preguntaEditar.getTipoPregunta());
        if(preguntaEditar.getTipoPregunta().matches("Opción Múltiple")){intent1
        .putExtra("listaRespuesta",preguntaEditar.getListaRespuesta());}
        intent1.putExtra("formID", formID);
        intent1.putExtra("numberQuestions", NumberofQuestions);
        startActivityForResult(intent1, 102);
    }
}