package com.example.encuestasmexicoandroid.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.encuestasmexicoandroid.Classes.FormList;
import com.example.encuestasmexicoandroid.Classes.Formulario;
import com.example.encuestasmexicoandroid.R;

import org.w3c.dom.Text;

import java.text.Normalizer;
import java.util.List;

public class FormAdapter extends RecyclerView.Adapter<FormAdapter.FormHolder> {
    private Context mContext;
    private List<FormList> mFormularioList;
    private onRecyclerClickListener mOnRecyclerClickListener;

    public FormAdapter(Context context, List<FormList> formularioList, onRecyclerClickListener onRecyclerClickListener){
        mContext = context;
        mFormularioList = formularioList;
        mOnRecyclerClickListener = onRecyclerClickListener;
    }

    @NonNull
    @Override
    public FormAdapter.FormHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.form_item, parent, false);
        return new FormHolder(view, mOnRecyclerClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull FormAdapter.FormHolder holder, int position) {
        FormList currentForm = mFormularioList.get(position);
        //Asignar valor correspondiente de cada formulario
        String formName = currentForm.getNombreFormulario();
        String status = currentForm.getEstatus();
        String inicio = currentForm.getFechaInicio();
        String fin = currentForm.getFechaFin();

        holder.form_name.setText(formName);
        holder.form_inicio.setText(inicio);
        holder.form_fin.setText(fin);
        holder.form_status.setText(status);


    }

    @Override
    public int getItemCount() {
        return mFormularioList.size();
    }

    public class FormHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView form_name;
        public TextView form_status;
        public TextView form_inicio;
        public TextView form_fin;
        onRecyclerClickListener onRecyclerClickListener;

        public FormHolder(@NonNull View itemView, onRecyclerClickListener onRecyclerClickListener) {
            super(itemView);
            form_name = itemView.findViewById(R.id.text_view_form_name);
            form_status = itemView.findViewById(R.id.text_view_form_status);
            form_inicio = itemView.findViewById(R.id.text_view_form_inicio);
            form_fin = itemView.findViewById(R.id.text_view_form_fin);

            this.onRecyclerClickListener = onRecyclerClickListener;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            onRecyclerClickListener.onFormClick(getAdapterPosition());
        }
    }

    public interface onRecyclerClickListener{
        void onFormClick(int position);
    }
}
