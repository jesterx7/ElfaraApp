package com.elfara.user.elfaraapp.ui;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.elfara.user.elfaraapp.R;
import com.elfara.user.elfaraapp.Utils.HelperUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class UploadPhotoFragment extends Fragment {
    private HelperUtils helper;
    private View view;
    private Button btnUpload, btnSubmit;


    public UploadPhotoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_upload_photo, container, false);

        helper = new HelperUtils((AppCompatActivity)getActivity(), getContext());
        btnUpload = view.findViewById(R.id.btnUploadUploadPhoto);
        btnSubmit = view.findViewById(R.id.btnSubmitUploadPhoto);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helper.changeFragment(new UploadNewPhotoFragment());
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helper.changeFragment(new PhotoListFragment());
            }
        });

        return view;
    }

}
