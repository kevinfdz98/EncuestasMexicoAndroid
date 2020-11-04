package com.example.encuestasmexicoandroid.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.encuestasmexicoandroid.Classes.Usuario;
import com.example.encuestasmexicoandroid.R;
import com.firebase.ui.auth.data.model.User;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder> {
    private Context mContext;
    private List<Usuario> mUsuarioLista;

    public UserAdapter(Context context, List<Usuario> userList){
        mContext = context;
        mUsuarioLista = userList;
    }

    @NonNull
    @Override
    public UserAdapter.UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserHolder holder, int position) {
        Usuario currentUsuario = mUsuarioLista.get(position);
        // Asignar valor correspondiente de cada elemento
        String name = currentUsuario.getNombreUsuario();
        String type = currentUsuario.getTipoUsuario();
        String email = currentUsuario.getCorreoUsuario();

        holder.user_name.setText(name);
        holder.user_email.setText(email);
        holder.user_type.setText(type);
    }

    @Override
    public int getItemCount() {
        return mUsuarioLista.size();
    }

    public class UserHolder extends RecyclerView.ViewHolder {
        public TextView user_name;
        public TextView user_type;
        public TextView user_email;

        public UserHolder(@NonNull View itemView) {
            super(itemView);
            user_name = itemView.findViewById(R.id.text_view_name_user);
            user_type = itemView.findViewById(R.id.tex_view_type_user);
            user_email = itemView.findViewById(R.id.text_view_email);
        }
    }
}
