package com.elfara.user.elfaraapp.ui;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.elfara.user.elfaraapp.Model.Session;
import com.elfara.user.elfaraapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {
    private View view;
    private ImageView imgBtnSampling;
    private LinearLayout llInputSelling, llAccessSettings, llReportSelling, llReportSampling, llDatabase, llAddUser;
    private Session session;

    public MainFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);

        imgBtnSampling = view.findViewById(R.id.imgBtnSamplingMain);
        llInputSelling = view.findViewById(R.id.llInputSellingMain);
        llAccessSettings = view.findViewById(R.id.llAccessSettingsMain);
        llReportSelling = view.findViewById(R.id.llReportSellingMain);
        llReportSampling = view.findViewById(R.id.llReportSamplingMain);
        llDatabase = view.findViewById(R.id.llDatabaseMain);
        llAddUser = view.findViewById(R.id.llAddUserMain);

        session = new Session(view.getContext());
        int level = Integer.parseInt(session.getSession("level"));
        switch (level) {
            case 0:
                llAccessSettings.setVisibility(View.GONE);
                llReportSelling.setVisibility(View.GONE);
                llReportSampling.setVisibility(View.GONE);
                llDatabase.setVisibility(View.GONE);
                llAddUser.setVisibility(View.GONE);
                break;
            case 1:
                llInputSelling.setVisibility(View.GONE);
                llAccessSettings.setVisibility(View.GONE);
                llDatabase.setVisibility(View.GONE);
                llAddUser.setVisibility(View.GONE);
                break;
            default:
                break;
        }

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
