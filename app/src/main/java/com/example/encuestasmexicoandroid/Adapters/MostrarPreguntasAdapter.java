package com.example.encuestasmexicoandroid.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.encuestasmexicoandroid.Classes.Pregunta;
import com.example.encuestasmexicoandroid.R;

import java.util.List;

public class MostrarPreguntasAdapter extends RecyclerView.Adapter<MostrarPreguntasAdapter.mostrarPreguntasHolder> {

    private Context mContext;
    private List<Pregunta> mPreguntaList;
    private onRecyclerClickListener mOnRecyclerListener;

    public MostrarPreguntasAdapter(Context context, List<Pregunta> preguntas, onRecyclerClickListener onRecyclerCLickListener) {
        mContext = context;
        mPreguntaList = preguntas;
        mOnRecyclerListener = onRecyclerCLickListener;
    }

    @NonNull
    @Override
    public mostrarPreguntasHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.pregunta_item_2, parent, false);
        return new mostrarPreguntasHolder(view, mOnRecyclerListener);
    }

    @Override
    public void onBindViewHolder(@NonNull mostrarPreguntasHolder holder, int position) {
        Pregunta pregunta = mPreguntaList.get(position);
        String preguntaTexto = pregunta.getTextoPregunta();

        holder.pregunta_texto.setText(preguntaTexto);
    }

    @Override
    public int getItemCount() {
        return  mPreguntaList.size();
    }

    public class mostrarPreguntasHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView pregunta_texto;
        onRecyclerClickListener onRecyclerClickListener;

        public mostrarPreguntasHolder(@NonNull View itemview, onRecyclerClickListener onRecyclerClickListener) {
            super(itemview);
            pregunta_texto = itemview.findViewById(R.id.pregunta_text);
            this.onRecyclerClickListener = onRecyclerClickListener;
            itemview.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onRecyclerClickListener.onPreguntaClick(getAdapterPosition());
        }
    }

    public interface onRecyclerClickListener {
        void onPreguntaClick(int position);
    }
}
