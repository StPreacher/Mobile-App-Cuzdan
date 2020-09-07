package com.kumu.czdan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class CoinActivity extends AppCompatActivity {


    ArrayList<String> altinList,alisList,satisList,yuzdeList ;
    Dialog dialog;
    RecyclerView recyclerView3;

    private String url = "https://altin.doviz.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coin);
        recyclerView3 = findViewById(R.id.recycleView3);

        altinList = new ArrayList<>();
        alisList = new ArrayList<>();
        satisList = new ArrayList<>();
        yuzdeList = new ArrayList<>();

        dialog = new Dialog(this);

        new getData().execute();

    }

    public void questionMarkButton(View v){

        Toast.makeText(this, "Veriler Anlık Olarak Çekilmektedir..", Toast.LENGTH_LONG).show();

    }

    public void backButton (View v){

     onBackPressed();

    }

    @SuppressLint("StaticFieldLeak")
    public class getData extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                Document document = Jsoup.connect(url).timeout(3*1000).get();
                Elements veriler = document.select("body div div div div table tbody tr td ");

                for (int i = 0;i<81;i=i+5){
                    if(veriler.get(i).text().matches("")){
                        altinList.add(veriler.get(i+1).text());
                        alisList.add(veriler.get(i+2).text());
                        satisList.add(veriler.get(i+3).text());
                        yuzdeList.add(veriler.get(i+4).text());
                        i=i+1;
                    }else {
                        altinList.add(veriler.get(i).text());
                        alisList.add(veriler.get(i+1).text());
                        satisList.add(veriler.get(i+2).text());
                        yuzdeList.add(veriler.get(i+3).text());
                    }
                }

                System.out.println(altinList);
                System.out.println(alisList);
                System.out.println(satisList);
                System.out.println(yuzdeList);


            } catch (IOException e) {
                System.out.println(e.getLocalizedMessage());
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog.setContentView(R.layout.dialog_pop_up);
            ProgressBar progressBar = dialog.findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog.show();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            recyclerView3.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            RecycleAdapterDoviz recycleAdapterDoviz = new RecycleAdapterDoviz(altinList,alisList,satisList,yuzdeList);
            recyclerView3.setAdapter(recycleAdapterDoviz);

            dialog.dismiss();

        }
    }



}