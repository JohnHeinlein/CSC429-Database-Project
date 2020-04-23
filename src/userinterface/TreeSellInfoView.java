package userinterface;

import impresario.IModel;
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
                makeField("Customer Phone"));

        addContent("Customer Email",
                makeField("Customer Email"));

        submitButton();
        cancelButton();
    }

    public void submit(){
        scrapeFieldsUnsafe();
        Debug.logMsg(
                """
                  
                  Payment type: %s
                 Customer name: %s %s
                Customer phone: %s
                Customer email: %s
                """,
                props.getProperty("paymentType"),
                props.getProperty("firstName"), props.getProperty("lastName"),
                props.getProperty("customerPhone"),
                props.getProperty("customerEmail")
        );
    }
    @Override
    public void updateState(String key, Object value) {

    }
}
