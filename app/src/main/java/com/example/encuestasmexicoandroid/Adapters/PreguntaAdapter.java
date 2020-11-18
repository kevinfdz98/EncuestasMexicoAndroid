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

import org.w3c.dom.Text;

import java.util.List;

public class PreguntaAdapter extends RecyclerView.Adapter<PreguntaAdapter.preguntaHolder>{
    private Context mContext;
    private List<Pregunta> mPreguntasList;
    private onRecyclerClickListener mOnRecyclerListener;

    public PreguntaAdapter(Context context, List<Pregunta> preguntas, onRecyclerClickListener onRecyclerClickListener ){
        mContext = context;
        mPreguntasList = preguntas;
        mOnRecyclerListener = onRecyclerClickListener;
    }

    @NonNull
    @Override
    public preguntaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.pregunta_item,parent,false);
        return new preguntaHolder(view, mOnRecyclerListener);
    }

    @Override
    public void onBindViewHolder(@NonNull preguntaHolder holder, int position) {
        Pregunta pregunta = mPreguntasList.get(position);
        String preguntaTexto = pregunta.getTextoPregunta();
        String preguntaTipo = pregunta.getTipoPregunta();

        holder.pregunta_texto.setText(preguntaTexto);
        holder.pregunta_tipo.setText(preguntaTipo);

    }

    @Override
    public int getItemCount() {
        return mPreguntasList.size();
    }

    public class preguntaHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView pregunta_texto;
        public TextView pregunta_tipo;
        onRecyclerClickListener onRecyclerClickListener;

        public preguntaHolder(@NonNull View itemView, onRecyclerClickListener onRecyclerClickListener) {
            super(itemView);
            pregunta_texto = itemView.findViewById(R.id.pregunta_text_view_texto);
            pregunta_tipo = itemView.findViewById(R.id.pregunta_text_view_tipo);
            this.onRecyclerClickListener = onRecyclerClickListener;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            onRecyclerClickListener.onPreguntaClick(getAdapterPosition());
        }
    }
    public interface onRecyclerClickListener{
        void onPreguntaClick(int position);
    }

}
