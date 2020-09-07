package com.kumu.czdan;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecycleAdapterDoviz extends RecyclerView.Adapter<RecycleAdapterDoviz.Tutucu>{

    private ArrayList<String> paraBirimiList;
    private ArrayList<String> alisList;
    private ArrayList<String> satisList;
    private ArrayList<String> yuzdeList;

    public RecycleAdapterDoviz(ArrayList<String> paraBirimiList, ArrayList<String> alisList, ArrayList<String> satisList, ArrayList<String> yuzdeList) {
        this.paraBirimiList = paraBirimiList;
        this.alisList = alisList;
        this.satisList = satisList;
        this.yuzdeList = yuzdeList;
    }

    @NonNull
    @Override
    public RecycleAdapterDoviz.Tutucu onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycle_row_doviz,parent,false);
        return new Tutucu(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleAdapterDoviz.Tutucu holder, int position) {

        holder.tipText.setText(paraBirimiList.get(position));
        holder.alisText.setText(alisList.get(position));
        holder.satisText.setText(satisList.get(position));
        holder.yuzdeText.setText(yuzdeList.get(position));

    }

    @Override
    public int getItemCount() {
        return paraBirimiList.size();
    }

    public class Tutucu extends RecyclerView.ViewHolder {

        TextView tipText,alisText,satisText,yuzdeText;

        public Tutucu(@NonNull View itemView) {
            super(itemView);
            tipText = itemView.findViewById(R.id.tipText);
            alisText = itemView.findViewById(R.id.alisText);
            satisText = itemView.findViewById(R.id.satisText);
            yuzdeText = itemView.findViewById(R.id.yuzdeText);

        }
    }
}
