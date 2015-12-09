package evacuation.utils.statistics;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

public class StatisticsToFile {

    static String filename = "EvacuationStatistics.csv";
    static String[] header = header();
    static ArrayList<String[]> allLines = new ArrayList<>();

    private static String[] header() {
        return new String[] {
                "incidentType",
                "initNumActive",
                "initNumHerding",
                "initNumConservative",
                "initNumDoors",
                "finalTotalNumHurt",
                "finalNumEscaped",
                "finalNumDead",
                "evacuationTimeSeconds"
        };
    }

    public static boolean fileExists(){

        CSVReader reader;
        try {
            reader = new CSVReader(new FileReader(filename), CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER);
            allLines = (ArrayList<String[]>) reader.readAll();
        } catch (IOException e) {
            System.out.println("The file did not exist.");
            return false;
        }
        try {
            reader.close();
        } catch (IOException e) {
            System.out.println("Error closing file.");
            return false;
        }

        System.out.println("The file exists.");
        return true;
    }

    public static void createFileWithHeader(){

        CSVWriter writer = null;
        try {
            writer = new CSVWriter(new FileWriter(filename), CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER);
        } catch (IOException e) {
            System.out.println("Unable to create file.");
        }
        // feed in your array (or convert your data to an array)

        writer.writeNext(header);
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Unable to create file.");
        }

        System.out.println("File created with success.");
    }

    public static void writeInstanceLine(SimulationInitialState iState, SimulationFinalState fState){
        CSVWriter writer = null;
        try {
            writer = new CSVWriter(new FileWriter(filename), CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER);
        } catch (IOException e) {
            System.out.println("Unable to add statistics.");
        }

        String[] statistics = getStatistics(iState, fState);

        allLines.add(statistics);
        writer.writeAll(allLines);

        try {
            writer.close();
        } catch (IOException e) {
            System.out.println("Unable to add statistics.");
        }

        System.out.println("Statistics added with success.");
    }

    private static String[] getStatistics(SimulationInitialState iState, SimulationFinalState fState) {

        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
        otherSymbols.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("#.000", otherSymbols);

        return new String[] {
                fState.incidentType,
                String.valueOf(iState.numActive),
                String.valueOf(iState.numHerding),
                String.valueOf(iState.numConservative),
                String.valueOf(iState.numDoors),
                String.valueOf(fState.numHurt),
                String.valueOf(fState.numEscaped),
                String.valueOf(fState.numDead),
                String.valueOf(df.format(fState.evacuationTimeSeconds))
        };
    }

}
