package com.liarstudio.courierservice.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.net.Uri;
import android.support.v7.app.AlertDialog;
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
import com.liarstudio.courierservice.R;


public class PackageEdit extends AppCompatActivity {

    public static final int REQUEST_MAP = 2;

    int position = -1;
    int status = 0;
    double[] coordinates;

    Package pkg;

    //Sender Fields
    EditText editTextSenderAddress;
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
    TextView textViewPackPrice;
    TextView textViewFinal;
    EditText editTextFinalCommentary;
    //Spinners
    Spinner spinner;
    Spinner spinnerRecipient;
    Spinner spinnerFinalStatus;
    //Buttons
    Button buttonSetCoordinates;
    Button buttonCalculatePrice;
    Button buttonConfirm;


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
            coordinates = pkg.getCoordinates();
            initFieldsForEdit();
        }
        else {
            Package pkg = new Package();

            Calendar c = Calendar.getInstance();
            c.add(Calendar.DAY_OF_YEAR, 1);

            pkg.setDate(c);

            coordinates = new double[] {0,0};
            TextView textViewPackDate = (TextView)findViewById(R.id.textViewPackDate);
            textViewPackDate.setText(Package.getStringDate(c));
        }
    }

    /*
    ****** INITIALIZE ZONE ******
    */
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
        textViewPackPrice = (TextView) findViewById(R.id.textViewPackPrice);


        textViewFinal = (TextView) findViewById(R.id.textViewFinal);
        editTextFinalCommentary = (EditText) findViewById(R.id.editTextFinalCommentary);



        initSpinners();
        initDatePicker();
        initButtons();
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
            public void onNothingSelected(AdapterView<?> arg0) {}});

        spinnerRecipient = (Spinner) findViewById(R.id.spinnerRecipient);
        spinnerRecipient.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View v,int position, long id) {
                // On selecting a spinner item
                if (position > 0){
                    editTextRecipientCompanyName.setVisibility(View.INVISIBLE);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) buttonSetCoordinates.getLayoutParams();
                    params.addRule(RelativeLayout.BELOW, R.id.spinnerRecipient);
                    buttonSetCoordinates.setLayoutParams(params);
                } else {
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) buttonSetCoordinates.getLayoutParams();
                    params.addRule(RelativeLayout.BELOW, R.id.editTextRecipientCompanyName);
                    buttonSetCoordinates.setLayoutParams(params);
                    editTextRecipientCompanyName.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {}});
    }

    void initDatePicker() {
        Button buttonPickDate = (Button)findViewById(R.id.buttonPickDate);
        DatePickerDialog.OnDateSetListener listener = (view, year, month, dayOfMonth) -> {
            //pkg.date = new GregorianCalendar(year, month, dayOfMonth);
            Calendar chosenDate = new GregorianCalendar(year, month, dayOfMonth);
            if (chosenDate.compareTo(Calendar.getInstance()) > 0)
            {
                pkg.setDate(chosenDate);
                textViewPackDate.setText(Package.getStringDate(pkg.getDate()));
            }
        };

        buttonPickDate.setOnClickListener(e -> {
            DatePickerDialog dpd = new DatePickerDialog(this, listener, pkg.getDate().get(Calendar.YEAR),
                    pkg.getDate().get(Calendar.MONTH), pkg.getDate().get(Calendar.DAY_OF_MONTH));
            dpd.show();
        });

    }

    void initButtons() {
        buttonConfirm = (Button) findViewById(R.id.buttonConfirm);
        buttonConfirm.setOnClickListener(v -> {
            if (validate()) {
                if (status == 1)
                    packageCloseCheckDialog();
                else
                    coordinateCheckDialog();}
        });
        buttonCalculatePrice = (Button)findViewById(R.id.buttonCalculatePrice);
        buttonCalculatePrice.setOnClickListener(v -> {
            if (validateDimensions())
                textViewPackPrice.setText(Double.toString(pkg.getPrice()));
        });

        buttonSetCoordinates = (Button)findViewById(R.id.buttonSetCoordinates);
        buttonSetCoordinates.setOnClickListener(v -> {
            Intent mapIntent = new Intent(this, MapsActivity.class);
            startActivityForResult(mapIntent, REQUEST_MAP);
        });

    }

    void initFieldsForEdit() {
        Person sender = pkg.getSender();

        editTextSenderAddress.setText(sender.getAddress());
        editTextSenderName.setText(sender.getName());
        editTextSenderEmail.setText(sender.getEmail());
        editTextSenderPhone.setText(sender.getPhone());
        editTextSenderCompanyName.setText(sender.getCompanyName());

        Person recipient = pkg.getRecipient();
        editTextRecipientAddress.setText(recipient.getAddress());
        editTextRecipientName.setText(recipient.getName());
        editTextRecipientEmail.setText(recipient.getEmail());
        editTextRecipientPhone.setText(recipient.getPhone());
        editTextRecipientCompanyName.setText(recipient.getCompanyName());


        //c = new GregorianCalendar(pkg.getDate().get(Calendar.YEAR), pkg.getDate().get(Calendar.MONTH), pkg.getDate().get(Calendar.DAY_OF_MONTH));
        textViewPackDate.setText(pkg.getStringDate());

        editTextPackName.setText(pkg.getName());
        double[] dimensions = pkg.getSizes();
        editTextPackW.setText(Double.toString(dimensions[0]));
        editTextPackH.setText(Double.toString(dimensions[1]));
        editTextPackD.setText(Double.toString(dimensions[2]));
        editTextPackWeight.setText(Double.toString(pkg.getWeight()));
        buttonConfirm.setText("Изменить");


        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) buttonConfirm.getLayoutParams();
        params.addRule(RelativeLayout.BELOW, R.id.spinnerFinalStatus);
        buttonConfirm.setLayoutParams(params);



        spinnerFinalStatus = (Spinner) findViewById(R.id.spinnerFinalStatus);

        textViewFinal.setVisibility(View.VISIBLE);
        spinnerFinalStatus.setVisibility(View.VISIBLE);

        spinnerFinalStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View v,int position, long id) {
                // On selecting a spinner item
                status = position;

                if (position == 0){
                    editTextFinalCommentary.setVisibility(View.INVISIBLE);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) buttonConfirm.getLayoutParams();
                    params.addRule(RelativeLayout.BELOW, R.id.spinnerFinalStatus);
                    buttonConfirm.setLayoutParams(params);
                } else {
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) buttonConfirm.getLayoutParams();
                    params.addRule(RelativeLayout.BELOW, R.id.editTextFinalCommentary);
                    buttonConfirm.setLayoutParams(params);
                    editTextFinalCommentary.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {}});


    }


    /*
    ****** VALIDATION ZONE ******
    */

    boolean validate() {

        boolean valid = true;

        String checkString = editTextSenderAddress.getText().toString();
        if (checkString.isEmpty())
        {
            valid = false;
            editTextSenderAddress.setError("Адрес не может быть пустым.");
        }
        else
            pkg.getSender().setAddress(checkString);

        checkString = editTextSenderName.getText().toString();
        if (checkString.isEmpty())
        {
            valid = false;
            editTextSenderName.setError("Имя не может быть пустым.");
        }
        else
            pkg.getSender().setName(checkString);

        checkString = editTextSenderEmail.getText().toString();

        if (!checkString.isEmpty() &&
                checkString.matches("^[a-z0-9._%+-]+@[a-z0-9.-]+\\\\.[a-z]{2,6}$")) {
            valid = false;
            editTextSenderEmail.setError("Введен неверный e-mail.");
        }
        else
            pkg.getSender().setEmail(checkString);


        checkString = editTextSenderPhone.getText().toString();
        if (checkString.length() != 14)
        {
            valid = false;
            editTextSenderPhone.setError("Введен некорректный номер телефона.");
        }
        else
            pkg.getSender().setPhone(checkString);

        checkString = editTextRecipientAddress.getText().toString();
        if (checkString.isEmpty())
        {
            valid = false;
            editTextRecipientAddress.setError("Адрес не может быть пустым.");
        } else
            pkg.getRecipient().setAddress(checkString);


        checkString = editTextRecipientName.getText().toString();
        if (checkString.isEmpty())
        {
            valid = false;
            editTextRecipientName.setError("Имя не может быть пустым.");
        } else
            pkg.getRecipient().setName(checkString);


        checkString = editTextRecipientEmail.getText().toString();

        if (!checkString.isEmpty() &&
                checkString.matches("^[a-z0-9._%+-]+@[a-z0-9.-]+\\\\.[a-z]{2,6}$")) {
            valid = false;
            editTextRecipientEmail.setError("Введен неверный e-mail.");
        } else
            pkg.getRecipient().setEmail(checkString);


        checkString = editTextRecipientPhone.getText().toString();
        if (checkString.length() != 14)
        {
            valid = false;
            editTextRecipientPhone.setError("Введен некорректный номер телефона.");
        } else
            pkg.getRecipient().setPhone(checkString);

        checkString = editTextPackName.getText().toString();
        if (checkString.isEmpty()) {
            valid = false;
            editTextPackName.setError("Введен некорректный номер телефона.");
        } else
            pkg.setName(checkString);

        valid = validateDimensions() ? valid : false;

        if (status > 0 && editTextFinalCommentary.getText().toString().isEmpty()) {
            valid = false;
            editTextFinalCommentary.setError("Неверный комментарий.");
        }

        return valid;
    }

    boolean validateDimensions() {
        boolean valid = true;
        String checkString = editTextPackWeight.getText().toString();
        double numericValue = checkString.isEmpty() ? 0 : Double.parseDouble(checkString);

        if (numericValue <= 0 || numericValue > 10000)
        {
            editTextPackWeight.setError("Некорректный вес.");
            valid = false;
        } else
            pkg.setWeight(numericValue);

        double[] dimensions = new double[3];

        checkString = editTextPackW.getText().toString();
        numericValue = checkString.isEmpty() ? 0 : Double.parseDouble(checkString);
        if (numericValue == 0  || numericValue > 10000) {
            valid = false;
            editTextPackW.setError("Некорректные размеры посылки.");
        }
            else dimensions[0] = numericValue;
        checkString = editTextPackH.getText().toString();
        numericValue = checkString.isEmpty() ? 0 : Double.parseDouble(checkString);
        if (numericValue == 0  || numericValue > 10000) {
            valid = false;
            editTextPackH.setError("Некорректные размеры посылки.");
        } else dimensions[1] = numericValue;


        checkString = editTextPackD.getText().toString();
        numericValue = checkString.isEmpty() ? 0 : Double.parseDouble(checkString);
        if (numericValue == 0  || numericValue > 10000) {
            valid = false;
            editTextPackD.setError("Некорректные размеры посылки.");
        } else dimensions[2] = numericValue;
        if (valid) {
            pkg.setSizes(dimensions);
            pkg.setPrice();
        }

        return valid;

    }

    /*
    ****** SUPPORT ZONE ******
    */


    /*Package loadPackage() {
        Person sender = new Person(spinner.getSelectedItemPosition(), editTextSenderName.getText().toString(),
                editTextSenderPhone.getText().toString(), editTextSenderEmail.getText().toString(),
                editTextSenderAddress.getText().toString(), editTextSenderCompanyName.getText().toString());

        Person recipient = new Person(spinnerRecipient.getSelectedItemPosition(), editTextRecipientName.getText().toString(),
                editTextRecipientPhone.getText().toString(), editTextRecipientEmail.getText().toString(),
                editTextRecipientAddress.getText().toString(), editTextRecipientCompanyName.getText().toString(), coordinates);
        double[] dimensions = {
                Double.parseDouble(editTextPackW.getText().toString()),
                Double.parseDouble(editTextPackW.getText().toString()),
                Double.parseDouble(editTextPackW.getText().toString())};
         return new Package(status, sender, recipient, editTextPackName.getText().toString(), c, dimensions,
                Double.parseDouble(editTextPackWeight.getText().toString()));
    }*/

    void packageCloseCheckDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Уведомление")
                .setMessage("Как вы желаете оповестить заказчика?")
                .setNegativeButton("Телефон", (dialog, which) -> {

                    Intent intentPhone = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + editTextSenderPhone.getText().toString()));
                    startActivity(intentPhone);

                    passAndFinish();
                })
                .setPositiveButton("Не уведомлять", (dialog, which) -> passAndFinish());
        if (!editTextSenderPhone.getText().toString().isEmpty())
            builder.setNeutralButton("E-mail", (dialog, which) -> {
                Intent intentEmail = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", editTextSenderEmail.getText().toString(), null));
                startActivity(intentEmail);
                passAndFinish();
            });

        builder.create().show();
    }

    void coordinateCheckDialog() {
        if (coordinates[0] != 0 && coordinates[1] != 0) {
            passAndFinish();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Уведомление")
                .setMessage("Вы не указали координаты. Желаете продолжить без них?")
                .setPositiveButton("Продолжить", (dialog, which) -> {
                    passAndFinish();
                })
                .setNegativeButton("Вернуться", (dialog, which) -> {

                });
        builder.create().show();
    }
    private void passAndFinish() {

       //Package pkg = loadPackage();
        Intent data = new Intent();

        data.putExtra("jsonPackageChild", new Gson().toJson(pkg));
        data.putExtra("packageChildPosition", position);

        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_MAP) {
            coordinates = data.getDoubleArrayExtra("coordinates");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
