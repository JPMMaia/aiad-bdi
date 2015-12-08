package evacuation.utils;

import com.opencsv.CSVWriter;
import java.io.FileWriter;
import java.io.IOException;

public class StatisticsFile {

    static String filename = "EvacuationStatistics.csv";
    static String[] header = generateHeader();

    private static String[] generateHeader() {
        return new String[] {
          "hello"
        };
    }

    public static boolean fileExists(){
        return false;
    }

    public static boolean createFileWithHeader(){

        CSVWriter writer = null;
        try {
            writer = new CSVWriter(new FileWriter(filename), ',');
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        // feed in your array (or convert your data to an array)

        writer.writeNext(header);
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        System.out.println("File created with success.");
        return true;
    }

    public static boolean writeInstanceLine(){
        return false;
    }

}
