package com.example.encuestasmexicoandroid.ui.logout;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.encuestasmexicoandroid.Login;
import com.example.encuestasmexicoandroid.R;
import com.google.firebase.auth.FirebaseAuth;

public class LogOut extends Fragment {

    public LogOut() {
        // Required empty public constructor
    }

    public static LogOut newInstance(String param1, String param2) {
        LogOut fragment = new LogOut();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getContext(), Login.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_log_out, container, false);
    }
}