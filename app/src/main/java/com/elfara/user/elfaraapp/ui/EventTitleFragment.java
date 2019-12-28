package com.elfara.user.elfaraapp.ui;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.elfara.user.elfaraapp.Core.ApiClient;
import com.elfara.user.elfaraapp.Core.ApiInterface;
import com.elfara.user.elfaraapp.Model.Event;
import com.elfara.user.elfaraapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventTitleFragment extends Fragment {
    private View view;
    private EditText edtEventTitle;
    private Button btnChange;
    private ProgressBar progressBar;


    public EventTitleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_event_title, container, false);

        edtEventTitle = view.findViewById(R.id.edtEventTitleEvent);
        btnChange = view.findViewById(R.id.btnChangeEvent);
        progressBar = view.findViewById(R.id.progressBarEvent);

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edtEventTitle.getText().toString().isEmpty()) {
                    progressBar.setVisibility(View.VISIBLE);
                    String newTitle = edtEventTitle.getText().toString();
                    updateEventTitle(newTitle);
                } else {
                    Toast.makeText(view.getContext(), "Field Cannot be Empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void updateEventTitle(String newTitle) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Event> eventCall = apiInterface.changeEventTitle(newTitle);

        eventCall.enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {
                if (response.isSuccessful() && response.body().getSuccess()) {
                    edtEventTitle.setText("");
                    Toast.makeText(view.getContext(), "Title Changed Successfully", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                } else {
                    Toast.makeText(view.getContext(), "Failed to Change Title", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                Toast.makeText(view.getContext(), "Error to Change Title", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

}
