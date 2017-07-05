package com.liarstudio.courierservice;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.util.Calendar;
import android.icu.util.GregorianCalendar;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.liarstudio.courierservice.BaseClasses.Package;

import java.util.Date;

public class PackageEdit extends AppCompatActivity {


    Calendar c;
    Package pkg;
    int position = -1;
    EditText editTextSenderAddress ;
    EditText editTextSenderName;
    EditText editTextSenderEmail;
    EditText editTextSenderPhone;

    EditText editTextRecipientAddress;
    EditText editTextRecipientName;
    EditText editTextRecipientEmail;
    EditText editTextRecipientPhone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_package_edit);

        editTextSenderAddress = (EditText) findViewById(R.id.editTextSenderAddress);
        editTextSenderName = (EditText) findViewById(R.id.editTextSenderName);
        editTextSenderEmail = (EditText) findViewById(R.id.editTextSenderEmail);
        editTextSenderPhone = (EditText) findViewById(R.id.editTextSenderPhone);

        editTextRecipientAddress = (EditText) findViewById(R.id.editTextRecipientAddress);
        editTextRecipientName = (EditText) findViewById(R.id.editTextRecipientName);
        editTextRecipientEmail = (EditText) findViewById(R.id.editTextRecipientEmail);
        editTextRecipientPhone = (EditText) findViewById(R.id.editTextRecipientPhone);

        c = Calendar.getInstance();

        Intent intent = getIntent();

        if (intent.hasExtra("jsonPackage") && intent.hasExtra("packagePosition"))
        {
            String jsonPackage = intent.getStringExtra("jsonPackage");
            pkg = new Gson().fromJson(jsonPackage, Package.class);
            position = intent.getIntExtra("packagePosition", -1);
            initFieldsForEdit();
        }
        else {
            pkg = new Package();

            pkg.date = new Date(System.currentTimeMillis() + (1000 * 60 * 60 * 24));
            TextView textViewPackDate = (TextView)findViewById(R.id.textViewPackDate);
            textViewPackDate.setText("Дата доставки: " + pkg.getDate());
        }
        Button buttonConfirm = (Button) findViewById(R.id.buttonConfirm);
        buttonConfirm.setOnClickListener(buttonConfirmPressed());


    }

    private View.OnClickListener buttonConfirmPressed() {
        return (View v)  -> {
            if (validate())
            {
                Intent processData = new Intent();

                processData.putExtra("jsonPackageChild", new Gson().toJson(pkg));
                processData.putExtra("packageChildPosition", position);

                setResult(RESULT_OK);
                finish();
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    void initFieldsForEdit() {
        editTextSenderAddress.setText(pkg.sender.address);
        editTextSenderName.setText(pkg.sender.name);
        editTextSenderEmail.setText(pkg.sender.email);
        editTextSenderPhone.setText(pkg.sender.phone);


        editTextRecipientAddress.setText(pkg.sender.address);
        editTextRecipientName.setText(pkg.sender.name);
        editTextRecipientEmail.setText(pkg.sender.email);
        editTextRecipientPhone.setText(pkg.sender.phone);



        editTextRecipientAddress.setError("Kuk");


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

            c.setTime(pkg.date);

            DatePickerDialog dpd = new DatePickerDialog(this, listener, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
            dpd.show();
        });
    }

    boolean validate() {
        boolean valid = true;
        return valid;
    }

}
