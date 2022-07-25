import static org.junit.Assert.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;

public class AppTest {
    @Test
    public void testCheckFileValidPass(){
        String file = "C:\\Users\\Javier\\Documents\\GitHub\\Software-Testing-Mini-Campaign\\sample_file_1_test.csv";
        assertTrue(App.checkFileValid(file));
    }

    @Test
    public void testCheckFileValidFail() {
        String file = "12345";
        assertFalse(App.checkFileValid(file));
    }

    @Test
    public void testCheckFileInvalidFileExtension() {
        String file = "C:\\Users\\Javier\\Documents\\GitHub\\Software-Testing-Mini-Campaign\\sample_file_1_test.xlsx";
        assertFalse(App.checkFileValid(file));
    }

    @Test
    public void testGetNextLine(){
        String file = "C:\\Users\\Javier\\Documents\\GitHub\\Software-Testing-Mini-Campaign\\sample_file_1_test.csv";
        String result = "hello";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            result = App.getNextLine(reader);
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // ﻿ the diamond is a result of parsing from file or something ﻿
        assertEquals("Customer ID#,Account No.,Currency,Type,Balance", result);
    }

    @Test
    public void testParseRow(){
        String[] headers = "Customer ID#,Account No.".split(",");
        String[] data = "ID99,BOS8059799".split(",");
        HashMap<String, String> result = App.parseRow(headers, data);
        HashMap<String, String> expected = new HashMap<>();
        expected.put("Customer ID#", "ID99");
        expected.put("Account No.", "BOS8059799");
        assertEquals(expected, result);
    }

    @Test
    public void testReadFile(){
        String[] headers = "Customer ID#,Account No.,Currency,Type,Balance".split(",");
        String[] data = "ID99,BOS8059799,SGD,CURRENT,208045".split(",");
        String file = "C:\\Users\\Javier\\Documents\\GitHub\\Software-Testing-Mini-Campaign\\sample_file_1_test.csv";
        ArrayList<HashMap<String, String>> result = App.readFile(file);
        HashMap<String, String> temp = new HashMap<>();
        for(int i = 0; i<headers.length;i++){
            temp.put(headers[i], data[i]);
        }
        ArrayList<HashMap<String, String>> expected = new ArrayList<>();
        expected.add(temp);
        assertEquals(expected, result);
    }

    @Test
    public void testGetFileHeader(){
        String[] headers = "Customer ID#,Account No.,Currency,Type,Balance".split(",");
        String[] data = "ID99,BOS8059799,SGD,CURRENT,208045".split(",");
        HashMap<String, String> temp = new HashMap<>();
        for (int i = 0; i < headers.length; i++) {
            temp.put(headers[i], data[i]);
        }
        ArrayList<String> ret = new ArrayList<>();
        for (String i: temp.keySet()){
            ret.add(i);
        }
        String[] result = App.getFileHeader(temp);
        assertArrayEquals(ret.toArray(new String[0]), result);
    }

    @Test
    public void testCheckUniqueCombiInHeaderTrue(){
        String[] header = "Customer ID#,Account No.,Currency,Type".split(",");
        String[] unique = "Customer ID#,Account No.,Currency,Type".split(",");
        assertTrue(App.checkUniqueCombiInHeader(header, unique));
    }
    @Test
    public void testCheckUniqueCombiInHeaderFalse(){
        String[] header = "Customer ID#,Account No.,Currency,Type".split(",");
        String[] unique = "Customer ID#, Account No., Currency,Type".split(",");
        assertFalse(App.checkUniqueCombiInHeader(header, unique));
    }

    @Test
    public void testCompareUniqueCombiTrue(){
        String[] headers = "Customer ID#,Account No.,Currency,Type,Balance".split(",");
        String[] data = "ID99,BOS8059799,SGD,CURRENT,208045".split(",");
        HashMap<String, String> entry1 = new HashMap<>();
        for (int i = 0; i < headers.length; i++) {
            entry1.put(headers[i], data[i]);
        }
        HashMap<String, String> entry2 = new HashMap<>();
        for (int i = 0; i < headers.length; i++) {
            entry2.put(headers[i], data[i]);
        }
        String[] uniqueCombi = "Customer ID#,Account No.,Currency,Type".split(",");

        assertTrue(App.compareUniqueCombi(entry1, entry2, uniqueCombi));
    }

