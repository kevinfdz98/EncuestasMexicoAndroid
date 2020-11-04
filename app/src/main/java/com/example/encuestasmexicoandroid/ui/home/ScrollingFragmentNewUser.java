package com.example.encuestasmexicoandroid.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.encuestasmexicoandroid.R;

class ScrollingFragmentNewUser extends Fragment {

    public ScrollingFragmentNewUser(){
        //Require empty public constructor
    }

    public static ScrollingFragmentNewUser newInstance(){
        ScrollingFragmentNewUser fragmentNewUser = new ScrollingFragmentNewUser();
        return fragmentNewUser;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_scrolling_new_user, container, false);

        return root;

    }
}