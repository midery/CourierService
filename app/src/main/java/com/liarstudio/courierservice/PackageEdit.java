package com.liarstudio.courierservice;

import android.app.DatePickerDialog;
import android.content.Intent;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.liarstudio.courierservice.BaseClasses.Package;
import com.liarstudio.courierservice.BaseClasses.Person;


public class PackageEdit extends AppCompatActivity {


    Calendar c;
    Package pkg;
    int position = -1;

    //Sender Fields
    EditText editTextSenderAddress ;
    EditText editTextSenderName;
    EditText editTextSenderEmail;
    EditText editTextSenderPhone;
    EditText editTextSenderCompanyName;
    //Recipient Fields
    EditText editTextRecipientAddress;
    EditText editTextRecipientName;
    EditText editTextRecipientEmail;
    EditText editTextRecipientPhone;
    EditText editTextRecipientCompanyName;
    TextView textViewRecipient;
    //Pack Fields
    TextView textViewPack;
    EditText editTextPackName;
    EditText editTextPackW;
    EditText editTextPackH;
    EditText editTextPackD;
    EditText editTextPackWeight;
    TextView textViewPackDate;
    Button buttonConfirm;
    Spinner spinner;
    Spinner spinnerRecipient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_package_edit);

        initView();
        Intent intent = getIntent();

        if (intent.hasExtra("jsonPackage") && intent.hasExtra("packagePosition"))
        {
            String jsonPackage = intent.getStringExtra("jsonPackage");
            pkg = new Gson().fromJson(jsonPackage, Package.class);
            position = intent.getIntExtra("packagePosition", -1);

            initFieldsForEdit();
        }
        else {
            c = Calendar.getInstance();
            c.add(Calendar.DAY_OF_YEAR, 1);

            TextView textViewPackDate = (TextView)findViewById(R.id.textViewPackDate);
            textViewPackDate.setText(Package.getStringDate(c));
        }

        initView();

    }


    void initView() {
        editTextSenderAddress = (EditText) findViewById(R.id.editTextSenderAddress);
        editTextSenderName = (EditText) findViewById(R.id.editTextSenderName);
        editTextSenderEmail = (EditText) findViewById(R.id.editTextSenderEmail);
        editTextSenderPhone = (EditText) findViewById(R.id.editTextSenderPhone);
        editTextSenderPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        editTextSenderCompanyName = (EditText) findViewById(R.id.editTextSenderCompanyName);

        textViewRecipient = (TextView) findViewById(R.id.textViewRecipient);
        editTextRecipientAddress = (EditText) findViewById(R.id.editTextRecipientAddress);
        editTextRecipientName = (EditText) findViewById(R.id.editTextRecipientName);
        editTextRecipientEmail = (EditText) findViewById(R.id.editTextRecipientEmail);
        editTextRecipientPhone = (EditText) findViewById(R.id.editTextRecipientPhone);
        editTextRecipientPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        editTextRecipientCompanyName = (EditText) findViewById(R.id.editTextRecipientCompanyName);


        textViewPack = (TextView) findViewById(R.id.textViewPack);
        editTextPackName = (EditText) findViewById(R.id.editTextPackName);
        editTextPackW = (EditText) findViewById(R.id.editTextPackW);
        editTextPackH = (EditText) findViewById(R.id.editTextPackH);
        editTextPackD = (EditText) findViewById(R.id.editTextPackD);
        editTextPackWeight = (EditText) findViewById(R.id.editTextPackWeight);
        textViewPackDate = (TextView) findViewById(R.id.textViewPackDate);

        buttonConfirm = (Button) findViewById(R.id.buttonConfirm);
        buttonConfirm.setOnClickListener(buttonConfirmPressed());

        initSpinners();
        initDatePicker();
    }

    void initSpinners() {
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View v,int position, long id) {
                // On selecting a spinner item
                if (position > 0){
                    editTextSenderCompanyName.setVisibility(View.INVISIBLE);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) textViewRecipient.getLayoutParams();
                    params.addRule(RelativeLayout.BELOW, R.id.spinner);
                    textViewRecipient.setLayoutParams(params);
                } else {
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) textViewRecipient.getLayoutParams();
                    params.addRule(RelativeLayout.BELOW, R.id.editTextSenderCompanyName);
                    textViewRecipient.setLayoutParams(params);
                    editTextSenderCompanyName.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }});

        spinnerRecipient = (Spinner) findViewById(R.id.spinnerRecipient);
        spinnerRecipient.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View v,int position, long id) {
                // On selecting a spinner item
                if (position > 0){
                    editTextRecipientCompanyName.setVisibility(View.INVISIBLE);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) textViewPack.getLayoutParams();
                    params.addRule(RelativeLayout.BELOW, R.id.spinnerRecipient);
                    textViewPack.setLayoutParams(params);
                } else {
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) textViewPack.getLayoutParams();
                    params.addRule(RelativeLayout.BELOW, R.id.editTextRecipientCompanyName);
                    textViewPack.setLayoutParams(params);
                    editTextRecipientCompanyName.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }});

    }

    void initDatePicker() {
        Button buttonPickDate = (Button)findViewById(R.id.buttonPickDate);
        DatePickerDialog.OnDateSetListener listener = (view, year, month, dayOfMonth) -> {
            //pkg.date = new GregorianCalendar(year, month, dayOfMonth);
            Calendar chosenDate = new GregorianCalendar(year, month, dayOfMonth);
            if (chosenDate.compareTo(Calendar.getInstance()) > 0)
            {
                c = chosenDate;
                textViewPackDate.setText(Package.getStringDate(c));
            }
        };

        buttonPickDate.setOnClickListener(e -> {
            DatePickerDialog dpd = new DatePickerDialog(this, listener, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
            dpd.show();
        });

    }

    void initFieldsForEdit() {
        editTextSenderAddress.setText(pkg.sender.address);
        editTextSenderName.setText(pkg.sender.name);
        editTextSenderEmail.setText(pkg.sender.email);
        editTextSenderPhone.setText(pkg.sender.phone);

        editTextRecipientAddress.setText(pkg.recipient.address);
        editTextRecipientName.setText(pkg.recipient.name);
        editTextRecipientEmail.setText(pkg.recipient.email);
        editTextRecipientPhone.setText(pkg.recipient.phone);


        c = new GregorianCalendar(pkg.date.get(Calendar.YEAR), pkg.date.get(Calendar.MONTH), pkg.date.get(Calendar.DAY_OF_MONTH));
        textViewPackDate.setText(Package.getStringDate(c));

        editTextPackName.setText(pkg.name);
        editTextPackW.setText(Integer.toString(pkg.dimensions[0]));
        editTextPackH.setText(Integer.toString(pkg.dimensions[1]));
        editTextPackD.setText(Integer.toString(pkg.dimensions[2]));
        editTextPackWeight.setText(Double.toString(pkg.weight));
    }


    private View.OnClickListener buttonConfirmPressed() {
        return (View v)  -> {
            if (validate())
            {
                pkg = loadPackage();
                Intent processData = new Intent();

                processData.putExtra("jsonPackageChild", new Gson().toJson(pkg));
                processData.putExtra("packageChildPosition", position);

                setResult(RESULT_OK, processData);
                finish();
            }
        };
    }



    Package loadPackage() {
        Person sender = new Person(spinner.getSelectedItemPosition(), editTextSenderName.getText().toString(),
                editTextSenderPhone.getText().toString(), editTextSenderEmail.getText().toString(),
                editTextSenderAddress.getText().toString(), editTextSenderCompanyName.getText().toString());

        Person recipient = new Person(spinnerRecipient.getSelectedItemPosition(), editTextRecipientName.getText().toString(),
                editTextRecipientPhone.getText().toString(), editTextRecipientEmail.getText().toString(),
                editTextRecipientAddress.getText().toString(), editTextRecipientCompanyName.getText().toString());
        int[] dimensions = {
                Integer.parseInt(editTextPackW.getText().toString()),
                Integer.parseInt(editTextPackW.getText().toString()),
                Integer.parseInt(editTextPackW.getText().toString())};
        return new Package(0, sender, recipient, editTextPackName.getText().toString(), c, dimensions,
                Double.parseDouble(editTextPackWeight.getText().toString()));
    }



    boolean validate() {

        boolean valid = true;

        String checkString = editTextSenderAddress.getText().toString();
        if (checkString.isEmpty())
        {
            valid = false;
            editTextSenderAddress.setError("Адрес не может быть пустым.");
        }

        checkString = editTextSenderName.getText().toString();
        if (checkString.isEmpty())
        {
            valid = false;
            editTextSenderName.setError("Имя не может быть пустым.");
        }

        checkString = editTextSenderEmail.getText().toString();

        if (!checkString.isEmpty() &&
                checkString.matches("^[a-z0-9._%+-]+@[a-z0-9.-]+\\\\.[a-z]{2,6}$")) {
            valid = false;
            editTextSenderEmail.setError("Введен неверный e-mail.");
        }

        checkString = editTextSenderPhone.getText().toString();
        if (checkString.length() != 14)
        {
            valid = false;
            editTextSenderPhone.setError("Введен некорректный номер телефона.");
        }

        checkString = editTextRecipientAddress.getText().toString();
        if (checkString.isEmpty())
        {
            valid = false;
            editTextRecipientAddress.setError("Адрес не может быть пустым.");
        }

        checkString = editTextRecipientName.getText().toString();
        if (checkString.isEmpty())
        {
            valid = false;
            editTextRecipientName.setError("Имя не может быть пустым.");
        }

        checkString = editTextRecipientEmail.getText().toString();

        if (!checkString.isEmpty() &&
                checkString.matches("^[a-z0-9._%+-]+@[a-z0-9.-]+\\\\.[a-z]{2,6}$")) {
            valid = false;
            editTextRecipientEmail.setError("Введен неверный e-mail.");
        }

        checkString = editTextRecipientPhone.getText().toString();
        if (checkString.length() != 14)
        {
            valid = false;
            editTextRecipientPhone.setError("Введен некорректный номер телефона.");
        }

        checkString = editTextPackName.getText().toString();
        if (checkString.isEmpty()) {
            valid = false;
            editTextPackName.setError("Введен некорректный номер телефона.");
        }

        checkString = editTextPackWeight.getText().toString();
        double numericValue = checkString.isEmpty() ? 0 : Double.parseDouble(checkString);

        if (numericValue <= 0 || numericValue > 10000)
        {
            editTextPackWeight.setError("Некорректный вес.");
            valid = false;
        }

        int numericIntValue;

        checkString = editTextPackW.getText().toString();
        numericIntValue = checkString.isEmpty() ? 0 : Integer.parseInt(checkString);
        if (numericIntValue == 0  || numericIntValue > 10000) {
            valid = false;
            editTextPackW.setError("Некорректные размеры посылки.");
        }
        checkString = editTextPackH.getText().toString();
        numericIntValue = checkString.isEmpty() ? 0 : Integer.parseInt(checkString);
        if (numericIntValue == 0  || numericIntValue > 10000) {
            valid = false;
            editTextPackH.setError("Некорректные размеры посылки.");
        }

        checkString = editTextPackD.getText().toString();
        numericIntValue = checkString.isEmpty() ? 0 : Integer.parseInt(checkString);
        if (numericIntValue == 0  || numericIntValue > 10000) {
            valid = false;
            editTextPackD.setError("Некорректные размеры посылки.");
        }

        return valid;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
