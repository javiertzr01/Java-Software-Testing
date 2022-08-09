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
    public void testCheckFileInvalidPathFail() {
        String file = "C:\\invalid";
        assertFalse(App.checkFileValid(file));
    }

    @Test
    public void testCheckFileValidFileInvalidPath() {
        String file = "sample_file_1_test.csv";
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
    public void entryCreation(){
        String[] headers = "Customer ID#,Account No.,Currency,Type,Balance".split(",");
        String[] data = "ID99,BOS8059799,SGD,CURRENT,208045".split(",");
        String file = "C:\\Users\\Javier\\Documents\\GitHub\\Software-Testing-Mini-Campaign\\sample_file_1_test.csv";
        Entry masterEntry = new Entry(headers, data, file);

        HashMap<String, String> dictionary = new HashMap<>();

        for (int i = 0; i < headers.length; i++){
            dictionary.put(headers[i], data[i]);
        }

        assertArrayEquals(headers, masterEntry.getHeader());
        assertEquals(dictionary, masterEntry.getDict());
        assertEquals(file, masterEntry.getFileName());
    }

    @Test
    public void entryAddData(){
        String[] headers = "Customer ID#,Account No.,Currency,Type,Balance".split(",");
        String[] data = "ID99,BOS8059799,SGD,CURRENT,208045".split(",");
        String file = "C:\\Users\\Javier\\Documents\\GitHub\\Software-Testing-Mini-Campaign\\sample_file_1_test.csv";
        Entry masterEntry = new Entry(headers, data, file);

        HashMap<String, String> dictionary = new HashMap<>();

        for (int i = 0; i < headers.length; i++){
            dictionary.put(headers[i], data[i]);
        }

        dictionary.put("Error", "new Error");

        masterEntry.addData("Error", "new Error");

        assertEquals(dictionary, masterEntry.getDict());
    }

    @Test
    public void testParseRow(){
        String[] headers = "Customer ID#,Account No.".split(",");
        String[] data = "ID99,BOS8059799".split(",");
        Entry result = App.parseRow(headers, data, "file");
        Entry expected = new Entry(headers, data, "file");
        assertEquals(expected, result);
    }

    @Test
    public void testReadFile(){
        String[] headers = "Customer ID#,Account No.,Currency,Type,Balance".split(",");
        String[] data = "ID99,BOS8059799,SGD,CURRENT,208045".split(",");
        String file = "C:\\Users\\Javier\\Documents\\GitHub\\Software-Testing-Mini-Campaign\\sample_file_1_test.csv";
        ArrayList<Entry> result = App.readFile(file);
        Entry temp = new Entry(headers, data, file);
        ArrayList<Entry> expected = new ArrayList<>();
        expected.add(temp);
        assertEquals(expected, result);
    }

    @Test
    public void testGetFileHeader(){
        String[] headers = "Customer ID#,Account No.,Currency,Type,Balance".split(",");
        String[] data = "ID99,BOS8059799,SGD,CURRENT,208045".split(",");
        Entry temp = new Entry(headers, data, "file");
        String[] result = App.getFileHeader(temp);
        assertArrayEquals(headers, result);
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
        Entry entry1 = new Entry(headers, data, "file1");
        Entry entry2 = new Entry(headers, data, "file2");
        String[] uniqueCombi = "Customer ID#,Account No.,Currency,Type".split(",");

        assertTrue(App.compareUniqueCombi(entry1, entry2, uniqueCombi));
    }

    @Test
    public void testCompareUniqueCombiFalse() {
        String[] headers = "Customer ID#,Account No.,Currency,Type,Balance".split(",");
        String[] data = "ID99,BOS8059799,SGD,CURRENT,208045".split(",");
        String[] headers2 = "Customer ID#,Account No.,Different,Type,Balance".split(",");
        String[] data2 = "ID99,BOS8059799,SGD,CURRENT,999999".split(",");
        Entry entry1 = new Entry(headers, data, "file1");
        Entry entry2 = new Entry(headers2, data2, "file2");
        String[] uniqueCombi = "Customer ID#,Account No.,Currency,Type".split(",");

        assertFalse(App.compareUniqueCombi(entry1, entry2, uniqueCombi));
    }

    @Test
    public void testCompareDataTrue() {
        String[] headers = "Customer ID#,Account No.,Currency,Type,Balance".split(",");
        String[] data = "ID99,BOS8059799,SGD,CURRENT,208045".split(",");
        Entry entry1 = new Entry(headers, data, "file1");
        Entry entry2 = new Entry(headers, data, "file2");

        assertEquals(null, App.compareData(entry1, entry2));
    }

    @Test
    public void testCompareDataFalse() {
        String[] headers = "Customer ID#,Account No.,Currency,Type,Balance".split(",");
        String[] data = "ID99,BOS8059799,SGD,CURRENT,208045".split(",");
        String[] headers2 = "Customer ID#,Account No.,Currency,Type,Balance".split(",");
        String[] data2 = "ID99,BOS8059799,SGD,CURRENT,999999".split(",");
        Entry entry1 = new Entry(headers, data, "file1");
        Entry entry2 = new Entry(headers2, data2, "file2");
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
        Entry entry1 = new Entry(headers, data, "file1");
        ArrayList<Entry> array1 = new ArrayList<>();
        array1.add(entry1);

        Entry entry2 = new Entry(headers2, data2, "file2");
        ArrayList<Entry> array2 = new ArrayList<>();
        array2.add(entry2);

        String[] uniqueCombi = "Customer ID#,Account No.,Currency,Type".split(",");

        ArrayList<EntryException> result = App.processFiles(array1, array2, uniqueCombi);

        ArrayList<EntryException> expected = new ArrayList<>();
        EntryException expectedEntry = new EntryException(entry2, "Balance mismatch");
        expected.add(expectedEntry);

        assertEquals(expected, result);
    }

    @Test
    public void testProcessFilesSameEntry() {
        String[] headers = "Customer ID#,Account No.,Currency,Type,Balance".split(",");
        String[] data = "ID99,BOS8059799,SGD,CURRENT,208045".split(",");
        String[] headers2 = "Customer ID#,Account No.,Currency,Type,Balance".split(",");
        String[] data2 = "ID99,BOS8059799,SGD,CURRENT,208045".split(",");

        Entry entry1 = new Entry(headers, data, "file1");
        ArrayList<Entry> array1 = new ArrayList<>();
        array1.add(entry1);

        Entry entry2 = new Entry(headers2, data2, "file2");
        ArrayList<Entry> array2 = new ArrayList<>();
        array2.add(entry2);

        String[] uniqueCombi = "Customer ID#,Account No.,Currency,Type".split(",");

        ArrayList<EntryException> result = App.processFiles(array1, array2, uniqueCombi);

        ArrayList<EntryException> expected = new ArrayList<>();
        assertEquals(expected, result);
    }
}
