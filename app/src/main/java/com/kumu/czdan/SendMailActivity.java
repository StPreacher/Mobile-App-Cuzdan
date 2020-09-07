package com.kumu.czdan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class SendMailActivity extends AppCompatActivity {

    EditText editTextSubject,editTextMessage;
    private final String[] recipients = {"4m.software4@gmail.com"};
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_mail);

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        editTextSubject = findViewById(R.id.editTextSubject);
        editTextMessage = findViewById(R.id.editTextMessage);


    }



    public void sendButton(View view){

        if(editTextSubject.getText().toString().matches("")||editTextMessage.getText().toString().matches("")){
            Toast.makeText(this, "Boş bırakmayınız..", Toast.LENGTH_SHORT).show();
        }else{

            String subject = editTextSubject.getText().toString();
            String message = editTextMessage.getText().toString();

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_EMAIL,recipients);
            intent.putExtra(Intent.EXTRA_SUBJECT,subject);
            intent.putExtra(Intent.EXTRA_TEXT,message);

            intent.setType("message/rfc822");
            startActivity(Intent.createChooser(intent,"Bir uygulama seçin"));


        }




    }


    //Geri tuşu
    public void backButton (View view){
        onBackPressed();
    }

}