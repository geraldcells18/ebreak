package com.example.admin.ebreak;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import es.dmoral.toasty.Toasty;
import spencerstudios.com.bungeelib.Bungee;

public class Main7Activity extends AppCompatActivity {

    ImageView query_btn;
    Spinner spinner_month, spinner_day, spinner_year;
    CheckBox checkBox1, checkBox2, checkBox3;
    int list_logo = R.drawable.ic_bubble;

    String data_link = "", IP = "";

    private ListView androidListView;
    private ConsumptionInflater consumptionInflater;

    void getIPVal() {
        IP = getResources().getString(R.string.ip_add);
        data_link = IP + "/main/";
    }

    private String parseMonthToInteger(String _month) {
        switch (_month) {
            case "January":
                _month = "01";
                break;
            case "February":
                _month = "02";
                break;
            case "March":
                _month = "03";
                break;
            case "April":
                _month = "04";
                break;
            case "May":
                _month = "05";
                break;
            case "June":
                _month = "06";
                break;
            case "July":
                _month = "07";
                break;
            case "August":
                _month = "08";
                break;
            case "September":
                _month = "09";
                break;
            case "October":
                _month = "10";
                break;
            case "November":
                _month = "11";
                break;
            case "December":
                _month = "12";
                break;
        }

        return _month;
    }

    private String parseIntegerToMonth(String _month) {
        switch (_month) {
            case "01":
                _month = "January";
                break;
            case "02":
                _month = "February";
                break;
            case "03":
                _month = "March";
                break;
            case "04":
                _month = "April";
                break;
            case "05":
                _month = "May";
                break;

            case "06":
                _month = "June";
                break;
            case "07":
                _month = "July";
                break;
            case "08":
                _month = "August";
                break;
            case "09":
                _month = "September";
                break;
            case "10":
                _month = "October";
                break;
            case "11":
                _month = "November";
                break;
            case "12":
                _month = "December";
                break;
        }

        return _month;
    }

