package com.example.admin.ebreak;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;
import spencerstudios.com.bungeelib.Bungee;

public class Main3Activity extends AppCompatActivity {

    long backPress = 0;
    CardView cardView_switch, cardView_consumption, cardView_bill, cardView_tipid, cardView_history;

    private void setCardViewClickListener(CardView cardViewClickListener) {
        cardViewClickListener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int cardview_id = v.getId();
                switch (cardview_id) {
                    case R.id.consumption_module:
                        startActivity(new Intent(Main3Activity.this, Main2Activity.class));
                        finish();
                        Bungee.slideLeft(Main3Activity.this);
                        break;
                    case R.id.switch_module:
                        startActivity(new Intent(Main3Activity.this, Main4Activity.class));
                        finish();
                        Bungee.slideLeft(Main3Activity.this);
                        break;
                    case R.id.calculator_module:
                        startActivity(new Intent(Main3Activity.this, Main5Activity.class));
                        finish();
                        Bungee.slideLeft(Main3Activity.this);
                        break;
                    case R.id.tips_module:
                        startActivity(new Intent(Main3Activity.this, Main6Activity.class));
                        finish();
                        Bungee.slideLeft(Main3Activity.this);
                        break;
                    case R.id.reports_module:
                        startActivity(new Intent(Main3Activity.this, Main7Activity.class));
                        finish();
                        Bungee.slideLeft(Main3Activity.this);
                        break;
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        cardView_consumption = (CardView) findViewById(R.id.consumption_module);
        cardView_switch = (CardView) findViewById(R.id.switch_module);
        cardView_bill = (CardView) findViewById(R.id.calculator_module);
        cardView_tipid = (CardView) findViewById(R.id.tips_module);
        cardView_history = (CardView) findViewById(R.id.reports_module);

        setCardViewClickListener(cardView_consumption);
        setCardViewClickListener(cardView_switch);
        setCardViewClickListener(cardView_bill);
        setCardViewClickListener(cardView_tipid);
        setCardViewClickListener(cardView_history);
    }

    @Override
    public void onBackPressed() {
        if (backPress + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
        } else {
            Toasty.info(Main3Activity.this, "Press back again to close", Toast.LENGTH_SHORT, true).show();
        }
        backPress = System.currentTimeMillis();
    }
}
