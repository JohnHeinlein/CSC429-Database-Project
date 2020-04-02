package userinterface;

import impresario.IModel;

public class TreeUpdateDeleteView extends View {

    public TreeUpdateDeleteView(IModel model) {
        super(model, "TreeUpdateDeleteView");

        setTitle("Update or Delete a Tree");

        addContent("Barcode",
                makeField("Barcode"));


        footButt(makeButt("Search",e->{
            scrapeFields();
            myModel.stateChangeRequest("TreeSearch",props);
        }));

        cancelButton();
    }

    @Override
    public void updateState(String key, Object value) {

    }
}
