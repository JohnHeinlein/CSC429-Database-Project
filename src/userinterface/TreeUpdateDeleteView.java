package userinterface;

import impresario.IModel;

public class TreeUpdateDeleteView extends View {

    public TreeUpdateDeleteView(IModel model) {
        super(model, "TreeUpdateDeleteView");

        setTitle("Update or Delete a Tree");

        addContent("Barcode",
                makeField("Barcode"));


        footButt(makeButt("Update",e->{
            scrapeFields();
            myModel.stateChangeRequest("TreeUpdate",props);
        }));

        footButt(makeButt("Delete",e->{
            scrapeFields();
            myModel.stateChangeRequest("TreeDelete",props);
        }));

        cancelButton();
    }

    @Override
    public void updateState(String key, Object value) {

    }
}
