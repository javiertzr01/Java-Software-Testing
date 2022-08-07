import java.util.ArrayList;
import java.util.HashMap;

public class Entry {
    private String[] headers;
    private HashMap<String, String> dict = new HashMap<>();
    private String fileName;

    public Entry(){
        headers = null;
    }

    public Entry(String[] headers, String[] data, String fileName){
        this.headers = headers;
        this.fileName = fileName;
        setData(data);
    }

    public Entry(Entry entry){
        this.headers = entry.getHeader();
        this.dict = entry.getDict();
        this.fileName = entry.getFileName();
    }

    public void setData(String[] data){
        for (int i = 0; i < headers.length; i++) {
            dict.put(headers[i], data[i]);
        }
    }

    public void addData(String key, String value){
        dict.put(key, value);
    }

    public HashMap<String, String> getDict(){
        return this.dict;
    }

    public String[] getHeader(){
        return this.headers;
    }

    public String getFileName(){
        return this.fileName;
    }

    public boolean equals(Entry entry){
        HashMap<String, String> entryDict = entry.getDict();
        try {
            for (String i: headers){
                if(dict.get(i).equals(entryDict.get(i)) == false){
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }

    public boolean equals(Entry entry, String[] uniqueCombi){
        HashMap<String, String> entryDict = entry.getDict();
        for (String key: uniqueCombi){
            if(this.dict.get(key).equals(entryDict.get(key)) == false){
                return false;
            }
        }
        return true;
    }

    public ArrayList<String> compare(Entry entry){
        ArrayList<String> ret = new ArrayList<>();
        HashMap<String, String> entryDict = entry.getDict();
        for (String key: dict.keySet()){
            if(this.dict.get(key).equals(entryDict.get(key)) == false){
                ret.add(key);
            }
        }
        return ret;
    }
}
