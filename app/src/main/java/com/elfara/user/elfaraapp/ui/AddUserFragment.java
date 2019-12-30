package com.elfara.user.elfaraapp.ui;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddUserFragment extends Fragment {
    private View view;
    private EditText edtNama, edtEmail, edtPassword, edtConfirmPassword, edtAlamat, edtHandphone;
    private Spinner spinnerLevel;
    private Switch switchStatus;
    private Button btnSubmit;
    private ProgressBar progressBar;
    private Session session;
    private FunctionEventLog functionEventLog;


    public AddUserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_add_user, container, false);

        edtNama = view.findViewById(R.id.edtNamaAddUser);
        edtEmail = view.findViewById(R.id.edtEmailAddUser);
        edtPassword = view.findViewById(R.id.edtPasswordAddUser);
        edtConfirmPassword = view.findViewById(R.id.edtConfirmPassowrdAddUser);
        edtAlamat = view.findViewById(R.id.edtAlamatAddUser);
        edtHandphone = view.findViewById(R.id.edtTelpAddUser);
        spinnerLevel = view.findViewById(R.id.spinnerAddUser);
        switchStatus = view.findViewById(R.id.switchAddUser);
        btnSubmit = view.findViewById(R.id.btnSubmitAddUser);
        progressBar = view.findViewById(R.id.progressBarAddUser);

        session = new Session(view.getContext());
        functionEventLog = new FunctionEventLog(view.getContext());

        List<String> arrListLevels;
        if (Integer.parseInt(session.getSession("level")) > 2) {
            arrListLevels = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.owner_levels)));
        } else {
            arrListLevels = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.levels)));
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(view.getContext(), R.layout.spinner_access_settings, R.id.tvSpinnerAccessSettings, arrListLevels);
        spinnerLevel.setAdapter(arrayAdapter);

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

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean check = checkField();
                if (check) {
                    String status = "OPN";
                    if (!switchStatus.isChecked()) {
                        status = "DEL";
                    }
                    if (edtPassword.getText().toString().equals(edtConfirmPassword.getText().toString())) {
                        progressBar.setVisibility(View.VISIBLE);
                        saveUser(
                                edtNama.getText().toString(), edtEmail.getText().toString(), edtPassword.getText().toString(),
                                edtAlamat.getText().toString(), edtHandphone.getText().toString(), status,
                                spinnerLevel.getSelectedItemPosition()
                        );
                    } else {
                        Toast.makeText(view.getContext(), "Password doesn't match", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(view.getContext(), "Please fill all Field", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void saveUser(String nama, final String email, String password, String alamat, String handphone, String status, int level) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<User> userCall = apiInterface.addUser(
          nama, email, password, alamat, handphone, status, level
        );

        userCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(view.getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    clearField();
                    functionEventLog.writeEventLog("Add User with Email " + email);
                    progressBar.setVisibility(View.GONE);
                } else {
                    Toast.makeText(view.getContext(), "Failed to Add User, please check connection and email", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(view.getContext(), "Error to Add User", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private Boolean checkField() {
        if (edtNama.getText().toString().isEmpty()) return false;
        else if (edtEmail.getText().toString().isEmpty()) return false;
        else if (edtPassword.getText().toString().isEmpty()) return false;
        else if (edtConfirmPassword.getText().toString().isEmpty()) return false;
        else if (edtAlamat.getText().toString().isEmpty()) return false;
        else if (edtHandphone.getText().toString().isEmpty()) return false;
        else return true;
    }

    private void clearField() {
        edtNama.setText("");
        edtEmail.setText("");
        edtPassword.setText("");
        edtConfirmPassword.setText("");
        edtAlamat.setText("");
        edtHandphone.setText("");
        spinnerLevel.setSelection(0);
        switchStatus.setChecked(true);
    }
}
