/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package adblockchaindemo.userinterface;

import javafx.collections.ObservableList;

/**
 *
 * @author dom
 */
public class Search {
    public Search() {
        
    }
    
    public int findItem(ObservableList<TagEntry> obv, String reader, String epc) {
        int length = obv.size();
        int result = -1;
        for(int i=0; i<length; i++) {
            if(obv.get(i).getReader().equalsIgnoreCase(reader) && obv.get(i).getEpc().equalsIgnoreCase(epc)) {
                result = i;
            }
        }
        return result;
    }
}
