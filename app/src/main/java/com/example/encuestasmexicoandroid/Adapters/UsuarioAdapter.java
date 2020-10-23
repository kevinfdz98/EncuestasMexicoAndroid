package com.example.encuestasmexicoandroid.Adapters;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.encuestasmexicoandroid.Classes.Usuario;
import com.example.encuestasmexicoandroid.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import org.w3c.dom.Text;

public class UsuarioAdapter extends FirestoreRecyclerAdapter <Usuario, UsuarioAdapter.UsuarioHolder>{

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public UsuarioAdapter(@NonNull FirestoreRecyclerOptions<Usuario> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull UsuarioHolder holder, int position, @NonNull Usuario model) {
        Log.d("DEBUG", "onBindViewHolder: SI llego aqui jaja");
        holder.textViewNombre.setText(model.getNombreUsuario());
        holder.textViewTipo.setText(model.getTipoUsuario());
        holder.textViewCorreo.setText(model.getCorreoUsuario());
    }

    @NonNull
    @Override
    public UsuarioHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item,
                parent,false);
        return new UsuarioHolder(v);
    }

    class UsuarioHolder extends RecyclerView.ViewHolder{

        TextView textViewNombre;
        TextView textViewTipo;
        TextView textViewCorreo;

        public UsuarioHolder(@NonNull View itemView) {
            super(itemView);

            textViewNombre =  itemView.findViewById(R.id.text_view_name_user);
            textViewCorreo = itemView.findViewById(R.id.text_view_email);
            textViewTipo = itemView.findViewById(R.id.tex_view_type_user);

        }
    }

}
