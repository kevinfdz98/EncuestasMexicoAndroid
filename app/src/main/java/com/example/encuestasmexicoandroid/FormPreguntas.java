package com.example.encuestasmexicoandroid;

import androidx.annotation.NonNull;
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
    FloatingActionButton questionEdit;


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
        final DocumentReference documentReference = formRef.document(formID);
        displayQuestions(documentReference);
        questionEdit = findViewById(R.id.button_add_new_pregunta);
        questionEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplication(), QuestionView.class);
                intent1.putExtra("editForm", formID);
                startActivityForResult(intent1, 101);
            }
        });


    }

    public void displayQuestions(DocumentReference documentReference){
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

    public void  initAdapaterRecycler(){
        preguntaAdapter = new PreguntaAdapter(this,preguntaList, this);
        recyclerView.setAdapter(preguntaAdapter);
    }

    @Override
    public void onPreguntaClick(int position) {
        String textoPregunta;
        Intent intent1 = new Intent(getApplication(), QuestionView.class);
        intent1.putExtra("editForm", formID);
        intent1.putExtra("numberQuestions", NumberofQuestions);
        startActivityForResult(intent1, 102);
    }
}