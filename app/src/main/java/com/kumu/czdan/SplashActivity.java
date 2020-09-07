package com.kumu.czdan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {

    ImageView logo;
    SQLiteDatabase db;
    ArrayList<String> arrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        arrayList = new ArrayList<>();

        try {
            db = this.openOrCreateDatabase("UserInfos", MODE_PRIVATE, null);
            db.execSQL("CREATE TABLE IF NOT EXISTS userInfos (ID INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR,pay VARCHAR,day VARCHAR)");
        } catch (Exception e) {
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }

        logo = findViewById(R.id.logo);
        Thread logoAnimation = new Thread() {
            @Override
            public void run() {
                super.run();
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.logo_animation);
                logo.startAnimation(animation);
            }
        };
        logoAnimation.start();
        Thread redirect = new Thread() {
            @Override
            public void run() {
                super.run();

                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Cursor cursor = db.rawQuery("SELECT * FROM userInfos", null);

                int nameIx = cursor.getColumnIndex("name");


                while (cursor.moveToNext()) {

                    arrayList.add(cursor.getString(nameIx));

                }

                if (arrayList.size() == 0) {
                    Intent intent = new Intent(SplashActivity.this, UserInfoActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                cursor.close();


            }
        };
        redirect.start();
    }
}