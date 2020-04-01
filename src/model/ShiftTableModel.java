package model;

import java.util.Vector;

public class ShiftTableModel{
    private String id;
    private String sessionId;
    private String scoutId;
    private String companionName;
    private String startTime;
    private String endTime;
    private String companionHours;

    public ShiftTableModel(Vector<String> data){
        id = data.get(0);
        sessionId = data.get(0);
        scoutId = data.get(0);
        companionName = data.get(0);
        startTime = data.get(0);
        endTime = data.get(0);
        companionHours = data.get(0);
    }
    public String getId(){ return id; }
    public void setId(String newVal){id = newVal;}

    public String getSessionId(){ return sessionId; }
    public void setSessionId(String newVal){sessionId = newVal;}

    public String getScoutId(){ return scoutId; }
    public void setScoutId(String newVal){scoutId = newVal;}

    public String getCompanionName(){ return companionName; }
    public void setCompanionName(String newVal){companionName = newVal;}

    public String getStartTime(){ return startTime; }
    public void setStartTime(String newVal){startTime = newVal;}

    public String getEndTime(){ return endTime; }
    public void setEndTime(String newVal){endTime = newVal;}

    public String getCompanionHours(){ return companionHours; }
    public void setCompanionHours(String newVal){companionHours = newVal;}

}