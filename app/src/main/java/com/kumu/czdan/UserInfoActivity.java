package com.kumu.czdan;

import androidx.appcompat.app.AppCompatActivity;

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

public class UserInfoActivity extends AppCompatActivity {

    Spinner spinner;
    EditText nameText, payText;
    SQLiteDatabase database;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        nameText = findViewById(R.id.nameText);
        payText = findViewById(R.id.payText);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        spinner = findViewById(R.id.spinner);
        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(UserInfoActivity.this, R.array.gunler, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        try {
            database = this.openOrCreateDatabase("UserInfos", MODE_PRIVATE, null);
            database.execSQL("CREATE TABLE IF NOT EXISTS userInfos (ID INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR,pay VARCHAR,day VARCHAR)");
        } catch (Exception e) {
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }

        controlDatabase(); //Yerel veri tabanı kontrol fonksiyonu


    }

    public void saveButton(View view) {

        controlDatabase(); //Yerel veri tabanı kontrol fonksiyonu


        if (nameText.getText().toString().matches("") || payText.getText().toString().matches("") || spinner.getSelectedItem().toString().matches("")) {
            Toast.makeText(this, "Bilgileri eksiksiz giriniz!", Toast.LENGTH_LONG).show();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            String name = nameText.getText().toString();
            String pay = payText.getText().toString();
            String day = spinner.getSelectedItem().toString();
            try {
                String sqlString = "INSERT INTO userInfos (name,pay,day) VALUES (?,?,?)";
                SQLiteStatement sqLiteStatement = database.compileStatement(sqlString);
                sqLiteStatement.bindString(1, name);
                sqLiteStatement.bindString(2, pay);
                sqLiteStatement.bindString(3, day);
                sqLiteStatement.execute();

                Intent intent = new Intent(UserInfoActivity.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                finish();

            } catch (Exception e) {
                Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void controlDatabase() {

        Cursor cursor = database.rawQuery("SELECT * FROM userInfos", null);

        int nameIx = cursor.getColumnIndex("name");
        int payIx = cursor.getColumnIndex("pay");
        int dayIx = cursor.getColumnIndex("day");



        while (cursor.moveToNext()) {

            System.out.println("user name = " + cursor.getString(nameIx));
            System.out.println("pay = " + cursor.getString(payIx));
            System.out.println("day = " + cursor.getString(dayIx));



        }

        cursor.close();


    }

}