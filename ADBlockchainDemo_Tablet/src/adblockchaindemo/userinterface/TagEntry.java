package adblockchaindemo.userinterface;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Dominic Donofrio
 */
public class TagEntry {
    private final SimpleStringProperty epc;
    private final SimpleStringProperty item;
    private final SimpleStringProperty reader;
    private final SimpleStringProperty timeStamp;
    
    TagEntry(String epc, String item, String reader, String timeStamp) {
        this.epc = new SimpleStringProperty(epc);
        this.item = new SimpleStringProperty(item);
        this.reader = new SimpleStringProperty(reader);
        this.timeStamp = new SimpleStringProperty(timeStamp);
    }
    
    public String getEpc() {
        return epc.get();
    }
    
    public void addEpc(String e) {
        epc.set(e);
    }
    
    public String getItem() {
        return item.get();
    }
    
    public void addItem(String i) {
        item.set(i);
    }
    
    public String getReader() {
        return reader.get();
    }
    
    public void addReader(String r) {
        reader.set(r);
    }
    
    public String getTime() {
        return timeStamp.get();
    }
    
    public void addTime(String t) {
        timeStamp.set(t);
    }
    
    public boolean contains(String i) {
        if(getEpc().equals(i) || getItem().equals(i)
                || getReader().equals(i) || getTime().equals(i)) {
            return true;
        }
        else
            return false;
    }
}
