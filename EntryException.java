public class EntryException extends Entry {
    public EntryException(Entry entry, String exception){
        super(entry);
        addData("Error", exception);
    }
}
