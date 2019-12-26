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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.elfara.user.elfaraapp.Core.ApiClient;
import com.elfara.user.elfaraapp.Core.ApiInterface;
import com.elfara.user.elfaraapp.Model.User;
import com.elfara.user.elfaraapp.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccessSettingsFragment extends Fragment {
    private View view;
    private EditText edtEmail;
    private Button btnSearch, btnSave;
    private LinearLayout llUser;
    private TableLayout tableLayout;
    private ProgressBar progressBar;
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
        btnSearch = view.findViewById(R.id.btnSearchAccessSettings);
        btnSave = view.findViewById(R.id.btnSaveAccessSettings);
        llUser = view.findViewById(R.id.llUserAccessSettings);
        tableLayout = view.findViewById(R.id.tableAccessSettings);
        progressBar = view.findViewById(R.id.progressBarAccessSettings);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!edtEmail.getText().toString().isEmpty()) {
                    progressBar.setVisibility(View.VISIBLE);
                    tableLayout.removeAllViews();
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

    private void updateUserAccess(String user_email, int user_level) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<User> userCall = apiInterface.updateUserAccess(user_email, user_level);

        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body().getSuccess()) {
                    Toast.makeText(view.getContext(), "User Access Updated", Toast.LENGTH_SHORT).show();
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
                    TableRow tableRow = new TableRow(view.getContext());
                    tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                    TextView tvNama = new TextView(view.getContext());
                    tvNama.setText(response.body().getName());
                    tvNama.setTextSize(14);
                    tvNama.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                    tableRow.addView(tvNama);
                    ArrayList<String> listLevels = new ArrayList<>();
                    listLevels.add("Admin");
                    listLevels.add("Sales");
                    listLevels.add("Product");
                    listLevels.add("Manager");
                    level = response.body().getLevel();
                    userEmail = response.body().getEmail();
                    Spinner spinnerLevel = new Spinner(view.getContext());
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(view.getContext(), R.layout.spinner_access_settings, R.id.tvSpinnerAccessSettings, listLevels);
                    spinnerLevel.setAdapter(arrayAdapter);
                    spinnerLevel.setSelection(response.body().getLevel());
                    spinnerLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            level = i;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                            level = response.body().getLevel();
                        }
                    });
                    tableRow.addView(spinnerLevel);
                    tableLayout.addView(tableRow, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                    llUser.setVisibility(View.VISIBLE);
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
