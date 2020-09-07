package com.kumu.czdan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;

public class UserInfoUpdateActivity extends AppCompatActivity {

    EditText nameText,payText;
    Spinner spinner;
    ProgressBar progressBar;
    SQLiteDatabase database;
    ArrayList<String> arrayList;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_update);

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);



        nameText = findViewById(R.id.nameText);
        payText = findViewById(R.id.payText);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        arrayList = new ArrayList<>();

        spinner = findViewById(R.id.spinner);
        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(UserInfoUpdateActivity.this, R.array.gunler, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        try {
            database = this.openOrCreateDatabase("UserInfos", MODE_PRIVATE, null);
            database.execSQL("CREATE TABLE IF NOT EXISTS userInfos (ID INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR,pay VARCHAR,day VARCHAR)");
        } catch (Exception e) {
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }

        fillTheEditText();



    }

    public void backButton (View v){

        onBackPressed();

    }

    public void  saveButton(View v){

        if (nameText.getText().toString().matches("") || payText.getText().toString().matches("") || spinner.getSelectedItem().toString().matches("")) {
            Toast.makeText(this, "Bilgileri eksiksiz giriniz!", Toast.LENGTH_LONG).show();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            String name = nameText.getText().toString();
            String pay = payText.getText().toString();
            String day = spinner.getSelectedItem().toString();
            try {

                Cursor cursor = database.rawQuery("SELECT * FROM userInfos", null);

                int nameIx = cursor.getColumnIndex("name");


                while (cursor.moveToNext()) {
                    arrayList.add(cursor.getString(nameIx));
                }


                ContentValues contentValues = new ContentValues();
                contentValues.put("name",name);
                contentValues.put("pay",pay);
                contentValues.put("day",day);

                database.update("userInfos",contentValues ,"ID=?",new String[]{"1"});

                Intent intent = new Intent(UserInfoUpdateActivity.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                finish();

            } catch (Exception e) {
                Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }



    public void fillTheEditText() {

        Cursor cursor = database.rawQuery("SELECT * FROM userInfos", null);

        int nameIx = cursor.getColumnIndex("name");
        int payIx = cursor.getColumnIndex("pay");
        int dayIx = cursor.getColumnIndex("day");
        int IdIx = cursor.getColumnIndex("ID");


        while (cursor.moveToNext()) {

            nameText.setText(cursor.getString(nameIx));
            payText.setText(cursor.getString(payIx));
            spinner.setSelection(Integer.parseInt(cursor.getString(dayIx)));


            System.out.println("user name = " + cursor.getString(nameIx));
            System.out.println("pay = " + cursor.getString(payIx));
            System.out.println("day = " + cursor.getString(dayIx));
            System.out.println("day = " + cursor.getString(IdIx));


        }

        cursor.close();


    }




}