    private void getDefaultReports() {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setTimeout(1500);
        asyncHttpClient.get(data_link + "daily_fetch.php", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {

                    androidListView.invalidateViews();
                    androidListView.invalidate();

                    ArrayList<Consumption> consumptionArrayList = new ArrayList<>();

                    String data = new String(responseBody, "UTF-8");
                    if (data.equals("No history found")) {
                        Toasty.warning(Main7Activity.this, "No history found", Toast.LENGTH_SHORT, false).show();
                    }

                    JSONArray jsonArray = new JSONArray(data);
                    JSONObject jsonObject;

                    for (int ctr = 0; ctr < jsonArray.length(); ctr++) {
                        jsonObject = jsonArray.getJSONObject(ctr);

                        String kWh_Total = jsonObject.getString("kWh_Total");
                        String data_date = jsonObject.getString("data_date");
                        String feedBackData = "";
                        float consumption = Float.parseFloat(kWh_Total);

                        if (consumption <= 3.3) {
                            feedBackData = "Excellent Usage";
                        }
                        if (consumption >= 3.4) {
                            feedBackData = "High Usage";
                        }

                        consumptionArrayList.add(new Consumption(list_logo, kWh_Total, feedBackData, data_date));
                    }

                    consumptionInflater = new ConsumptionInflater(Main7Activity.this, consumptionArrayList);
                    androidListView.setAdapter(consumptionInflater);
                    consumptionInflater.notifyDataSetChanged();


                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void getMonthlyReports(String _month, String _year) {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setTimeout(1500);
        asyncHttpClient.get(data_link + "monthly.php?month=" + parseMonthToInteger(_month) + "&year=" + _year, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {

                    androidListView.invalidateViews();
                    androidListView.invalidate();

                    ArrayList<Consumption> consumptionArrayList = new ArrayList<>();
                    String data = new String(responseBody, "UTF-8");

                    JSONArray jsonArray = new JSONArray(data);
                    JSONObject jsonObject;

                    for (int ctr = 0; ctr < jsonArray.length(); ctr++) {
                        jsonObject = jsonArray.getJSONObject(ctr);

                        String kWh_Total = jsonObject.getString("kWh_Total");
                        String data_date = jsonObject.getString("data_date");

                        if (kWh_Total.equals("null")) {
                            Toasty.warning(Main7Activity.this, "No history found", Toast.LENGTH_SHORT, false).show();
                            getDefaultReports();
                        }

                        String feedBackData = "";
                        float consumption = Float.parseFloat(kWh_Total);

                        if (consumption <= 100) {
                            feedBackData = "Excellent Usage";
                        }
                        if (consumption >= 101) {
                            feedBackData = "High Usage";
                        }

                        String _parseMonth = parseIntegerToMonth(data_date.substring(0, 2));
                        data_date = "As of " + _parseMonth + " " + data_date.substring(2, data_date.length());

                        consumptionArrayList.add(new Consumption(list_logo, kWh_Total, feedBackData, data_date));
                    }

                    consumptionInflater = new ConsumptionInflater(Main7Activity.this, consumptionArrayList);
                    androidListView.setAdapter(consumptionInflater);
                    consumptionInflater.notifyDataSetChanged();


                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void getSpecDayRecord(String _day) {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setTimeout(1500);
        asyncHttpClient.get(data_link + "spec_day.php?day=" + _day, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {

                    androidListView.invalidateViews();
                    androidListView.invalidate();

                    ArrayList<Consumption> consumptionArrayList = new ArrayList<>();
                    String data = new String(responseBody, "UTF-8");

                    if (data.equals("No history found")) {
                        Toasty.warning(Main7Activity.this, "No history found", Toast.LENGTH_SHORT, false).show();
                        getDefaultReports();
                    }

                    JSONArray jsonArray = new JSONArray(data);
                    JSONObject jsonObject;

                    for (int ctr = 0; ctr < jsonArray.length(); ctr++) {
                        jsonObject = jsonArray.getJSONObject(ctr);

                        String kWh_Total = jsonObject.getString("kWh_Total");
                        String data_date = jsonObject.getString("data_date");


                        String feedBackData = "";
                        float consumption = Float.parseFloat(kWh_Total);

                        if (consumption <= 3.3) {
                            feedBackData = "Excellent Usage";
                        }
                        if (consumption >= 3.4) {
                            feedBackData = "High Usage";
                        }

                        String _parseMonth = parseIntegerToMonth(data_date.substring(0, 2));
                        data_date = _parseMonth + data_date.substring(2, data_date.length());

                        consumptionArrayList.add(new Consumption(list_logo, kWh_Total, feedBackData, data_date));
                    }

                    consumptionInflater = new ConsumptionInflater(Main7Activity.this, consumptionArrayList);
                    androidListView.setAdapter(consumptionInflater);
                    consumptionInflater.notifyDataSetChanged();


                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void getMonthlyRecord(String _month) {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setTimeout(1500);
        asyncHttpClient.get(data_link + "monthly_record.php?month=" + parseMonthToInteger(_month), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {

                    androidListView.invalidateViews();
                    androidListView.invalidate();

                    ArrayList<Consumption> consumptionArrayList = new ArrayList<>();
                    String data = new String(responseBody, "UTF-8");

                    if (data.equals("No history found")) {
                        Toasty.warning(Main7Activity.this, "No history found", Toast.LENGTH_SHORT, false).show();
                        getDefaultReports();
                    }

                    JSONArray jsonArray = new JSONArray(data);
                    JSONObject jsonObject;

                    for (int ctr = 0; ctr < jsonArray.length(); ctr++) {
                        jsonObject = jsonArray.getJSONObject(ctr);

                        String kWh_Total = jsonObject.getString("kWh_Total");
                        String data_date = jsonObject.getString("data_date");

                        String feedBackData = "";
                        float consumption = Float.parseFloat(kWh_Total);

                        if (consumption <= 3.3) {
                            feedBackData = "Excellent Usage";
                        }
                        if (consumption >= 3.4) {
                            feedBackData = "High Usage";
                        }

                        String _parseMonth = parseIntegerToMonth(data_date.substring(0, 2));
                        data_date = _parseMonth + data_date.substring(2, data_date.length());

                        consumptionArrayList.add(new Consumption(list_logo, kWh_Total, feedBackData, data_date));
                    }

                    consumptionInflater = new ConsumptionInflater(Main7Activity.this, consumptionArrayList);
                    androidListView.setAdapter(consumptionInflater);
                    consumptionInflater.notifyDataSetChanged();


                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void getYearlyReports(String _year) {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setTimeout(1500);
        asyncHttpClient.get(data_link + "year.php?year=" + _year, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {

                    androidListView.invalidateViews();
                    androidListView.invalidate();

                    ArrayList<Consumption> consumptionArrayList = new ArrayList<>();
                    String data = new String(responseBody, "UTF-8");

                    JSONArray jsonArray = new JSONArray(data);
                    JSONObject jsonObject;

                    for (int ctr = 0; ctr < jsonArray.length(); ctr++) {
                        jsonObject = jsonArray.getJSONObject(ctr);

                        String kWh_Total = jsonObject.getString("kWh_Total");
                        String data_date = jsonObject.getString("data_date");

                        if (kWh_Total.equals("null")) {
                            Toasty.warning(Main7Activity.this, "No history found", Toast.LENGTH_SHORT, false).show();
                            getDefaultReports();
                        }

                        String feedBackData = "";
                        float consumption = Float.parseFloat(kWh_Total);

                        if (consumption <= 1200) {
                            feedBackData = "Excellent Usage";
                        }
                        if (consumption >= 1201){
                            feedBackData = "High Usage";
                        }

                        consumptionArrayList.add(new Consumption(list_logo, kWh_Total, feedBackData, data_date));
                    }

                    consumptionInflater = new ConsumptionInflater(Main7Activity.this, consumptionArrayList);
                    androidListView.setAdapter(consumptionInflater);
                    consumptionInflater.notifyDataSetChanged();


                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }


    private void getReportsBySpecific(String _month, String _day, String _year) {
        if (!_month.isEmpty() && !_day.isEmpty() && !_year.isEmpty()) {
            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
            asyncHttpClient.setTimeout(1500);
            asyncHttpClient.get(data_link + "specific_fetch.php?month=" + parseMonthToInteger(_month) + "&day=" + _day + "&year=" + _year, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        String dataToParse = new String(responseBody, "UTF-8");

                        if (dataToParse.equals("No history found")) {
                            Toasty.warning(Main7Activity.this, "No history found", Toast.LENGTH_SHORT, false).show();
                            getDefaultReports();
                        }

                        JSONObject jsonObject = new JSONObject(dataToParse);
                        String kWh_Total = jsonObject.getString("kWh_Total");
                        String data_date = jsonObject.getString("data_date");
                        String feedBackData = "";
                        float consumption = Float.parseFloat(kWh_Total);

                        if (consumption <= 3.3) {
                            feedBackData = "Excellent Usage";
                        }
                        if (consumption >= 3.4) {
                            feedBackData = "High Usage";
                        }

                        ArrayList<Consumption> consumptionArrayList = new ArrayList<>();
                        consumptionArrayList.add(new Consumption(list_logo, kWh_Total, feedBackData, data_date));
                        consumptionInflater = new ConsumptionInflater(Main7Activity.this, consumptionArrayList);
                        androidListView.setAdapter(consumptionInflater);
                        consumptionInflater.notifyDataSetChanged();

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });
        }
    }

    private void generateHistory() {

        LayoutInflater inflater_query = LayoutInflater.from(this);
        View view_query = inflater_query.inflate(R.layout.search_layout, null);

        spinner_month = (Spinner) view_query.findViewById(R.id.spinner_month);
        spinner_day = (Spinner) view_query.findViewById(R.id.spinner_day);
        spinner_year = (Spinner) view_query.findViewById(R.id.spinner_year);

        checkBox1 = (CheckBox) view_query.findViewById(R.id.set_month);
        checkBox2 = (CheckBox) view_query.findViewById(R.id.set_day);
        checkBox3 = (CheckBox) view_query.findViewById(R.id.set_year);


        ArrayAdapter<CharSequence> charSequenceArrayAdapter = ArrayAdapter.createFromResource(this, R.array.data_month, android.R.layout.simple_spinner_item);
        charSequenceArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_month.setAdapter(charSequenceArrayAdapter);

        charSequenceArrayAdapter = ArrayAdapter.createFromResource(this, R.array.data_day, android.R.layout.simple_spinner_item);
        spinner_day.setAdapter(charSequenceArrayAdapter);

        charSequenceArrayAdapter = ArrayAdapter.createFromResource(this, R.array.data_year, android.R.layout.simple_spinner_item);
        spinner_year.setAdapter(charSequenceArrayAdapter);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(view_query);
        alertDialogBuilder.setCancelable(true);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Search",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                if (!checkBox1.isChecked() && !checkBox2.isChecked() && !checkBox3.isChecked()) {
                                    getDefaultReports();
                                } else if (checkBox1.isChecked() && checkBox2.isChecked() && checkBox3.isChecked()) {
                                    getReportsBySpecific(spinner_month.getSelectedItem().toString(), spinner_day.getSelectedItem().toString(), spinner_year.getSelectedItem().toString());
                                } else if (checkBox1.isChecked() && !checkBox2.isChecked() && checkBox3.isChecked()) {
                                    getMonthlyReports(spinner_month.getSelectedItem().toString(), spinner_year.getSelectedItem().toString());
                                } else if (!checkBox1.isChecked() && checkBox2.isChecked() && !checkBox3.isChecked()) {
                                    getSpecDayRecord(spinner_day.getSelectedItem().toString());
                                } else if (!checkBox1.isChecked() && !checkBox2.isChecked() && checkBox3.isChecked()) {
                                    getYearlyReports(spinner_year.getSelectedItem().toString());
                                } else if (checkBox1.isChecked() && !checkBox2.isChecked() && !checkBox3.isChecked()) {
                                    getMonthlyRecord(spinner_month.getSelectedItem().toString());
                                } else if (checkBox1.isChecked() && checkBox2.isChecked() && !checkBox3.isChecked()) {
                                    Toasty.warning(Main7Activity.this, "Invalid Search", Toast.LENGTH_SHORT, false).show();
                                } else if (!checkBox1.isChecked() && checkBox2.isChecked() && checkBox3.isChecked()) {
                                    Toasty.warning(Main7Activity.this, "Invalid Search", Toast.LENGTH_SHORT, false).show();
                                }
                                dialog.cancel();
                            }
                        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main7);

        getIPVal();
        getDefaultReports();

        androidListView = (ListView) findViewById(R.id.report_holder);
        query_btn = (ImageView) findViewById(R.id.data_query);

        query_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateHistory();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Main7Activity.this, Main3Activity.class));
        finish();
        Bungee.slideRight(Main7Activity.this);
        super.onBackPressed();
    }
}
