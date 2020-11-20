package com.example.encuestasmexicoandroid.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.encuestasmexicoandroid.Classes.FormList;
import com.example.encuestasmexicoandroid.R;

import java.util.List;

public class FormAdapter2 extends RecyclerView.Adapter<FormAdapter2.FormHolder2>{
    private Context mContext;
    private List<FormList> mFormularioList;
    private FormAdapter2.onRecyclerClickListener mOnRecyclerClickListener;

    public FormAdapter2(Context context, List<FormList> formularioList, FormAdapter2.onRecyclerClickListener onRecyclerClickListener){
        mContext = context;
        mFormularioList = formularioList;
        mOnRecyclerClickListener = onRecyclerClickListener;
    }

    @NonNull
    @Override
    public FormAdapter2.FormHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.form_item_2, parent, false);
        return new FormAdapter2.FormHolder2(view, mOnRecyclerClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull FormAdapter2.FormHolder2 holder, int position) {
        FormList currentForm = mFormularioList.get(position);
        //Asignar valor correspondiente de cada formulario
        String formName = currentForm.getNombreFormulario();

        holder.form_name.setText(formName);
    }

    @Override
    public int getItemCount() {
        return mFormularioList.size();
    }

    public class FormHolder2 extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView form_name;

        FormAdapter2.onRecyclerClickListener onRecyclerClickListener;

        public FormHolder2(@NonNull View itemView, FormAdapter2.onRecyclerClickListener onRecyclerClickListener) {
            super(itemView);
            form_name = itemView.findViewById(R.id.text_view_form_name_2);

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
