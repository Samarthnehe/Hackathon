package com.example.yo7a.healthwatcher;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import org.apache.commons.math3.analysis.function.Add;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class AddContactPopUp extends AppCompatActivity implements SearchView.OnQueryTextListener {

    ListView lvContacts;
    SOSHelper sosHelper = new SOSHelper(this);
    ArrayList<Contact> allContactList = new ArrayList<>();
    SearchView svContact;
    HashMap<String, String> occ = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact_pop_up);

        lvContacts = (ListView) findViewById(R.id.lvContacts);
        svContact = (SearchView) findViewById(R.id.svContact);

//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//
//        int width = dm.widthPixels;
//        int height = dm.heightPixels;
//
//        getWindow().setLayout((int)(width*.8), (int)(height*.5));
//
//        WindowManager.LayoutParams params = getWindow().getAttributes();
//        params.gravity = Gravity.CENTER;
//        params.x = 0;
//        params.y = 100;
//
//        getWindow().setAttributes(params);

        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC ", null);
        startManagingCursor(cursor);

        int autoIncrement = 0;
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            phoneNumber = phoneNumber.replaceAll("[^\\d.]", "");

            if (phoneNumber.length() == 10){
                phoneNumber = "+91" + phoneNumber;
            }
            else if (phoneNumber.length() == 11){
                phoneNumber = "+91" + phoneNumber.substring(1);
            }
            else if (phoneNumber.length() == 12){
                phoneNumber = "+" + phoneNumber;
            }

            if (!occ.containsKey(phoneNumber)){
                Contact contact = new Contact(name, phoneNumber);
                allContactList.add(contact);
                occ.put(phoneNumber, name);
            }
        }

        for (int i=0; i<allContactList.size(); i++)
        Log.i("AllContacts", allContactList.get(i).getName() + " " + allContactList.get(i).getNumber());

        ContactAdapter contactAdapter = new ContactAdapter(AddContactPopUp.this, allContactList);
        lvContacts.setAdapter(contactAdapter);
        lvContacts.setTextFilterEnabled(true);

        lvContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                new AlertDialog.Builder(AddContactPopUp.this)
                        .setTitle("Add Contact?")
                        .setMessage("Are you sure you want to add this as SOS contact?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, (arg0, arg1) -> {
                            Contact contact = (Contact) lvContacts.getAdapter().getItem(position);
                            if (sosHelper.addData(contact.getName(), contact.getNumber())){
                                //sosHelper.addData(allContactList.get(position).getName(), allContactList.get(position).getNumber());
                                Log.i("SOSAllContacts", contact.getName() + " " + contact.getNumber());
                                Toast.makeText(AddContactPopUp.this, "The contact has been successfully added to your SOS contacts.", Toast.LENGTH_SHORT).show();
                            }

                        }).create().show();
            }
        });

        lvContacts.setTextFilterEnabled(true);
        setupSearchView();
    }

    private void setupSearchView() {
        svContact.setIconifiedByDefault(false);
        svContact.setOnQueryTextListener(AddContactPopUp.this);
        svContact.setSubmitButtonEnabled(true);
        svContact.setQueryHint("Search here...");
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        ContactAdapter adapter = (ContactAdapter) lvContacts.getAdapter();

        if (TextUtils.isEmpty(newText)) {
            //lvMenu.clearTextFilter();
            adapter.getFilter().filter(null);
        } else {
            //lvMenu.setFilterText(newText);
            adapter.getFilter().filter(newText);
        }
        return true;
    }

    public void onBackPressed() {
        startActivity(new Intent(AddContactPopUp.this, dashboard.class));
    }
}