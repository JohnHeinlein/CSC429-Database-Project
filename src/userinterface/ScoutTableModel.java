package userinterface;

import javafx.beans.property.SimpleStringProperty;
import model.Scout;

import java.util.Vector;

//==============================================================================
public class ScoutTableModel {

    private final SimpleStringProperty
            id, lastName, firstName, middleName, dateOfBirth,
            phoneNumber, email, troopId, status, dateStatusUpdated;


    //----------------------------------------------------------------------------
    public ScoutTableModel(Vector<String> scoutData) {
        id = new SimpleStringProperty(scoutData.elementAt(0));
        lastName = new SimpleStringProperty(scoutData.elementAt(1));
        firstName = new SimpleStringProperty(scoutData.elementAt(2));
        middleName = new SimpleStringProperty(scoutData.elementAt(3));
        dateOfBirth = new SimpleStringProperty(scoutData.elementAt(4));
        phoneNumber = new SimpleStringProperty(scoutData.elementAt(5));
        email = new SimpleStringProperty(scoutData.elementAt(6));
        troopId = new SimpleStringProperty(scoutData.elementAt(7));
        status = new SimpleStringProperty(scoutData.elementAt(8));
        dateStatusUpdated = new SimpleStringProperty(scoutData.elementAt(9));;
    }
    public String getId(){ return id.get();}
    public void setId(String id){this.id.set(id);}

    public String getLastName(){ return lastName.get();}
    public void setLastName(String lastName){this.lastName.set(lastName);}

    public String getFirstName(){ return firstName.get();}
    public void setFirstName(String firstName){this.firstName.set(firstName);}

    public String getMiddleName(){ return middleName.get();}
    public void setMiddleName(String middleName){this.middleName.set(middleName);}

    public String getDateOfBirth(){ return dateOfBirth.get();}
    public void setDateOfBirth(String dateOfBirth){this.dateOfBirth.set(dateOfBirth);}

    public String getPhoneNumber(){ return phoneNumber.get();}
    public void setPhoneNumber(String phoneNumber){this.phoneNumber.set(phoneNumber);}

    public String getEmail(){ return email.get();}
    public void setEmail(String email){this.email.set(email);}

    public String getTroopId(){ return troopId.get();}
    public void setTroopId(String troopId){this.troopId.set(troopId);}

    public String getStatus(){ return status.get();}
    public void setStatus(String status){this.status.set(status);}

    public String getDateStatusUpdated(){ return dateStatusUpdated.get();}
    public void setDateStatusUpdated(String dateStatusUpdated){this.dateStatusUpdated.set(dateStatusUpdated);}

}
