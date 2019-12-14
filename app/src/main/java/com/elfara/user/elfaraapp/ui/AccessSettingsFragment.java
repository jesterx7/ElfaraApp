package com.elfara.user.elfaraapp.ui;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elfara.user.elfaraapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccessSettingsFragment extends Fragment {
    private View view;


    public AccessSettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_access_settings, container, false);
        return view;
    }

}
