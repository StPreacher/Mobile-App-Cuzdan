package com.kumu.czdan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.material.navigation.NavigationView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.graphics.Color.TRANSPARENT;

public class MainActivity extends AppCompatActivity {

    //TODO Doviz sayfası değişti artık anlık geliyor.

    Dialog dialog, dialog1, dialog2, dialog3;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    private Toolbar actionBar;
    TextView nameTextHeader, maasTextHeader, kalanGun;
    SQLiteDatabase database, database1,database2;
    ArrayList<String> arrayList;
    ArrayList<String> maasList;
    ArrayList<String> harcamaList;
    ArrayList<String> tutarList;
    ArrayList<String> tarihList;
    ArrayList<String> dayList;
    ArrayList<String> addMoneyList;
    String name = "";
    String maas = "";
    String day = "";
    String currentDay = "";
    int dayInt;
    int currentDayInt;
    private PieChart mChart;
    EditText harcananYerText, tutarText, eklenilenTutarText;
    RecyclerView recyclerView;
    LottieAnimationView animationView;
    LottieAnimationView animationView1;
    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);



        //İsim, maaş ve gün veritabanı
        try {
            database = this.openOrCreateDatabase("UserInfos", MODE_PRIVATE, null);
            database.execSQL("CREATE TABLE IF NOT EXISTS userInfos (ID INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR,pay VARCHAR,day VARCHAR)");
        } catch (Exception e) {
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
        //Tarih,harcanan yer,tutar veritabanı
        try {
            database1 = this.openOrCreateDatabase("PayDetails", MODE_PRIVATE, null);
            database1.execSQL("CREATE TABLE IF NOT EXISTS payDetails(ID INTEGER PRIMARY KEY AUTOINCREMENT,tarih VARCHAR,harcanan_yer VARCHAR,tutar VARCHAR)");
        } catch (Exception e) {
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
        //Eklenen para veri tabanı
        try {
            database2 = this.openOrCreateDatabase("AddMoney",MODE_PRIVATE,null);
            database2.execSQL("CREATE TABLE IF NOT EXISTS addMoney(ID INTEGER PRIMARY KEY AUTOINCREMENT,money VARCHAR)");
        }catch (Exception e){
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }


        dialog = new Dialog(this);
        dialog1 = new Dialog(this);
        dialog2 = new Dialog(this);
        dialog3 = new Dialog(this);

        animationView = findViewById(R.id.animationView);
        toolbar = findViewById(R.id.toolbar);
        setActionBar(toolbar);
        navigationView = findViewById(R.id.nav_menu);
        //Headerr deki textview i tanıttık
        View headerView = navigationView.getHeaderView(0);
        maasTextHeader = headerView.findViewById(R.id.maasTextHeader);
        nameTextHeader = headerView.findViewById(R.id.nameTextHeader);
        animationView1 = headerView.findViewById(R.id.animationView1);
        kalanGun = findViewById(R.id.kalanGun);
        drawerLayout = findViewById(R.id.drawer);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        arrayList = new ArrayList<>();
        maasList = new ArrayList<>();
        harcamaList = new ArrayList<>();
        tutarList = new ArrayList<>();
        tarihList = new ArrayList<>();
        dayList = new ArrayList<>();
        addMoneyList = new ArrayList<>();
        countDownTime();
        fillTheListsFromDatabase();
        getKalanPara(tutarList);
        //Header için ismi çekiyoruz
        getName();
        //PieChart İşlemleri
        mChart = findViewById(R.id.chart);
        mChart.setBackgroundColor(Color.WHITE);
        moveOffScreen();
        doHalfPieChart();
        setData(tutarList.size());
        //RecycleView cast etme ve adaptörle bağlama işi
        recyclerView = findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecycleAdapter recycleAdapter = new RecycleAdapter(tarihList, harcamaList, tutarList);
        recyclerView.setAdapter(recycleAdapter);


        //TODO uygulama bittikten sonra "PieChart,RecycleView,HTML Parsing" olaylarını not et !!!


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {


                switch (item.getItemId()) {
                    case R.id.menu_profile:
                        Intent intent1 = new Intent(MainActivity.this, UserInfoUpdateActivity.class);
                        startActivity(intent1);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.menu_doviz:
                        Intent intent = new Intent(MainActivity.this, DovizActivity.class);
                        startActivity(intent);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.menu_coin:
                        Intent intent2 = new Intent(MainActivity.this, CoinActivity.class);
                        startActivity(intent2);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.menu_contact:
                        Intent intent3 = new Intent(MainActivity.this, SendMailActivity.class);
                        startActivity(intent3);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.new_month:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        dialog2.setContentView(R.layout.new_month_pop_up);
                        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(TRANSPARENT));
                        dialog2.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                        dialog2.show();
                        break;

                    case R.id.add_money:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        dialog3.setContentView(R.layout.add_money_pop_up);
                        dialog3.getWindow().setBackgroundDrawable(new ColorDrawable(TRANSPARENT));
                        dialog3.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                        dialog3.show();
                        break;


                }
                return true;
            }
        });
    }

    //Maaş gününe ne kadar kaldığını hesaplayan fonksiyon
    @SuppressLint("SetTextI18n")
    private void countDownTime() {

        Cursor cursor2 = database.rawQuery("SELECT * FROM userInfos", null);

        int dayIx = cursor2.getColumnIndex("day");

        while (cursor2.moveToNext()) {

            dayList.add(cursor2.getString(dayIx));

        }
        cursor2.close();

        for (int i = 0; i < dayList.size(); i++) {

            day += dayList.get(i);

        }

        dayInt = Integer.parseInt(day);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        String dateStr = formatter.format(date);

        char[] stringToCharArray = dateStr.toCharArray();

        for (int i = 0; i < 2; i++) {

            currentDay += stringToCharArray[i];

        }

        currentDayInt = Integer.parseInt(currentDay);

        if (dayInt < currentDayInt) {

            kalanGun.setText("Maaşa " + (30 - (currentDayInt - dayInt)) + " gün kaldı!");

        } else {

            kalanGun.setText("Maaşa " + (dayInt - currentDayInt) + " gün kaldı!");

        }


    }

    //Maaştan kalan paranın hesabı
    @SuppressLint("SetTextI18n")
    private void getKalanPara(ArrayList<String> tutarList) {

        Cursor cursor1 = database.rawQuery("SELECT * FROM userInfos", null);

        int payIx = cursor1.getColumnIndex("pay");
        int harcanan = 0;
        int eklenen = 0;

        while (cursor1.moveToNext()) {

            maasList.add(cursor1.getString(payIx));

        }
        cursor1.close();


        for (int i = 0; i < maasList.size(); i++) {

            maas += maasList.get(i);

        }

        for (int i = 0; i < tutarList.size(); i++) {

            harcanan += Integer.parseInt(tutarList.get(i));

        }

        for (int i = 0; i<addMoneyList.size();i++){

            eklenen += Integer.parseInt(addMoneyList.get(i));

        }

        maasTextHeader.setText("Kalan paranız " + ((Integer.parseInt(maas)+eklenen) - harcanan) + " ₺");


    }

    //Arraylistleri veritabanından doldurduk
    private void fillTheListsFromDatabase() {

        Cursor cursor = database1.rawQuery("SELECT * FROM payDetails", null);
        int tarihIx = cursor.getColumnIndex("tarih");
        int harcamaIx = cursor.getColumnIndex("harcanan_yer");
        int tutarIx = cursor.getColumnIndex("tutar");

        while (cursor.moveToNext()) {

            tarihList.add(cursor.getString(tarihIx));
            harcamaList.add(cursor.getString(harcamaIx));
            tutarList.add(String.valueOf(cursor.getInt(tutarIx)));

        }
        cursor.close();

        Cursor cursor2 = database2.rawQuery("SELECT * FROM addMoney", null);
        int moneyIx = cursor2.getColumnIndex("money");

        while (cursor2.moveToNext()){
            addMoneyList.add(cursor2.getString(moneyIx));
        }
        cursor2.close();

        //Giriş animasyonunun görünürlüğü
        if (tarihList.size() == 0) {
            animationView.setVisibility(View.VISIBLE);
        } else {
            animationView.setVisibility(View.INVISIBLE);
        }

    }

    // PieChartı yarım yapma ve animasyon verme olayları
    private void doHalfPieChart() {

        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        mChart.setDrawHoleEnabled(true);


        mChart.setMaxAngle(180);
        mChart.setRotationAngle(180);
        mChart.setCenterTextOffset(0, -20);
        mChart.animateY(1000, Easing.EaseInOutCubic);

        Legend l = mChart.getLegend();

        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);

        mChart.setEntryLabelColor(Color.WHITE);
        mChart.setEntryLabelTextSize(12f);
        mChart.setUsePercentValues(true);
        mChart.setRotationEnabled(false);

        l.setYOffset(35f);


    }

    // PieCharta veri girişi olayı
    private void setData(int count) {
        ArrayList<PieEntry> values = new ArrayList<>();

        for (int i = 0; i < count; i++) {

            values.add(new PieEntry(Float.parseFloat(tutarList.get(i)), harcamaList.get(i)));

        }

        PieDataSet dataSet = new PieDataSet(values, "");
        dataSet.setSelectionShift(5f);
        dataSet.setSliceSpace(3f);
        dataSet.setColors(PieChartColor.COLORS);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(15f);
        data.setValueTextColor(Color.WHITE);

        mChart.setData(data);
        mChart.invalidate();

    }

    // PieChartın yeri
    private void moveOffScreen() {

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int height = metrics.heightPixels;
        int offset = (int) (height * 0.1);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mChart.getLayoutParams();
        params.setMargins(0, 0, 0, -offset);
        mChart.setLayoutParams(params);


    }

    //Toolbar
    public void setActionBar(Toolbar actionBar) {
        this.actionBar = actionBar;
    }

    // Kullanıcının ismini headere yazma olayı
    public void getName() {

        Cursor cursor = database.rawQuery("SELECT * FROM userInfos", null);

        int nameIx = cursor.getColumnIndex("name");


        while (cursor.moveToNext()) {

            arrayList.add(cursor.getString(nameIx));

        }
        System.out.println(arrayList);
        cursor.close();

        for (int i = 0; i < arrayList.size(); i++) {

            name += arrayList.get(i);

        }

        System.out.println(name);

        nameTextHeader.setText(name);


    }

    //Harcama ekleme diyalog açılma işlemleri
    public void harcamaEkleButton(View view) {

        dialog1.setContentView(R.layout.get_harcama_tutar);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(TRANSPARENT));
        dialog1.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog1.show();

    }

    //Veri tabanına kaydetme işlemleri
    public void saveButton(View view) {
        harcananYerText = dialog1.findViewById(R.id.harcananYerText);
        tutarText = dialog1.findViewById(R.id.tutarText);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy , HH:mm");
        Date dateObj = new Date();

        if (harcananYerText.getText().toString().matches("") || tutarText.getText().toString().matches("")) {
            Toast.makeText(this, "Bilgileri eksiksiz giriniz!", Toast.LENGTH_LONG).show();
        } else {

            String harcananYer = harcananYerText.getText().toString();
            String tutar = tutarText.getText().toString();
            String tarih = dateFormat.format(dateObj);

            try {
                String sqlString = "INSERT INTO payDetails (tarih,harcanan_yer,tutar) VALUES (?,?,?)";
                SQLiteStatement sqLiteStatement = database1.compileStatement(sqlString);
                sqLiteStatement.bindString(1, tarih);
                sqLiteStatement.bindString(2, harcananYer);
                sqLiteStatement.bindString(3, tutar);
                sqLiteStatement.execute();


                dialog1.dismiss();


                //Ana aktiviteyi refreshliyor ve geçiş animasyonunu yok ediyor harika bir yöntem :)
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);


            } catch (Exception e) {
                Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }


        }


    }

    //Yeni aya başlama evet butonu
    public void newMonthYesButton(View view) {
        //Database silme işlemi
        database1.delete("PayDetails", null, null);
        dialog2.dismiss();
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);

    }

    //Yeni aya başlama hayır butonu
    public void newMonthNoButton(View view) {

        dialog2.dismiss();

    }

    //Para ekleme butonu
    @SuppressLint("SetTextI18n")
    public void addButton(View view) {

        eklenilenTutarText = dialog3.findViewById(R.id.addedMoney);
        if (eklenilenTutarText.getText().toString().matches("")){
            Toast.makeText(this, "Lütfen bir değer giriniz..", Toast.LENGTH_SHORT).show();
        }else{

            String money = eklenilenTutarText.getText().toString();

            try {

                String sqlString = "INSERT INTO addMoney (money) VALUES (?)";
                SQLiteStatement sqLiteStatement = database2.compileStatement(sqlString);
                sqLiteStatement.bindString(1,money);
                sqLiteStatement.execute();

            }catch (Exception e){
                Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
            dialog3.dismiss();

            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);



        }

    }

}