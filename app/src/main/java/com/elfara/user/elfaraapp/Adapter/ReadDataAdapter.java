package com.elfara.user.elfaraapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.elfara.user.elfaraapp.Model.ReadData;
import com.elfara.user.elfaraapp.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvNama.setText(getReadDataArrayList().get(position).getNamapelanggan());
        holder.tvTanggal.setText(getReadDataArrayList().get(position).getTanggal());
        holder.tvSelling.setText(String.valueOf(getReadDataArrayList().get(position).getSelling()));
        holder.tvSampling.setText(String.valueOf(getReadDataArrayList().get(position).getSampling()));
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
