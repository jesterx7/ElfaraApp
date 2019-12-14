package com.elfara.user.elfaraapp.ui;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elfara.user.elfaraapp.Model.ReadData;
import com.elfara.user.elfaraapp.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReadDataFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private ArrayList<ReadData> readDataArrayList;


    public ReadDataFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_read_data, container, false);

        recyclerView = view.findViewById(R.id.rvReadData);
        readDataArrayList = new ArrayList<>();
        getReadData();
        return view;
    }

    private void getReadData() {

    }

}