    @Test
    public void testCompareUniqueCombiFalse() {
        String[] headers = "Customer ID#,Account No.,Currency,Type,Balance".split(",");
        String[] data = "ID99,BOS8059799,SGD,CURRENT,208045".split(",");
        String[] headers2 = "Customer ID#,Account No.,Different,Type,Balance".split(",");
        String[] data2 = "ID99,BOS8059799,SGD,CURRENT,999999".split(",");
        HashMap<String, String> entry1 = new HashMap<>();
        for (int i = 0; i < headers.length; i++) {
            entry1.put(headers[i], data[i]);
        }
        HashMap<String, String> entry2 = new HashMap<>();
        for (int i = 0; i < headers2.length; i++) {
            entry2.put(headers2[i], data2[i]);
        }
        String[] uniqueCombi = "Customer ID#,Account No.,Currency,Type".split(",");

        assertFalse(App.compareUniqueCombi(entry1, entry2, uniqueCombi));
    }

    @Test
    public void testCompareDataTrue() {
        String[] headers = "Customer ID#,Account No.,Currency,Type,Balance".split(",");
        String[] data = "ID99,BOS8059799,SGD,CURRENT,208045".split(",");
        HashMap<String, String> entry1 = new HashMap<>();
        for (int i = 0; i < headers.length; i++) {
            entry1.put(headers[i], data[i]);
        }
        HashMap<String, String> entry2 = new HashMap<>();
        for (int i = 0; i < headers.length; i++) {
            entry2.put(headers[i], data[i]);
        }

        assertEquals(null, App.compareData(entry1, entry2));
    }

    @Test
    public void testCompareDataFalse() {
        String[] headers = "Customer ID#,Account No.,Currency,Type,Balance".split(",");
        String[] data = "ID99,BOS8059799,SGD,CURRENT,208045".split(",");
        String[] headers2 = "Customer ID#,Account No.,Currency,Type,Balance".split(",");
        String[] data2 = "ID99,BOS8059799,SGD,CURRENT,999999".split(",");
        HashMap<String, String> entry1 = new HashMap<>();
        for (int i = 0; i < headers.length; i++) {
            entry1.put(headers[i], data[i]);
        }
        HashMap<String, String> entry2 = new HashMap<>();
        for (int i = 0; i < headers2.length; i++) {
            entry2.put(headers2[i], data2[i]);
        }
        ArrayList<String> expected = new ArrayList<>();
        expected.add("Balance");
        assertEquals(expected, App.compareData(entry1, entry2));
    }

    @Test
    public void testProcessFilesDifferentEntry(){
        String[] headers = "Customer ID#,Account No.,Currency,Type,Balance".split(",");
        String[] data = "ID99,BOS8059799,SGD,CURRENT,208045".split(",");
        String[] headers2 = "Customer ID#,Account No.,Currency,Type,Balance".split(",");
        String[] data2 = "ID99,BOS8059799,SGD,CURRENT,999999".split(",");
        HashMap<String, String> temp = new HashMap<>();
        for(int i = 0; i<headers.length;i++){
            temp.put(headers[i], data[i]);
        }
        ArrayList<HashMap<String, String>> array1 = new ArrayList<>();
        array1.add(temp);

        HashMap<String, String> temp2 = new HashMap<>();
        for(int i = 0; i<headers.length;i++){
            temp2.put(headers2[i], data2[i]);
        }
        ArrayList<HashMap<String, String>> array2 = new ArrayList<>();
        array2.add(temp2);

        String[] uniqueCombi = "Customer ID#,Account No.,Currency,Type".split(",");

        ArrayList<HashMap<String, String>> result = App.processFiles(array1, array2, uniqueCombi);

        ArrayList<HashMap<String, String>> expected = new ArrayList<>();
        expected.add(temp);
        assertEquals(expected, result);
    }

    @Test
    public void testProcessFilesSameEntry() {
        String[] headers = "Customer ID#,Account No.,Currency,Type,Balance".split(",");
        String[] data = "ID99,BOS8059799,SGD,CURRENT,208045".split(",");
        String[] headers2 = "Customer ID#,Account No.,Currency,Type,Balance".split(",");
        String[] data2 = "ID99,BOS8059799,SGD,CURRENT,208045".split(",");
        HashMap<String, String> temp = new HashMap<>();
        for (int i = 0; i < headers.length; i++) {
            temp.put(headers[i], data[i]);
        }
        ArrayList<HashMap<String, String>> array1 = new ArrayList<>();
        array1.add(temp);

        HashMap<String, String> temp2 = new HashMap<>();
        for (int i = 0; i < headers.length; i++) {
            temp2.put(headers2[i], data2[i]);
        }
        ArrayList<HashMap<String, String>> array2 = new ArrayList<>();
        array2.add(temp2);

        String[] uniqueCombi = "Customer ID#,Account No.,Currency,Type".split(",");

        ArrayList<HashMap<String, String>> result = App.processFiles(array1, array2, uniqueCombi);

        ArrayList<HashMap<String, String>> expected = new ArrayList<>();
        assertEquals(expected, result);
    }
}
