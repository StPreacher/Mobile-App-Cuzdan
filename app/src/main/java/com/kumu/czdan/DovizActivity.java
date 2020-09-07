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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class DovizActivity extends AppCompatActivity {

    RecyclerView recyclerView2;
    ArrayList<String> paraBirimiList;
    ArrayList<String> alisList;
    ArrayList<String> satisList;
    ArrayList<String> yuzdeList;
    private String URL = "https://kur.doviz.com/";
    Dialog dialog;
    private AdView mAdView;
    private TextView lastUpdate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doviz);

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        dialog = new Dialog(this);
        recyclerView2 = findViewById(R.id.recycleView2);
        paraBirimiList = new ArrayList<>();
        alisList = new ArrayList<>();
        satisList = new ArrayList<>();
        yuzdeList = new ArrayList<>();
        lastUpdate = findViewById(R.id.lastUpdate);


        new getData().execute();


    }

    public void backButton(View v) {

        onBackPressed();

    }


    @SuppressLint("StaticFieldLeak")
    public class getData extends AsyncTask<Void, Void, Void> {

        @SuppressLint({"SetTextI18n", "WrongThread"})
        @Override
        protected Void doInBackground(Void... voids) {

            try {
                Document doc = Jsoup.connect(URL).timeout(30 * 1000).get();
                Elements kurlar = doc.select("body div div div table tbody td");

                lastUpdate.setText("Son Güncelleme: "+kurlar.get(6).text());

                for (int i = 0;i<449;i=i+7){
                    if(kurlar.get(i).text().matches("")){
                        paraBirimiList.add(kurlar.get(i+1).text());
                        alisList.add(kurlar.get(i+2).text());
                        satisList.add(kurlar.get(i+3).text());
                        yuzdeList.add(kurlar.get(i+6).text());
                        i=i+1;
                    }else {
                        paraBirimiList.add(kurlar.get(i).text());
                        alisList.add(kurlar.get(i+1).text());
                        satisList.add(kurlar.get(i+2).text());
                        yuzdeList.add(kurlar.get(i+5).text());
                    }
                }


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

            recyclerView2.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            RecycleAdapterDoviz recycleAdapterDoviz = new RecycleAdapterDoviz(paraBirimiList,alisList,satisList,yuzdeList);
            recyclerView2.setAdapter(recycleAdapterDoviz);
            dialog.dismiss();


        }
    }


}