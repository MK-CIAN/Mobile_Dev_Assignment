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
    // List to store MeteorShowers data
    private List<MeteorShowers> meteorShowers;

    // Layout inflater for inflating the custom layout
    private LayoutInflater inflater;

    // Constructor to initialize the adapter with data and context
    public MeteorShowersAdapter(Context context, List<MeteorShowers> meteorShowers){
        this.meteorShowers = meteorShowers;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        // Return the total number of items in the data set
        return meteorShowers.size();
    }

    @Override
    public Object getItem(int position) {
        // Return the data item at the specified position
        return meteorShowers.get(position);
    }

    @Override
    public long getItemId(int position) {
        // Return the unique identifier for the item at the specified position
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if a recycled view is available, otherwise inflate a new one
        if(convertView == null){
            convertView = inflater.inflate(R.layout.list_item_meteor_shower, parent, false);
        }

        // Get the MeteorShowers object for the current position
        MeteorShowers meteorShower = meteorShowers.get(position);

        // Find and set data to TextViews in the layout
        TextView dateTextView = convertView.findViewById(R.id.textDate);
        TextView eventTextView = convertView.findViewById(R.id.eventName);
        TextView constellationTextView = convertView.findViewById(R.id.constellation);
        TextView meteorPerHourTextView = convertView.findViewById(R.id.meteorPerHour);
        TextView descriptionTextView = convertView.findViewById(R.id.description);

        // Log position and details for debugging
        Log.d("MeteorShowersAdapter", "Position: " + position);
        Log.d("MeteorShowersAdapter", "Item: " + meteorShower.toString());

        // Set data to TextViews
        eventTextView.setText(meteorShower.getEvent());
        dateTextView.setText(meteorShower.getDate());
        constellationTextView.setText(meteorShower.getConstellation());
        meteorPerHourTextView.setText(meteorShower.getMeteorsPerHour());
        descriptionTextView.setText(meteorShower.getDescription());

        // Return the populated view for the current item
        return convertView;
    }
}
