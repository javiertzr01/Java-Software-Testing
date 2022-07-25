import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays;

public class App {

    static boolean checkFileExists(String fileName){
        File file = new File(fileName);
        return file.exists();
    }

    static String getNextLine(BufferedReader reader) {
        try {
            String line = reader.readLine();
            if (line == null){
                return null;
            }
            if (line.indexOf("ï»¿") == 0) {
                line = line.replaceAll("ï»¿", "");
            }
            if (line.indexOf("﻿") == 0) {
                line = line.replaceAll("﻿", "");
            }
            return line;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    static HashMap<String, String> parseRow(String[] headers, String[] data){
        // Create hashmap for an instance
        HashMap<String, String> entry = new HashMap<>();
        for (int i = 0; i<headers.length; i++){
            entry.put(headers[i], data[i]);
        }
        return entry;
    }

    static ArrayList<HashMap<String, String>> readFile(String file){
        try {
            BufferedReader csvReader = new BufferedReader(new FileReader(file));
            // Read headers
            String[] headers = getNextLine(csvReader).split(",");
            
            String row;

            ArrayList<HashMap<String, String>> parsedFile = new ArrayList<>();

            // Start parsing
            while ((row = getNextLine(csvReader)) != null) {
                String[] data = row.split(",");
                HashMap<String, String> entry = parseRow(headers, data);
                // Put each entry into arraylist
                parsedFile.add(entry);
                // break;
            }
            csvReader.close();
            return parsedFile;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // In case error
        return null;
    }

    static String[] getFileHeader(HashMap<String, String> entry){
        ArrayList<String> ret = new ArrayList<>();
        for(String i: entry.keySet()){
            ret.add(i);
        }
        return ret.toArray(new String[0]);
    }

    static boolean checkSameHeaders(String[] header1, String[] header2){
        return Arrays.equals(header1, header2);
    }

    static boolean checkUniqueCombiInHeader(String[] header, String[] uniqueCombi){
        boolean result = true;
        for(String i: uniqueCombi){
            if(Arrays.asList(header).indexOf(i) == -1){
                result = false;
                break;
            }
        }
        return result;
    }

    static boolean compareUniqueCombi(HashMap<String, String> entry1, HashMap<String, String> entry2, String[] uniqueCombi){
        if (entry1.equals(entry2)){
            return true;
        }
        boolean check = true;
        for (String key : uniqueCombi) {
            String key1 = entry1.get(key);
            String key2 = entry2.get(key);
            if (key1.equals(key2) == false) {
                check = false;
                break;
            }
        }
        if (check == true) {
            return true;
        }
        return false;
    }

    static ArrayList<String> compareData(HashMap<String, String> entry1, HashMap<String, String> entry2){
        if (entry1.equals(entry2)) {
            return null;
        }
        ArrayList<String> ret = new ArrayList<>();
        for (String key: entry1.keySet()){
            String value1 = entry1.get(key);
            String value2 = entry2.get(key);
            if (value1.equals(value2) == false){
                ret.add(key);
            }
        }
        return ret;
    }

    static ArrayList<HashMap<String, String>> processFiles(ArrayList<HashMap<String, String>> array1, ArrayList<HashMap<String, String>> array2, String[] uniqueCombi){
        ArrayList<HashMap<String, String>> exception = new ArrayList<>();
        for (HashMap<String, String> entry1 : array1) {
            boolean present = false;
            for (HashMap<String, String> entry2 : array2) {
                if(compareUniqueCombi(entry1, entry2, uniqueCombi)){
                    present = true;
                    ArrayList<String> diff = compareData(entry1, entry2);
                    if (diff != null && diff.size() != 0) {
                        String error = String.join("and", diff) + " mismatch";
                        entry1.put("Error", error);
                        exception.add(entry1);
                    }
                    break;
                }
            }
            if (present == false){
                entry1.put("Error", "Not found in other file");
                exception.add(entry1);
            }
        }
        return exception;
    }
    public static void main(String[] args) {

        String fileName1 = "sample_file_1.csv";
        String fileName2 = "sample_file_2.csv";
        String uniqueCombi = "Customer ID#, Account No., Currency, Type";

        if (!checkFileExists(fileName1) || !checkFileExists(fileName2)){
            System.out.println("File does not exist");
            return;
        }


        String[] uniqueCombiArray = uniqueCombi.split(",");

        // Strip leading and trailing spaces
        for (String i: uniqueCombiArray){
            uniqueCombiArray[Arrays.asList(uniqueCombiArray).indexOf(i)] = i.strip();
        }

        // Read File 1
        ArrayList<HashMap<String, String>> parsedFile1 = readFile(fileName1);
        String[] header1 = getFileHeader(parsedFile1.get(0));
        // Read File 2
        ArrayList<HashMap<String, String>> parsedFile2 = readFile(fileName2);
        String[] header2 = getFileHeader(parsedFile2.get(0));

        // check same header
        if (checkSameHeaders(header1, header2)){
            if (uniqueCombiArray.length != 0 && checkUniqueCombiInHeader(header1, uniqueCombiArray)){
                try {
                    FileWriter csvWriter = new FileWriter("exceptions.csv");
                    ArrayList<String> remaindingHeaders = new ArrayList<>();
                    for (String h: header1){
                        if(!Arrays.asList(uniqueCombiArray).contains(h)){
                            remaindingHeaders.add(h);
                        }
                    }
                    String header = String.join(",", uniqueCombiArray) + "," + String.join(",", remaindingHeaders) + ",Error\n";
                    csvWriter.append(header);

                    // 2 cases, missing entry, or wrong entry.
                    ArrayList<HashMap<String, String>> exceptionsList = processFiles(parsedFile1, parsedFile2, uniqueCombiArray);
                    ArrayList<HashMap<String, String>> exceptionsList2 = processFiles(parsedFile2, parsedFile1, uniqueCombiArray);
                    for(HashMap<String,String> e : exceptionsList2){
                        if(!exceptionsList.contains(e)){
                            exceptionsList.add(e);
                        }
                    }

                    for (HashMap<String,String> h: exceptionsList){
                        ArrayList<String> exception = new ArrayList<>();
                        for (String key: uniqueCombiArray){
                            exception.add(h.get(key));
                        }
                        for(String key: remaindingHeaders){
                            exception.add(h.get(key));
                        }
                        exception.add(h.get("Error"));
                        csvWriter.append(String.join(",",exception) + "\n");
                    }
                    csvWriter.flush();
                    csvWriter.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else{
                System.out.println("Unique Combination is Invalid.\nAborting...");
            }
        }
        else{
            System.out.println("Files do not have the same headers.\nAborting...");
        }
    }
}
