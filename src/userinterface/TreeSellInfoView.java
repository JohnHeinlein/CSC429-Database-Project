package userinterface;

import impresario.IModel;
import utilities.Alerts;
import utilities.Debug;

public class TreeSellInfoView extends View{
    public TreeSellInfoView(IModel model) {
        super(model, "TreeSellInfoView");

        addContent("Payment Type",
                makeComboBox("Cash", "Check"));

        addContent("Customer Name",
                makeField("First Name"),
                makeField("Last Name"));

        addContent("Customer Phone",
                makeField("Customer Phone", "phone"));

        addContent("Customer Email",
                makeField("Customer Email","email"));

        submitButton();
        cancelButton();
    }

    protected void submit(){
        scrapeFieldsUnsafe();

        String fname = props.getProperty("firstName");
        String lname = props.getProperty("lastName");
        String phone = props.getProperty("customerPhone");
        String email = props.getProperty("custoemrEmail");

        Debug.logMsg(
                """
                  
                  Payment type: %s
                 Customer name: %s %s
                Customer phone: %s
                Customer email: %s
                """,
                props.getProperty("paymentType"), fname, lname, phone, email
        );

        boolean noName  = fname.equals("") || lname.equals("");
        boolean noPhone = phone.equals("");
        boolean noEmail = email.equals("");

        if(noName && noPhone && noEmail){
            Alerts.errorMessage("Must provide name, phone, or email");
        }else {
            myModel.stateChangeRequest("TreeSellInfoViewSubmit", props);
        }
    }
    @Override
    public void updateState(String key, Object value) {

    }
}
