package com.elfara.user.elfaraapp.ui;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.elfara.user.elfaraapp.Core.ApiClient;
import com.elfara.user.elfaraapp.Core.ApiInterface;
import com.elfara.user.elfaraapp.Model.Event;
import com.elfara.user.elfaraapp.Model.Session;
import com.elfara.user.elfaraapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {
    private View view;
    private TextView tvEventName;
    private ImageView imgBtnSampling;
    private LinearLayout llInputSelling, llAccessSettings, llReportSelling, llReportSampling, llDatabase, llAddUser;
    private ProgressBar progressBar;
    private Session session;

    public MainFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);

        tvEventName = view.findViewById(R.id.tvEventName);
        imgBtnSampling = view.findViewById(R.id.imgBtnSamplingMain);
        llInputSelling = view.findViewById(R.id.llInputSellingMain);
        llAccessSettings = view.findViewById(R.id.llAccessSettingsMain);
        llReportSelling = view.findViewById(R.id.llReportSellingMain);
        llReportSampling = view.findViewById(R.id.llReportSamplingMain);
        llDatabase = view.findViewById(R.id.llDatabaseMain);
        llAddUser = view.findViewById(R.id.llAddUserMain);
        progressBar = view.findViewById(R.id.progressBarMain);

        progressBar.setVisibility(View.VISIBLE);
        getEventName();

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

    private void getEventName() {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Event> eventCall = apiInterface.getEventName();

        eventCall.enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {
                if (response.isSuccessful() && response.body().getSuccess()) {
                    tvEventName.setText(response.body().getNama());
                    tvEventName.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                } else {
                    Toast.makeText(view.getContext(), "Failed Connect to Server", Toast.LENGTH_SHORT).show();
                    tvEventName.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                Toast.makeText(view.getContext(), "Error when Connect to Server", Toast.LENGTH_SHORT).show();
                tvEventName.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void changeFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_content, fragment);
        transaction.addToBackStack("tag");
        transaction.commit();
    }

}
