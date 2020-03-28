package com.elfara.user.elfaraapp.ui;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.elfara.user.elfaraapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditEventFragment extends Fragment {
    private View view;
    private ProgressBar progressBar;
    private EditText edtNamaEvent;
    private Button btnChange;


    public EditEventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_edit_event, container, false);

        progressBar = view.findViewById(R.id.progressBarEditEvent);
        edtNamaEvent = view.findViewById(R.id.edtEventNameEditEvent);
        btnChange = view.findViewById(R.id.btnChangeEditEvent);

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtNamaEvent.getText().toString().isEmpty()) {

                }
            }
        });
        return view;
    }

}
