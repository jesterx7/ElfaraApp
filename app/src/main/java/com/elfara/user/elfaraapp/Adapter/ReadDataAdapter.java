package com.elfara.user.elfaraapp.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.elfara.user.elfaraapp.Core.ApiClient;
import com.elfara.user.elfaraapp.Core.ApiInterface;
import com.elfara.user.elfaraapp.Model.ReadData;
import com.elfara.user.elfaraapp.R;
import com.elfara.user.elfaraapp.ui.EditDataFragment;
import com.elfara.user.elfaraapp.ui.FormFragment;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReadDataAdapter extends RecyclerView.Adapter<ReadDataAdapter.ViewHolder> {
    private Context context;
    private ArrayList<ReadData> readDataArrayList;

    public ReadDataAdapter(Context context, ArrayList<ReadData> readDataArrayList) {
        this.context = context;
        this.readDataArrayList = readDataArrayList;
    }

    public ArrayList<ReadData> getReadDataArrayList() {
        return readDataArrayList;
    }

    public void setReadDataArrayList(ArrayList<ReadData> readDataArrayList) {
        this.readDataArrayList = readDataArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_read_data, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.tvNama.setText(getReadDataArrayList().get(position).getNamapelanggan());
        holder.tvTanggal.setText(getReadDataArrayList().get(position).getTanggal());
        holder.tvSelling.setText(String.valueOf(getReadDataArrayList().get(position).getSelling()));
        holder.tvSampling.setText(String.valueOf(getReadDataArrayList().get(position).getSampling()));
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("idtransaksi", String.valueOf(getReadDataArrayList().get(position).getIdtransaksi()));
                FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                Fragment fragment = new EditDataFragment();
                fragment.setArguments(bundle);
                transaction.replace(R.id.frame_content, fragment);
                transaction.addToBackStack("tag");
                transaction.commit();
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Delete this data ?");
                builder.setCancelable(true);

                builder.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteData(getReadDataArrayList().get(position).getIdtransaksi());
                                holder.itemView.setVisibility(View.GONE);
                            }
                        });

                builder.setNegativeButton(
                        "Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Action when cancel
                            }
                        });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }

    private void deleteData(int idtransaksi) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ReadData> dataCall = apiInterface.deleteDataTransaksi(idtransaksi);
        dataCall.enqueue(new Callback<ReadData>() {
            @Override
            public void onResponse(Call<ReadData> call, Response<ReadData> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Failed to Delete Data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReadData> call, Throwable t) {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return getReadDataArrayList().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvNama, tvTanggal, tvSelling, tvSampling;
        private Button btnEdit, btnDelete;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNama = itemView.findViewById(R.id.tvNamaReadData);
            tvTanggal = itemView.findViewById(R.id.tvTanggalReadData);
            tvSelling = itemView.findViewById(R.id.tvSellingReadData);
            tvSampling = itemView.findViewById(R.id.tvSamplingReadData);
            btnEdit = itemView.findViewById(R.id.btnEditReadData);
            btnDelete = itemView.findViewById(R.id.btnDeleteReadData);

        }
    }
}
