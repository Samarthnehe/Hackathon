package com.example.yo7a.healthwatcher;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BloodPressureResult extends AppCompatActivity {

    private String user, Date;
    int SP, DP;
    DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    Date today = Calendar.getInstance().getTime();
    VitalSignsHelper vitalSignsHelper = new VitalSignsHelper(this);
    private SharedPreferences loginPreferences;
    UserDB userDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_pressure_result);

        Date = df.format(today);
        TextView TBP = (TextView) this.findViewById(R.id.BPT);
        ImageButton SBP = (ImageButton) this.findViewById(R.id.SendBP);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            SP = bundle.getInt("SP");
            DP = bundle.getInt("DP");
            user = bundle.getString("Usr");

            loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
            String uname = loginPreferences.getString("username", "");

            userDB = new UserDB(this);
            Cursor c = userDB.getusername(uname);

            c.moveToNext();
            user = c.getString(0);

            TBP.setText(String.valueOf(SP + " / " + DP));
            vitalSignsHelper.addData(user, "", "", SP + " / " + DP, "");
        }

        SBP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{"recipient@example.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "Health Watcher");
                i.putExtra(Intent.EXTRA_TEXT, user + "'s Blood Pressure " + "\n" + " at " + Date + " is :    " + SP + " / " + DP);
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(BloodPressureResult.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(BloodPressureResult.this, Primary.class);
        i.putExtra("Usr", user);
        startActivity(i);
        finish();
        super.onBackPressed();
    }
}
