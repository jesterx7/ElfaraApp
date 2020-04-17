package com.elfara.user.elfaraapp.ui;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.elfara.user.elfaraapp.Core.ApiClient;
import com.elfara.user.elfaraapp.Core.ApiInterface;
import com.elfara.user.elfaraapp.Function.FunctionEventLog;
import com.elfara.user.elfaraapp.Model.DefaultResponse;
import com.elfara.user.elfaraapp.R;
import com.elfara.user.elfaraapp.Utils.HelperUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditEventFragment extends Fragment {
    private View view;
    private HelperUtils helper;
    private ProgressBar progressBar;
    private EditText edtNamaEvent;
    private Button btnChange;
    private ApiInterface apiInterface;
    private FunctionEventLog functionEventLog;
    private int idevent;
    private String eventName;


    public EditEventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        AppCompatActivity appCompatActivity = (AppCompatActivity)getActivity();
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        view = inflater.inflate(R.layout.fragment_edit_event, container, false);
        helper = new HelperUtils((AppCompatActivity)getActivity(), getContext());
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        functionEventLog = new FunctionEventLog(view.getContext());

        idevent = getArguments().getInt("idevent", 1);
        eventName = getArguments().getString("event_name", "Event Name");

        progressBar = view.findViewById(R.id.progressBarEditEvent);
        edtNamaEvent = view.findViewById(R.id.edtEventNameEditEvent);
        btnChange = view.findViewById(R.id.btnChangeEditEvent);

        edtNamaEvent.setText(eventName);

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtNamaEvent.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Event Name cannot be Empty", Toast.LENGTH_SHORT).show();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    updateEventName(edtNamaEvent.getText().toString());
                }
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Edit Event Name");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            getActivity().getSupportFragmentManager().popBackStack();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateEventName(final String newEventName) {
        Call<DefaultResponse> call = apiInterface.updateEventName(
                idevent, newEventName
        );
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, Response<DefaultResponse> response) {
                if (response.isSuccessful() && response.body().getSuccess()) {
                    functionEventLog.writeEventLog("Updated Event Name From " + eventName + " To " + newEventName);
                    Toast.makeText(getContext(), "Event Name Updated Successfully", Toast.LENGTH_SHORT).show();
                    helper.changeFragment(new EventTitleFragment());
                } else {
                    Toast.makeText(getContext(), "Failed to Change Event Name", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error when chang event name : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

}
