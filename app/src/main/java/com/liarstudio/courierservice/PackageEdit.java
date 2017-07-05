package com.liarstudio.courierservice;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.icu.util.GregorianCalendar;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.liarstudio.courierservice.BaseClasses.Package;

import java.util.Date;

public class PackageEdit extends AppCompatActivity {


    Package pkg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_package_edit);

        Intent intent = getIntent();
        if (intent.getExtras() != null)
        {
            String jsonPackage = intent.getStringExtra("jsonPackage");
            pkg = new Gson().fromJson(jsonPackage, Package.class);
            initFieldsForEdit();
        }
        else {
            pkg = new Package();
            pkg.date = new Date(pkg.date.getTime() + (1000 * 60 * 60 * 24));
            TextView textViewPackDate = (TextView)findViewById(R.id.textViewPackDate);
            textViewPackDate.setText("Дата доставки: " + pkg.getDate());
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    void initFieldsForEdit() {
        EditText editTextSenderAddress = (EditText)findViewById(R.id.editTextSenderAddress);
        EditText editTextSenderName = (EditText)findViewById(R.id.editTextSenderName);
        EditText editTextSenderEmail = (EditText)findViewById(R.id.editTextSenderEmail);
        EditText editTextSenderPhone = (EditText)findViewById(R.id.editTextSenderPhone);

        editTextSenderAddress.setText(pkg.sender.address);
        editTextSenderName.setText(pkg.sender.name);
        editTextSenderEmail.setText(pkg.sender.email);
        editTextSenderPhone.setText(pkg.sender.phone);

        EditText editTextRecipientAddress = (EditText)findViewById(R.id.editTextRecipientAddress);
        EditText editTextRecipientName = (EditText)findViewById(R.id.editTextRecipientName);
        EditText editTextRecipientEmail = (EditText)findViewById(R.id.editTextRecipientEmail);
        EditText editTextRecipientPhone = (EditText)findViewById(R.id.editTextRecipientPhone);

        editTextRecipientAddress.setText(pkg.sender.address);
        editTextRecipientName.setText(pkg.sender.name);
        editTextRecipientEmail.setText(pkg.sender.email);
        editTextRecipientPhone.setText(pkg.sender.phone);


        TextView textViewPackDate = (TextView)findViewById(R.id.textViewPackDate);


        Button buttonPickDate = (Button)findViewById(R.id.buttonPickDate);
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                pkg.date = new GregorianCalendar(year, month, dayOfMonth).getTime();

                textViewPackDate.setText("Дата доставки: " + pkg.getDate());
            }
        };

        buttonPickDate.setOnClickListener(e -> {

            Calendar c = Calendar.getInstance();
            c.setTime(pkg.date);

            DatePickerDialog dpd = new DatePickerDialog(this, listener, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
            dpd.show();
        });
    }

}
