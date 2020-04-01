package model;

import java.util.Vector;

public class TransactionTableModel{
    private String id;
    private String sessionId;
    private String transactionType;
    private String barcode;
    private String transactionAmount;
    private String paymentMethod;
    private String customerName;
    private String customerPhone;
    private String customerEmail;
    private String transactionDate;
    private String transactionTime;
    private String dateStatusUpdated;

    public TransactionTableModel(Vector<String> data){
        id = data.get(0);
        sessionId = data.get(0);
        transactionType = data.get(0);
        barcode = data.get(0);
        transactionAmount = data.get(0);
        paymentMethod = data.get(0);
        customerName = data.get(0);
        customerPhone = data.get(0);
        customerEmail = data.get(0);
        transactionDate = data.get(0);
        transactionTime = data.get(0);
        dateStatusUpdated = data.get(0);
    }
    public String getId(){ return id; }
    public void setId(String newVal){id = newVal;}

    public String getSessionId(){ return sessionId; }
    public void setSessionId(String newVal){sessionId = newVal;}

    public String getTransactionType(){ return transactionType; }
    public void setTransactionType(String newVal){transactionType = newVal;}

    public String getBarcode(){ return barcode; }
    public void setBarcode(String newVal){barcode = newVal;}

    public String getTransactionAmount(){ return transactionAmount; }
    public void setTransactionAmount(String newVal){transactionAmount = newVal;}

    public String getPaymentMethod(){ return paymentMethod; }
    public void setPaymentMethod(String newVal){paymentMethod = newVal;}

    public String getCustomerName(){ return customerName; }
    public void setCustomerName(String newVal){customerName = newVal;}

    public String getCustomerPhone(){ return customerPhone; }
    public void setCustomerPhone(String newVal){customerPhone = newVal;}

    public String getCustomerEmail(){ return customerEmail; }
    public void setCustomerEmail(String newVal){customerEmail = newVal;}

    public String getTransactionDate(){ return transactionDate; }
    public void setTransactionDate(String newVal){transactionDate = newVal;}

    public String getTransactionTime(){ return transactionTime; }
    public void setTransactionTime(String newVal){transactionTime = newVal;}

    public String getDateStatusUpdated(){ return dateStatusUpdated; }
    public void setDateStatusUpdated(String newVal){dateStatusUpdated = newVal;}

}