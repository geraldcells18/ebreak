package com.example.admin.ebreak;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.ycuwq.datepicker.date.DatePickerDialogFragment;
import com.ycuwq.datepicker.date.DatePicker;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import cz.msebera.android.httpclient.Header;
import es.dmoral.toasty.Toasty;
import spencerstudios.com.bungeelib.Bungee;

public class Main5Activity extends AppCompatActivity {

    Button button_calc;
    EditText kwh, base;
    TextView total_price;
    DatePicker datePicker;
    FloatingActionButton floatingActionButton;

    String IP = "";

    String date_from = "";
    String data_to = "";

    private void getKWH(String date_from, String date_to) {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setTimeout(1500);

        asyncHttpClient.get(IP + "/main/bill.php?from=" + date_from + "&to=" + date_to, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String data = new String(responseBody, "UTF-8");
                    data = data.trim();
                    if (data.equals("0")) {
                        Toasty.warning(getApplicationContext(), "No record found to be calculated", Toast.LENGTH_SHORT).show();
                    } else {
                        base.setText("");
                        kwh.setText("");
                        kwh.setText(data.trim());
                        kwh.setSelection(kwh.getText().length());
                        base.requestFocus();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toasty.warning(getApplicationContext(), "Can't connect to server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getIPVal() {
        IP = getResources().getString(R.string.ip_add);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        getIPVal();

        button_calc = (Button) findViewById(R.id.btn_calc);
        kwh = (EditText) findViewById(R.id.kwh_textBox);
        base = (EditText) findViewById(R.id.multiplier_textBox);
        total_price = (TextView) findViewById(R.id.price);

        datePicker = findViewById(R.id.datePicker);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab2);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialogFragment datePickerDialogFragment = new DatePickerDialogFragment();
                Toasty.warning(getApplicationContext(), "Date Start", Toast.LENGTH_SHORT, false).show();
                datePickerDialogFragment.setOnDateChooseListener(new DatePickerDialogFragment.OnDateChooseListener() {
                    @Override
                    public void onDateChoose(int year, int month, int day) {
                        Toasty.warning(getApplicationContext(), "Date End", Toast.LENGTH_SHORT).show();
                        date_from = String.valueOf(year) + "-" + String.format("%02d", month) + "-" + String.format("%02d", day);
                        DatePickerDialogFragment datePickerDialogFragment2 = new DatePickerDialogFragment();
                        datePickerDialogFragment2.setOnDateChooseListener(new DatePickerDialogFragment.OnDateChooseListener() {
                            @Override
                            public void onDateChoose(int year, int month, int day) {
                                data_to = String.valueOf(year) + "-" + String.format("%02d", month) + "-" + String.format("%02d", day);
                                getKWH(date_from, data_to);
                            }
                        });
                        datePickerDialogFragment2.show(getSupportFragmentManager(), "DatePickerDialogFragment");
                    }
                });
                datePickerDialogFragment.show(getSupportFragmentManager(), "DatePickerDialogFragment");
            }
        });

        button_calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (kwh.getText().toString().isEmpty()) {
                        Toasty.info(Main5Activity.this, "Kindly input kWh before calculating", Toast.LENGTH_SHORT, true).show();
                    } else if (base.getText().toString().isEmpty()) {
                        Toasty.info(Main5Activity.this, "Kindly input base price before calculating", Toast.LENGTH_SHORT, true).show();
                    } else {
                        double kwh_num = Double.parseDouble(kwh.getText().toString().trim());
                        double base_num = Double.parseDouble(base.getText().toString().trim());
                        double total = kwh_num * base_num;
                        NumberFormat formatter = new DecimalFormat("#0.00");
                        total_price.setText("₱ " + String.valueOf(formatter.format(total)));
                    }
                } catch (Exception ex) {
                    Toasty.info(Main5Activity.this, "Invalid Value", Toast.LENGTH_SHORT, true).show();
                }
            }
        });

        kwh.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (kwh.getText().toString().isEmpty()) {
                    total_price.setText("0.00");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        base.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (base.getText().toString().equals("")) {
                    total_price.setText("0.00");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Main5Activity.this, Main3Activity.class));
        finish();
        Bungee.slideRight(Main5Activity.this);
        super.onBackPressed();
    }
}
