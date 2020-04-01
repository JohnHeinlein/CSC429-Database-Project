package model;

import java.util.Vector;

public class TreeTypeTableModel{
    private String id;
    private String typeDescription;
    private String cost;
    private String barcodePrefix;

    public TreeTypeTableModel(Vector<String> data){
        id = data.get(0);
        typeDescription = data.get(1);
        cost = data.get(2);
        barcodePrefix = data.get(3);
    }
    public String getId(){ return id; }
    public void setId(String newVal){id = newVal;}

    public String getTypeDescription(){ return typeDescription; }
    public void setTypeDescription(String newVal){typeDescription = newVal;}

    public String getCost(){ return cost; }
    public void setCost(String newVal){cost = newVal;}

    public String getBarcodePrefix(){ return barcodePrefix; }
    public void setBarcodePrefix(String newVal){barcodePrefix = newVal;}

}