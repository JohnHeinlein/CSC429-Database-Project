package userinterface;

import impresario.IModel;

public class TreeSellView extends View{
    public TreeSellView(IModel model) {
        super(model, "TreeSellView");

        setTitle("Sell a Tree");

        addContent("Barcode",
                makeField("Barcode"));

        addContent("Payment Type",
                makeComboBox("Cash", "Check"));

        addContent("Customer Name",
                makeField("First Name"),
                makeField("Last Name"));

        addContent("Customer Phone",
                makeField("Customer Phone"));

        addContent("Customer Email",
                makeField("Customer Email"));

        submitButton("TreeSellSubmit");
        cancelButton();
    }

    @Override
    public void updateState(String key, Object value) {

    }
}
