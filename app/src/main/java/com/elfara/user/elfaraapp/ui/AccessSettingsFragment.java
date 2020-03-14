package com.elfara.user.elfaraapp.ui;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.elfara.user.elfaraapp.Core.ApiClient;
import com.elfara.user.elfaraapp.Core.ApiInterface;
import com.elfara.user.elfaraapp.Function.FunctionEventLog;
import com.elfara.user.elfaraapp.Model.Session;
import com.elfara.user.elfaraapp.Model.User;
import com.elfara.user.elfaraapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccessSettingsFragment extends Fragment {
    private View view;
    private EditText edtEmail, edtNama, edtAlamat, edtTelp;
    private Spinner spinnerLevel;
    private Switch switchStatus;
    private Button btnSearch, btnSave;
    private LinearLayout llUser;
    private ProgressBar progressBar;
    private Session session;
    private FunctionEventLog functionEventLog;
    private int level = 0;
    private String userEmail = "";


    public AccessSettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_access_settings, container, false);

        edtEmail = view.findViewById(R.id.edtEmailAccessSettings);
        edtNama = view.findViewById(R.id.edtNamaAccessSettings);
        edtAlamat = view.findViewById(R.id.edtAlamatAccessSettings);
        edtTelp = view.findViewById(R.id.edtTelpAccessSettings);
        spinnerLevel = view.findViewById(R.id.spinnerAccessSettings);
        switchStatus = view.findViewById(R.id.switchAccessSettings);
        btnSearch = view.findViewById(R.id.btnSearchAccessSettings);
        btnSave = view.findViewById(R.id.btnSaveAccessSettings);
        llUser = view.findViewById(R.id.llUserAccessSettings);
        progressBar = view.findViewById(R.id.progressBarAccessSettings);

        spinnerLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                level = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        switchStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    switchStatus.setText("Aktif");
                } else {
                    switchStatus.setText("Non-Aktif");
                }
            }
        });

        session = new Session(view.getContext());
        functionEventLog = new FunctionEventLog(view.getContext());

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edtEmail.getText().toString().isEmpty()) {
                    progressBar.setVisibility(View.VISIBLE);
                    getUser(edtEmail.getText().toString());
                } else {
                    Toast.makeText(view.getContext(), "Email cannot be Empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                updateUserAccess(userEmail, level);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Access Settings");
    }

    private void updateUserAccess(final String user_email, int user_level) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        String status = "OPN";
        if (!switchStatus.isChecked()) {
            status = "DEL";
        }
        Call<User> userCall = apiInterface.updateUserAccess(
                user_email, edtNama.getText().toString(), edtAlamat.getText().toString(),
                edtTelp.getText().toString(), status, user_level
        );

        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body().getSuccess()) {
                    Toast.makeText(view.getContext(), "User Access Updated", Toast.LENGTH_SHORT).show();
                    functionEventLog.writeEventLog("Changed User Access with email " + user_email);
                    progressBar.setVisibility(View.GONE);
                } else {
                    Toast.makeText(view.getContext(), "Failed to Update", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(view.getContext(), "Error to Update", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void getUser(final String email) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<User> userCall = apiInterface.getUser(email);

        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, final Response<User> response) {
                if (response.isSuccessful() && response.body().getSuccess()) {
                    if (response.body().getLevel() > Integer.parseInt(session.getSession("level"))) {
                        Toast.makeText(view.getContext(), "You dont have permission!", Toast.LENGTH_SHORT).show();
                    } else {
                        edtNama.setText(response.body().getName());
                        edtAlamat.setText(response.body().getAlamat());
                        edtTelp.setText(response.body().getHandphone());
                        List<String> arrListLevels;
                        if (Integer.parseInt(session.getSession("level")) > 2) {
                            arrListLevels = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.owner_levels)));
                        } else {
                            arrListLevels = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.levels)));
                        }
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(view.getContext(), R.layout.spinner_access_settings, R.id.tvSpinnerAccessSettings, arrListLevels);
                        spinnerLevel.setAdapter(arrayAdapter);
                        if (response.body().getStatus().equals("DEL")) {
                            switchStatus.setChecked(false);
                        } else {
                            switchStatus.setChecked(true);
                        }
                        level = response.body().getLevel();
                        spinnerLevel.setSelection(level);
                        userEmail = response.body().getEmail();

                        llUser.setVisibility(View.VISIBLE);
                    }
                    progressBar.setVisibility(View.GONE);
                } else {
                    Toast.makeText(view.getContext(), "Failed to get user data", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(view.getContext(), "Error to get user", Toast.LENGTH_SHORT);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

}
