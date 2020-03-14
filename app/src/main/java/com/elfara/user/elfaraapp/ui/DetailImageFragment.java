package com.elfara.user.elfaraapp.ui;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.elfara.user.elfaraapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailImageFragment extends Fragment {
    private View view;
    private ImageView imgDetailImage;
    private Button btnBackDetailImage;
    private String urlImage;


    public DetailImageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_detail_image, container, false);

        imgDetailImage = view.findViewById(R.id.imgDetailImage);
        btnBackDetailImage = view.findViewById(R.id.btnBackDetailImage);

        urlImage = getArguments().getString("urlImage");
        Glide.with(getContext())
                .load(urlImage)
                .into(imgDetailImage);

        btnBackDetailImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return view;
    }

}
