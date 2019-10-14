package com.example.nikita.gpslogger;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.ref.SoftReference;
import java.util.ArrayList;

public class CSVList extends AppCompatActivity {

    ListView csvList;
    ArrayList<String> filepath = new ArrayList <>();
    FileReader reader;
    BufferedReader bufferedReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_csvlist);
        csvList = (ListView) findViewById(R.id.csvList);
        final ArrayList<String> listItems = new ArrayList <>();

        if(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            File dir = new File(android.os.Environment.getExternalStorageDirectory(), "GPSLogger");
            walkdir(dir);
        }

        for(int index = 0; index < filepath.size(); index++) {
            System.out.println("files in GPSLogger: " + filepath.get(index));
            String[] temp = filepath.get(index).split("/");
            listItems.add(temp[temp.length - 1]);
        }

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems);
        csvList.setAdapter(adapter);
        csvList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView <?> adapterView, View view, int i, long l) {

                File selectedfile = new File(Environment.getExternalStorageDirectory()  +
                                    File.separator + "/GPSLogger",
                                    listItems.get(i));
                System.out.println("View Map path: " + selectedfile.getAbsolutePath());

                try {
                    reader = new FileReader(selectedfile);
                    bufferedReader = new BufferedReader(reader);
                    String output = "";
                    String temp;
                    while ((temp = bufferedReader.readLine()) != null) {
                        output += temp + "\n";
                    }
                    reader.close();
                    // getOutputFileContents(output);

                    Intent intent = new Intent(CSVList.this, MapsActivity.class);
                    intent.putExtra("markerLocations", output);
                    startActivity(intent);
                    //Toast.makeText(CSVList.this, output, Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(CSVList.this, "File not found", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }
        });
    }

    private void getOutputFileContents(String output) {
        System.out.println("The contents of the output file are: ");
        String[] temp = output.split("\n");
        for(int i = 0; i < temp.length; i++) {
            System.out.println(temp[i]);
        }
    }

    private void walkdir(File dir) {
        File listfiles[] = dir.listFiles();

        if(listfiles != null) {
            for(int i = 0; i < listfiles.length; i++) {
                if(listfiles[i].isDirectory()) {
                    walkdir(listfiles[i]);
                } else {
                    // put a check on the extension to be .csv
                    filepath.add(listfiles[i].getAbsolutePath());
                }
            }
        }
    }
}
