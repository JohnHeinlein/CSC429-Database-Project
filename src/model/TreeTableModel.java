package model;

import java.util.Vector;

public class TreeTableModel{
    private String barcode;
    private String treeType;
    private String notes;
    private String status;
    private String dateStatusUpdated;

    public TreeTableModel(Vector<String> data){
        barcode = data.get(0);
        treeType = data.get(0);
        notes = data.get(0);
        status = data.get(0);
        dateStatusUpdated = data.get(0);
    }
    public String getBarcode(){ return barcode; }
    public void setBarcode(String newVal){barcode = newVal;}

    public String getTreeType(){ return treeType; }
    public void setTreeType(String newVal){treeType = newVal;}

    public String getNotes(){ return notes; }
    public void setNotes(String newVal){notes = newVal;}

    public String getStatus(){ return status; }
    public void setStatus(String newVal){status = newVal;}

    public String getDateStatusUpdated(){ return dateStatusUpdated; }
    public void setDateStatusUpdated(String newVal){dateStatusUpdated = newVal;}

}