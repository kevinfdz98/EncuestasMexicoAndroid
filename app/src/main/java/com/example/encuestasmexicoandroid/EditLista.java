package com.example.encuestasmexicoandroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.encuestasmexicoandroid.Classes.Listas;
import com.example.encuestasmexicoandroid.ui.slideshow.SlideshowFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.List;

public class EditLista extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference listasRef = db.collection("Listas");

    private Listas listas;

    LinearLayout linearLayout;

    int contador = 1;
    int x = 0;
    EditText editTextTitle;
    EditText editTextRespuesta1;
    List<EditText> allEds = new ArrayList<EditText>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_nueva_lista);
        linearLayout = findViewById(R.id.LinearLayout);
        editTextTitle = findViewById(R.id.editTextNombre);
        editTextRespuesta1 = findViewById(R.id.Respuesta1);
        allEds.add(editTextRespuesta1);

        listas = (Listas) getIntent().getSerializableExtra("Listas");

        editTextTitle.setText(listas.getNombre());
        editTextRespuesta1.setText(listas.getRespuestas().get(0));

        for (int i = 1; i < listas.getRespuestas().size(); i++) {
            addPreguntas1(null, listas.getRespuestas().get(i));
        }

    }

    public void addPreguntas(View v) {
        contador++;
        TextView textView = new TextView(this);
        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setText("Respuesta " + contador + " *");
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setTextColor(Color.BLACK);
        linearLayout.addView(textView);

        EditText editText = new EditText(this);
        allEds.add(editText);
        editText.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        editText.setHint("Respuesta " + contador);
        editText.setId(contador);
        editText.setEms(10);
        editText.setTag("EditTextRespuesta" + contador);
        linearLayout.addView(editText);
    }

    public void addPreguntas1(View v, String string) {
        contador++;
        TextView textView = new TextView(this);
        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setText("Respuesta " + contador + " *");
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setTextColor(Color.BLACK);
        linearLayout.addView(textView);

        EditText editText = new EditText(this);
        allEds.add(editText);
        editText.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        editText.setHint("Respuesta " + contador);
        editText.setId(contador);
        editText.setEms(10);
        editText.setText(string);
        editText.setTag("EditTextRespuesta" + contador);
        linearLayout.addView(editText);
    }


    public void guardar(View v) {

        String nombre = editTextTitle.getText().toString();

        List<String> respuestas = new ArrayList<>();

        for (int i = 0; i < allEds.size(); i++) {
            String test = allEds.get(i).getText().toString();
            if (test.matches("") || nombre.matches("")) {
                Toast.makeText(EditLista.this, "Falto llenar un campo!", Toast.LENGTH_SHORT).show();
                return;
            } else {
                respuestas.add(allEds.get(i).getText().toString());
            }
        }

        Listas listas1 = new Listas(nombre, respuestas);

       listasRef.document(listas.getDocumentID()).set(listas1, SetOptions.merge())
               .addOnSuccessListener(new OnSuccessListener<Void>() {
                   @Override
                   public void onSuccess(Void aVoid) {
                       Toast.makeText(EditLista.this, "Lista modificada exitosamente!", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                   }
               }).addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
           }
       });
    }

    public void terminar(View v) {
        setResult(RESULT_CANCELED);
        finish();
    }
}