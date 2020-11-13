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
    private onRecyclerClickListener mOnRecyclerClickListener;

    public UserAdapter(Context context, List<Usuario> userList, onRecyclerClickListener onRecyclerClickListener){
        mContext = context;
        mUsuarioLista = userList;
        mOnRecyclerClickListener = onRecyclerClickListener;
    }

    @NonNull
    @Override
    public UserAdapter.UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_item, parent, false);
        return new UserHolder(view, mOnRecyclerClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserHolder holder, int position) {
        Usuario currentUsuario = mUsuarioLista.get(position);
        // Asignar valor correspondiente de cada elemento
        String name = currentUsuario.getNombreUsuario();
        String type = currentUsuario.getTipoUsuario();
        String email = currentUsuario.getCorreoUsuario();
        String status = currentUsuario.getEstatus();


        holder.user_name.setText(name);
        holder.user_email.setText(email);
        holder.user_type.setText(type);
        holder.user_status.setText(status);

    }

    @Override
    public int getItemCount() {
        return mUsuarioLista.size();
    }

    public class UserHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView user_name;
        public TextView user_type;
        public TextView user_email;
        public TextView user_status;
        onRecyclerClickListener onRecyclerClickListener;

        public UserHolder(@NonNull View itemView, onRecyclerClickListener onRecyclerClickListener) {
            super(itemView);
            user_name = itemView.findViewById(R.id.text_view_name_user);
            user_type = itemView.findViewById(R.id.tex_view_type_user);
            user_email = itemView.findViewById(R.id.text_view_email);
            user_status = itemView.findViewById(R.id.text_view_status_user);

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
