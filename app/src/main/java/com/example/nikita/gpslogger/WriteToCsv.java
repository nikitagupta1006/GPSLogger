package com.example.nikita.gpslogger;
import android.content.pm.PackageManager;
import android.os.Environment;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class WriteToCsv {

    ArrayList<String[]> locations;
    String fileName;
    FileWriter writer;

    WriteToCsv(ArrayList<String[]> locations) {
        this.locations = locations;
    }

    private void writeCsvHeader(String h1, String h2, String h3) throws IOException {
        String line = String.format("%s,%s,%s\n", h1,h2,h3);
        writer.write(line);
    }

    private void writeCsvData(double _lat, double _long, double _speed) throws IOException {
        String line = String.format("%f,%f,%f\n", _lat, _long, _speed);
        writer.write(line);
    }

    public void write() throws IOException{

        fileName = "GPSLogger" + new Date().getTime() + ".csv";
        String base_dir = "GPSLogger";
        File f = new File(Environment.getExternalStorageDirectory(), base_dir);
        if (!f.exists()) {
          f.mkdirs();
        }

        File file = new File(f.getAbsolutePath() + File.separator + fileName);
        System.out.println("csv file: " + file.getAbsolutePath());
        writer = new FileWriter(file);
        writeCsvHeader("Latitude", "Longitude", "Speed");
        for(int i = 0; i < locations.size(); i++) {
            writeCsvData(Double.parseDouble(locations.get(i)[0]),
                    Double.parseDouble(locations.get(i)[1]),
                    Double.parseDouble(locations.get(i)[2]));
        }
        writer.flush();
        writer.close();
    }

}
