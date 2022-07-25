import static org.junit.Assert.*;
import java.io.*;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

public class AppTest {
    @Before
    public void runBeforeTests(){
        
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
        // This fails for idk what reason
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
}
