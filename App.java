import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class App {
    public static void main(String[] args) {

        String fileName = "sample_file_1.csv";
        String fileName2 = "sample_file_3.csv";
        String row = "";
        HashMap<String, bankAcc> f1Accounts = new HashMap<>();
        HashMap<String, bankAcc> f2Accounts = new HashMap<>();

        // Read File 1
        try {
            BufferedReader csvReader = new BufferedReader(new FileReader(fileName));
            // Skip headers
            row = csvReader.readLine();

            // Start parsing
            while ((row = csvReader.readLine()) != null) {
                if (row.indexOf("ï»¿") == 0) {
                    row = row.replaceAll("ï»¿", "");
                }
                String[] data = row.split(",");
                // Create object for each instance, and put in map
                f1Accounts.put(data[1], new bankAcc(data[0], data[1], data[2], data[3], Integer.parseInt(data[4])));
                // break;
            }
            csvReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Read File 2
        try {
            BufferedReader csvReader = new BufferedReader(new FileReader(fileName2));
            // Skip headers
            row = csvReader.readLine();

            // Start parsing
            while ((row = csvReader.readLine()) != null) {
                if (row.indexOf("ï»¿") == 0) {
                    row = row.replaceAll("ï»¿", "");
                }
                String[] data = row.split(",");
                // Create object for each instance, and put in map
                f2Accounts.put(data[1], new bankAcc(data[0], data[1], data[2], data[3], Integer.parseInt(data[4])));
                // break;
            }
            csvReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<String> exceptionsList = new ArrayList<>();

        try {
            FileWriter csvWriter = new FileWriter("exceptions.csv");
            csvWriter.append("File 1 Customer ID#");
            csvWriter.append(",");
            csvWriter.append("File 1 Account No.");
            csvWriter.append(",");
            csvWriter.append("File 1 Currency");
            csvWriter.append(",");
            csvWriter.append("File 1 Type");
            csvWriter.append(",");
            csvWriter.append("File 1 Balance");
            csvWriter.append(",");
            csvWriter.append("File 2 Customer ID#");
            csvWriter.append(",");
            csvWriter.append("File 2 Account No.");
            csvWriter.append(",");
            csvWriter.append("File 2 Currency");
            csvWriter.append(",");
            csvWriter.append("File 2 Type");
            csvWriter.append(",");
            csvWriter.append("File 2 Balance");
            csvWriter.append("\n");

            // Loop through file 1
            for (String i : f1Accounts.keySet()) {
                if (f2Accounts.containsKey(i)) {
                    // Compare
                    if (f2Accounts.get(i).equals(f1Accounts.get(i)) == false) {
                        // Add to exceptions
                        exceptionsList.add(i);
                        // Write to File
                        csvWriter.append(String.join(",", f1Accounts.get(i).asList()));
                        csvWriter.append(",");
                        csvWriter.append(String.join(",", f2Accounts.get(i).asList()));
                        csvWriter.append("\n");
                    }
                } else {
                    // Add to exceptions list
                    exceptionsList.add(i);
                    // Put null for File 2
                    String[] nullEntry = { "null", "null", "null", "null", "null" };
                    // Write to File
                    csvWriter.append(String.join(",", f1Accounts.get(i).asList()));
                    csvWriter.append(",");
                    csvWriter.append(String.join(",", nullEntry));
                    csvWriter.append("\n");
                }
            }

            // Loop through file 2
            for (String i : f2Accounts.keySet()) {
                if (f1Accounts.containsKey(i)) {
                    // Compare
                    if (exceptionsList.contains(i) == false) {
                        if (f2Accounts.get(i).equals(f1Accounts.get(i)) == false) {
                            // Add to exceptions
                            exceptionsList.add(i);
                            // Write to File
                            csvWriter.append(String.join(",", f1Accounts.get(i).asList()));
                            csvWriter.append(",");
                            csvWriter.append(String.join(",", f2Accounts.get(i).asList()));
                            csvWriter.append("\n");
                        }
                    }
                } else {
                    // Add to exceptions list
                    exceptionsList.add(i);
                    // Put null for File 2
                    String[] nullEntry = { "null", "null", "null", "null", "null" };
                    // Write to File
                    csvWriter.append(String.join(",", nullEntry));
                    csvWriter.append(",");
                    csvWriter.append(String.join(",", f2Accounts.get(i).asList()));
                    csvWriter.append("\n");
                }
            }

            csvWriter.flush();
            csvWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

// // Read "Unique Combination"
// try {
// BufferedReader csvReader = new BufferedReader(new FileReader(fileName2));
// // Skip headers
// row = csvReader.readLine();

// // Start parsing
// while ((row = csvReader.readLine()) != null) {
// if (row.indexOf("ï»¿") == 0) {
// row = row.replaceAll("ï»¿", "");
// }
// String[] data = row.split(",");
// // Create object for each instance, and put in map
// bankAcc tempAccount = new bankAcc(data[0], data[1], data[2], data[3],
// Integer.parseInt(data[4]));
// if (accounts.containsKey(data[1])){
// if (accounts.get(data[1]).equals(tempAccount) == false){
// System.out.println(data[1]);
// }
// }
// else{
// // Add data to exceptions
// }
// }
// csvReader.close();
// } catch (FileNotFoundException e) {
// e.printStackTrace();
// } catch (IOException e) {
// e.printStackTrace();
// }
// }
// }
