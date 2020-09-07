package com.kumu.czdan;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.PostHolder> {

    private ArrayList<String> tarihList;
    private ArrayList<String> harcamaList;
    private ArrayList<String> tutarList;

    public RecycleAdapter(ArrayList<String> tarihList, ArrayList<String> harcamaList, ArrayList<String> tutarList) {
        this.tarihList = tarihList;
        this.harcamaList = harcamaList;
        this.tutarList = tutarList;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycle_row,parent,false);
        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {

        holder.tarihText.setText(tarihList.get(position));
        holder.harcamaText.setText(harcamaList.get(position));
        holder.tutarText.setText(tutarList.get(position)+"₺");

    }

    @Override
    public int getItemCount() {
        return tutarList.size();
    }

    class PostHolder extends RecyclerView.ViewHolder{

        TextView tarihText,harcamaText,tutarText;

        public PostHolder(@NonNull View itemView) {
            super(itemView);

            tarihText = itemView.findViewById(R.id.tarihText);
            harcamaText = itemView.findViewById(R.id.yerText);
            tutarText = itemView.findViewById(R.id.tutarText);

        }
    }


}
