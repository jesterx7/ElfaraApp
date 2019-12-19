package com.elfara.user.elfaraapp.ui;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.elfara.user.elfaraapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {
    private View view;
    private ImageView imgBtnSampling;

    public MainFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);

        imgBtnSampling = view.findViewById(R.id.imgBtnSamplingMain);

        imgBtnSampling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(new SummarySampling());
            }
        });

        return view;
    }

    public void changeFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_content, fragment);
        transaction.addToBackStack("tag");
        transaction.commit();
    }

}
