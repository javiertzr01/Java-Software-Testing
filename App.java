import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class App {

    static boolean checkFileValid(String fileName){
        File file = new File(fileName);
        // convert the file name into string
        String name = file.toString();

        int index = name.lastIndexOf('.');
        if(index > 0) {
            String extension = name.substring(index + 1);
            if (!extension.equals("csv")){
                return false;
            }
        }
        return file.exists();
    }

    static String getNextLine(BufferedReader reader) {
        try {
            String line = reader.readLine();
            if (line == null) {
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

    static Entry parseRow(String[] headers, String[] data, String file) {
        // Create entry for an instance

        Entry entry = new Entry(headers, data, file);
        return entry;
    }

    static ArrayList<Entry> readFile(String file) {
        try {
            BufferedReader csvReader = new BufferedReader(new FileReader(file));
            // Read headers
            String[] headers = getNextLine(csvReader).split(",");

            String row;

            ArrayList<Entry> parsedFile = new ArrayList<>();

            // Start parsing
            while ((row = getNextLine(csvReader)) != null) {
                String[] data = row.split(",");
                Entry entry = parseRow(headers, data, file);
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

    static String[] getFileHeader(Entry entry) {
        return entry.getHeader();
    }

    static boolean checkSameHeaders(String[] header1, String[] header2) {
        return Arrays.equals(header1, header2);
    }

    static boolean checkUniqueCombiInHeader(String[] header, String[] uniqueCombi) {
        boolean result = true;
        for (String i : uniqueCombi) {
            if (Arrays.asList(header).indexOf(i) == -1) {
                result = false;
                break;
            }
        }
        return result;
    }

    static boolean compareUniqueCombi(Entry entry1, Entry entry2,
            String[] uniqueCombi) {
        if (entry1.equals(entry2)) {
            return true;
        }
        if (entry1.equals(entry2, uniqueCombi)){
            return true;
        }
        return false;
    }

    static ArrayList<String> compareData(Entry entry1, Entry entry2) {
        // if entry 1 and 2 are the same, then return null for no difference
        if (entry1.equals(entry2)) {
            return null;
        }
        ArrayList<String> key_diff = entry1.compare(entry2);
        return key_diff;
    }

    static ArrayList<EntryException> processFiles(ArrayList<Entry> array1,
            ArrayList<Entry> master, String[] uniqueCombi) {
        ArrayList<EntryException> exception = new ArrayList<>();
        for (Entry entry1 : master) {
            boolean present = false;
            for (Entry entry2 : array1) {
                if (compareUniqueCombi(entry1, entry2, uniqueCombi)) {
                    // Check if array1 is missing any data
                    present = true;
                    // Assuming that array1 contains the data in the master list
                    // Check the rest of the columns to find any discreptancies
                    ArrayList<String> key_diff = compareData(entry1, entry2);
                    if (key_diff != null && key_diff.size() != 0) {
                        String errorString = String.join("and", key_diff) + " mismatch";
                        EntryException entryException = new EntryException(entry1, errorString);
                        exception.add(entryException);
                    }
                    break;
                }
            }
            if (present == false) {
                // Finding Entry Exception 
                String errorString = "Not found in " + array1.get(0).getFileName();
                EntryException entryException = new EntryException(entry1, errorString);
                exception.add(entryException);
            }
        }
        return exception;
    }

    public static void main(String[] args) {

        String fileName1 = "sample_file_1.csv";
        String fileName2 = "sample_file_3.csv";
        String uniqueCombi = "Customer ID#, Account No., Currency, Type";

        if (args.length == 3){
            fileName1 = args[0];
            fileName2 = args[1];
            uniqueCombi = args[2];
        }
        else{
            System.out.println("No args input, using default values");
        }

        if (!checkFileValid(fileName1) || !checkFileValid(fileName2)) {
            System.out.println("File does not exist");
            return;
        }

        String[] uniqueCombiArray = uniqueCombi.split(",");

        // Strip leading and trailing spaces
        for (String i : uniqueCombiArray) {
            uniqueCombiArray[Arrays.asList(uniqueCombiArray).indexOf(i)] = i.strip();
        }

        ArrayList<Entry> master = new ArrayList<>();

        // Read File 1
        ArrayList<Entry> parsedFile1 = readFile(fileName1);
        String[] header1 = getFileHeader(parsedFile1.get(0));
        // Read File 2
        ArrayList<Entry> parsedFile2 = readFile(fileName2);
        String[] header2 = getFileHeader(parsedFile2.get(0));


        // check same header
        if (checkSameHeaders(header1, header2)) {
            if (uniqueCombiArray.length != 0 && checkUniqueCombiInHeader(header1, uniqueCombiArray)) {
                try {
                    FileWriter csvWriter = new FileWriter("exceptions.csv");
                    ArrayList<String> remainingHeaders = new ArrayList<>();
                    // Adding headers to an ArrayList that are not in unique combination
                    for (String h : header1) {
                        if (!Arrays.asList(uniqueCombiArray).contains(h)) {
                            remainingHeaders.add(h);
                        }
                    }
                    String header = String.join(",", uniqueCombiArray) + "," + String.join(",", remainingHeaders)
                            + ",Error\n";
                    csvWriter.append(header);

                    // Adding to master list
                    for (Entry entry: parsedFile1){
                        master.add(entry);
                    }
                    for (Entry entry: parsedFile2){
                        master.add(entry);
                    }

                    // 2 cases, missing entry, or wrong entry.
                    ArrayList<EntryException> exceptionsList = processFiles(parsedFile1, master,
                            uniqueCombiArray);
                    ArrayList<EntryException> exceptionsList2 = processFiles(parsedFile2, master,
                            uniqueCombiArray);

                    for (EntryException e : exceptionsList2) {
                        // if (!exceptionsList.contains(e)) {
                            exceptionsList.add(e);
                        // }
                    }

                    for (EntryException h : exceptionsList) {
                        ArrayList<String> exception = new ArrayList<>();
                        for (String key : uniqueCombiArray) {
                            exception.add(h.getDict().get(key));
                        }
                        for (String key : remainingHeaders) {
                            exception.add(h.getDict().get(key));
                        }
                        exception.add(h.getDict().get("Error"));
                        csvWriter.append(String.join(",", exception) + "\n");
                    }
                    csvWriter.flush();
                    csvWriter.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Unique Combination is Invalid.\nAborting...");
            }
        } else {
            System.out.println("Files do not have the same headers.\nAborting...");
        }
    }
}
