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
import com.elfara.user.elfaraapp.Function.FunctionEventLog;
import com.elfara.user.elfaraapp.Model.Session;
import com.elfara.user.elfaraapp.Model.User;
import com.elfara.user.elfaraapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {
    private View view;
    private EditText edtOldPassword, edtNewPassword, edtConfirmPassword;
    private Button btnSave;
    private ProgressBar progressBar;
    private Session session;
    private FunctionEventLog functionEventLog;


    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, container, false);

        edtOldPassword = view.findViewById(R.id.edtOldPasswordSettings);
        edtNewPassword = view.findViewById(R.id.edtNewPasswordSettings);
        edtConfirmPassword = view.findViewById(R.id.edtConfirmPassowrdSettings);
        btnSave = view.findViewById(R.id.btnSaveSettings);
        progressBar = view.findViewById(R.id.progressBarSettings);

        session = new Session(view.getContext());
        functionEventLog = new FunctionEventLog(view.getContext());

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                changePassword(
                        edtOldPassword.getText().toString(),
                        edtNewPassword.getText().toString(),
                        edtConfirmPassword.getText().toString()
                );
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Settings");
    }

    private void changePassword(String oldPassword, final String newPassword, String confirmPassword) {
        System.out.println("OLD PASSWORD SES: " + session.getSession("password"));
        System.out.println("OLD PASSWORD INP: " + oldPassword);
        if (session.getSession("password").equals(oldPassword)) {
            if (newPassword.equals(confirmPassword)) {
                edtOldPassword.setText("");
                edtNewPassword.setText("");
                edtConfirmPassword.setText("");
                ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                Call<User> userCall = apiInterface.changePassword(
                        session.getSession("email"), newPassword
                );
                userCall.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.isSuccessful() && response.body().getSuccess()) {
                            functionEventLog.writeEventLog("Changed Password");
                            Toast.makeText(view.getContext(), "Password Changed Successfully", Toast.LENGTH_SHORT).show();
                            session.setSession("password", newPassword);
                            progressBar.setVisibility(View.GONE);
                        } else {
                            Toast.makeText(view.getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(view.getContext(), "Failed to Change Password", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                });
            } else {
                edtConfirmPassword.setText("");
                edtNewPassword.setText("");
                Toast.makeText(view.getContext(), "Password doesnt Match", Toast.LENGTH_SHORT).show();
            }
        } else {
            edtOldPassword.setText("");
            Toast.makeText(view.getContext(), "Wrong Password", Toast.LENGTH_SHORT).show();
        }
    }
}
