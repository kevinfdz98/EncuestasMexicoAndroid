package com.example.encuestasmexicoandroid;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.AndroidException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.Any;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;

public class dialog_response extends AppCompatDialogFragment {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference listasRef = db.collection("Listas");

    private EditText editText;
    private dialogResponseListener listener;
    Spinner spinnerAnswers;
    String tipo;
    String texto;
    String lista;
    String idPregunta;
    int position;
    String [] si_no = {"SI", "NO"};
    String [] rango = {"1","2","3","4", "5","6","7","8","9","10"};
    ArrayList<String> prueba;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_response, null);
        spinnerAnswers = view.findViewById(R.id.spinner_Encuestador);
        editText = view.findViewById(R.id.dialog_response_get);
        Bundle bundle = getArguments();
       texto = bundle.getString("texto");
       tipo = bundle.getString("tipo");
       idPregunta = bundle.getString("idPregunta");
       position = bundle.getInt("position");
       prueba = new ArrayList<>();
        if (tipo.equals("Opción Múltiple")){
           lista =  bundle.getString("lista");
            getOpciones();
        }else{
            if(tipo.equals("Pregunta Abierta") || tipo.equals("Código Postal") || tipo.equals("Númerica") ){
                spinnerAnswers.setVisibility(View.GONE);
            }else {adaptRepsonse();}

        }


        builder.setView(view).setTitle(bundle.getString("texto"))
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String response = editText.getText().toString();
                        listener.applyAnswer(idPregunta, response, texto, tipo, position);

                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (dialogResponseListener) context;
        } catch (ClassCastException e) {
           throw new ClassCastException(context.toString() + "must implement ExampleDialog");
        }

    }

    public interface dialogResponseListener{
        void applyAnswer(String idPregunta ,String response, String texto, String tipo,int Position);
    }

    public void getOpciones(){
       listasRef.whereEqualTo("nombre", lista)
               .get()
               .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                   @Override
                   public void onComplete(@NonNull Task<QuerySnapshot> task) {
                       if (task.isSuccessful()) {
                           for (QueryDocumentSnapshot document : task.getResult()) {
                              Log.d("TAG", document.getId() + " => " + document.getData());
                              prueba = (ArrayList<String>) document.get("respuestas");

                           }
                           adaptRepsonse();
                       } else {
                           Log.d("TAG", "Error getting documents: ", task.getException());
                       }
                   }
               });
    }

    public void adaptRepsonse(){
        if (tipo.equals("SI/NO")){ ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, si_no);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerAnswers.setAdapter(arrayAdapter);
        }
        if(tipo.equals("Del 1 al 10")){ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, rango);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerAnswers.setAdapter(arrayAdapter);
        }
        if (tipo.equals("Opción Múltiple")){
            String[] prueba2 = prueba.toArray(new String[0]);
            ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, prueba2);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerAnswers.setAdapter(arrayAdapter);
        }
        editText.setVisibility(View.GONE);

        spinnerAnswers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    editText.setText(adapterView.getItemAtPosition(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //
    }
}
