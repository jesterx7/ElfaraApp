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
import com.elfara.user.elfaraapp.Model.InputData;
import com.elfara.user.elfaraapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FormFragment extends Fragment {
    private View view;
    private EditText edtNama, edtTanggal, edtAlamat, edtTTL, edtTelp, edtSelling, edtSampling, edtMedsos;
    private Button btnSubmit;
    private ProgressBar progressBar;


    public FormFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_form, container, false);

        edtNama = view.findViewById(R.id.edtNamaForm);
        edtTanggal = view.findViewById(R.id.edtTanggalForm);
        edtAlamat = view.findViewById(R.id.edtAlamatForm);
        edtMedsos = view.findViewById(R.id.edtInstagramForm);
        edtSampling = view.findViewById(R.id.edtJumlahSamplingForm);
        edtSelling = view.findViewById(R.id.edtJumlahSellingForm);
        edtTelp = view.findViewById(R.id.edtTelpForm);
        edtTTL = view.findViewById(R.id.edtTanggalLahirForm);
        btnSubmit = view.findViewById(R.id.btnSubmitForm);
        progressBar = view.findViewById(R.id.progressBarForm);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                Date c = Calendar.getInstance().getTime();

                InputData inputData = new InputData();
                inputData.setNamapelanggan(edtNama.getText().toString());
                inputData.setTanggal(edtTanggal.getText().toString());
                inputData.setAlamat(edtAlamat.getText().toString());
                inputData.setMediasosial(edtMedsos.getText().toString());
                inputData.setNomortelepon(edtTelp.getText().toString());
                inputData.setSelling(Integer.valueOf(edtSelling.getText().toString()));
                inputData.setSampling(Integer.valueOf(edtSampling.getText().toString()));
                inputData.setTanggallahir(edtTTL.getText().toString());
                inputData.setIdsales(1);
                saveForm(inputData);
                //getData();
            }
        });
        return view;
    }

    public void saveForm(InputData inputData) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<InputData> sellingCall = apiInterface.saveNote(
                inputData.getNamapelanggan(), inputData.getTanggal(), inputData.getTanggallahir(),
                inputData.getAlamat(), inputData.getNomortelepon(), inputData.getMediasosial(),
                inputData.getSelling(), inputData.getSampling(), inputData.getIdsales());

        sellingCall.enqueue(new Callback<InputData>() {
            @Override
            public void onResponse(Call<InputData> call, Response<InputData> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(view.getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                } else {
                    Toast.makeText(view.getContext(), "Failed to Response", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<InputData> call, Throwable t) {
                Toast.makeText(view.getContext(), "Insert Failed!!", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    public void getData() {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<InputData>> sellingCall = apiInterface.getNote();

        sellingCall.enqueue(new Callback<List<InputData>>() {
            @Override
            public void onResponse(Call<List<InputData>> call, Response<List<InputData>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(view.getContext(), "Insert Success!!", Toast.LENGTH_SHORT).show();
                    System.out.println("RESPONSE ::: " + response.body());
                }
            }

            @Override
            public void onFailure(Call<List<InputData>> call, Throwable t) {
                Toast.makeText(view.getContext(), "Insert Failed!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
