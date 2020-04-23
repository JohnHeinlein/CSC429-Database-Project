package userinterface;

import impresario.IModel;

public class TreeSellView extends View {

    public TreeSellView(IModel model) {
        super(model, "TreeSellView");

        setTitle("Sell a Tree");

        addContent("Barcode",
                makeField("Barcode", 6, 6, "numeric"));

        submitButton();
        cancelButton();
    }
//    public void submit(){
//        if(scrapeFields()){
//            try{
//                Tree tree = new Tree(props.getProperty("barcode"));
//                myModel.stateChangeRequest("TreeSellSubmit",tree);
//            }catch(InvalidPrimaryKeyException IPKE){
//                Alerts.errorMessage("Invalid barcode!");
//            }
//        }
//    }
    @Override
    public void updateState(String key, Object value) {

    }
}
