package model;

import java.util.Vector;

public class SessionTableModel{
    private String id;
    private String startDate;
    private String startTime;
    private String endTime;
    private String startingCash;
    private String endingCash;
    private String totalCheckTransactionsAmount;
    private String notes;

    public SessionTableModel(Vector<String> data){
        id = data.get(0);
        startDate = data.get(0);
        startTime = data.get(0);
        endTime = data.get(0);
        startingCash = data.get(0);
        endingCash = data.get(0);
        totalCheckTransactionsAmount = data.get(0);
        notes = data.get(0);
    }
    public String getId(){ return id; }
    public void setId(String newVal){id = newVal;}

    public String getStartDate(){ return startDate; }
    public void setStartDate(String newVal){startDate = newVal;}

    public String getStartTime(){ return startTime; }
    public void setStartTime(String newVal){startTime = newVal;}

    public String getEndTime(){ return endTime; }
    public void setEndTime(String newVal){endTime = newVal;}

    public String getStartingCash(){ return startingCash; }
    public void setStartingCash(String newVal){startingCash = newVal;}

    public String getEndingCash(){ return endingCash; }
    public void setEndingCash(String newVal){endingCash = newVal;}

    public String getTotalCheckTransactionsAmount(){ return totalCheckTransactionsAmount; }
    public void setTotalCheckTransactionsAmount(String newVal){totalCheckTransactionsAmount = newVal;}

    public String getNotes(){ return notes; }
    public void setNotes(String newVal){notes = newVal;}

}