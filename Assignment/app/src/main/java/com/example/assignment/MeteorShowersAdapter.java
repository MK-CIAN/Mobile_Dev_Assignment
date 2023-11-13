package com.example.assignment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

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
            convertView = inflater.inflate(R.layout.activity_events_list, parent, false);
        }

        MeteorShowers meteorShower = meteorShowers.get(position);

        //Finds and sets the data to a TextView in the layout
        TextView dateTextView = convertView.findViewById(R.id.textDate);
        TextView eventTextView = convertView.findViewById(R.id.eventName);
        TextView constellationTextView = convertView.findViewById(R.id.constellation);
        TextView meteorPerHourTextView = convertView.findViewById(R.id.meteorPerHour);
        TextView descriptionTextView = convertView.findViewById(R.id.description);

        dateTextView.setText(meteorShower.getDate());
        eventTextView.setText(meteorShower.getEvent());
        constellationTextView.setText(meteorShower.getConstellation());
        meteorPerHourTextView.setText(meteorShower.getMeteorsPerHour());
        descriptionTextView.setText(meteorShower.getDescription());

        return convertView;
    }
}
