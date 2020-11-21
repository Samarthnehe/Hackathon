package com.example.weCare.application;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class HeartRateResult extends AppCompatActivity {

    private String user, Date;
    int HR;
    DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    Date today = Calendar.getInstance().getTime();
    VitalSignsHelper vitalSignsHelper = new VitalSignsHelper(this);
    private SharedPreferences loginPreferences;
    UserDB userDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_rate_result);

        Date = df.format(today);
        TextView RHR = (TextView) this.findViewById(R.id.HRR);
        ImageButton SHR = (ImageButton) this.findViewById(R.id.SendHR);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            HR = bundle.getInt("bpm");
            user = bundle.getString("Usr");
            Log.i("DEBUG_TAG", "ccccc" + user);

            loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
            String uname = loginPreferences.getString("username", "");

            userDB = new UserDB(this);
            Cursor c = userDB.getusername(uname);

            c.moveToNext();
            user = c.getString(0);

            RHR.setText(String.valueOf(HR));
            vitalSignsHelper.addData(user, String.valueOf(HR), "", "", "");
        }

        SHR.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{"recipient@example.com"});
            i.putExtra(Intent.EXTRA_SUBJECT, "Health Watcher");
            i.putExtra(Intent.EXTRA_TEXT, user + "'s Heart Rate " + "\n" + " at " + Date + " is :    " + HR);
            try {
                startActivity(Intent.createChooser(i, "Send mail..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(HeartRateResult.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(HeartRateResult.this, Primary.class);
        i.putExtra("Usr", user);
        startActivity(i);
        finish();
    }
}
