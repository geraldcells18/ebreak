package com.example.admin.ebreak;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONObject;

import java.util.Calendar;

import cz.msebera.android.httpclient.Header;
import es.dmoral.toasty.Toasty;
import spencerstudios.com.bungeelib.Bungee;

public class Main2Activity extends AppCompatActivity {

    String IP = "";
    String updated_kWh_Link = "";
    String updated_amps_link = "";

    FloatingActionButton floatingActionButton;

    TextView textView1, textView2, textView3, textView4, textView5;

    Button btnFeedBack;

    LayoutInflater inflater;
    View view;

    EditText editText;

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            getKiloWattHourData(updated_kWh_Link);
            getAmperesData(updated_amps_link);
            handler.postDelayed(this, 1500);
        }
    };

    void getIPVal() {
        Calendar calendar = Calendar.getInstance();
        IP = getResources().getString(R.string.ip_add);
        updated_kWh_Link = IP + "/main/last_kwh.php?month=" + String.format("%02d", calendar.get(Calendar.MONTH) + 1) + "&year=" + String.valueOf(calendar.get(Calendar.YEAR));
        updated_amps_link = IP + "/main/last_amps.php";
    }

    private void getKiloWattHourData(String link) {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setTimeout(1500);

        asyncHttpClient.get(link, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String json_data = new String(responseBody, "UTF-8");
                    JSONObject jsonObject = new JSONObject(json_data);
                    textView1.setText(jsonObject.get("kWh_Total").toString());
                    textView2.setText(jsonObject.get("kWh1").toString());
                    textView3.setText(jsonObject.get("kWh2").toString());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void getAmperesData(String link) {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setTimeout(1500);

        asyncHttpClient.get(link, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String json_data = new String(responseBody, "UTF-8");
                    JSONObject jsonObject = new JSONObject(json_data);

                    textView4.setText(jsonObject.get("relay_amps1").toString());
                    textView5.setText(jsonObject.get("relay_amps2").toString());

                    double _amps1 = Math.round(Double.parseDouble(textView4.getText().toString()));
                    double _amps2 = Double.parseDouble(textView5.getText().toString());

                    if (_amps1 >= 6.0) {
                        displayNotification("Lightning outlet ampere reach", 1);

                        //LIGHTNING SWITCH OFF
                    }
                    if (_amps2 >= 6.0) {
                        displayNotification("Wall outlet ampere reach", 2);

                        // WALL SWITCH OFF
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }


    private void displayNotification(String mess, int _analyzer) {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.main_logo);
        mBuilder.setContentTitle(getResources().getString(R.string.app_name));
        mBuilder.setContentText(mess);

        Intent resultIntent = new Intent(this, Main2Activity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(Main2Activity.class);

        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(_analyzer, mBuilder.build());
    }

    private void displayFeedBack(int layout_num) {

        inflater = LayoutInflater.from(this);

        if (layout_num == 1) {
            view = inflater.inflate(R.layout.feed_back1, null);
        } else {
            view = inflater.inflate(R.layout.feed_back2, null);
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.setCancelable(true);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("DISMISS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        getIPVal();

        textView1 = (TextView) findViewById(R.id.kwh);
        textView2 = (TextView) findViewById(R.id.kwh1);
        textView3 = (TextView) findViewById(R.id.kwh2);
        textView4 = (TextView) findViewById(R.id.amps1);
        textView5 = (TextView) findViewById(R.id.amps2);

        btnFeedBack = (Button) findViewById(R.id.btn_feedback);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                inflater = LayoutInflater.from(Main2Activity.this);
                view = inflater.inflate(R.layout.updater, null);

                editText = (EditText) view.findViewById(R.id.textBox);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Main2Activity.this);
                alertDialogBuilder.setView(view);
                alertDialogBuilder.setCancelable(true);

                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("ADD",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                                        asyncHttpClient.setTimeout(1500);

                                        asyncHttpClient.get(IP + "/main/add_kwh.php?new_kwh=" + editText.getText().toString(), new AsyncHttpResponseHandler() {
                                            @Override
                                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                                try {
                                                    String response = new String(responseBody, "UTF-8");
                                                    Toasty.warning(Main2Activity.this, response, Toast.LENGTH_SHORT, false).show();
                                                } catch (Exception ex) {
                                                    ex.printStackTrace();
                                                }
                                            }

                                            @Override
                                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                                Toasty.error(Main2Activity.this, "Failed to add.", Toast.LENGTH_SHORT, false).show();
                                            }
                                        });

                                        dialog.cancel();
                                    }
                                });

                alertDialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        btnFeedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double kWhConsumption = Math.round(Double.parseDouble(textView1.getText().toString()));
                if (kWhConsumption >= 0 && kWhConsumption <= 100) {
                    displayFeedBack(1);
                } else if (kWhConsumption >= 101 && kWhConsumption <= 500) {
                    displayFeedBack(2);
                }
            }
        });

        getKiloWattHourData(updated_kWh_Link);
        getAmperesData(updated_amps_link);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(Main2Activity.this, Main3Activity.class));
        finish();
        Bungee.slideRight(Main2Activity.this);
        super.onBackPressed();
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }

    @Override
    public void onResume() {
        handler.postDelayed(runnable, 1500);
        super.onResume();
    }

    @Override
    public void onStart() {
        handler.postDelayed(runnable, 1500);
        super.onStart();
    }
}
