package com.example.yo7a.healthwatcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.math3.distribution.LogNormalDistribution;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class dashboard extends AppCompatActivity {

    Button btnAddContact, btnVitalSigns, btnWebBot, btnSendAll;
    TextView tvWelcomeText, tvO2, tvHR, tvRR, tvBP;
    ListView lvSOSContacts;
    ArrayList<Contact> contacts = new ArrayList<>();
    SOSHelper sosHelper;
    private SharedPreferences loginPreferences;
    UserDB userDB;
    String name;
    VitalSignsHelper vitalSignsHelper = new VitalSignsHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        btnAddContact = (Button) findViewById(R.id.btnAddContact);
        btnVitalSigns = (Button) findViewById(R.id.btnVitalSigns);
        btnWebBot = (Button) findViewById(R.id.btnWebBot);
        btnSendAll = (Button) findViewById(R.id.btnSendAll);
        tvWelcomeText = (TextView) findViewById(R.id.tvWelcomeText);
        tvBP = (TextView) findViewById(R.id.tvBP);
        tvHR = (TextView) findViewById(R.id.tvHR);
        tvO2 = (TextView) findViewById(R.id.tvO2);
        tvRR = (TextView) findViewById(R.id.tvRR);
        lvSOSContacts = (ListView) findViewById(R.id.lvSOSContacts);

        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/murrayhillboldregular.ttf");
        btnAddContact.setTypeface(type);
        btnVitalSigns.setTypeface(type);
        btnWebBot.setTypeface(type);
        btnSendAll.setTypeface(type);

        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        String uname = loginPreferences.getString("username", "");

        userDB = new UserDB(this);
        Cursor c = userDB.getusername(uname);

        c.moveToNext();
        name = c.getString(0);
        Log.i("LogName", name);

        tvWelcomeText.setText("Welcome! " + name);

        sosHelper = new SOSHelper(this);
        Cursor cursor = sosHelper.getListContents();

        if (cursor.getCount() == 0){

        }
        else {
            while (cursor.moveToNext()){
                contacts.add(new Contact(cursor.getString(cursor.getColumnIndex("contact_name")), cursor.getString(cursor.getColumnIndex("contact_phone"))));
                Log.i("SOSContact", cursor.getString(0) + " " + cursor.getString(1));
            }
        }

        SOSContactAdapter sosContactAdapter = new SOSContactAdapter(this, contacts);
        lvSOSContacts.setAdapter(sosContactAdapter);

        lvSOSContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                new AlertDialog.Builder(dashboard.this)
                        .setTitle("Sending SOS message...")
                        .setMessage("Are you sure you want to continue?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, (arg0, arg1) -> {
                            Intent intent=new Intent(getApplicationContext(), dashboard.class);
                            PendingIntent pi=PendingIntent.getActivity(getApplicationContext(), 0, intent,0);

                            //String name = contacts.get(position).getName();
                            String no = contacts.get(position).getNumber();

                            String msg = "This is a SOS message sent by " + name +" demanding help from you urgently because of the detoriated health condition. \n\nDo not reply on this number.";

                            SmsManager sms = SmsManager.getDefault();
                            sms.sendTextMessage(no, null, msg, pi,null);

                            Toast.makeText(getApplicationContext(), "Message Sent successfully!",
                                    Toast.LENGTH_LONG).show();
                        }).create().show();
            }
        });

        btnVitalSigns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(dashboard.this, Primary.class));
            }
        });

        btnAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(dashboard.this, AddContactPopUp.class));
            }
        });

        btnSendAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(dashboard.this)
                        .setTitle("SOS Alert!")
                        .setMessage("Are you sure you want to send an SOS to all the SOS contacts?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, (arg0, arg1) -> {
                            for (int i=0; i<contacts.size(); i++){
                                Intent intent=new Intent(getApplicationContext(), dashboard.class);
                                PendingIntent pi=PendingIntent.getActivity(getApplicationContext(), 0, intent,0);

                                String name = contacts.get(i).getName();
                                String no = contacts.get(i).getNumber();

                                String msg = "This is a SOS message sent by " + name +" demanding help from you urgently because of the detoriated health condition. \n\nDo not reply on this number.";

                                SmsManager sms = SmsManager.getDefault();
                                sms.sendTextMessage(no, null, msg, pi,null);
                            }
                            Toast.makeText(getApplicationContext(), "Message Sent successfully!",
                                    Toast.LENGTH_LONG).show();
                            System.exit(0);
                        }).create().show();
            }
        });

        c = vitalSignsHelper.getListContents(name);

        if (c.getCount() == 0){
        }
        else {
            while (c.moveToNext()){
                Log.i("LatestCheck", c.getString(0) + " " + c.getString(1)  + " "
                        + c.getString(2)  + " " + c.getString(3)  + " " + c.getString(4));
                if (!c.getString(1).equals("")){
                    tvHR.setText("Heart Rate: \n" + c.getString(1) + " bpm");
                }
                else {
                    tvHR.setText("Heart Rate: \n" + "NA");
                }
                if (!c.getString(2).equals("")){
                    tvO2.setText("Oxygen Saturation: \n" + c.getString(2) + " %");
                }
                else {
                    tvO2.setText("Oxygen Saturation: \n" + "NA");
                }
                if (!c.getString(3).equals("")){
                    tvBP.setText("Blood Pressure: \n" + c.getString(3));
                }
                else {
                    tvBP.setText("Blood Pressure: \n" + "NA");
                }
                if (!c.getString(4).equals("")){
                    tvRR.setText("Respiration Rate: \n" + c.getString(4) + " bpm");
                }
                else {
                    tvRR.setText("Respiration Rate: \n" + "NA");
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, (arg0, arg1) -> {
                    dashboard.super.onBackPressed();
                    finish();
                    System.exit(0);
                }).create().show();
    }
}