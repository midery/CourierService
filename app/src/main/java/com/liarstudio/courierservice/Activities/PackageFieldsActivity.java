package com.liarstudio.courierservice.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.liarstudio.courierservice.API.ApiUtils;
import com.liarstudio.courierservice.API.User;
import com.liarstudio.courierservice.API.UserAPI;
import com.liarstudio.courierservice.BaseClasses.Person;
import com.liarstudio.courierservice.BaseClasses.Package;
import com.liarstudio.courierservice.R;
import org.apache.commons.validator.routines.EmailValidator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.liarstudio.courierservice.API.ApiUtils.IS_ADMIN;


public class PackageFieldsActivity extends AppCompatActivity {


    /*
    ****** FIELDS AREA ******
    */

    public static final int REQUEST_MAP = 2;

    Package pkg;
    UserAPI api;


    List<String> allStatuses = Arrays.asList("Новая", "Назначенная", "В процессе",
            "Отклоненная", "Завершенная");


    /*
    ****** VIEW FIELDS AREA ******
    */


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
    TextView textViewStatus;
    EditText editTextCommentary;
    TextView textViewPackSizeDimension;
    TextView textViewPackWeightDimension;
    TextView textViewCourierList;

    //Spinners
    Spinner spinner;
    Spinner spinnerRecipient;
    Spinner spinnerStatus;
    Spinner spinnerCourierList;
    //Buttons
    Button buttonSetCoordinates;
    Button buttonCalculatePrice;
    Button buttonConfirm;
    Button buttonPickDate;
    Button buttonDelete;

    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_package_fields);


        //Чтобы каждый раз при открытии посылки не вываливалась клавиатура
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        initView();
        Intent intent = getIntent();


        //Определяем, для чего посылка - для создания, или редактирования
        if (intent.hasExtra("jsonPackage"))
        {
            String jsonPackage = intent.getStringExtra("jsonPackage");
            pkg = new Gson().fromJson(jsonPackage, Package.class);

            //Если пользователь - администратор, то загружаем API
            if (IS_ADMIN) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(ApiUtils.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                api = retrofit.create(UserAPI.class);

            }

            initFieldsForEdit();
        }
        else {


            pkg = new Package();

            //Создаем календарь по-умолчанию с датой равной текущему дню + 1;
            Calendar c = Calendar.getInstance();
            c.add(Calendar.DAY_OF_YEAR, 1);
            pkg.setDate(c);

            //Загружаем ID курьера у текущего пользователя.
            pkg.setCourierId(ApiUtils.CURRENT_USER.getId());
            pkg.setStatus(0);

            TextView textViewPackDate = (TextView)findViewById(R.id.textViewPackDate);
            textViewPackDate.setText(Package.getStringDate(c));
        }
    }

    /*
    ****** INITIALIZE AREA ******
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
        textViewPackSizeDimension = (TextView) findViewById(R.id.textViewPackSizeDimension);
        editTextPackWeight = (EditText) findViewById(R.id.editTextPackWeight);
        textViewPackWeightDimension = (TextView) findViewById(R.id.textViewPackWeightDimension);
        textViewPackDate = (TextView) findViewById(R.id.textViewPackDate);
        textViewPackPrice = (TextView) findViewById(R.id.textViewPackPrice);
        buttonPickDate = (Button)findViewById(R.id.buttonPickDate);



        spinnerCourierList = (Spinner) findViewById(R.id.spinnerCourierList);
        textViewCourierList = (TextView) findViewById(R.id.textViewCourierList);


        textViewStatus = (TextView) findViewById(R.id.textViewStatus);
        editTextCommentary = (EditText) findViewById(R.id.editTextCommentary);
        spinnerStatus = (Spinner) findViewById(R.id.spinnerStatus);

        progressBar = (ProgressBar) findViewById(R.id.progressBarPackageField);

        switch (Package.WEIGHT_PROGRAM_STATE) {
            case 0:
                textViewPackWeightDimension.setText(R.string.weight_kg);
                break;
            case 1:
                textViewPackWeightDimension.setText(R.string.weight_lb);
                break;
            default:
                break;
        }

        switch (Package.SIZE_PROGRAM_STATE) {
            case 0:
                textViewPackSizeDimension.setText(R.string.size_sm);
                break;
            case 1:
                textViewPackSizeDimension.setText(R.string.size_inch);
                break;
            default:
                break;
        }

        initSpinners();
        initDatePicker();
        initButtons();
    }

    /*
    * Функция, инициализирующая поведение spinner'ов отправителя и получателя
     */
    void initSpinners() {


        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View v,int position, long id) {
                if (position > 0)
                    editTextSenderCompanyName.setVisibility(View.GONE);
                else
                    editTextSenderCompanyName.setVisibility(View.VISIBLE);

            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {}});


        spinnerRecipient = (Spinner) findViewById(R.id.spinnerRecipient);
        spinnerRecipient.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View v,int position, long id) {
                if (position > 0)
                    editTextRecipientCompanyName.setVisibility(View.GONE);
                 else
                    editTextRecipientCompanyName.setVisibility(View.VISIBLE);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {}});
    }


    void initDatePicker() {
        DatePickerDialog.OnDateSetListener listener = (view, year, month, dayOfMonth) -> {

            Calendar chosenDate = new GregorianCalendar(year, month, dayOfMonth);

            //Если выбранная дата не раньше, чем завтра, обновляем дату у редактируемой посылки
            if (chosenDate.compareTo(Calendar.getInstance()) > 0)
            {
                pkg.setDate(chosenDate);
                textViewPackDate.setText(Package.getStringDate(pkg.getDate()));
            }
            else
                Toast.makeText(this,"Невозможно установить дату раньше сегодняшней.", Toast.LENGTH_LONG).show();
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
            //Проверяем поля
            //Если послылка завершенная, и readonly - просто закрываем activity
            //иначе, если она не readonly, спрашиваем, как уведомить отправителя
            //иначе, показываем диалог проверки координат
            if (!editTextPackName.isEnabled())
                finish();
            else {
                if (validate()) {
                    if (pkg.getStatus() == 4)
                        packageFinalCheckDialog();
                    else
                        coordinatesEmptyCheckDialog();
                }
            }
        });
        buttonCalculatePrice = (Button)findViewById(R.id.buttonCalculatePrice);
        buttonCalculatePrice.setOnClickListener(l -> {
            if (validateDimensions())
                textViewPackPrice.setText(Double.toString(pkg.getPrice()));
        });

        //Запускаем MapActivity и ждем от нее результата
        buttonSetCoordinates = (Button)findViewById(R.id.buttonSetCoordinates);
        buttonSetCoordinates.setOnClickListener(l -> {
            Intent mapIntent = new Intent(this, MapsActivity.class);
            mapIntent.putExtra("coordinates", pkg.getCoordinates());
            startActivityForResult(mapIntent, REQUEST_MAP);
        });

        //Кнопка удалить, которая видна только администратору
        buttonDelete = (Button)findViewById(R.id.buttonDelete);
        buttonDelete.setOnClickListener(l -> deleteDialog());
        if (IS_ADMIN)
            buttonDelete.setVisibility(View.VISIBLE);
    }


    void initFieldsForEdit() {
        Person sender = pkg.getSender();

        editTextSenderAddress.setText(sender.getAddress());
        editTextSenderName.setText(sender.getName());
        editTextSenderEmail.setText(sender.getEmail());
        editTextSenderPhone.setText(sender.getPhone());
        editTextSenderCompanyName.setText(sender.getCompanyName());
        spinner.setSelection(sender.getType());
        editTextSenderCompanyName.setText(sender.getCompanyName());


        Person recipient = pkg.getRecipient();
        editTextRecipientAddress.setText(recipient.getAddress());
        editTextRecipientName.setText(recipient.getName());
        editTextRecipientEmail.setText(recipient.getEmail());
        editTextRecipientPhone.setText(recipient.getPhone());
        editTextRecipientCompanyName.setText(recipient.getCompanyName());
        spinnerRecipient.setSelection(recipient.getType());
        editTextRecipientCompanyName.setText(recipient.getCompanyName());


        textViewPackDate.setText(pkg.getStringDate());

        editTextPackName.setText(pkg.getName());
        double[] dimensions = pkg.getSizes();
        editTextPackW.setText(Double.toString(dimensions[0]));
        editTextPackH.setText(Double.toString(dimensions[1]));
        editTextPackD.setText(Double.toString(dimensions[2]));
        editTextPackWeight.setText(Double.toString(pkg.getWeight()));


        buttonConfirm.setText("Обновить");


        //Список строк для отображения статусов
        List<String> statuses;

        switch (pkg.getStatus()) {
            case 0:
                if (IS_ADMIN) {
                    statuses = Arrays.asList("Назначенная");
                    loadCourierList();
                } else {
                    statuses = Arrays.asList("Новая");
                }
                break;
            case 1:
                pkg.setStatus(2);
                statuses = Arrays.asList("В процессе", "Отклоненная");
                break;
            case 2:
                statuses = Arrays.asList("В процессе", "Отклоненная", "Завершенная");
                break;
            case 3:
                statuses = Arrays.asList("Назначенная", "Завершенная");
                loadCourierList();
                editTextCommentary.setVisibility(View.VISIBLE);
                break;
            default:
                statuses = Arrays.asList("Завершенная");
                setReadOnlyView();
                if (IS_ADMIN) {
                    loadCourierByID();
                    spinnerCourierList.setEnabled(false);
                }
                break;
        }



        //Устанавливаем Listener для spinnerStatus и устанавливаем список статусов как его контент
        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View v,int position, long id) {
                if (position == 0)
                    editTextCommentary.setVisibility(View.GONE);
                else
                    editTextCommentary.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {}});


        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                R.layout.support_simple_spinner_dropdown_item, statuses);
        spinnerStatus.setAdapter(spinnerAdapter);
        textViewStatus.setVisibility(View.VISIBLE);
        editTextCommentary.setText(pkg.getCommentary());

        spinnerStatus.setVisibility(View.VISIBLE);
    }

    void setReadOnlyView() {
        editTextSenderAddress.setEnabled(false);
        editTextSenderName.setEnabled(false);
        editTextSenderEmail.setEnabled(false);
        editTextSenderPhone.setEnabled(false);
        editTextSenderCompanyName.setEnabled(false);
        spinner.setEnabled(false);
        editTextSenderCompanyName.setEnabled(false);

        editTextRecipientAddress.setEnabled(false);
        editTextRecipientName.setEnabled(false);
        editTextRecipientEmail.setEnabled(false);
        editTextRecipientPhone.setEnabled(false);
        editTextRecipientCompanyName.setEnabled(false);
        spinnerRecipient.setEnabled(false);
        editTextRecipientCompanyName.setEnabled(false);

        textViewPackDate.setEnabled(false);

        editTextPackName.setEnabled(false);
        editTextPackW.setEnabled(false);
        editTextPackH.setEnabled(false);
        editTextPackD.setEnabled(false);
        editTextPackWeight.setEnabled(false);
        buttonSetCoordinates.setEnabled(false);
        buttonPickDate.setEnabled(false);

        buttonConfirm.setText("Закрыть");

        spinnerStatus.setEnabled(false);
        spinnerStatus.setOnItemSelectedListener(null);
        spinnerCourierList.setEnabled(false);
        editTextCommentary.setVisibility(View.VISIBLE);
        editTextCommentary.setEnabled(false);

    }


    /*
    ****** VALIDATION AREA ******
    */


    /*
    * Функция проверки введенной пользователем информации
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

        if (!checkString.isEmpty() && !EmailValidator.getInstance().isValid(checkString))
        {


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

        pkg.getSender().setCompanyName(editTextSenderCompanyName.getText().toString());
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

        if (!checkString.isEmpty() && !EmailValidator.getInstance().isValid(checkString)) {
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

        pkg.getSender().setCompanyName(editTextRecipientCompanyName.getText().toString());


        checkString = editTextPackName.getText().toString();
        if (checkString.isEmpty()) {
            valid = false;
            editTextPackName.setError("Введен некорректный номер телефона.");
        } else
            pkg.setName(checkString);

        valid = validateDimensions() && valid;

        if (spinnerStatus.getVisibility() == View.VISIBLE)
            pkg.setStatus(allStatuses.indexOf(spinnerStatus.getSelectedItem().toString()));

        checkString = editTextCommentary.getText().toString();
        if ((pkg.getStatus() == 3 || pkg.getStatus() == 4) && checkString.isEmpty()) {
            valid = false;
            editTextCommentary.setError("Неверный комментарий.");
        } else
            pkg.setCommentary(checkString);

        return valid;
    }

    /*
    * Функция проверки значений с плавающей запятой: веса и размеров посылки
     */
    boolean validateDimensions() {
        boolean valid = true;
        String checkString = editTextPackWeight.getText().toString();
        double numericValue = checkString.isEmpty() || checkString.startsWith(".") ? 0 : Double.parseDouble(checkString);

        if (numericValue <= 0 || numericValue > 10000)
        {
            editTextPackWeight.setError("Некорректный вес.");
            valid = false;
        } else
            pkg.setWeight(numericValue);

        double[] dimensions = new double[3];

        checkString = editTextPackW.getText().toString();
        numericValue = checkString.isEmpty() || checkString.startsWith(".") ? 0 : Double.parseDouble(checkString);
        if (numericValue == 0  || numericValue > 10000) {
            valid = false;
            editTextPackW.setError("Некорректные размеры посылки.");
        }
            else dimensions[0] = numericValue;
        checkString = editTextPackH.getText().toString();
        numericValue = checkString.isEmpty() || checkString.startsWith(".") ? 0 : Double.parseDouble(checkString);
        if (numericValue == 0  || numericValue > 10000) {
            valid = false;
            editTextPackH.setError("Некорректные размеры посылки.");
        } else dimensions[1] = numericValue;


        checkString = editTextPackD.getText().toString();
        numericValue = checkString.isEmpty() || checkString.startsWith(".") ? 0 : Double.parseDouble(checkString);
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
    ****** DIALOG AREA ******
    */


    /*
    * Диалог перед отметкой посылки, как завершенная. Спрашиваем у пользователя, как следует уведомить отправителя
     */
    void packageFinalCheckDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Уведомление")
                .setMessage("Как вы желаете оповестить заказчика?")
                .setNegativeButton("Телефон", (dialog, which) -> {

                    Intent intentPhone = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + editTextSenderPhone.getText().toString()));
                    startActivity(intentPhone);

                    finishAndSend();
                })
                .setPositiveButton("Не уведомлять", (dialog, which) -> finishAndSend());
        if (!editTextSenderPhone.getText().toString().isEmpty())
            builder.setNeutralButton("E-mail", (dialog, which) -> {
                Intent intentEmail = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", editTextSenderEmail.getText().toString(), null));
                startActivity(intentEmail);
                finishAndSend();
            });

        builder.create().show();
    }


    /*
    * Если координаты
     */
    void coordinatesEmptyCheckDialog() {
        double[] coordinates = pkg.getCoordinates();
        if (coordinates[0] != 0 && coordinates[1] != 0) {
            finishAndSend();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Уведомление")
                .setMessage("Вы не указали координаты. Желаете продолжить без них?")
                .setPositiveButton("Продолжить", (dialog, which) -> finishAndSend())
                .setNegativeButton("Вернуться", (dialog, which) -> {});
        builder.create().show();
    }


    void deleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Уведомление")
                .setMessage("Вы действительно желаете удалить посылку?")
                .setPositiveButton("Продолжить", (dialog, which) -> finishAndSend("packageToDelete"))
                .setNegativeButton("Вернуться", (dialog, which) -> {});
        builder.create().show();
    }

    /*
    ****** FINALIZE AREA ******
    */

    /*
    * Функция завершения PackageFieldsActivity и отсылки результирующей посылки в запустившую ее activity
     */
    private void finishAndSend() {

        Intent data = new Intent();

        data.putExtra("packageToAdd", new Gson().toJson(pkg));

        setResult(RESULT_OK, data);
        finish();
    }

    /*
    * Перегруженная версия метода finsihAndSend() для того, чтобы задать другой тэг для отправляемой
    * посылки
    */
    private void finishAndSend(String extraName) {
        Intent data = new Intent();

        data.putExtra(extraName, new Gson().toJson(pkg));

        setResult(RESULT_OK, data);
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_MAP) {
            pkg.setCoordinates(data.getDoubleArrayExtra("coordinates"));
        }
    }


    /*
    ****** SERVER AREA ******
    */



    /*
    * Функция загрузки списка курьера с сервера
     */
    private void loadCourierList() {
        textViewCourierList.setVisibility(View.VISIBLE);
        spinnerCourierList.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        api.loadUsers(0).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                switch (response.code()) {
                    case HttpURLConnection.HTTP_OK:


                        //Загружаем список курьеров с сервера, заполняем строковый список их
                        // id и именами, загружаем их в spinnerCourierList и устанавливаем
                        // выбранным элементом, курьера, создавшего эту посылку.

                        List<User> users = response.body();

                        if (users != null) {

                            List<String> names = new ArrayList<>();
                            int i = 0, position = -1;
                            for (User user : users) {
                                names.add(user.getId() + ". " + user.getName());
                                if (user.getId() == pkg.getCourierId())
                                    position = i;
                                i++;
                            }
                            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(PackageFieldsActivity.this,
                                    R.layout.support_simple_spinner_dropdown_item, names);
                            spinnerCourierList.setAdapter(spinnerAdapter);
                            if (position != -1)
                                spinnerCourierList.setSelection(position);

                            spinnerCourierList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    String fullName = spinnerCourierList.getSelectedItem().toString();
                                    pkg.setCourierId(Integer.parseInt(fullName.split(". ")[0]));
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });
                        } else {
                            spinnerCourierList.setVisibility(View.GONE);
                            textViewCourierList.setVisibility(View.GONE);
                        }
                        break;
                    case HttpURLConnection.HTTP_NOT_FOUND:
                        Toast.makeText(PackageFieldsActivity.this, "Произошла ошибка работы с базой данных.",
                                Toast.LENGTH_LONG).show();

                        break;
                    default:
                        Toast.makeText(PackageFieldsActivity.this, "Произошла ошибка на стороне сервера.",
                                Toast.LENGTH_LONG).show();
                        break;
                }
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(PackageFieldsActivity.this, "Время ожидание ответа от сервера истекло.",
                        Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);

            }
        });
    }
    private void loadCourierByID() {
        textViewCourierList.setVisibility(View.VISIBLE);
        spinnerCourierList.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        api.loadUser(pkg.getCourierId()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                switch (response.code()) {
                    case HttpURLConnection.HTTP_OK:

                        //Загружаем курьера, создавшего посылку, и помещаем его в spinner.
                        //Если курьера нет в списке(например, он был уволен и удален, убираем
                        //соответствующие view-элементы


                        User user = response.body();

                        if (user != null) {

                            List<String> names = Arrays.asList(user.getId() + ". " + user.getName());

                            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(PackageFieldsActivity.this,
                                    R.layout.support_simple_spinner_dropdown_item, names);
                            spinnerCourierList.setAdapter(spinnerAdapter);
                        } else {
                            spinnerCourierList.setVisibility(View.GONE);
                            textViewCourierList.setVisibility(View.GONE);
                        }
                        break;
                    case HttpURLConnection.HTTP_NOT_FOUND:
                        Toast.makeText(PackageFieldsActivity.this, "Произошла ошибка работы с базой данных.",
                                Toast.LENGTH_LONG).show();

                        break;
                    default:
                        Toast.makeText(PackageFieldsActivity.this, "Произошла ошибка на стороне сервера.",
                                Toast.LENGTH_LONG).show();
                        break;
                }
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(PackageFieldsActivity.this, "Время ожидание ответа от сервера истекло.",
                        Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
