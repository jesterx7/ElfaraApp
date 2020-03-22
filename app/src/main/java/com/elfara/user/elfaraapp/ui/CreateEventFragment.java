package com.elfara.user.elfaraapp.ui;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.elfara.user.elfaraapp.Core.ApiClient;
import com.elfara.user.elfaraapp.Core.ApiInterface;
import com.elfara.user.elfaraapp.Model.DefaultResponse;
import com.elfara.user.elfaraapp.R;
import com.elfara.user.elfaraapp.Utils.HelperUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateEventFragment extends Fragment {
    private View view;
    private EditText edtEventName;
    private Switch switchSelected;
    private Button btnCreate;
    private HelperUtils helper;
    private int selected = 1;

    public CreateEventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        AppCompatActivity appCompatActivity = (AppCompatActivity)getActivity();
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        view = inflater.inflate(R.layout.fragment_create_event, container, false);
        helper = new HelperUtils((AppCompatActivity)getActivity(), getContext());

        edtEventName = view.findViewById(R.id.edtEventNameCreateEvent);
        switchSelected = view.findViewById(R.id.switchCreateEvent);
        btnCreate = view.findViewById(R.id.btnCreateCreateEvent);

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edtEventName.getText().toString().isEmpty()) {
                    if (!switchSelected.isChecked()) selected = 0;
                    createEvent(edtEventName.getText().toString());
                } else {
                    Toast.makeText(getContext(),"Event Name Must be Filled !!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    private void createEvent(String eventName) {
        ApiInterface apiInterface = new ApiClient().getApiClient().create(ApiInterface.class);
        Call<DefaultResponse> call = apiInterface.createEvent(
                eventName, selected
        );
        call.enqueue(new Callback<DefaultResponse>() {
            @Override
            public void onResponse(Call<DefaultResponse> call, retrofit2.Response<DefaultResponse> response) {
                if (response.isSuccessful() && response.body().getSuccess()) {
                    Toast.makeText(getContext(), "Event Created", Toast.LENGTH_SHORT).show();
                    helper.changeFragment(new EventTitleFragment());
                } else {
                    Toast.makeText(getContext(), "Failed to Create", Toast.LENGTH_SHORT).show();
                    System.out.println("RESPONSE : " + response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<DefaultResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
