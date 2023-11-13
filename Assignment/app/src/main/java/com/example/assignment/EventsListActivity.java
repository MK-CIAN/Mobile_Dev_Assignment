package com.example.assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EventsListActivity extends AppCompatActivity {
    private List<MeteorShowers> meteorShowers = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_list);

        readCsvFile();
        setupListView();
    }

    private void readCsvFile(){
        try {
            CSVReader csvReader = new CSVReader(new FileReader("res/raw/meteorshowertest.csv"));
            String[] nextRecord;
            while((nextRecord = csvReader.readNext()) != null) {
                MeteorShowers meteorShower = new MeteorShowers(
                        nextRecord[0],
                        nextRecord[1],
                        nextRecord[2],
                        nextRecord[3],
                        nextRecord[4]
                );
                meteorShowers.add(meteorShower);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
    }

    private void setupListView() {
        ListView listView = findViewById(R.id.listView);
        MeteorShowersAdapter adapter = new MeteorShowersAdapter(this, meteorShowers);
        listView.setAdapter(adapter);
    }
}