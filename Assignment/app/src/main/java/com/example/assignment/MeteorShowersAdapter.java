package com.example.assignment;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class MeteorShowersAdapter extends BaseAdapter {
    private List<MeteorShowers> meteorShowers;
    private LayoutInflater inflater;

    public MeteorShowersAdapter(Context context, List<MeteorShowers> meteorShowers){
        this.meteorShowers = meteorShowers;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return meteorShowers.size();
    }

    @Override
    public Object getItem(int position) {
        return meteorShowers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = inflater.inflate(R.layout.list_item_meteor_shower, parent, false);
        }

        MeteorShowers meteorShower = meteorShowers.get(position);


        //Finds and sets the data to a TextView in the layout
        TextView dateTextView = convertView.findViewById(R.id.textDate);
        TextView eventTextView = convertView.findViewById(R.id.eventName);
        TextView constellationTextView = convertView.findViewById(R.id.constellation);
        TextView meteorPerHourTextView = convertView.findViewById(R.id.meteorPerHour);
        TextView descriptionTextView = convertView.findViewById(R.id.description);

        Log.d("MeteorShowersAdapter", "Position: " + position);
        Log.d("MeteorShowersAdapter", "Item: " + meteorShower.toString());
        eventTextView.setText(meteorShower.getEvent());
        dateTextView.setText(meteorShower.getDate());
        constellationTextView.setText(meteorShower.getConstellation());
        meteorPerHourTextView.setText(meteorShower.getMeteorsPerHour());
        descriptionTextView.setText(meteorShower.getDescription());

        return convertView;
    }
}
