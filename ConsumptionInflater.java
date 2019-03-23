package com.example.admin.ebreak;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class ConsumptionInflater extends ArrayAdapter<Consumption> {

    private Context mContext;
    private List<Consumption> consumptionList = new ArrayList<>();

    public ConsumptionInflater(@NonNull Context context, @LayoutRes ArrayList<Consumption> list) {
        super(context, 0, list);
        mContext = context;
        consumptionList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.query_layout, parent, false);

        Consumption consumption = consumptionList.get(position);

        ImageView image = (ImageView) listItem.findViewById(R.id.reports_ic);
        image.setImageResource(consumption.getRep_ic());

        TextView text_kwh = (TextView) listItem.findViewById(R.id.total_kwh);
        text_kwh.setText(consumption.getRep_kwh());

        TextView text_feed = (TextView) listItem.findViewById(R.id.feedback_data);
        text_feed.setText(consumption.getRep_feed());

        TextView text_date = (TextView) listItem.findViewById(R.id.date_data);
        text_date.setText(consumption.getRep_date());

        return listItem;
    }
}
