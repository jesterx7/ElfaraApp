package com.elfara.user.elfaraapp.ui;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.elfara.user.elfaraapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class UploadPhotoFragment extends Fragment {
    private View view;
    private Button btnUpload, btnSubmit;


    public UploadPhotoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_upload_photo, container, false);

        btnUpload = view.findViewById(R.id.btnUploadUploadPhoto);
        btnSubmit = view.findViewById(R.id.btnSubmitUploadPhoto);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                Fragment fragment = new UploadNewPhotoFragment();
                transaction.replace(R.id.frame_content, fragment);
                transaction.addToBackStack("tag");
                transaction.commit();
            }
        });

        return view;
    }

}
