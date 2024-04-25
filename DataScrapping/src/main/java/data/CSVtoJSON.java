package data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CSVtoJSON {

    public static void main(String[] args) {
        // Specify the input CSV files and corresponding output JSON files
        convertCsvToJson("binance.csv", "binance.json");
        convertCsvToJson("opensea.csv", "opensea.json");
    }

    private static void convertCsvToJson(String csvFilePath, String jsonFilePath) {
        try {
            String csvData = readCsvFile(csvFilePath);
            JSONArray jsonArray = convertCsvToJsonArray(csvData);
            writeJsonFile(jsonArray, jsonFilePath);
            System.out.println("Conversion from " + csvFilePath + " to " + jsonFilePath + " completed successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String readCsvFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }

    private static JSONArray convertCsvToJsonArray(String csvData) {
        String[] lines = csvData.split("\n");
        String[] headers = lines[0].split(",");
        JSONArray jsonArray = new JSONArray();

        for (int i = 1; i < lines.length; i++) {
            String[] values = lines[i].split(",");
            JSONObject jsonObject = new JSONObject();

            for (int j = 0; j < headers.length; j++) {
                jsonObject.put(headers[j], values[j]);
            }

            jsonArray.put(jsonObject);
        }

        return jsonArray;
    }

    private static void writeJsonFile(JSONArray jsonArray, String filePath) throws IOException {
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            fileWriter.write(jsonArray.toString(2)); // The second parameter is the indentation level
        }
    }
}
