package com.example.encuestasmexicoandroid.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.encuestasmexicoandroid.Classes.Listas;
import com.example.encuestasmexicoandroid.R;
import com.firebase.ui.auth.data.model.User;

import java.util.List;

public class ListasAdapter extends RecyclerView.Adapter<ListasAdapter.ListasHolder> {
    private Context mContext;
    private List<Listas> mListasList;
    private onRecyclerClickListener mOnRecyclerClickListener;

    public ListasAdapter(Context context, List<Listas> listasList, onRecyclerClickListener onRecyclerClickListener){
        mContext = context;
        mListasList = listasList;
        mOnRecyclerClickListener = onRecyclerClickListener;
    }

    @NonNull
    @Override
    public ListasAdapter.ListasHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.listas_item, parent, false);
        return new ListasHolder(view, mOnRecyclerClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ListasAdapter.ListasHolder holder, int position) {
        Listas currentItem = mListasList.get(position);
        // Asignar valor correspondiente de cada elemento

        String nombre = currentItem.getNombre();

        holder.mNombre.setText(nombre);

    }

    @Override
    public int getItemCount() {
        return mListasList.size();
    }

    public class ListasHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView mNombre;
        onRecyclerClickListener onRecyclerClickListener;

        public ListasHolder(@NonNull View itemView, onRecyclerClickListener onRecyclerClickListener) {
            super(itemView);
            mNombre = itemView.findViewById(R.id.textViewNombre);

            this.onRecyclerClickListener = onRecyclerClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onRecyclerClickListener.onUserClick(getAdapterPosition());
        }
    }

    public interface onRecyclerClickListener{
        void onUserClick(int position);
    }
}