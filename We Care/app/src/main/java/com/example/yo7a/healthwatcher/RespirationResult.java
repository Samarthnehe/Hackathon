package com.example.yo7a.healthwatcher;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RespirationResult extends AppCompatActivity {

    private String user, Date;
    int RR;
    DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    Date today = Calendar.getInstance().getTime();
    VitalSignsHelper vitalSignsHelper = new VitalSignsHelper(this);
    private SharedPreferences loginPreferences;
    UserDB userDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_respiration_result);

        Date = df.format(today);
        TextView RRR = (TextView) this.findViewById(R.id.RRR);
        ImageButton SRR = (ImageButton) this.findViewById(R.id.SendRR);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            RR = bundle.getInt("bpm");
            user = bundle.getString("Usr");

            loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
            String uname = loginPreferences.getString("username", "");

            userDB = new UserDB(this);
            Cursor c = userDB.getusername(uname);

            c.moveToNext();
            user = c.getString(0);

            RRR.setText(String.valueOf(RR));
            vitalSignsHelper.addData(user, "", "", "", String.valueOf(RR));
        }

        SRR.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{"recipient@example.com"});
            i.putExtra(Intent.EXTRA_SUBJECT, "Health Watcher");
            i.putExtra(Intent.EXTRA_TEXT, user + "'s Respiration Rate " + "\n" + " at " + Date + " is :  " + RR);
            try {
                startActivity(Intent.createChooser(i, "Send mail..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(RespirationResult.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {

        Intent i = new Intent(RespirationResult.this, Primary.class);
        i.putExtra("Usr", user);
        startActivity(i);
        finish();
        super.onBackPressed();

    }
}
